package model;
import java.util.ArrayList;
import java.util.List;

import model.State;
import network.Network;

public class Controller {

	private ArrayList<State> parkingState;
	private List<Network> networks;
	
	public Controller(List<Network> networks) {
		parkingState = new ArrayList<>();
		this.networks = networks;
		initParkingState();
	}

	public Controller() {
	}

	private void initParkingState() {

		for(int i = 0; i < 10; i ++) {
			parkingState.add(new State());
		}
	}

	public void processMessage(String message) {
		//System.out.println("SERVER GET: " + message);
    	/*
    	 * structure of message:
    	 * @command:@index:@clientID
    	 * 
    	 */
		String[] info = message.split(":");
		String command = info[0];
		int index = Integer.parseInt(info[1]);
		String sensorId = info[2];
		switch(command) {
		case "ComeIn":
			if(parkingState.get(index).ComeIn(sensorId)) {
				sendMessage(message);
			}else System.out.println("Reject ComeIn");
			break;
		case "ComeOut":
			if(parkingState.get(index).ComeOut(sensorId))
				sendMessage(message);
			else System.out.println("Reject ComeOut");
			break;
			default:
				System.out.println("Unknown Command --> " + message);
				break;
		}
		
	}

	private void sendMessage(String message) {
		for(Network network : this.networks) {
			network.sendMessage(message);
		}
	}
}
