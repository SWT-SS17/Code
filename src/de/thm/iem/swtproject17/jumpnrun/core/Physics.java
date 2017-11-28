package de.thm.iem.swtproject17.jumpnrun.core;

import java.util.ArrayList;

/* Die Klasse Physics �bernimmt physikalische Berechnungen wie z.B. Bewegung und Kollision
 * Die Klasse ist ein Singleton d.h. knn die Instanz der Klasse �ber
 * Physics.getInstance() erhalten werden
 */

public class Physics {
	
	static private Physics instance = null; //speichert die Instanz
	double t; //speichert die Zeit seit dem letzten Frame vergangen ist
	
	
	protected Physics() { //Konstruktor - macht nicht viel
		
	}
	
	public static Physics getInstance() { //methode um die instanz zu erhalten
		if ( instance == null ) { //solnge es noch keine instanz gibt
			instance = new Physics(); // erzeuge eine neue
		}
		return instance; //gib die Instanz zur�ck
	}
	
	private void setFrameTime() { //setzt die Zeit des jetzigen Frames
		t = TickControl.getInstance().getFrameTimePassed();
	}
	
	public void calculateMovement() { //f�hrt die Physikalischen Berechnungen durch
		setFrameTime(); //setzt die Zeit des jetzigen Frames
		ArrayList<MovingEntity> finished = new ArrayList<MovingEntity>(); //speichert alle Beweglichen Entit�ten welche bereits bearbeitet
		//wurden und die eine Hitbox und damit Kollisionen haben
		MessageHandler mInstance = MessageHandler.getInstance(); //holt den MessageHandler um die Kollisionen zu �bergeben
		for ( MovingEntity entity : EntityControl.getInstance().getMovingEntities() ) { //f�r alle beweglichen Entit�ten
			entity.getPosition().add( entity.getVelocity().mulc( t ).add( entity.getAcceleration().mulc( t * t * 0.5 ) ) );
			// Die Position der Entit�t wird berechnet:
			// s = s0 + v0*t + 1/2 * a * t� blo� mit Vektoren
			entity.getVelocity().add( entity.getAcceleration().mulc( t ) );
			// Die neue Geschwindigkeit der Entit�t wird berechnet
			// v = v0 + a * t
			if ( entity.getHitbox() == null ) //falls die Entit�t keine Hitbox und damit keine Kollisionen hat
				continue; //Kollisionsberechnung �berspringen
			for ( Entity entity2 : EntityControl.getInstance().getImmobileEntities() ) //f�r alle Entit�ten welche unbeweglich sind und Kollisionen haben
				if ( entity.collidesWith( entity2 ) ) //wenn die Bewegliche Entit�t damit Kollidiert
					mInstance.addCollision( entity, entity2 ); //dann f�ge eine Kollisionsnachricht hinzu
			for ( MovingEntity entity2 : finished  ) //f�r alle beweglichen Entit�ten welche bereits fertig bewegt sind und Kollisionen haben
				if ( entity.collidesWith( entity2 ) ) //wenn Entit�t mit einer dieser Entit�ten Kollidiert
					mInstance.addCollision( entity, entity2 ); //f�ge eine Kollisionsnachricht hinzu
			finished.add( entity ); //f�ge die Entit�t der Liste fertiger Elemente hinzu
		}
	}
}