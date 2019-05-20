import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import model.Controller;
import network.MulticastConnection;
import network.Network;
import network.TCPConnection;
import network.UDPConnection;

public class ParkingCashServerStartup {

	private static final int tcpServerPort = 7777;
	public static void main(String[] args) {
		
		List<Network> networks = new ArrayList<>();
		// add udp and multicast into the network list
		networks.add(new UDPConnection());
		networks.add(new MulticastConnection());
				
		Controller controller = new Controller(networks);
		
		for(Network network : networks) {
			new Thread(new Runnable() {

				@Override
				public void run() {
					while(true) {
						String message = network.receiveMessage();
						controller.processMessage(message);
					}

				}
				
			}).start();
		}
		
		// cause tcp is different from upd, it needs its own startup
		startTCPServer(networks, controller);

	}
	
	private static void startTCPServer(List<Network> networks, Controller controller) {
		// new a serversocket for tcp and waiting tcp connection
		try {
			ServerSocket server = new ServerSocket(tcpServerPort);
			Socket socket;
			while(true) {
				TCPConnection tcp;
				socket = server.accept();
				tcp = new TCPConnection(socket);
				networks.add(tcp);
				new Thread(() -> {
					while(true) {
						String message = tcp.receiveMessage();
						if(message == null ) {
							networks.remove(tcp);
							break;
						}
						controller.processMessage(message);
					}
				}).start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
