package de.thm.iem.swtproject17.jumpnrun.core;

import java.util.ArrayList;

/* Die Klasse Physics übernimmt physikalische Berechnungen wie z.B. Bewegung und Kollision
 * Die Klasse ist ein Singleton d.h. knn die Instanz der Klasse über
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
		return instance; //gib die Instanz zurück
	}
	
	private void setFrameTime() { //setzt die Zeit des jetzigen Frames
		t = TickControl.getInstance().getFrameTimePassed();
	}
	
	public void calculateMovement() { //führt die Physikalischen Berechnungen durch
		setFrameTime(); //setzt die Zeit des jetzigen Frames
		ArrayList<MovingEntity> finished = new ArrayList<MovingEntity>(); //speichert alle Beweglichen Entitäten welche bereits bearbeitet
		//wurden und die eine Hitbox und damit Kollisionen haben
		MessageHandler mInstance = MessageHandler.getInstance(); //holt den MessageHandler um die Kollisionen zu übergeben
		for ( MovingEntity entity : EntityControl.getInstance().getMovingEntities() ) { //für alle beweglichen Entitäten
			entity.getPosition().add( entity.getVelocity().mulc( t ).add( entity.getAcceleration().mulc( t * t * 0.5 ) ) );
			// Die Position der Entität wird berechnet:
			// s = s0 + v0*t + 1/2 * a * t² bloß mit Vektoren
			entity.getVelocity().add( entity.getAcceleration().mulc( t ) );
			// Die neue Geschwindigkeit der Entität wird berechnet
			// v = v0 + a * t
			if ( entity.getHitbox() == null ) //falls die Entität keine Hitbox und damit keine Kollisionen hat
				continue; //Kollisionsberechnung überspringen
			for ( Entity entity2 : EntityControl.getInstance().getImmobileEntities() ) //für alle Entitäten welche unbeweglich sind und Kollisionen haben
				if ( entity.collidesWith( entity2 ) ) //wenn die Bewegliche Entität damit Kollidiert
					mInstance.addCollision( entity, entity2 ); //dann füge eine Kollisionsnachricht hinzu
			for ( MovingEntity entity2 : finished  ) //für alle beweglichen Entitäten welche bereits fertig bewegt sind und Kollisionen haben
				if ( entity.collidesWith( entity2 ) ) //wenn Entität mit einer dieser Entitäten Kollidiert
					mInstance.addCollision( entity, entity2 ); //füge eine Kollisionsnachricht hinzu
			finished.add( entity ); //füge die Entität der Liste fertiger Elemente hinzu
		}
	}
}