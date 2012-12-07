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
		verticalSort=false;
		for(int ip=0;ip<Projectile.activeProjectiles.size;ip++){
			horizontal.add(Projectile.activeProjectiles.get(ip));
		}
		for(int ie=0;ie<Enemy.activeEnemies.size;ie++){
			horizontal.add(Enemy.activeEnemies.get(ie));
		}
		for(int ip=0;ip<Projectile.activeProjectiles.size;ip++){
			vertical.add(Projectile.activeProjectiles.get(ip));
		}
		for(int ie=0;ie<Enemy.activeEnemies.size;ie++){
			vertical.add(Enemy.activeEnemies.get(ie));
		}
		horizontal.sort();
		vertical.sort();
	}
	public static void checkCollisions(){
		Projectile p;
		Enemy e;
		for(int ip=0;ip<Projectile.activeProjectiles.size;ip++){
			for(int ie=0;ie<Enemy.activeEnemies.size;ie++){
				p=Projectile.activeProjectiles.get(ip);
				e=Enemy.activeEnemies.get(ie);
				if(p.collisionWith(e)){
					p.executeAttack(e);
				}
			}
		}
	}
}
