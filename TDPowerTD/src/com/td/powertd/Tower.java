package com.td.powertd;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ArrayMap;


public class Tower implements GameActor{
	public static ArrayMap<Integer,Tower> activeTowers;
	public static ArrayMap<Integer,Tower> inactiveTowers;
	public static ArrayMap<Integer,Tower> transitioningTowers;
	public static ArrayMap<String,Tower> towerTypes;
	protected static ArrayMap<String,Mesh> meshMap;
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
	//Only public enemy constructor
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
			base.bind();
			
			base.unbind();
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
	}

	@Override
	public Vector2 getLocation() {
		// TODO Auto-generated method stub
		return location;
	}
	@Override
	public void update(float delta){
		
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
}
