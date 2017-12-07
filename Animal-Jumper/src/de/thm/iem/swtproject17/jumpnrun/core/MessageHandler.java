package de.thm.iem.swtproject17.jumpnrun.core;

import java.util.ArrayList;

/* Der MessageHandler ist eine Klasse welche Events und ähnliches ( z.B. Kollisionen und das ausl�sen von Items ) 
 * in Form von Nachrichten sammelt und diese Nachrichten dann behandelt wenn es den Befehl dazu bekommt
 * Die Klasse ist ein Singleton d.h. lässt sich die einzige instanz über MesageHandler.getInstance() erhalten
 * Zudem muss die Klasse über MessageHandler.startUp() gestartet sein bevor eine Instanz erhalten werden kann
 */

public class MessageHandler {
	
	private static MessageHandler instance; //zum Speichern der Instanz
	ArrayList<Message> messages = null;    //zum Speichern der Messages
	
	public static void shutdown() {
		instance = null;
	}
	
	public static void startup() {
		instance = new MessageHandler();
	}
	
	public static MessageHandler getInstance() { //gibt die instanz zurück
		if ( instance == null ) {
			new IllegalStateException( "Trying to get instance of MessageHandler without using startup" );
		}
		return instance;
	}
	
	protected MessageHandler() { //Konstruktor 
		messages = new ArrayList<Message>();
	}
	
	public void addCollision( Entity en1, Entity en2 ) { //wird von der Physics Klasse benutzt um Kollisionen hinzuzufügen
		messages.add( new Collision( en1, en2 ) );
	}
	
	public void handleMessages() { //Befehl um alle Nachrichten zu behandeln
		for( Message message : messages ) { //Für alle Nachrichten
			message.handleMessage(); //behandle Nachricht
		}
		messages = new ArrayList<Message>(); //Setze die ArrayList mit den Nachrichten zurück
	}
	
	abstract class Message { //abstrakte Nachricht Klasse von der alle konkreten Nachrichten Klassen erben
		abstract void handleMessage(); //definiert das alle Nachrichten zumindest eine Methode zum behandeln haben m�ssen
	}
	
	private class Collision extends Message { //Collision Klasse - ist die Nachrichtenklasse welche eine Kollision darstellt
		
		Entity en1 = null;
		Entity en2 = null;
		
		public Collision( Entity en1, Entity en2 ) { //speichert beide Enitities die Kollidiert sind
			this.en1 = en1;
			this.en2 = en2;
		}
		
		public void handleMessage() { //und ruft dann später eine onCollide Methode auf solbald sie behandelt wird
			en1.onCollide( en2 );
			en2.onCollide( en1 );
		}
		
	}
	
}
