package network;

import controller.Controller;

public interface Network {

	/**
	 * initialize socket and other resources
	 * @author AncientCoin
	 * @param controller 
	 */
	void initialize(Controller controller);
	
	void sendMessage(String message);
	
	String receiveMessage();

	void close();
}
