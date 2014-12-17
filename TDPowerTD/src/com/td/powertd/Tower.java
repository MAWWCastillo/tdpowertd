package com.td.powertd;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ArrayMap;



public class Tower implements Collider{
	protected enum TowerState{
		SEARCHING,
		TARGETING,
		FIRING,
		DYING
	};
	public static ArrayMap<Integer,Tower> activeTowers;
	public static ArrayMap<Integer,Tower> inactiveTowers;
	public static ArrayMap<Integer,Tower> transitioningTowers;
	//Need to convert these two to HASH maps.
	public static ArrayMap<String,Tower> towerTypes;
	protected static ArrayMap<String,Mesh> meshMap;
	TowerState state;
	public static Integer nextTower;
	public static GameMap gameMap;
	protected Integer id;
	protected Vector2 location;
	Sprite activeSprite;
	protected float rotation;
	protected float coolDown;
	protected float range;
	protected float currentCoolDown;
	protected static Mesh base;
	protected int cost;
	protected Mesh mesh;
	protected String projectileType;	//Need to convert to hash
	protected Enemy target;
	protected float timeToAct;
	public static void initializeTowers(GameMap newGameMap){
		activeTowers=new ArrayMap<Integer,Tower>();
		inactiveTowers=new ArrayMap<Integer,Tower>();
		transitioningTowers=new ArrayMap<Integer,Tower>();
		towerTypes=new ArrayMap<String, Tower>();
		meshMap=new ArrayMap<String,Mesh>();
		nextTower=0;
		gameMap=newGameMap;
	}
	
	private Tower(Tower that, Vector2 newLocation){
		this.duplicate(that, newLocation);
	}
	
	private void duplicate(Tower that, Vector2 newLocation){
		this.mesh=that.mesh;
		this.projectileType=that.projectileType;
		this.coolDown=that.coolDown;
		this.rotation=0;
		this.cost=that.cost;
		this.range=that.range;
		this.location=newLocation;
	}
	
	public static void registerNewMeshType(String meshName, Mesh mesh){
		meshMap.put(meshName, mesh);
	}
	
	public static void registerNewTowerType(
			String TowerType, String meshName,
			String projectileType, float coolDown,
			int cost, float range
			){
		Vector2 o=new Vector2(0,0);
		Tower t=new Tower(meshName,projectileType, coolDown, cost, range, o);
		towerTypes.put(TowerType, t);
	}
	
	//Only public tower constructor
	public static void spawnNewTower(String TowerType, Vector2 newLocation){
		Tower p;
		if(inactiveTowers.size>0){
			p=inactiveTowers.getValueAt(0);
			p.duplicate(towerTypes.get(TowerType), newLocation);
		}else{
			p=new Tower(towerTypes.get(TowerType), newLocation);
			p.id=nextTower;
			nextTower++;
		}
		activeTowers.put(p.id, p);
	}

	public static void renderTowers(){
		Tower t;
		for(int i=0;i<activeTowers.size;i++){
			t=activeTowers.getValueAt(i);
			//Render Base
			base.bind();
			
			base.unbind();
			//Render tower
			t.mesh.bind();
			//Rotate by orientation
			//Translate to location
			t.mesh.unbind();
		}
	}

	private Tower(
			String meshName, String newProjectileType, float newCoolDown,
			int newCost, float newRange, Vector2 location
		) {
		super();
		this.location = location;
		rotation=0;
		coolDown=newCoolDown;
		currentCoolDown=0;
		cost=newCost;
		range=newRange;
		mesh=meshMap.get(meshName);
		projectileType=newProjectileType;
		state=TowerState.SEARCHING;
	}

	@Override
	public Vector2 getLocation() {
		return location;
	}
	
	@Override
	public void update(float delta){
		allotTime(delta);
		while(this.timeToAct>0.0f){
			applyCoolDown(delta);
			switch(state){
				case SEARCHING:
					state=updateSearching();
					break;
				case TARGETING:
					state=updateTargeting();
					break;
				case FIRING:
					state=updateFiring();
					break;
				case DYING:
					state=updateDying();
					break;
			}
		}
	}

	protected TowerState updateDying() {
		// TODO Auto-generated method stub
		return null;
	}
	
	//Looking for trouble
	protected TowerState updateSearching(){
		TowerState newState=TowerState.SEARCHING;
		Enemy ClosestEnemy=Enemy.getClosestEnemy(this.location);
		//Did we find an enemy?
		if(ClosestEnemy==null){resetTime();return TowerState.SEARCHING;} //Nope!
		if(ClosestEnemy.collisionWith(this)){
			target=ClosestEnemy;
			this.state=TowerState.TARGETING;
		}
		return newState;
	}
	
	//Looking at Trouble
	protected TowerState updateTargeting(){
		//If the target is out of range...
		if(!this.target.collisionWith(this)){
			return TowerState.SEARCHING;
		}
		if(cooledDown()){
			return TowerState.FIRING;
		}
		else{
			resetTime();
			return TowerState.TARGETING;
		}
	}

	//Killing Trouble
	protected TowerState updateFiring(){
		try{
			Projectile.spawnNewProjectile(this.projectileType, this.location,this.target);
		}catch(Exception e){
			System.out.println(e.toString());
		}
		applyHeatUp();
		return TowerState.TARGETING;
	}
	
	protected void applyCoolDown(float delta){
		if(cooledDown()){return;}
		this.currentCoolDown-=delta;
		if(this.currentCoolDown<0.0f){this.currentCoolDown=0.0f;}
	}
	
	protected void applyHeatUp(){
		this.currentCoolDown=this.coolDown;
	}
	
	protected boolean cooledDown(){
		return currentCoolDown==0.0f;
	}
	
	protected void allotTime(float delta){
		this.timeToAct=delta;
	}
	
	protected void resetTime(){
		this.timeToAct=0.0f;
	}
	
	public float getRange() {
		return range;
	}

	@Override
	public void die() {
		gameMap.playerSellsTower((int)location.x, (int)location.y);
		transitioningTowers.put(id, this);
	}
	
	public static void transitionTowers(){
		Tower t;
		for(int i=0;i<transitioningTowers.size;i++){
			t=transitioningTowers.getValueAt(i);
			activeTowers.removeKey(t.id, false);
			inactiveTowers.put(t.id, t);
		}
		transitioningTowers.clear();
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
		float distance=that.getLocation().dst2(this.location);
		float bubbles=this.range+((Enemy)that).getRadius();
		if(bubbles>=distance) return true;
		else return false;
	}
}
