package de.thm.iem.swtproject17.jumpnrun.core;

import java.util.ArrayList;

/* Die EntityControl Klasse ist eine Klasse welche die Existenz von Entit�ten verwaltet
 * Zu ihren Aufgaben geh�rt, dass erzeugen, kategorisieren und zu entfernen
 * Die Klasse ist ein Singleton und man kann die Instanz �ber EntityControl.getInstance() erhalten
 */

public class EntityControl {
	private static EntityControl instance = null; //speichert die Instanz
	
	private ArrayList<Entity> worldEntities,toSpawnEntities, toDespawnEntities, immobileEntities, logicalEntities;
	private ArrayList<MovingEntity> movingEntities;
	private ArrayList<Cam> cameras;
	//Listen um Unterschiedliche Typen von Entit�ten zu sperichern
	
	protected EntityControl() {
		worldEntities = new ArrayList<Entity>();
		toSpawnEntities = new ArrayList<Entity>();
		toDespawnEntities = new ArrayList<Entity>();
		immobileEntities = new ArrayList<Entity>();
		logicalEntities = new ArrayList<Entity>();
		movingEntities = new ArrayList<MovingEntity>();
		cameras = new ArrayList<Cam>();
		//Instanziert die Listen
	}
		
	public static EntityControl getInstance() {
		if ( instance == null )
			new IllegalStateException( "Used Entity Control without starting up properly!" );
		//anders als andere Singletons muss die EntityControl hoch und runtergefahren werden bevor sie benutzt werden kann
		return instance; //gibt die Instanzen zur�ck
	}
	
	public static void shutdown() { //wird benutzt um die EntityControl runterzufahren wenn sie gestartet ist
		if ( instance == null )
			new IllegalStateException( "trying to make EntityControl shutdown when it is already shutdown" );
		instance = null; 
	}
	
	public static void startup() { //wird benutzt um die EntityControl zu starten wenn sie noch nicht gestartet ist
		if ( instance != null )
			new IllegalStateException( "trying to make EntityControl startup when it is already started" );
		instance = new EntityControl();
	}
	
	
	//Wird benutzt um eine neue Kamera hinzuzufügen - eine Kamera welche eine gewisse Entitität beobachtet
	public Cam addCamera( Entity toFocus ) {
		Cam camera = new Cam( toFocus );
		cameras.add( camera );
		return camera;
	}
	
	//spawn methode wird benutzt um eine Entit�t zu instanzieren
	//e.g. Entity adler = EntityControl.getInstance().spawn( Adler ); wobei Adler eine Klasse ist die von Entity erbt
	public Entity spawn( Class<? extends Entity> entityType ) throws InstantiationException, IllegalAccessException {
		Entity entity = entityType.newInstance(); //erzeugt eine Instanz der Klasse die als Parameter �bergeben wurde
		addEntity( entity ); //f�gt die Entit�t der Liste von Entit�ten hinzu die der Welt hinzugef�gt werden
		return entity; //und gibt sie zur�ck
	}
	
	//despawn methode wird benutzt um eine Entit�t zu l�schen
	//e.g. EntityControl.getInstance().despawn( adler ); 
	public void despawn( Entity entity )
	{
		if ( !entity.getDespawned() ) //wenn die Entit�t nicht bereits gel�scht wurde
		{
			entity.setDespawned(); //wird sie als gel�scht gesetzt
			removeEntity( entity ); //und f�gt die Entit�t der List von Entit�ten hinzu die entfernt werden m�ssen
		}
	}
	
	private void addEntity( Entity entity ) //methode um Entit�t der Liste von Entit�ten hinzuzuf�gen die hinzugef�gt werden m�ssen
	{
		toSpawnEntities.add( entity );
	}
	
	private void removeEntity( Entity entity ) //methode um Entit�t der Liste von Entit�ten hinzuzuf�gen die entfernt werden m�ssen
	{
		if ( !toDespawnEntities.contains( entity ) && worldEntities.contains( entity ) )
			toDespawnEntities.add( entity );
	}
	
	public void processEntityLife() //entfernt und f�gt Elemente hinzu von den Listen der Elemente die Hinzugef�gt/Entfernt werden m�ssen
	{
		for ( Entity entity : toDespawnEntities ) { //f�r alle Entit�ten die Entfernt werden m�ssen
			worldEntities.remove( entity ); //von der allgemeinen Liste entfernen
			if ( entity instanceof MovingEntity ) {
				movingEntities.remove( (MovingEntity)entity ); //falls es eine Bewegliche Entit�t ist von der Liste Beweglicher Entit�ten entfernen
			}
			else if ( entity.getHitbox() != null ) { //falls es keine Bewegliche Entit�t ist und eine Hitbox hat
				immobileEntities.remove( entity ); //von der Liste unbeweglicher Physikalischer Elemente entfernen
			}
			if ( entity.hasLogic() ) { //falls eine Entit�t Logik hat die regelm��ig ausgef�hrt wird
				logicalEntities.remove( entity ); //entferne die Entit�t von der Liste der Entit�ten mit Logik
			}
		}
		toDespawnEntities = new ArrayList<Entity>(); //setzte die Liste von Entit�ten zur�ck die gel�scht werden m�ssen
		for ( Entity entity : toSpawnEntities ) { //f�r alle Entit�ten die hinzugef�gt werden m�ssen
			if ( entity.getDespawned() ) //falls die Entit�t bereits gel�scht wurde
				continue; //�berspringe das hinzuf�gen
			worldEntities.add( entity ); //zur allgemeinen Liste hinzuf�gen
			if ( entity instanceof MovingEntity ) { //falls es eine bewegliche Entit�t ist
				movingEntities.add( (MovingEntity)entity ); //zur beweglichen Liste hinzuf�gen
			}
			else if ( entity.getHitbox() != null ) { //andernfalls und falls es eine Hitbox hat
				immobileEntities.add( entity ); //zur Liste Physikalischer Unbeweglicher Entit�ten hinzuf�gen
			}
			if ( entity.hasLogic() ) { //falls eine Entit�t Logik hat die regelm��ig ausgef�hrt wird
				logicalEntities.add( entity ); //zur Liste logischer Entit�ten hinzuf�gen
			}
		}
		toSpawnEntities = new ArrayList<Entity>(); //setzt die Liste mit Entit�ten zur�ck die hinzugef�gt werden m�ssen
	}
	
	public ArrayList<Entity> getWorldEntities() { //gibt die Liste der Entit�ten zur�ck die in der Welt vorhanden sind
		return worldEntities;
	}
	
	public ArrayList<Entity> getLogicalEntities() { //gibt die Liste der Entit�ten zur�ck die Logik haben die regelm��ig ausgef�hrt
		return logicalEntities; //werden muss
	}
	
	public ArrayList<MovingEntity> getMovingEntities() { //gibt die Liste beweglicher Elemente zur�ck
		return movingEntities;
	}
	
	public ArrayList<Entity> getImmobileEntities() { //gibt die Liste unbeweglicher Elemente zur�ck, die eine Hitbox haben
		return immobileEntities;
	}
	
	public ArrayList<Cam> getCameras() {
		return cameras;
	}
	
}
