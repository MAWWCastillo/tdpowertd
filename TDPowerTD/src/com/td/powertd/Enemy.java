package com.td.powertd;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ArrayMap;

public class Enemy implements Collider, GameActor {
	public static ArrayMap<Integer,Enemy> activeEnemies;
	public static ArrayMap<Integer,Enemy> inactiveEnemies;
	public static ArrayMap<Integer,Enemy> transitioningEnemies;
	public static ArrayMap<String,Enemy> enemyTypes;
	public static GameMap gameMap;
	public static Integer nextEnemy;
	protected static ArrayMap<String,Mesh> meshMap;
	protected Integer id;
	protected Mesh mesh;
	protected Vector2 location;
	protected int health;
	protected int points;
	protected Vector2 nextCell;
	protected boolean statusSlow;	//I'd like to get this one in...
	protected boolean statusFire;	//One of these days...
	protected float radius;
	protected float rotation;
	float speed;
	//Static class creation
	public static void initializeEnemies(GameMap newGameMap){
		activeEnemies=new ArrayMap<Integer,Enemy>();
		inactiveEnemies=new ArrayMap<Integer,Enemy>();
		transitioningEnemies=new ArrayMap<Integer,Enemy>();
		enemyTypes=new ArrayMap<String, Enemy>();
		meshMap=new ArrayMap<String,Mesh>();
		nextEnemy=0;
		gameMap=newGameMap;
	}
	//Object instantiation
	private Enemy(Enemy that, Vector2 newLocation){
		this.duplicate(that, newLocation);
	}
	private void duplicate(Enemy that, Vector2 newLocation){
		this.health=that.health;
		this.mesh=that.mesh;
		this.points=that.points;
		this.radius=that.radius;
		this.speed=that.speed;
		this.statusFire=false;
		this.statusSlow=false;
		this.location=newLocation;
	}
	
	public static void registerNewMeshType(String meshName, Mesh mesh){
		meshMap.put(meshName, mesh);
	}
	public static void registerNewEnemyType(
			String enemyType, String meshName, int newHealth,
			int newPoints, float newRadius, float newSpeed
			){
		Vector2 o=new Vector2(0,0);
		Enemy e=new Enemy(newHealth, meshName, newPoints, newSpeed, o);
		enemyTypes.put(enemyType, e);
	}
	//Only public enemy constructor
	public static void spawnNewEnemy(String enemyType, Vector2 newLocation){
		Enemy e;
		if(inactiveEnemies.size>0){
			e=inactiveEnemies.getValueAt(0);
			e.duplicate(enemyTypes.get(enemyType), newLocation);
		}else{
			e=new Enemy(enemyTypes.get(enemyType), newLocation);
			e.id=nextEnemy;
			nextEnemy++;
		}
		activeEnemies.put(e.id, e);
	}
	private Enemy(
			int newHealth, String meshName, int newPoints,
			float newSpeed, Vector2 newLocation
		) {
		super();
		health=newHealth;
		points=newPoints;
		rotation=0;
		speed=newSpeed;
		mesh=meshMap.get(meshName);
	}
	public float getRadius() {
		return radius;
	}
	public boolean isStatusSlow() {
		return statusSlow;
	}

	public void setStatusSlow(boolean statusSlow) {
		this.statusSlow = statusSlow;
	}

	public boolean isStatusFire() {
		return statusFire;
	}

	public void setStatusFire(boolean statusFire) {
		this.statusFire = statusFire;
	}
	public void setLocation(Vector2 newLocation){
		location=newLocation;
	}
	@Override
	public Vector2 getLocation() {
		return location;
	}

	@Override
	public void update(float delta) {
		if(health<=0) die();
		
		
	}
	public void receiveDamage(int points){
		health-=points;
	}
	@Override
	public void die(){
		Player.getLocalInstance().earnPoints(points);
		transitioningEnemies.put(id, this);
	}
	public static void transitionEnemies(){
		Enemy e;
		for(int i=0;i<transitioningEnemies.size;i++){
			e=transitioningEnemies.getValueAt(i);
			activeEnemies.removeKey(e.id, false);
			inactiveEnemies.put(e.id, e);
		}
		transitioningEnemies.clear();
	}

	@Override
	public int compareTo(Collider that) {
		float thisLoc;
		float thatLoc;
			if(CollisionTable.verticalSort){
				thisLoc=this.getLocation().y;
				thatLoc=that.getLocation().y;
			}else{
				thisLoc=this.getLocation().x;
				thatLoc=that.getLocation().x;
			}
			if(thisLoc>thatLoc) return 1;
			if(thisLoc<thatLoc) return -1;
			return 0;
	}
	@Override
	public boolean collisionWith(Collider that) {
		// TODO Auto-generated method stub
		return false;
	}
	public static void renderEnemies(){
		Enemy e;
		for(int i=0;i<activeEnemies.size;i++){
			e=activeEnemies.getValueAt(i);
			e.mesh.bind();
			
			//Rotate by orientation
			//Translate to location
			e.mesh.unbind();
		}
	}

	public static Enemy getClosestEnemy(Vector2 location){
		float shortestDistance=Float.MAX_VALUE;
		float tempDistance;
		Enemy currentClosest=null;
		for(Enemy e:activeEnemies.values){
			tempDistance=e.location.dst2(location);
			if(tempDistance<shortestDistance){
				currentClosest=e;
				shortestDistance=tempDistance;
			}
		}
		return currentClosest;
	}

}
