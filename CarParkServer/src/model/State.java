package model;

public class State {
	
	private boolean available;
	private String clientID;
	
	public State() {
		available = true;
		clientID = null;
	}
	
	public boolean ComeIn(String clientID) {
		if(available) {
			this.clientID = clientID;
			available = false;
			return true;
		} else {
			return false;
		}
	}
	
	public boolean ComeOut(String clientID) {
		if(available) return false;
		else {
			if(this.clientID.equals(clientID)) {
				this.clientID = null;
				available = true;
				return true;
			}else return false;
		}
	}
	
	
}
