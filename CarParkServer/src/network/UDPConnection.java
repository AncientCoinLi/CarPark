package network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class UDPConnection implements Network {

	private final int sendPort = 8888;
	private final int byteSize = 2048;
	private final int receivePort = 8889;

	
	private DatagramSocket sendSocket;
	private DatagramSocket receiveSocket;
	private InetAddress sendAddress;
	private DatagramPacket sendPacket;
	private DatagramPacket receivePacket;
	private List<InetAddress> addresses;
	
	
	public UDPConnection() {
		initialise();
	}
	

	@Override
	public void initialise() {
		try {
			this.sendSocket = new DatagramSocket();
			this.receiveSocket = new DatagramSocket(this.receivePort);
			addresses = new ArrayList<>();
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void sendMessage(String message) {
		byte[] messageBytes = message.getBytes();
		for(InetAddress sendAddress: this.addresses) {
			sendPacket = new DatagramPacket(messageBytes, messageBytes.length, sendAddress, sendPort);
			try {
				sendSocket.send(sendPacket);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	@Override
	public String receiveMessage() {
		byte[] receive = new byte[byteSize];
		receivePacket = new DatagramPacket(receive, byteSize);
		try {
			receiveSocket.receive(receivePacket);
			if(!addresses.contains(receivePacket.getAddress())) {
				addresses.add(receivePacket.getAddress());
			}
			return new String(receivePacket.getData()).trim();
		} catch (IOException e) {
			e.printStackTrace();
		}		return null;
	}

}
