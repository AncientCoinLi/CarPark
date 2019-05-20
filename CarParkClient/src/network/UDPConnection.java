package network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import controller.Controller;

public class UDPConnection implements Network {
	private final int byteSize = 2048;
	private final int sendPort = 8889;
	private final int receivePort = 8888;
	private final String sendHostName = "localhost";
	
	private InetAddress sendAddress;
	private DatagramSocket receiveSocket;
	private DatagramSocket sendSocket;
	private DatagramPacket sendPacket;
	private DatagramPacket receivePacket;

	@Override
	public void initialize(Controller controller) {
		try {
			controller.setNetwork(this);
			receiveSocket = new DatagramSocket(receivePort);
			sendSocket = new DatagramSocket();
			sendAddress = InetAddress.getByName(sendHostName);
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}

	@Override
	public void sendMessage(String message) {
		byte[] messageBytes = message.getBytes();
		sendPacket = new DatagramPacket(messageBytes, messageBytes.length, sendAddress, sendPort);
		try {
			sendSocket.send(sendPacket);
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}

	@Override
	public String receiveMessage() {
		byte[] bytes = new byte[byteSize];
		receivePacket = new DatagramPacket(bytes, byteSize);
		try {
			receiveSocket.receive(receivePacket);
			return new String(receivePacket.getData()).trim();
		} catch(SocketException se) {
			System.out.println(se.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void close() {
		receiveSocket.close();
		sendSocket.close();		
	}

}
