package com.td.powertd;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;


public class Projectile implements Collider {
	public static ArrayMap<Integer,Projectile> activeProjectiles;
	public static ArrayMap<Integer,Projectile> inactiveProjectiles;
	public static ArrayMap<Integer,Projectile> transitioningProjectiles;
	protected static ArrayMap<String,Projectile> projectileTypes;
	protected static ArrayMap<String,Mesh> meshMap;
	protected static ArrayMap<String, ProjectileAttack> attackMap;
	public static Integer nextProjectile;
	public static GameMap gameMap;
	protected Mesh mesh;
	protected ProjectileAttack attack;
	protected Integer id;
	protected float speed;
	protected float rotation;
	protected Vector2 location;
	protected Enemy Target;
	protected float radius;
	protected boolean selectiveTargeting;
	
	public static void initializeProjectiles(GameMap newGameMap){
		activeProjectiles=new ArrayMap<Integer,Projectile>();
		inactiveProjectiles=new ArrayMap<Integer,Projectile>();
		transitioningProjectiles=new ArrayMap<Integer,Projectile>();
		projectileTypes=new ArrayMap<String, Projectile>();
		attackMap=new ArrayMap<String, ProjectileAttack>();
		meshMap=new ArrayMap<String,Mesh>();
		nextProjectile=0;
		gameMap=newGameMap;
	}
	public static void registerNewMeshType(String meshName, Mesh mesh){
		meshMap.put(meshName, mesh);
	}
	public static void registerNewAttack(String attackName, ProjectileAttack pa){
		attackMap.put(attackName, pa);
	}
	public static void registerNewProjectileType(
			String projectileType, String meshName, String attackType, float newRadius,
			boolean newSelectiveTargeting, float newVelocity
			){
		Vector2 o=new Vector2(0,0);
		Projectile p=new Projectile(meshMap.get(meshName), attackMap.get(attackType), null, newRadius, newSelectiveTargeting,  newVelocity, o);
		projectileTypes.put(projectileType, p);
	}
	private Projectile(Projectile that, Vector2 newLocation){
		this.duplicate(that, newLocation);
	}
	private void duplicate(Projectile that, Vector2 newLocation){
		this.mesh=that.mesh;
		this.radius=that.radius;
		this.speed=that.speed;
		this.location=newLocation;
		this.selectiveTargeting=that.selectiveTargeting;
		this.attack=that.attack;
	}
	//Only public projectile constructor
	public static void spawnNewProjectile(String projectileType, Vector2 newLocation, Enemy newTarget) throws Exception{
		Projectile p;
		if(inactiveProjectiles.size>0){
			p=inactiveProjectiles.getValueAt(0);
			p.duplicate(projectileTypes.get(projectileType), newLocation);
		}else{
			p=new Projectile(projectileTypes.get(projectileType), newLocation);
			p.id=nextProjectile;
			nextProjectile++;
		}
		if(p.selectiveTargeting==true){
			if(newTarget==null) throw new Exception("Unknown Target!");
			p.Target=newTarget;
		}
		activeProjectiles.put(p.id, p);
	}

	private Projectile(Mesh newMesh, ProjectileAttack pa, Enemy target, float radius,
			boolean selectiveTargeting, float speed,
			Vector2 location) {
		super();
		this.location = location;
		Target = target;
		this.radius = radius;
		this.attack=pa;
		this.selectiveTargeting = selectiveTargeting;
		this.speed = speed;
		id=new Integer(nextProjectile);
		nextProjectile++;
		mesh=newMesh;
		activeProjectiles.put(id, this);
	}
	@Override
	public Vector2 getLocation() {
		return location;
	}
	public static void initializeProjectiles(){
		activeProjectiles=new ArrayMap<Integer,Projectile>();
		nextProjectile=0;
	}
	public static void renderProjectiles(){
		Projectile p;
		for(int i=0;i<activeProjectiles.size;i++){
			p=activeProjectiles.getValueAt(i);
			p.mesh.bind();
			//Rotate by orientation
			//Translate to location
			p.mesh.unbind();
		}
	}

	@Override
	public void update(float delta){
		if(Target==null) die();
		rotation=Target.location.sub(this.location).angle();
		float travelDistance=delta*speed;
		
		if(location.dst2(Target.getLocation())<(travelDistance*travelDistance)){
			location=Target.getLocation();
		}else{
			Vector2 TravelVector=new Vector2(travelDistance,0).rotate(rotation);
			location.add(TravelVector);
		}
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
		if(that.getClass()!=Enemy.class) return false;
		if(selectiveTargeting&&that!=Target) return false;
		float distance=that.getLocation().dst2(this.location);
		float bubbles=this.radius+((Enemy)that).getRadius();
		if(bubbles>=distance) return true;
		else return false;
	}
	public void executeAttack(Enemy enemyTarget){
		attack.attack(enemyTarget);
		die();
	}
	@Override
	public void die(){
		transitioningProjectiles.put(id, this);
	}
	public static void transitionProjectiles(){
		Projectile p;
		for(int i=0;i<transitioningProjectiles.size;i++){
			p=transitioningProjectiles.getValueAt(i);
			activeProjectiles.removeKey(p.id, false);
			inactiveProjectiles.put(p.id, p);
		}
		transitioningProjectiles.clear();
	}
}
