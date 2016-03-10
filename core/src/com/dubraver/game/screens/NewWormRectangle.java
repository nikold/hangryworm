package com.dubraver.game.screens;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.dubraver.game.screens.Game3Screen.DirectStates;

public class NewWormRectangle extends Rectangle {

	NewWormRectangle next;
	public Texture textureBody;
	public float prevX;
	public float prevY;
	public DirectStates currentDirectState;
	public DirectStates prevDirectState;

	public NewWormRectangle(NewWormRectangle next, float x, float y, float width, float height) {
		super(x,  y,  width,  height);
		this.next = next;
		
		Pixmap pixmap = new Pixmap(50, 50, Format.RGBA8888);
		pixmap.setColor(1, 0, 1, .6f);
		pixmap.fill() ;

		textureBody = new Texture(pixmap);
	}

	public void ChangeCurrentCoordinates(){
		if(next != null){
			float lastXWormCoordinate = next.x;
			float lastYWormCoordinate = next.y;

			currentDirectState = next.currentDirectState;
			if(currentDirectState == Game3Screen.DirectStates.RIGHT){
				lastXWormCoordinate = next.x - width;
				lastYWormCoordinate = y;
			}
			if(currentDirectState == Game3Screen.DirectStates.LEFT){
				lastXWormCoordinate = next.x + width;
				lastYWormCoordinate = y;
			}
			if(currentDirectState == Game3Screen.DirectStates.UP){
				lastXWormCoordinate = x;
				lastYWormCoordinate = next.y - width;
			}
			if(currentDirectState == Game3Screen.DirectStates.DOWN){
				lastXWormCoordinate = x;
				lastYWormCoordinate = next.y + width;
			}
			System.out.println("n x " + next.x + ". n y " + next.y);
			System.out.println("x " + lastXWormCoordinate + ". y " + lastYWormCoordinate);
			this.x = lastXWormCoordinate;
			this.y = lastYWormCoordinate;
			prevDirectState = currentDirectState;
		}
	}
}
