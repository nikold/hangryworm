package com.dubraver.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.dubraver.game.HungryWorm;
import com.dubraver.game.utils.Constants;

public class MenuScreen implements Screen {

	HungryWorm game;
	
	OrthographicCamera camera;
	Skin skin;
	
	final String text1 = "Start game";
	final String text2 = "Options";
	final String text3 = "Help";
	final String text4 = "Exit";
	
	final GlyphLayout layout1;
	final GlyphLayout layout2;
	final GlyphLayout layout3;
	final GlyphLayout layout4;
	
	
	final float fontX;
	final float fontY;
	
	final int buttonHeight;
	final int buttonWidth;
	
	TextButton newGameButton1;
	TextButton newGameButton2;
	TextButton newGameButton3;
	TextButton newGameButton4;
	
	Stage gameStage;
	
	public MenuScreen(HungryWorm game){

		this.game = game;
		camera = new OrthographicCamera();
		camera.setToOrtho(false, Constants.APP_WIDTH, Constants.APP_HEIGHT);
		
		gameStage=new Stage();
	    Gdx.input.setInputProcessor(gameStage);
		
		layout1= new GlyphLayout(game.font, text1);
		layout2= new GlyphLayout(game.font, text2);
		layout3= new GlyphLayout(game.font, text3);
		layout4= new GlyphLayout(game.font, text4);
		
		buttonHeight = (int)Gdx.graphics.getHeight()/10;
		buttonWidth = (int)Gdx.graphics.getWidth()/4;
		
		fontX = (Gdx.graphics.getWidth() - layout1.width) / 15;
		fontY = ((Gdx.graphics.getHeight() + layout1.height) / 2) + buttonHeight*3;
		
		createBasicSkin();
        
        newGameButton1 = new TextButton(text1, skin);
        newGameButton1.setPosition(fontX, fontY-(buttonHeight + 5));
        
        newGameButton2 = new TextButton(text2, skin);
        newGameButton2.setPosition(fontX, fontY-(buttonHeight + 5)*2);
        
        newGameButton3 = new TextButton(text3, skin);
        newGameButton3.setPosition(fontX, fontY-(buttonHeight + 5)*3);
        
        newGameButton4 = new TextButton(text4, skin); 
        newGameButton4.setPosition(fontX, fontY-(buttonHeight + 5)*4);
        
        createButtonsListeners();
        
        gameStage.addActor(newGameButton1);
        gameStage.addActor(newGameButton2);
        gameStage.addActor(newGameButton3);
        gameStage.addActor(newGameButton4);
	}
	
	private void createBasicSkin(){
		  //Create a font
		  BitmapFont font = new BitmapFont();
		  skin = new Skin();
		  skin.add("default", font);

		  //Create a texture
		  Pixmap pixmap = new Pixmap(buttonWidth, buttonHeight, Pixmap.Format.RGB888);
		  pixmap.setColor(Color.YELLOW);
		  pixmap.fill();
		  skin.add("background",new Texture(pixmap));

		  //Create a button style
		  TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
		  textButtonStyle.up = skin.newDrawable("background", Color.GRAY);
		  textButtonStyle.down = skin.newDrawable("background", Color.DARK_GRAY);
		  textButtonStyle.checked = skin.newDrawable("background", Color.DARK_GRAY);
		  textButtonStyle.over = skin.newDrawable("background", Color.LIGHT_GRAY);
		  textButtonStyle.font = skin.getFont("default");
		  skin.add("default", textButtonStyle);
	}
	
	private void createButtonsListeners(){
		
		newGameButton1.addListener(new InputListener() {
	        public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
	        	game.setScreen(new GameScreen(game));
		        dispose();
	            return true;  // must return true for touchUp event to occur
	        }
	        public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
	            
	        }
	    });
		
		newGameButton2.addListener(new InputListener() {
	        public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
	            
	            return true;  // must return true for touchUp event to occur
	        }
	        public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
	            
	        }
	    });
		
		newGameButton3.addListener(new InputListener() {
	        public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
	            return true;  // must return true for touchUp event to occur
	        }
	        public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
	            
	        }
	    });
		
		newGameButton4.addListener(new InputListener() {
	        public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
	            Gdx.app.exit();
	            return true;  // must return true for touchUp event to occur
	        }
	        public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
	           
	        }
	    });
	}
	
	@Override
	public void show() {
		// TODO Auto-generated method stub

	}

	@Override
	public void render(float delta) {
	
        Gdx.gl.glClearColor(0, 0f, 1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        gameStage.act();
        gameStage.draw();
	}

	@Override
	public void resize(int width, int height) {
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
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}
}
