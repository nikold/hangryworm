package com.dubraver.game.screens;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.math.Rectangle;
import com.dubraver.game.screens.Game2Screen.DirectStates;

public class WormRectangle extends Rectangle {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	WormRectangle next;
	public Texture textureBody;
	public float prevX;
	public float prevY;
	//private DirectStates nextDirectState;
	private DirectStates currentDirectState;
	private float diffX;
	private float newXRight;
	private float newXLeft;
	private float diffY;
	private float newYUp; 
	private float newYDown;
	
	private void SetDirectState(DirectStates nextDirectState) {
		if (newXRight == x && diffY == 0 && currentDirectState != nextDirectState && nextDirectState != DirectStates.RIGHT && nextDirectState != DirectStates.LEFT){

			currentDirectState = nextDirectState;
		}
		if (newXLeft == x && diffY == 0 && currentDirectState != nextDirectState && nextDirectState != DirectStates.RIGHT && nextDirectState != DirectStates.LEFT){

			currentDirectState = nextDirectState;
		}
		if (newYUp == y && diffX == 0 && currentDirectState != nextDirectState && nextDirectState != DirectStates.UP && nextDirectState != DirectStates.DOWN){

			currentDirectState = nextDirectState;
		}
		if (newYDown == y && diffX == 0 && currentDirectState != nextDirectState && nextDirectState != DirectStates.DOWN){

			currentDirectState = nextDirectState;
		}
	}
	
	private void CalculateNextStepCoordinates(){
		
		diffX = x % width;
		newXRight  = x - diffX + width;
		newXLeft = x - diffX;
		
		diffY = y % height;
		newYUp = y - diffY + height;
		newYDown = y - diffY;
	}

	protected void SetNextDirectState(DirectStates DirectState){
		//nextDirectState = DirectState;
	}
	
	protected void SetCurrentDirectState(DirectStates DirectState){
		currentDirectState = DirectState;
	}
	
	protected DirectStates GetNextDirectState(){
		return null;
	}
	
	protected DirectStates GetCurrentDirectState(){
		return currentDirectState;
	}
	
	public WormRectangle(WormRectangle next, float x, float y, float width, float height) {
		super(x,  y,  width,  height);
		this.next = next;
		
		Pixmap pixmap = new Pixmap(50, 50, Format.RGBA8888);
		pixmap.setColor(1, 0, 1, .6f);
		pixmap.fill();

		textureBody = new Texture(pixmap);
	}
	
	public void SavePrevCoordinates(DirectStates currentDirectState,float width){
		
		float lastXWormCoordinate = 0;
		float lastYWormCoordinate = 0;
		SetDirectState(currentDirectState);
		
		if(currentDirectState == DirectStates.RIGHT){
			lastXWormCoordinate = x - width;
			lastYWormCoordinate = y;
		}
		if(currentDirectState == DirectStates.LEFT){
			lastXWormCoordinate = x + width;
			lastYWormCoordinate = y;
		}
		if(currentDirectState == DirectStates.UP){
			lastXWormCoordinate = x;
			lastYWormCoordinate = y - width;
		}
		if(currentDirectState == DirectStates.DOWN){
			lastXWormCoordinate = x;
			lastYWormCoordinate = y + width;
		}
		
		prevX = lastXWormCoordinate; 
		prevY = lastYWormCoordinate;
		CalculateNextStepCoordinates();
	}
	
	public void ChangeCurrentCoordinates(){
		if(next != null){
			this.x = next.prevX;
			this.y = next.prevY;
		}
	}
}
