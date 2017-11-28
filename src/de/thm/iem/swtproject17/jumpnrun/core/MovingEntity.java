package de.thm.iem.swtproject17.jumpnrun.core;

/* MovingEntity ist eine abstrakte Klasse von der alle beweglichen Entit�ten erben
 */

abstract class MovingEntity extends Entity {

	protected GameVector velocity; //speichert die Geschwindigkeit 
	protected GameVector acceleration; //speichert die Beschleunigung
	//Die erbenden Klassen sollen den konkreten GameVector ausw�hlen der hier eingef�gt werden soll
	
	public GameVector getVelocity() { //gibt die Geschwindigkeit zur�ck
		return velocity;
	}
	
	public GameVector getAcceleration() { //gibt die Beschleunigung zur�ck
		return acceleration;
	}
	
}
