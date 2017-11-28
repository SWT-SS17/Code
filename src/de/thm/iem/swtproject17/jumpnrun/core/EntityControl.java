package de.thm.iem.swtproject17.jumpnrun.core;

import java.util.ArrayList;

/* Die EntityControl Klasse ist eine Klasse welche die Existenz von Entitäten verwaltet
 * Zu ihren Aufgaben gehört, dass erzeugen, kategorisieren und zu entfernen
 * Die Klasse ist ein Singleton und man kann die Instanz über EntityControl.getInstance() erhalten
 */

public class EntityControl {
	private static EntityControl instance = null; //speichert die Instanz
	
	private ArrayList<Entity> worldEntities,toSpawnEntities, toDespawnEntities, immobileEntities, logicalEntities;
	private ArrayList<MovingEntity> movingEntities;
	//Listen um Unterschiedliche Typen von Entitäten zu sperichern
	
	protected EntityControl() {
		worldEntities = new ArrayList<Entity>();
		toSpawnEntities = new ArrayList<Entity>();
		toDespawnEntities = new ArrayList<Entity>();
		immobileEntities = new ArrayList<Entity>();
		logicalEntities = new ArrayList<Entity>();
		movingEntities = new ArrayList<MovingEntity>();
		//Instanziert die Listen
	}
		
	public static EntityControl getInstance() {
		if ( instance == null )
			new IllegalStateException( "Used Entity Control without starting up properly!" );
		//anders als andere Singletons muss die EntityControl hoch und runtergefahren werden bevor sie benutzt werden kann
		return instance; //gibt die Instanzen zurück
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
	
	//spawn methode wird benutzt um eine Entität zu instanzieren
	//e.g. Entity adler = EntityControl.getInstance().spawn( Adler ); wobei Adler eine Klasse ist die von Entity erbt
	public Entity spawn( Class<? extends Entity> entityType ) throws InstantiationException, IllegalAccessException {
		Entity entity = entityType.newInstance(); //erzeugt eine Instanz der Klasse die als Parameter übergeben wurde
		addEntity( entity ); //fügt die Entität der Liste von Entitäten hinzu die der Welt hinzugefügt werden
		return entity; //und gibt sie zurück
	}
	
	//despawn methode wird benutzt um eine Entität zu löschen
	//e.g. EntityControl.getInstance().despawn( adler ); 
	public void despawn( Entity entity )
	{
		if ( !entity.getDespawned() ) //wenn die Entität nicht bereits gelöscht wurde
		{
			entity.setDespawned(); //wird sie als gelöscht gesetzt
			removeEntity( entity ); //und fügt die Entität der List von Entitäten hinzu die entfernt werden müssen
		}
	}
	
	private void addEntity( Entity entity ) //methode um Entität der Liste von Entitäten hinzuzufügen die hinzugefügt werden müssen
	{
		toSpawnEntities.add( entity );
	}
	
	private void removeEntity( Entity entity ) //methode um Entität der Liste von Entitäten hinzuzufügen die entfernt werden müssen
	{
		if ( !toDespawnEntities.contains( entity ) && worldEntities.contains( entity ) )
			toDespawnEntities.add( entity );
	}
	
	public void processEntityLife() //entfernt und fügt Elemente hinzu von den Listen der Elemente die Hinzugefügt/Entfernt werden müssen
	{
		for ( Entity entity : toDespawnEntities ) { //für alle Entitäten die Entfernt werden müssen
			worldEntities.remove( entity ); //von der allgemeinen Liste entfernen
			if ( entity instanceof MovingEntity ) {
				movingEntities.remove( (MovingEntity)entity ); //falls es eine Bewegliche Entität ist von der Liste Beweglicher Entitäten entfernen
			}
			else if ( entity.getHitbox() != null ) { //falls es keine Bewegliche Entität ist und eine Hitbox hat
				immobileEntities.remove( entity ); //von der Liste unbeweglicher Physikalischer Elemente entfernen
			}
			if ( entity.hasLogic() ) { //falls eine Entität Logik hat die regelmäßig ausgeführt wird
				logicalEntities.remove( entity ); //entferne die Entität von der Liste der Entitäten mit Logik
			}
		}
		toDespawnEntities = new ArrayList<Entity>(); //setzte die Liste von Entitäten zurück die gelöscht werden müssen
		for ( Entity entity : toSpawnEntities ) { //für alle Entitäten die hinzugefügt werden müssen
			if ( entity.getDespawned() ) //falls die Entität bereits gelöscht wurde
				continue; //überspringe das hinzufügen
			worldEntities.add( entity ); //zur allgemeinen Liste hinzufügen
			if ( entity instanceof MovingEntity ) { //falls es eine bewegliche Entität ist
				movingEntities.add( (MovingEntity)entity ); //zur beweglichen Liste hinzufügen
			}
			else if ( entity.getHitbox() != null ) { //andernfalls und falls es eine Hitbox hat
				immobileEntities.add( entity ); //zur Liste Physikalischer Unbeweglicher Entitäten hinzufügen
			}
			if ( entity.hasLogic() ) { //falls eine Entität Logik hat die regelmäßig ausgeführt wird
				logicalEntities.add( entity ); //zur Liste logischer Entitäten hinzufügen
			}
		}
		toSpawnEntities = new ArrayList<Entity>(); //setzt die Liste mit Entitäten zurück die hinzugefügt werden müssen
	}
	
	public ArrayList<Entity> getWorldEntities() { //gibt die Liste der Entitäten zurück die in der Welt vorhanden sind
		return worldEntities;
	}
	
	public ArrayList<Entity> getLogicalEntities() { //gibt die Liste der Entitäten zurück die Logik haben die regelmäßig ausgeführt
		return logicalEntities; //werden muss
	}
	
	public ArrayList<MovingEntity> getMovingEntities() { //gibt die Liste beweglicher Elemente zurück
		return movingEntities;
	}
	
	public ArrayList<Entity> getImmobileEntities() { //gibt die Liste unbeweglicher Elemente zurück, die eine Hitbox haben
		return immobileEntities;
	}
	
}
