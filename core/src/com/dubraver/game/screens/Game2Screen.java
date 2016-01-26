package com.dubraver.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Blending;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Array;
import com.dubraver.game.HungryWorm;
import com.dubraver.game.utils.Constants;

public class Game2Screen implements Screen {

	HungryWorm game;
	OrthographicCamera camera;
	private Skin skin;
	private Stage gameStage;
	private String text = "Return";
	private TextButton newGameButton;

	private float speed = 1;
	private Skin skinUIButtons;

	private enum ButtonStates {
		LEFT_BUTTON_TOUCH_DOWN, LEFT_BUTTON_TOUCH_UP, RIGHT_BUTTON_TOUCH_DOWN, RIGHT_BUTTON_TOUCH_UP, EMPTY, DOWN_BUTTON_TOUCH_DOWN, UP_BUTTON_TOUCH_DOWN, UP_BUTTON_TOUCH_UP, DOWN_BUTTON_TOUCH_UP
	};

	public enum DirectStates {
		LEFT, RIGHT, DOWN, UP, EMPTY
	};

	ButtonStates currentButtonState = ButtonStates.EMPTY;
	DirectStates currentDirectState = DirectStates.EMPTY;
	DirectStates nextDirectState = DirectStates.EMPTY;
	DirectStates prevDirectState = DirectStates.EMPTY;

	@SuppressWarnings("unused")
	private Texture wormTexture;
	private WormRectangle wormRectangle;
	private ShapeRenderer shape = new ShapeRenderer();
	private Button btnLeft;
	private Button btnRight;
	private Button btnUp;
	private Button btnDown;

	private float newXRight = -1;
	private float newXLeft = -1;
	private float newYUp = -1;
	private float newYDown = -1;
	private float diffX;
	private float diffY;
	private float multiplier = 1f;
	
	private Texture foodTexture;
	private Rectangle foodRectangle;
	private boolean foodEated;
	private boolean foodTouched;
	
	private float lastXWormCoordinate = -1f;
	private float lastYWormCoordinate = -1f;
	
	private TextureRegion textureRegion;
	private Rectangle textureRegionBounds1;
	private Texture textureHeadLeft;
	private Texture textureHeadRight;
	private Texture textureHeadUp;
	private Texture textureHeadDown;
	
	private Array<WormRectangle> wormRectangles = new Array<WormRectangle>();

	public Game2Screen(HungryWorm game) {

		this.game = game;
		camera = new OrthographicCamera();
		camera.setToOrtho(false, Constants.APP_WIDTH, Constants.APP_HEIGHT);

		gameStage = new Stage();
		Gdx.input.setInputProcessor(gameStage);
		textureRegion = new TextureRegion(new Texture(
				Gdx.files.internal(Constants.BACKGROUND_IMAGE_PATH)));
		textureHeadLeft = new Texture(Gdx.files.internal(Constants.HEAD_LEFT_IMAGE_PATH));
		textureHeadRight= new Texture(Gdx.files.internal(Constants.HEAD_RIGHT_IMAGE_PATH));
		textureHeadUp= new Texture(Gdx.files.internal(Constants.HEAD_UP_IMAGE_PATH));
		textureHeadDown= new Texture(Gdx.files.internal(Constants.HEAD_DOWN_IMAGE_PATH));
		//textureBody= new Texture(Gdx.files.internal(Constants.BODY_IMAGE_PATH));
		
		textureRegionBounds1 = new Rectangle(0, 0,
				Constants.APP_WIDTH, Constants.APP_HEIGHT);
		
		createReturnButton(gameStage);
		createUIButtons(gameStage);
		addButtonsListeners();
		createWorm();
		createFood();
	}

	private void createWorm() {
		Pixmap pixmap = new Pixmap(50, 50, Format.RGBA8888);
		pixmap.setColor(1, 0, 1, .6f);
		pixmap.fill();

		wormTexture = new Texture(pixmap);
		wormRectangle = new WormRectangle(null,Constants.APP_WIDTH / 2,
				Constants.APP_HEIGHT / 2, 40, 40);
		wormRectangles.add(wormRectangle);
	}
	
