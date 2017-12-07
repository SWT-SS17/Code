package de.thm.iem.swtproject17.jumpnrun.core;

import de.thm.iem.swtproject17.jumpnrun.input.Input;
import de.thm.iem.swtproject17.jumpnrun.server.Server;


/* Die TickControl ist eine Klasse die daf�r zust�ndig ist, dass der Game-Loop ausgef�hrt wird.
 * d.h. Sorgt sie Schrittweise daf�r, dass nach dem einlesen der Eingabe die Physikalischen Berechnungen kommen
 * und danach die Ausgabe und danach wieder die Eingabe...
 */

public class TickControl {
	
	private static TickControl instance = null; //Die TickControl Klasse ist ein Singleton und die einzige Instanz wird hier gespeichert
	
	private boolean hasServer = false; //Speichert ob die TickControl Klasse den Server ansto�en soll oder nicht
	private boolean serverReady = false; //Speichert ob der Server mit einer Stufe fertig ist
	private boolean inputReady  = false; //Speichert ob die Eingabe mit einer Stufe fertig ist
	private boolean videoReady  = false; //Speichert ob die VideoAusgabe mit einer Stufe fertig ist
	private boolean audioReady  = false; //Speichert ob die AudioAusgabe mit eier Stufe fertig ist
	private boolean internalReady = false;
	
	private double frameTime,lastFrameTime; //speichert die Zeit seit dem letzten tick
	
	private boolean inStage = true; //Speichert ob die TickControl innerhalb einer stage ist oder nicht
	private boolean ended = true; //speichert ob das Spiel beendet wurde oder nicht
	private boolean stopped = true; //Speichert ob der GameLoop gestartet oder gestoppt ist
	private Stage currentStage = null; //Speichert die momentane Stufe bereits betreten wurde oder noch nicht
	
	/* Die einzelnen Schritte innerhalb eines Ticks die dazu n�tig sind um ein Bild korrekt zu Zeichnen 
	 * werden innerhalb der TickControl Klasse in diesem enum gesammelt.
	 * Wenn eine Stufe begonnen wird wird code ausgef�hrt um die in dieser Stufe verwendeten externen Module ( wie z.B. die AudioAusgabe )
	 * davon zu informieren, dass sie die in in dieser Stufe geplanten Aktionen ausf�hren m�ssen
	 * 
	 * Sobald diese Aktionen beendet sind informieren diese Module wieder diese Klasse.
	 * Daraufhin wird dann geschaut ob alle Module welche in dieser Stufe verwendet werden beendet sind.
	 * Wenn dies der Fall ist wird code ausgef�hrt um die Stufe zu beenden, die n�chte Stufe wird bestimmt und das ganze beginnt von vorne 
	 */
	private enum Stage { 
		INPUT { //INPUT Stage hier erhlaten alle Module welche Informationen zum Spielkern geben das Sagen ( Eingabe, Server )
			public void enter() { // wird aufgerufen sobald die Stage eingeleitet wird
				TickControl.instance.setFrameTime();
				TickControl.instance.inputStageReady(); // ruft einfach nur eine Methode der Klasse TickControl auf
			}
			public boolean isFinished() { //wird benutzt um zu �berpr�fen ob diese Stufe beendet ist
				TickControl instance = TickControl.instance;
				return instance.inputReady && ( !instance.hasServer || instance.serverReady ); 
			}
			public Stage getNext() { //wird aufgerufen um zu bestimmen welche die nachfolgende Stufe ist
				return INTERNAL; //geht zur INTERNAL Stage
			}
		},
		INTERNAL { //INTERNAL Stage hier erhalten alle Module welche innerhalb des Spielkerns sind das Sagen ( z.B. Physik )
			public void enter() {
				TickControl.instance.internalStageReady();
			}
			public boolean isFinished(){
				return instance.internalReady; //Noch nicht fertig
			}
			public Stage getNext() {
				return OUTPUT; //geht zur OUTPUT Stage
			}
		},
		OUTPUT { //OUTPUT Stage hier erhalten alle Module welche Informationen aus dem Spielkern holen das sagen ( Audio, Video, Server )
			public void enter() {
				TickControl.instance.outputStageReady();
			}
			public boolean isFinished() {
				TickControl instance = TickControl.instance;
				return instance.audioReady && instance.videoReady && ( !instance.hasServer || instance.serverReady );
			}
			public Stage getNext() {
				return INPUT; //beginnt wieder mit der INPUT Stage
			}
		},
		DEINITIALIZATION { //DEINITIALIZATION Stage, ist eine besondere Stage welche eingeleitet ist, wenn das eigentlich Spiel beendet ist
			//gestartet ist und auch zum Beenden eines laufenden Spiels genutzt wird.
			public void enter() { //beim Betreten dieser Stage werden alle externen Module heruntergefahren
				TickControl.instance.shutdownServices();
			}
			public boolean isFinished() {
				return !instance.ended;
			}
			public Stage getNext() {
				return INITIALIZATION; //geht weiter zur INITIALIZATION stage
			}
		},
		INITIALIZATION { //INITIALIZATION Stage, ist eine besondere Stage welche eingeleitet wird, wenn das eigentlich Spiel noch nicht
			//gestartet ist und gerade hochgefahren wird. In dieser Stage sollen alle Spieler erzeugt werden - die Levelgeneration richtig eingestellt werden etc.
			public void enter() { //beim Betreten dieser Stage werden alle externen Module hochgefahren
				TickControl.instance.startupServices();
			}
			public boolean isFinished() {
				return true; //Noch nicht fertig
			}
			public Stage getNext() {
				return INPUT; //beginnt dann mit der INPUT Stage
			}
		};
		abstract void enter(); // wird aufgerufen sobald eine Stage betreten wird
		abstract boolean isFinished(); // wird aufgerufen um zu �berpr�fen ob eine Stage beendet ist
		abstract Stage getNext(); // wird aufgerufen um die n�chste stage zu erhalten
	};
	
