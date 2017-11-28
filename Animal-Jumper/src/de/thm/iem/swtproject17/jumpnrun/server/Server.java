package de.thm.iem.swtproject17.jumpnrun.server;

public class Server {
	
	static private Server instance = null;
	
	static public Server getInstance() {
		if ( instance == null ) {
			instance = new Server();
		}
		return instance;
	}
	
	public void inputReady() {
		
	}
	
	public void outputReady() {
		
	}
	
}
