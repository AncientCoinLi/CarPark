package network;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class TCPConnection implements Network {
	private Socket socket;
	private BufferedReader input;
	private BufferedWriter output;

	public TCPConnection(Socket socket) {
		this.socket = socket;
		initialise();
	}
	
	@Override
	public void initialise() {
		try {
			input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			output = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
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
			System.out.println(e.getMessage());
			try {
				this.socket.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		
		return null;
	}

	

}
