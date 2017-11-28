package de.thm.iem.swtproject17.jumpnrun.core;

/* MovingEntity ist eine abstrakte Klasse von der alle beweglichen Entitäten erben
 */

abstract class MovingEntity extends Entity {

	protected GameVector velocity; //speichert die Geschwindigkeit 
	protected GameVector acceleration; //speichert die Beschleunigung
	//Die erbenden Klassen sollen den konkreten GameVector auswählen der hier eingefügt werden soll
	
	public GameVector getVelocity() { //gibt die Geschwindigkeit zurück
		return velocity;
	}
	
	public GameVector getAcceleration() { //gibt die Beschleunigung zurück
		return acceleration;
	}
	
}