	static public TickControl getInstance() { //TickControl ist ein Singleton: d.h. kann man auf diese Methode zugreifen
		if ( instance == null ) { //, um die einzige Instanz von TickControl zu erhalten
			instance = new TickControl();
		}
		return instance;
	}

	public void inputFinished() { //wird vom Eingabe Modul aufgerufen um zu signalisieren, dass es fertig ist
		if ( inputReady )
			new IllegalStateException( "Overlapping messages - inputReady already ready" );
		inputReady = true; //dies wird gespeichert
		progressStage(); //und danach wird der Mechanismus zum Einleiten der n�chsten Stage eingeleitet
	}
	
	public void serverFinished() { //wird vom Server Modul aufgerufen um zu signalisieren, dass es fertig ist
		if ( serverReady )
			new IllegalStateException( "Overlapping messages - serverReady already ready" );
		serverReady = true;
		progressStage();
	}
	
	public void videoFinished() { //wird vom Video Modul aufgerufen um zu signalisieren, dass es fertig ist
		if ( videoReady )
			new IllegalStateException( "Overlapping messages - videoReady already ready" );
		videoReady = true;
		progressStage();
	}
	
	public void audioFinished() { //wird vom Audio Modul aufgerufen um zu signalisieren, dass es fertig ist
		if ( audioReady )
			new IllegalStateException( "Overlapping messages - audioReady already ready" );
		audioReady = true;
		progressStage();
	}
	
	public void internalFinished() { //wird vom InternalHandler aufgerufen sobald es fertig ist
		if ( internalReady )
			new IllegalStateException( "Overlapping messages - internalReady already ready" );
		internalReady = true;
		progressStage();
	}
	
	public void initializationFinished() { //wird von der GameControl aufgerufen sobald sie fertig ist mit dem Initialisieren
		if ( currentStage != Stage.INITIALIZATION )
			new IllegalStateException( "Overlapping messages - initializationReady already ready" );
		stopped = false;
		progressStage();
		stopped = true;
	}
	
	public void play() { //startet die TickControl wenn sie gestoppt ist
		if ( !stopped )
			new IllegalStateException( "Trying to start already started TickControl" );
		stopped = false;
		progressStage();
	}
	
