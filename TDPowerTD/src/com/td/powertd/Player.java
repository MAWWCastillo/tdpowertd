package com.td.powertd;

public class Player {
	public static Player getLocalInstance(){ return localInstance;}
	public static Player localInstance;
	MyGdxGame myGame;
	public Player createLocalInstance(MyGdxGame myNewGame){
		localInstance=new Player(myNewGame);
		return localInstance;
	}
	protected int lives=0;
	protected Score myScore;
	public void loseLife(){
		lives--;
		if(lives<=0){
			die();
		}
	}
	public void earnPoints(int newPoints){
		myScore.addPoints(newPoints);
	}
	/**
	 * @param myNewGame
	 */
	private Player(MyGdxGame myNewGame) {
		myGame=myNewGame;
		lives=20;
		myScore=myNewGame.getLocalScore();
	}
	private void die(){
		
	}

}
