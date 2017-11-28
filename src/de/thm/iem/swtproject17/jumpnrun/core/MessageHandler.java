package de.thm.iem.swtproject17.jumpnrun.core;

import java.util.ArrayList;

/* Der MessageHandler ist eine Klasse welche Events und �hnliches ( z.B. Kollisionen und das ausl�sen von Items ) 
 * in Form von Nachrichten sammelt und diese Nachrichten dann behandelt wenn es den Befehl dazu bekommt
 * Die Klasse ist ein Singleton d.h. l�sst sich die einzige instanz �ber MesageHandler.getInstance() erhalten
 */

public class MessageHandler {
	
	private static MessageHandler instance; //zum Speichern der Instanz
	ArrayList<Message> messages = null;    //zum Speichern der Messages
	
	protected MessageHandler() { //Konstruktor 
		messages = new ArrayList<Message>();
	}
	
	public static MessageHandler getInstance() { //gibt die instanz zur�ck
		if ( instance == null ) {
			instance = new MessageHandler();
		}
		return instance;
	}
	
	public void addCollision( Entity en1, Entity en2 ) { //wird von der Physics Klasse benutzt um Kollisionen hinzuzuf�gen
		messages.add( new Collision( en1, en2 ) );
	}
	
	public void handleMessages() { //Befehl um alle Nachrichten zu behandeln
		for( Message message : messages ) { //F�r alle Nachrichten
			message.handleMessage(); //behandle Nachricht
		}
		messages = new ArrayList<Message>(); //Setze die ArrayList mit den Nachrichten zur�ck
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
		
		public void handleMessage() { //und ruft dann sp�ter eine onCollide auf solbald sie behandelt wird
			en1.onCollide( en2 );
			en2.onCollide( en1 );
		}
		
	}
	
}
