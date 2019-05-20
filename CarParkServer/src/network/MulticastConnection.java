package network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;

import model.Controller;

public class MulticastConnection implements Network{

	private final String groupHostName = "all-systems.mcast.net";
	private final int gourpPort = 4000;
	private final int byteSize = 2048;
	private final int receivePort = 9999;
	private MulticastSocket sendSocket;
	private DatagramSocket receiveSocket;
	private InetAddress groupAddress;
	private DatagramPacket sendPacket;
	private DatagramPacket receivePacket;
	
	public MulticastConnection() {
		initialise();
	}
	
	@Override
	public void initialise() {
		try {
			this.sendSocket = new MulticastSocket();
			this.receiveSocket = new DatagramSocket(this.receivePort);
			this.groupAddress = InetAddress.getByName(groupHostName);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	@Override
	public void sendMessage(String message) {
		byte[] messageBytes = message.getBytes();
		sendPacket = new DatagramPacket(messageBytes, messageBytes.length, groupAddress, gourpPort);
		try {
			sendSocket.send(sendPacket);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String receiveMessage() {
		byte[] receive = new byte[byteSize];
		receivePacket = new DatagramPacket(receive, byteSize);
		try {
			receiveSocket.receive(receivePacket);
			String message = new String(receivePacket.getData()).trim();
			return message;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
