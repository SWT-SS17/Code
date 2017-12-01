package de.thm.iem.swtproject17.jumpnrun.core;

/* Die Klasse InternalHandler ist eigentlich nur eine Sammlung von Methoden, welche 
 * die Internen Berechnungen in geordneter Reihenfolge durchf�hren.
 */


public class InternalHandler {
	
	public static void doInternalTick() { //f�hrt alles interne durch 
		executeEntityLogic(); //f�hrt die Logik aller Entitit�ten mit Logik aus
		doPhysics();          //f�hrt die Physikalischen Berechnungen durch
		handleMessages();     //behandelt alle Nachrichten
		updateCameras();
		addDeleteEntities();  //f�gt alle Entit�ten hinzu die hinzugef�gt werden m�ssen und l�scht alle die gel�scht werden m�ssen
		TickControl.getInstance().internalFinished(); //sagt der TickControl, dass diese Phase beendet ist
	}
	
	private static void executeEntityLogic() {
		for ( Entity entity : EntityControl.getInstance().getLogicalEntities() ) //f�r alle Entit�ten mit Logik
			entity.executeLogic(); //f�hre diese Logik aus
	}
	
	private static void doPhysics() {
		Physics.getInstance().calculateMovement(); //veranlast die Physik Bewegungen und Kollisionen zu berechenen
	}
	
	private static void handleMessages() {
		MessageHandler.getInstance().handleMessages(); //veranlasst den MessageHandler alle Nachrichten zu behandeln
	}
	
	private static void updateCameras() {
		for ( Cam camera : EntityControl.getInstance().getCameras() )
			camera.updateCameraLogic();
	}
	
	private static void addDeleteEntities() {
		EntityControl.getInstance().processEntityLife(); //veranlasst die 
	}
	
}
