package com.td.powertd;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

public class Score {
	private BitmapFont font;
	private SpriteBatch sb;
	private int score;
	private Vector2 loc;
	public void addPoints(int points){
		score+=points;
	}
	public Score(SpriteBatch newBatch, BitmapFont newFont, Vector2 newLoc){
		score=0;
		sb=newBatch;
		font=newFont;
		loc=newLoc;
	}
	public void render(){
		sb.getProjectionMatrix().setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		sb.begin();
		font.draw(sb, "fps: " + Gdx.graphics.getFramesPerSecond(), (int)loc.x, (int)loc.y);
		sb.end();
	}

}
