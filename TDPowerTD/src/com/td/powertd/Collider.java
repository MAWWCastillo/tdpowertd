package com.td.powertd;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

public interface Collider extends Comparable<Collider>, GameActor{
	public boolean collisionWith(Collider that);
}
