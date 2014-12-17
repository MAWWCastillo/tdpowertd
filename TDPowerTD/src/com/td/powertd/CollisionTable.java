/**
 * 
 */
package com.td.powertd;
import com.badlogic.gdx.utils.Array;
/**
 * @author dean
 *
 */
public class CollisionTable {
	public static boolean verticalSort;
	public static Array <Collider>vertical;
	public static Array <Collider>horizontal;
	public static void addAndSort(){
		//Add projectiles to collision tables
		for(Projectile p:Projectile.activeProjectiles.values){
			horizontal.add(p);
			vertical.add(p);
		}
		//Add projectiles to collision tables
		for(Enemy e:Enemy.activeEnemies.values){
			horizontal.add(e);
			vertical.add(e);
		}
		//Add towers to collision tables
		for(Tower t:Tower.activeTowers.values){
			horizontal.add(t);
			vertical.add(t);
		}

		verticalSort=false;
		horizontal.sort();
		verticalSort=true;
		vertical.sort();
	}
	//Need to implement faster O(n log n) algorithm
	//Checks and executes potential collisions
	public static void checkCollisions(){
		for(Projectile p:Projectile.activeProjectiles.values){
			for(Enemy e:Enemy.activeEnemies.values){
				if(p.collisionWith(e)){p.executeAttack(e);}
			}
		}
	}
}
