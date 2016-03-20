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

	public void SaveDirectState(){

		if (next.currentDirectState == DirectStates.EMPTY){
			prevX = x;
			prevY = y;
		}
		if (Math.abs(x - prevX) >= width){
			prevDirectState = next.currentDirectState;
			prevX = next.x;
			prevY = next.y;
		}
		if (Math.abs(y - prevY) >= width){
			prevDirectState = next.currentDirectState;
			prevX = next.x;
			prevY = next.y;
		}
	}

	public void ChangeCurrentCoordinates(){
		if(next != null){
			if(prevDirectState == Game3Screen.DirectStates.RIGHT){
				x = prevX - width;
				y = prevY;
			}
			if(prevDirectState == Game3Screen.DirectStates.LEFT){
				x = prevX + width;
				y = prevY;
			}
			if(prevDirectState == Game3Screen.DirectStates.UP){
				x = prevX;
				y = prevY - width;
			}
			if(prevDirectState == Game3Screen.DirectStates.DOWN){
				x = prevX;
				y = prevY + width;
			}
			currentDirectState = prevDirectState;
		}
	}
}
