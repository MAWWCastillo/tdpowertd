package com.td.powertd;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import danielBeard.DStarLite;

public class GameMap {
	private ParallaxCamera camera;
	private int [][]walkMap;
	private int [][]tileMap;
	private Vector2 start;
	private Vector2 goal;
	private int height;
	private int width;
	private DStarLite dsl;
	private DStarLite dslt;
	Texture tex[];
	public void setCamera(ParallaxCamera newCamera){
		camera=newCamera;
	}
	public void monsterEntersCell(int x, int y){
		if(((int)goal.x==x&&(int)goal.y==y)==false)
		walkMap[y][x]++;
	}
	public void monsterLeavesCell(int x, int y){
		if(walkMap[y][x]>0)
			walkMap[y][x]--;
	}
	public void playerPlacesTower(int x, int y){
		if(walkMap[y][x]==0){
			tileMap[y][x]=1;
			walkMap[y][x]=2;
			dsl.updateCell(x, y, -1);
			dslt.updateCell(x, y, -1);
		}
	}
	public void playerSellsTower(int x, int y){
		if(walkMap[y][x]>0){
			walkMap[y][x]=0;
			dsl.updateCell(x, y, 0);
			dslt.updateCell(x, y, 0);
		}
	}

	public boolean considerTowerRequest(int x, int y){
		Gdx.app.log("GameMap", "considerTowerRequest():Please fix me.");
		//Is Tower inside bounds?
		if(((y>0&&y<walkMap.length)&&(x>0&&x<walkMap[0].length))==false)
			return false;
		//Is spot occupied?
		if(walkMap[y][x]>0) return false;
		//Check if breaks path.
		dslt.updateCell(x, y, -1);
		if(dslt.replan()==false){
			dslt.updateCell(x, y, 0);
			return false;
		}
		return true;
	}
	void generateNewWalkMap(){
		int x,y;
		walkMap=new int[height][width];
		for(y=0;y<height;y++){
			for(x=0;x<width;x++)
				walkMap[y][x]=0;
		}
		for(y=0;y<height;y++){
			walkMap[y][0]=1;
			walkMap[y][width-1]=1;
			walkMap[y][1]=1;
			walkMap[y][width-2]=1;
		}
		for(x=0;x<width;x++){
			walkMap[0][x]=1;
			walkMap[height-1][x]=1;
		}
		walkMap[(int)start.x][(int)start.y]=0;
		walkMap[(int)goal.x][(int)goal.y]=0;
	}
	void generateNewTileMap(){
		int x,y;
		tileMap=new int[height][width];
		for(y=0;y<height;y++){
			for(x=0;x<width;x++)
				tileMap[y][x]=0;
		}
		for(y=0;y<height;y++){
			tileMap[y][0]=3;
			tileMap[y][width-1]=3;
			tileMap[y][1]=3;
			tileMap[y][width-2]=3;
		}
		for(x=0;x<width;x++){
			tileMap[0][x]=3;
			tileMap[height-1][x]=3;
		}
		tileMap[(int)start.x][(int)start.y]=2;
		tileMap[(int)goal.x][(int)goal.y]=2;
	}
	public void render(SpriteBatch sb){
		float left=width*16;
		float top=height*16;
		sb.setProjectionMatrix(camera.calculateParallaxMatrix(1f, 1));
		sb.begin();

		for(int y=0;y<height; y++){
			for(int x=0;x<width;x++){
				sb.draw(tex[tileMap[y][x]], x*32- left, y*32 -top);
			}
		}
		sb.end();
	}
	/**
	 * @param newTileMap
	 * @param newHeight
	 * @param newWidth
	 */
	public GameMap(int newHeight, int newWidth, int[][] newTileMap, int[][]newWalkMap, Vector2 newStart, Vector2 newGoal, AssetManager assetMan) {
		super();
		int x,y;
		tex=new Texture[4];
		tex[0]=new Texture(Gdx.files.internal("data/grass.png"));
		tex[1]=new Texture(Gdx.files.internal("data/dirt.png"));
		tex[2]=new Texture(Gdx.files.internal("data/wall.png"));
		tex[3]=new Texture(Gdx.files.internal("data/arrow.png"));
		tileMap = newTileMap;
		height = newHeight;
		width = newWidth;
		if(newStart==null){
			start=new Vector2(width-1, (height+1)>>1);
		}
		if(newGoal==null){
			goal=new Vector2(1, (height+1)>>1);
		}
		if(walkMap==null)
			generateNewWalkMap();
		dsl=new DStarLite();
		dslt=new DStarLite();
		dsl.init((int)start.x, (int)start.y, (int)goal.x, (int)goal.y);
		dslt.init((int)start.x, (int)start.y, (int)goal.x, (int)goal.y);
		for(y=0;y<height;y++){
			for(x=0;x<width;x++){
				if(walkMap[y][x]==1){
					dsl.updateCell(x, y, -1);
					dslt.updateCell(x, y, -1);
				}
			}
		}
		dsl.replan();
		dslt.replan();
	}
	
}
