package network;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import controller.Controller;

public class TCPConnection implements Network {
	
	private Socket socket;
	private final int port = 7777;
	private final String host = "localhost";
	private BufferedReader input;
	private BufferedWriter output;
	

	@Override
	public void initialize(Controller controller) {
		try {
			socket = new Socket(host, port);
			input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			output = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void sendMessage(String message) {
		try {
			output.append(message + System.lineSeparator());
			output.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String receiveMessage() {
		try {
			String message = input.readLine();
			return message;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void close() {
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
