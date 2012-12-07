package com.td.powertd;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

public interface GameActor {
	
	public Vector2 getLocation();

	public void update(float delta);

	public void die();
}
