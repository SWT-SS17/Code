package de.thm.iem.swtproject17.jumpnrun.core;

/* Die Klasse Entity ist eine Abstrakte Klasse von der alle Spielelemente erben, die
 * sp�ter ein Teil der Spielwelt werden ( z.B. Tiere, Hindernisse und Items die aufsammelbar sind )
 */

abstract class Entity { 
	
	private boolean despawned = false; //speichert ob eine Entity bereits gedespawned ist oder noch nicht
	protected GameVector position; //speichert die Position
	
	public void despawn() { //wird aufgerufen um eine Entit�t zu entfernen
		EntityControl.getInstance().despawn( this ); //entfernt die Entit�t aus der EntityControl
	}
	
	public void setDespawned() { //wird von der EntityComtrol aufgerufen um zu speichern ob ein Element bereits gel�scht ist oder noch nicht
		despawned = true; 
	}
	
	public boolean getDespawned() { //gibt zur�ck ob ein Entity bereits gel�scht ist oder noch nicht
		return despawned;
	}
	
	public GameVector getPosition() { //gibt die Position der Entity zur�ck
		return position;
	}
	
	abstract PhysicalBorder getHitbox(); //gibt die Form der Hitbox zur�ck ( null um Kollisionen zu deaktivieren )
	abstract GameVector getSize(); //gibt die Gr��e der Hitbox zur�ck
	
	public boolean hasLogic() { //gibt zur�ck ob Entity Logik besitzt welche regelm��ig ausgef�hrt werden muss oder nicht
		return false; //standardm��ig besitzen Entities keine Logik jedoch kann dieses Verhalten von erbenden Klassen ver�ndert werden
	}
	
	public void executeLogic() { //f�hrt die Logik der Entity aus ( hier passiert nichts, da keine Logik )
		
	}
	
	public void onCollide( Entity entity2 ) { //wird aufgerufen sobald 2 Entit�ten kollidieren 
		//�bergeben wird die Entit�t mit der diese Entit�t kollidiert ist
		//Standardm��ig passiert nichts.
	}
	
	public boolean collidesWith( Entity entity2  ) { //gibt zur�ck ob sich diese Entity mit entity2 �berschneidet
		GameVector dist = position.subc( entity2.getPosition() ).abs(); //berechnet die Distanz der Mittelpunkte der Entities
		PhysicalBorder border2 = entity2.getHitbox(); //gibt die Form der hitbox des 2 Elements zur�ck
		Entity entity1; //speichert eine Entity, sodass man f�r eine der sp�teren Berechnungen die Reihenfolge der Entities vertauschen kann 
		if ( getHitbox() == PhysicalBorder.HITBOX ) { //wenn die Form der Hitbox der 1. Entity ein Rechteck ist...
			if ( border2 == PhysicalBorder.HITBOX ) { //...und dass der zweiten auch ein Rechteck ist
				return dist.lequal( getSize().addc( entity2.getSize() ).div( 2.0 ) ); //Dann wird berechnet ob die Distanz der Mittelpunkte
				//kleiner als die Gr��e der Rechtecke ist
			} else { //...und das zweite eine Ellipse ist
				entity1 = this; //dann wird dieses Element in der Variable entity1 gespeichert
			}
		} 
		else if ( border2 == PhysicalBorder.HITELLIPSE ) { //Falls die Form der Hitbox des 1. und 2. Entities eine Ellipse ist
			return dist.div( getSize().addc( entity2.getSize() ).div( 2.0 ) ).magnitude() <= 1.0 ; //Dann wird berechnet ob die Distanz
			//der Mittelpunkte kleiner ist als die Gr��e der Ellipsen
			//im Unterschied zur ersten Berechnung wird hier die Euklidsche Distanz verwendet was zu der unterschiedlichen Form f�hrt
		}
		else {
			entity1 = entity2;
			entity2 = this;
		}
		//Wenn man an diesem Punkt angkommen ist ist 1. Hitbox ein Rechteck und die 2. eine Ellipse
		dist.subrem( entity1.getSize().divc( 2 ) ); //hier wird dann zuerst ein Punkt innerhalb des Quadrats gesucht der
		//M�glichst nahe an der Ellipse liegt
		return dist.div( entity2.getSize().divc( 2 ) ).magnitude() <= 1; //dann wird geschaut ob dieser Punkt innerhalb der Ellipse liegt
	}
	
}