	private void createWormElement(WormRectangle next, float x, float y) {

		WormRectangle wormElementRectangle = new WormRectangle(next,x,
				y, wormRectangle.width, wormRectangle.height);
		System.out.println("x " + x + ". y " + y);
		wormRectangles.add(wormElementRectangle);
	}
	
	//функци€ создающа€ в случайном месте квадрат еды
	private void createFood() {
		Pixmap pixmap = new Pixmap(50, 50, Format.RGBA8888);
		pixmap.setColor(1, 0, 0, .6f);
		pixmap.fill();
		
		foodTexture = new Texture(pixmap);
		int x = (int) MathUtils.random(2, (Constants.APP_WIDTH - wormRectangle.width)/ wormRectangle.width);
		int y = (int) MathUtils.random(2, (Constants.APP_HEIGHT - wormRectangle.height)/ wormRectangle.height);
		foodRectangle = new Rectangle(x * wormRectangle.width,y * wormRectangle.height,
				40, 40);
	}
	
	private void createReturnButton(Stage gameStage) {

		// Create a font
		BitmapFont font = new BitmapFont();
		skin = new Skin();
		skin.add("default", font);

		// Create a texture
		Pixmap.setBlending(Blending.None);
		Pixmap pixmap = new Pixmap(100, 100, Format.RGBA8888);

		pixmap.setColor(1, 1, 1, .6f);
		// pixmap.fill();
		pixmap.fillCircle(50, 50, 30);
		skin.add("background", new Texture(pixmap));

		// Create a button style
		TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
		textButtonStyle.up = skin.newDrawable("background", Color.GRAY);
		textButtonStyle.down = skin.newDrawable("background", Color.DARK_GRAY);
		textButtonStyle.checked = skin.newDrawable("background",
				Color.DARK_GRAY);
		textButtonStyle.over = skin.newDrawable("background", Color.LIGHT_GRAY);
		textButtonStyle.font = skin.getFont("default");
		skin.add("default", textButtonStyle);

		newGameButton = new TextButton(text, skin);
		newGameButton.setPosition(50,
				Gdx.graphics.getHeight() - newGameButton.getHeight());

		newGameButton.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				game.setScreen(new MenuScreen(game));
				dispose();
				return true; // must return true for touchUp event to occur
			}

			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {

			}
		});

		gameStage.addActor(newGameButton);
	}

	private void createUIButtons(Stage stage) {
		Texture texLeft = new Texture("arrow.png");
		Texture texRight = new Texture("arrow.png");
		Texture texUp = new Texture("arrow.png");
		Texture texDown = new Texture("arrow.png");

		Image imgLeft = new Image(texLeft);
		Image imgRight = new Image(texRight);
		Image imgUp = new Image(texUp);
		Image imgDown = new Image(texDown);

		skinUIButtons = new Skin();
		ButtonStyle buttonStyle = new ButtonStyle();
		skinUIButtons.add("default", buttonStyle);

		imgLeft.setScale(1f);
		imgLeft.setOrigin(30, 30);
		imgLeft.setColor(0, 0, 0, 0.2f);

		imgRight.setScale(1f);
		imgRight.setRotation(180);
		imgRight.setOrigin(30, 30);
		imgRight.setColor(0, 0, 0, 0.2f);

		imgUp.setScale(1f);
		imgUp.setRotation(-90);
		imgUp.setOrigin(30, 30);
		imgUp.setColor(0, 0, 0, 0.2f);

		imgDown.setScale(1f);
		imgDown.setRotation(90);
		imgDown.setOrigin(30, 30);
		imgDown.setColor(0, 0, 0, 0.2f);

		btnLeft = new Button(skinUIButtons);
		btnLeft.add(imgLeft).size(60, 60);

		btnRight = new Button(skinUIButtons);
		btnRight.add(imgRight).size(60, 60);

		btnUp = new Button(skinUIButtons);
		btnUp.add(imgUp).size(60, 60);

		btnDown = new Button(skinUIButtons);
		btnDown.add(imgDown).size(60, 60);

		Table tableLeftSide = new Table();
		Cell<Button> cellBtn1 = tableLeftSide.add(btnLeft);
		cellBtn1.spaceRight(30f);
		tableLeftSide.add(btnRight);
		tableLeftSide.setPosition(140, 40);

		Table tableRightSide = new Table();
		Cell<Button> cellBtn3 = tableRightSide.add(btnUp);
		cellBtn3.spaceBottom(30f);
		// cellBtn3.spaceRight(30f);
		tableRightSide.row();
		tableRightSide.add(btnDown);
		tableRightSide.setPosition(Gdx.graphics.getWidth() - 80, 80);

		stage.addActor(tableLeftSide);
		stage.addActor(tableRightSide);
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub

	}

	@Override
	public void render(float delta) {

		Gdx.gl.glClearColor(1, 1f, 1f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		speed = 2;
		//speed = speed * delta;
		//System.out.println("speed [" + speed + "]");
		camera.update();
		game.batch.setProjectionMatrix(camera.combined);
		
		ProcessKeyInput(delta);
		 
		MoveWormRectangle(currentDirectState, delta);
		for (int i = 0; i < wormRectangles.size; i++) {
			if(wormRectangles.get(i).next != null){
				System.out.println(wormRectangles.get(i).next.GetCurrentDirectState());
				wormRectangles.get(i).SavePrevCoordinates(wormRectangles.get(i).next.GetCurrentDirectState(), wormRectangle.width);
		
			}else{
				wormRectangles.get(i).SavePrevCoordinates(currentDirectState, wormRectangle.width);
			}
			
			wormRectangles.get(i).ChangeCurrentCoordinates();
		}
		
		if (currentDirectState == DirectStates.EMPTY){
			SetStartState(currentButtonState);
		}
		
		SetDirectState();
		CalculateNextStepCoordinates();

		game.batch.begin();
		game.batch.draw(textureRegion, textureRegionBounds1.x,
				textureRegionBounds1.y, Constants.APP_WIDTH,
				Constants.APP_HEIGHT);
		
		DrawHead();
		DrawElements();
		//создать массив из пр€моугольников. динамически пересчитывать координаты звеньев
		//каждое звено знает о впереди сто€щщем через ссылку
		//каждое звено получает координаты впереди сто€щего в рендере
		if (foodRectangle != null){

			game.batch.draw(foodTexture, foodRectangle.x,
					foodRectangle.y, foodRectangle.width,
					foodRectangle.height);
		}
		else{
			if(foodEated){
				createFood();
				foodEated = false;
			}
			//System.out.println("new food X " + lastXWormCoordinate);
			//System.out.println("new food Y " + lastYWormCoordinate);
			game.batch.draw(foodTexture, foodRectangle.x,
						foodRectangle.y, wormRectangle.width,
						wormRectangle.height);
		}
		
		game.batch.end();
		
		drawGridLines();
		
		gameStage.act();
		gameStage.draw();
		
		//√олова в квадрате еды. ”бираем квадрат еды. —татус foodEated = true														
		//≈сли foodEated = true и голова не в квадрате еды, то создаем на месте еды квадрат elem и добавл€ем в массив														
		//√олова мен€ет координату, элемент принимает координаты предыдущего положени€ головы														

		if (wormRectangle.overlaps(foodRectangle) && !foodEated) {

			foodTouched = true;
		}
		
		if(currentDirectState == DirectStates.RIGHT){
			lastXWormCoordinate = wormRectangle.x - wormRectangle.width;
			lastYWormCoordinate = wormRectangle.y;
		}
		if(currentDirectState == DirectStates.LEFT){
			lastXWormCoordinate = wormRectangle.x + wormRectangle.width;
			lastYWormCoordinate = wormRectangle.y;
		}
		if(currentDirectState == DirectStates.UP){
			lastXWormCoordinate = wormRectangle.x;
			lastYWormCoordinate = wormRectangle.y - wormRectangle.width;
		}
		if(currentDirectState == DirectStates.DOWN){
			lastXWormCoordinate = wormRectangle.x;
			lastYWormCoordinate = wormRectangle.y + wormRectangle.width;
		}
		
		if (foodTouched && foodRectangle.x == lastXWormCoordinate && foodRectangle.y == lastYWormCoordinate){

//			System.out.println("f.x " + foodRectangle.x + " f.y " + foodRectangle.y);
//			System.out.println("w.x " + wormRectangle.x + " w.y " + wormRectangle.y);
			createWormElement(wormRectangles.peek(), foodRectangle.x, foodRectangle.y);
			foodRectangle = null;
			foodEated  = true;
			foodTouched = false;
		}

	}

	private void SetDirectState() {
		if (newXRight == wormRectangle.x && diffY == 0 && currentDirectState != nextDirectState && nextDirectState != DirectStates.RIGHT && nextDirectState != DirectStates.LEFT){
			//System.out.println("newXRight [" + newXRight + "] wormRectangle.x ["+ wormRectangle.x + "]"  );
			currentDirectState = nextDirectState;
		}
		if (newXLeft == wormRectangle.x && diffY == 0 && currentDirectState != nextDirectState && nextDirectState != DirectStates.RIGHT && nextDirectState != DirectStates.LEFT){
			//System.out.println("newXLeft [" + newXLeft + "] wormRectangle.x ["+ wormRectangle.x + "]"  );
			currentDirectState = nextDirectState;
		}
		if (newYUp == wormRectangle.y && diffX == 0 && currentDirectState != nextDirectState && nextDirectState != DirectStates.UP && nextDirectState != DirectStates.DOWN){
			//System.out.println("newYUp [" + newYUp + "] wormRectangle.y ["+ wormRectangle.y + "]"  );
			currentDirectState = nextDirectState;
		}
		if (newYDown == wormRectangle.y && diffX == 0 && currentDirectState != nextDirectState && nextDirectState != DirectStates.DOWN){
			//System.out.println("newYDown [" + newYDown + "] wormRectangle.y ["+ wormRectangle.y + "]"  );
			currentDirectState = nextDirectState;
		}
	}
	
	private void CalculateNextStepCoordinates(){
		
		diffX = wormRectangle.x % wormRectangle.width * multiplier;
		newXRight  = wormRectangle.x - diffX + wormRectangle.width * multiplier;
		newXLeft = wormRectangle.x - diffX;
		
		diffY = wormRectangle.y % wormRectangle.height * multiplier;
		newYUp = wormRectangle.y - diffY + wormRectangle.height * multiplier;
		newYDown = wormRectangle.y - diffY;
	}

	private void DrawHead() {
		// TODO Auto-generated method stub
		switch (currentDirectState) {
		case DOWN:
			game.batch.draw(textureHeadDown, wormRectangle.x, wormRectangle.y,
					wormRectangle.width, wormRectangle.height);
			break;
		case EMPTY:
			game.batch.draw(textureHeadLeft, wormRectangle.x, wormRectangle.y,
					wormRectangle.width, wormRectangle.height);
			break;
		case LEFT:
			game.batch.draw(textureHeadLeft, wormRectangle.x, wormRectangle.y,
					wormRectangle.width, wormRectangle.height);
			break;
		case RIGHT:
			game.batch.draw(textureHeadRight, wormRectangle.x, wormRectangle.y,
					wormRectangle.width, wormRectangle.height);
			break;
		case UP:
			game.batch.draw(textureHeadUp, wormRectangle.x, wormRectangle.y,
					wormRectangle.width, wormRectangle.height);
			break;
		default:
			break;

		}
	}
	
	private void DrawElements() {
		// TODO Auto-generated method stub
		for (int i = 1; i < wormRectangles.size; i++) {
			WormRectangle currentElement = wormRectangles.get(i);
			game.batch.draw(currentElement.textureBody, currentElement.x, currentElement.y,
					currentElement.width, currentElement.height);
		}
	}

	private void drawGridLines(){
		 // Gridlines
		shape.setProjectionMatrix(camera.combined);
		shape.begin(ShapeType.Line);
	    shape.setColor(0,1,0,0.1f);
	    for (int i = 0; i < Gdx.graphics.getHeight() / wormRectangle.height * multiplier; i++) {
	        shape.line(0, i * wormRectangle.height* multiplier, Gdx.graphics.getWidth(), i * wormRectangle.height * multiplier);
	    }
	    for (int i = 0; i < Gdx.graphics.getWidth() / wormRectangle.height * multiplier; i++) {
	        shape.line(i * wormRectangle.height* multiplier, 0, i * wormRectangle.height * multiplier, Gdx.graphics.getHeight());
	    }
	    shape.end();
	}
	
	private void SetStartState(ButtonStates ButtonState){
		switch (ButtonState) {
		case DOWN_BUTTON_TOUCH_DOWN:
			currentDirectState =DirectStates.DOWN;
			break;
		case DOWN_BUTTON_TOUCH_UP:
			break;
		case EMPTY:
			break;
		case LEFT_BUTTON_TOUCH_DOWN:
			currentDirectState =DirectStates.LEFT;
			break;
		case LEFT_BUTTON_TOUCH_UP:
			break;
		case RIGHT_BUTTON_TOUCH_DOWN:
			currentDirectState =DirectStates.RIGHT;
			break;
		case RIGHT_BUTTON_TOUCH_UP:
			break;
		case UP_BUTTON_TOUCH_DOWN:
			currentDirectState =DirectStates.UP;
			break;
		case UP_BUTTON_TOUCH_UP:
			break;
		default:
			break;

		}
	}

	private void ProcessKeyInput(float delta) {
		if (!Gdx.input.justTouched())
			return;
		
		
		if (currentButtonState == ButtonStates.LEFT_BUTTON_TOUCH_DOWN) {
			nextDirectState = DirectStates.LEFT;
		}

		if (currentButtonState == ButtonStates.RIGHT_BUTTON_TOUCH_DOWN) {

			nextDirectState = DirectStates.RIGHT;
		}

		if (currentButtonState == ButtonStates.UP_BUTTON_TOUCH_DOWN) {
			nextDirectState = DirectStates.UP;
		}

		if (currentButtonState == ButtonStates.DOWN_BUTTON_TOUCH_DOWN) {
			nextDirectState = DirectStates.DOWN;
		}

		//System.out.println("currentButtonState [" + currentButtonState + "] currentDirectState " +currentDirectState + "] nextDirectState [" + nextDirectState + "]");
	}

	private void MoveWormRectangle(DirectStates directState, float delta) {

		switch (directState) {
		case RIGHT:
				MoveWormRight(delta);
				break;	
		case LEFT:
				MoveWormLeft(delta);
				break;
		case UP:
				MoveWormUp(delta);
				break;
		case DOWN:
				MoveWormDown(delta);
				break;
		case EMPTY:
		}
	}
	
	private void MoveWormUp(float delta) {
		
		if (wormRectangle.y < Constants.APP_HEIGHT - wormRectangle.height) {
			wormRectangle.y+=speed;
		}
	}
	
	private void MoveWormDown(float delta) {
		if (wormRectangle.y + wormRectangle.height > wormRectangle.height){
			wormRectangle.y-=speed;
		}
		
	}

	private void MoveWormRight(float delta) {
		
		if (wormRectangle.x < Constants.APP_WIDTH - wormRectangle.width) {
			wormRectangle.x+=speed;	
		}
	}
	
	private void MoveWormLeft(float delta) {
		if(wormRectangle.x + wormRectangle.width > wormRectangle.width){
			wormRectangle.x-=speed;
		}
	}

	
	private void addButtonsListeners() {

		btnLeft.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				currentButtonState = ButtonStates.LEFT_BUTTON_TOUCH_DOWN;

				return true;
			}

			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				currentButtonState = ButtonStates.LEFT_BUTTON_TOUCH_UP;
			}
		});

		btnRight.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				currentButtonState = ButtonStates.RIGHT_BUTTON_TOUCH_DOWN;
				return true;
			}

			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				currentButtonState = ButtonStates.RIGHT_BUTTON_TOUCH_UP;
			}
		});

		btnUp.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				currentButtonState = ButtonStates.UP_BUTTON_TOUCH_DOWN;

				return true;
			}

			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				currentButtonState = ButtonStates.UP_BUTTON_TOUCH_UP;
			}
		});

		btnDown.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				currentButtonState = ButtonStates.DOWN_BUTTON_TOUCH_DOWN;

				return true;
			}

			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				currentButtonState = ButtonStates.DOWN_BUTTON_TOUCH_UP;
			}
		});
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
