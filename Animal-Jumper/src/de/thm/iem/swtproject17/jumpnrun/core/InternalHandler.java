package de.thm.iem.swtproject17.jumpnrun.core;

/* Die Klasse InternalHandler ist eigentlich nur eine Sammlung von Methoden, welche 
 * die Internen Berechnungen in geordneter Reihenfolge durchführen.
 */


public class InternalHandler {
	
	public static void doInternalTick() { //führt alles interne durch 
		executeEntityLogic(); //führt die Logik aller Entititäten mit Logik aus
		doPhysics();          //führt die Physikalischen Berechnungen durch
		handleMessages();     //behandelt alle Nachrichten
		addDeleteEntities();  //fügt alle Entitäten hinzu die hinzugefügt werden müssen und löscht alle die gelöscht werden müssen
		TickControl.getInstance().internalFinished(); //sagt der TickControl, dass diese Phase beendet ist
	}
	
	private static void executeEntityLogic() {
		for ( Entity entity : EntityControl.getInstance().getLogicalEntities() ) //für alle Entitäten mit Logik
			entity.executeLogic(); //führe diese Logik aus
	}
	
	private static void doPhysics() {
		Physics.getInstance().calculateMovement(); //veranlast die Physik Bewegungen und Kollisionen zu berechenen
	}
	
	private static void handleMessages() {
		MessageHandler.getInstance().handleMessages(); //veranlasst den MessageHandler alle Nachrichten zu behandeln
	}
	
	private static void addDeleteEntities() {
		EntityControl.getInstance().processEntityLife(); //veranlasst die 
	}
	
}
