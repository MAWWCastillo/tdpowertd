package com.td.powertd;

public interface Collider extends Comparable<Collider>, GameActor{
	public boolean collisionWith(Collider that);
}