	public void pause() { // pausiert die TickControl wenn sie gestartet ist
		if ( stopped )
			new IllegalStateException( "Trying to stop an already stopped TickControl" );
		stopped = true;
	}
	
	public void endGame() { //beendet ein Spiel falls es gestartet ist oder wird
		if ( ended )
			new IllegalStateException( "Trying to end an already ended game" );
		stopped = true;
		ended = true;
		if ( currentStage == Stage.INITIALIZATION ) {
			currentStage = Stage.DEINITIALIZATION;
			if ( inStage )
				currentStage.enter();
			else
				inStage = true;
		}
	}
	
	public void initializeGame() { //initialisiert ein Spiel und bewegt das Spiel in den INITIALIZATION Stage
		ended = false;
		progressStage();
	}
	
	public boolean getStopped() { //gibt zurück ob die TickControl gestartet oder gestoppt ist
		return stopped;
	}
	
	public boolean hasServer() { //gibt zurück, ob die TickControl den Server verwendet oder nicht
		return hasServer;
	}
	
	public void setServerState( boolean newHasServer ) { //aktiviert oder deaktiviert den Server
		if ( currentStage != Stage.INITIALIZATION ) //funktioniert nur während der INITILIZATION Stage
			new IllegalStateException( "Trying to activate Server outside of INITIALIZATION Stage" );
		hasServer = newHasServer;
	}
	
	protected TickControl() {
		stopped = true;
		inStage = true;
		ended   = true;
		currentStage = Stage.DEINITIALIZATION;
	}
	
	private void progressStage() { //wird aufgerufen um die n�chste Stage zu beginnen 
		if ( !inStage ) {
			if ( !stopped ) {
				currentStage.enter();
				inStage = true;
			}
		} else if ( currentStage.isFinished() ) { //daf�r wird zuerst �berpr�ft ob die Jetzige beendet ist
			currentStage = currentStage.getNext(); //die n�chste Stufe wird herausgesucht
			inStage = false;
			if ( !stopped ) { //und wenn die TickControl nicht gestoppt ist wird diese sofort betreten
				currentStage.enter();
				inStage = true;
			}
			else if ( ended ) {
				currentStage = Stage.DEINITIALIZATION;
				currentStage.enter();
				inStage = true;
			}
		}
	}
	
	private void inputStageReady() {
		inputReady = false;
		Thread inputThread = new Thread() {
			@Override
			public void run(){
				Input.getInstance().inputReady();
			}
		};
		inputThread.start();
		if ( hasServer ) {
			serverReady = false;
			Thread serverThread = new Thread() {
				@Override
				public void run(){
					Server.getInstance().inputReady(); //
				}
			};
			serverThread.start();
		}
	}
	
	private void internalStageReady() {
		internalReady = false;
		Thread internalThread = new Thread() {
			@Override
			public void run(){
				InternalHandler.doInternalTick();
			}
		};
		internalThread.start();
	}
	
	private void outputStageReady() {
		videoReady = false;
		Thread videoThread = new Thread() {
			@Override
			public void run(){
				Video.getInstance().outputReady(); //
			}
		};
		videoThread.start();
		audioReady = false;
		Thread audioThread = new Thread() {
			public void run() {
				Audio.getInstance().outputReady();
			}
		};
		audioThread.start();
		if ( hasServer ) {
			serverReady = false;
			Thread serverThread = new Thread() {
				@Override
				public void run(){
					Server.getInstance().outputReady(); //
				}
			};
			serverThread.start();
		}
	}
	
	private void shutdownServices()
	{
		EntityControl.shutdown();
		MessageHandler.shutdown();
		//Not finished
	}
	
	private void startupServices()
	{
		lastFrameTime = frameTime = currentSeconds();
		EntityControl.startup();
		MessageHandler.startup();
		//Not finished
	}
	
	private static double currentSeconds() {
		return System.nanoTime() / 1.0e9;
	}
	
	public double getFrameTime() {
		return frameTime;
	}
	
	public double getFrameTimePassed() {
		return lastFrameTime;
	}
	
	private void setFrameTime() {
		double current = currentSeconds();
		lastFrameTime = frameTime - current;
		frameTime = current;
	}
	
}