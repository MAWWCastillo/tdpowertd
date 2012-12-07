package com.td.powertd;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GameScreen implements Screen{
	int tilesWide;
	int tilesHigh;
	int tileSize;
	SpriteBatch spriteBatch;
	GameMap gameMap;
	Game game;
	Player player;
	ParallaxCamera camera;
	OrthoCamController controller;
	/**
	 * @param myGame
	 */
	public GameScreen(Game myGame) {
		super();
		this.game = myGame;
		Gdx.app.log("Incomplete: GameScreen Constructor", "Need to pull in Configuration or map data for 'GameMap.'");
		gameMap=new GameMap(tilesWide, tilesHigh, null, null, null, null, null);//This needs to be fixed.
		camera = new ParallaxCamera(800, 480);
		controller = new OrthoCamController(camera);
		Gdx.input.setInputProcessor(controller);

		spriteBatch=new SpriteBatch();
		player=Player.getLocalInstance();
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0f, 0f, 0f, 1); //Clear to black
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		// keep camera in foreground layer bounds
		boolean updateCamera = false;
		if (camera.position.x < -1024 + camera.viewportWidth / 2) {
			camera.position.x = -1024 + (int)(camera.viewportWidth / 2);
			updateCamera = true;
		}

		if (camera.position.x > 1024 - camera.viewportWidth / 2) {
			camera.position.x = 1024 - (int)(camera.viewportWidth / 2);
			updateCamera = true;
		}

		if (camera.position.y < 0) {
			camera.position.y = 0;
			updateCamera = true;
		}
		// arbitrary height of scene
		if (camera.position.y > 400 - camera.viewportHeight / 2) {
			camera.position.y = 400 - (int)(camera.viewportHeight / 2);
			updateCamera = true;
		}

//		// background layer, no parallax, centered around origin
//		spriteBatch.setProjectionMatrix(camera.calculateParallaxMatrix(0, 0));
//		spriteBatch.disableBlending();
//		spriteBatch.begin();
//		spriteBatch.draw(layers[0], -(int)(layers[0].getRegionWidth() / 2), -(int)(layers[0].getRegionHeight() / 2));
//		spriteBatch.end();
//		spriteBatch.enableBlending();
//
//		// midground layer, 0.5 parallax (move at half speed on x, full speed on y)
//		// layer is 1024x320
//		spriteBatch.setProjectionMatrix(camera.calculateParallaxMatrix(0.5f, 1));
//		spriteBatch.begin();
//		spriteBatch.draw(layers[1], -512, -160);
//		spriteBatch.end();
//
//		// foreground layer, 1.0 parallax (move at full speed)
//		// layer is 2048x320
//		spriteBatch.setProjectionMatrix(camera.calculateParallaxMatrix(1f, 1));
//		spriteBatch.begin();
//		for (int i = 0; i < 9; i++) {
//			spriteBatch.draw(layers[2], i * layers[2].getRegionWidth() - 1024, -160);
//		}
		spriteBatch.end();

		// draw fps
		spriteBatch.getProjectionMatrix().setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		spriteBatch.begin();
//		font.draw(spriteBatch, "fps: " + Gdx.graphics.getFramesPerSecond(), 0, 30);
		spriteBatch.end();
        //Render Enemies
        //Render Tower Bases
        //Render Turrets
        //Render Projectiles
        //Render Score
		
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

}
