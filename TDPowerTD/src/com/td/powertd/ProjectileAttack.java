package com.td.powertd;

import com.badlogic.gdx.utils.ArrayMap;

public class ProjectileAttack {
	boolean slows;
	boolean enflames;
	int damage;
	public static ArrayMap<String, ProjectileAttack> types;
	public static void initialize(){
		types=new ArrayMap<String, ProjectileAttack>();
	}
	public static void registerNewType(String name, boolean slow, boolean fire, int damage){
		ProjectileAttack newAttack=new ProjectileAttack(slow,fire,damage);
		types.put(name, newAttack);
	}
	private ProjectileAttack(boolean slow, boolean fire, int attackDamage){
		slows=slow;
		enflames=fire;
		damage=attackDamage;
	}
	void attack(Enemy that){
		if(slows)that.setStatusSlow(true);
		if(enflames)that.setStatusFire(true);
		that.receiveDamage(damage);
	}
}
