package de.thm.iem.swtproject17.jumpnrun.core;

abstract class Player extends MovingEntity {
	
	private boolean local = true;
	private long id;
	private String name;
	private int lobbyIndex;
	
	public Player( long id, boolean local, String name, int lobbyIndex ) {
		this.id = id;
		this.local = local;
		this.name = name;
		this.lobbyIndex = lobbyIndex;
	}
	
	public int lobbyIndex() {
		return lobbyIndex;
	}
	
	public long getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public boolean getLocal() {
		return local;
	}

}