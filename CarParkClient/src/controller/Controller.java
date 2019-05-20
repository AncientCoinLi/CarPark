package controller;

import network.Network;

public class Controller {

	private Network network;
	
	public void close() {
		if(this.network == null) {
			return;
		}
		else this.network.close();
		
	}

	public void setNetwork(Network network) {
		this.network = network;
	}

}
