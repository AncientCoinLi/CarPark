package network;

public interface Network {

	void initialise();
	
	void sendMessage(String message);
	
	String receiveMessage();
}
