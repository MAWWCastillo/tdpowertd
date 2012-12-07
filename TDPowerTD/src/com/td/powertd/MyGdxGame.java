package com.td.powertd;

import java.util.Iterator;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
//import com.badlogic.gdx.
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

public class MyGdxGame implements ApplicationListener {
	   Texture dropImage;
	   Texture bucketImage;
	   ParallaxCamera camera;
	   SpriteBatch batcher;
	   Rectangle bucket;
	   Score localPlayerScore;
	   Score getLocalScore(){return localPlayerScore;}
	   BitmapFont scoreFont;

	   @Override
	   public void create() {
		   Gdx.app.log("MyLibGDXGame", "Game.create()");
		   Settings mySettings=Settings.getInstance();
		   //Create camera
		   camera = new ParallaxCamera(0, 0);
		   camera.setToOrtho(false, 800, 480);
		   batcher = new SpriteBatch();
		   scoreFont=new BitmapFont(Gdx.files.internal("data/arial-15.fnt"), false);
		   localPlayerScore=new Score(batcher, scoreFont, new Vector2(0,30));
	      // start the playback of the background music immediately
	   }
	
	@Override
	public void dispose() {
		Gdx.app.log("MyLibGDXGame", "Game.dispose()");
		//dispose any object you created to free up the memory
		batcher.dispose();
	}

	@Override
	public void render() {		
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		camera.update();		
		batcher.setProjectionMatrix(camera.combined);
//		batcher.begin();
//		batcher.draw(bucketImage,bucket.x, bucket.y);
//		for(Rectangle raindrop: raindrops) {
//			batcher.draw(dropImage, raindrop.x, raindrop.y);
//		}
//		batcher.end();
//		if(Gdx.input.isTouched()) {
//			Vector3 touchPos = new Vector3();
//			touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
//			camera.unproject(touchPos);
//			bucket.x = touchPos.x - 48 / 2;
//		}
//		if(Gdx.input.isKeyPressed(Keys.LEFT)) bucket.x -= 200 * Gdx.graphics.getDeltaTime();
//		if(Gdx.input.isKeyPressed(Keys.RIGHT)) bucket.x += 200 * Gdx.graphics.getDeltaTime();
//		if(TimeUtils.nanoTime() - lastDropTime > 1000000000) spawnRaindrop();
//		Iterator<Rectangle> iter = raindrops.iterator();
//		while(iter.hasNext()) {
//			Rectangle raindrop = iter.next();
//			raindrop.y -= 200 * Gdx.graphics.getDeltaTime();
//			if(raindrop.y + 48 < 0) iter.remove();
//			if(raindrop.overlaps(bucket)) {
//				dropSound.play();
//				iter.remove();
//			}
//		}
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
		Gdx.app.log("MyLibGDXGame", "Game.pause()");
	}

	@Override
	public void resume() {
	}
	public void registerTypes(){
		
	}
}
