package com.td.powertd;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;

public final class Configuration {
	Element xmlRoot;
	FileHandle configurationFile;
	private static Configuration thisInstance;
	public static Configuration getInstance(){
		if(thisInstance==null){
			thisInstance=new Configuration();
		}
		return thisInstance;
	}
	private Configuration(){
		configurationFile=Gdx.files.internal("data/Configuration.xml");

		XmlReader xmlFile = new XmlReader();
		String sxml = configurationFile.readString();
		sxml = sxml.replaceAll("(?m)(?s)<!--.*?-->", "\n");
		xmlRoot = xmlFile.parse(sxml);
		
	}
	private void loadAll(){
		loadMeshes();			//Least dependencies
		loadProjectileAttacks(); 
		loadProjectiles();
		loadTowers();			//Greatest dependencies
		loadEnemies();
	}
	private void loadMeshes(){
		
	}
	private void loadProjectileAttacks(){
		int damage;
		boolean fire;
		boolean slow;
		String name;
		Element newAttack;
		Element ProjectileAttacks=xmlRoot.getChildByName("ProjectileAttacks");
		for(int i=0;i<ProjectileAttacks.getChildCount();i++){
			newAttack=ProjectileAttacks.getChild(i);
			damage=newAttack.getInt("damage");
			slow=newAttack.getBoolean("slows");
			fire=newAttack.getBoolean("enflames");
			name=newAttack.get("name");
			ProjectileAttack.registerNewType(name, slow, fire, damage);
		}
	}
	private void loadProjectiles(){
		String attack;
		String mesh;
		String name;
		Element newProjectile;
		float velocity;
		float radius;
		Element Projectiles=xmlRoot.getChildByName("Projectiles");
		for(int i=0;i<Projectiles.getChildCount();i++){
			newProjectile=Projectiles.getChild(i);
			attack=newProjectile.get("attack");
			velocity=newProjectile.getFloat("velocity");
			radius=newProjectile.getFloat("radius");
			mesh=newProjectile.get("mesh");
			name=newProjectile.get("name");
			Projectile.registerNewProjectileType(name, mesh, attack, radius, true, velocity);
		}
	}
	private void loadTowers(){
		Element Towers=xmlRoot.getChildByName("Towers");
		for(int i=0;i<Towers.getChildCount();i++){
			
		}
	}
	private void loadEnemies(){
		Element Enemies=xmlRoot.getChildByName("Enemies");
		for(int i=0;i<Enemies.getChildCount();i++){
			
		}
	}
}
