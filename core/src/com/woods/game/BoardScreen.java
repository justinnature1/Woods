package com.woods.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.sun.prism.shader.Solid_TextureRGB_AlphaTest_Loader;


/**
 * This will implement a 'Board screen' for the actual gameplay
 */
public class BoardScreen implements Screen
{
    /**
     * This will help set the 'state' of the game. Whether it is running or paused or etc.
     */
    public enum State
    {
        PAUSE,
        RUN,
        RESUME,
        STOPPED
    }

    OrthographicCamera theCamera;
    //Board aBoard;
    BoardController aBoardController;
    Woods game;
    ShapeRenderer aShape;
    int rows;
    int columns;
    Screen aScreen;
    Music adventureMusic;
    boolean conflict;
    State stateOfGame;
    Stage uiStage;

    final Button.ButtonStyle resetButtonStyle;
    final Button.ButtonStyle exitButtonStyle;
    Texture resetTexture;
    Texture exitTexture;
    TextureRegion resetRegion;
    TextureRegion exitRegion;
    Button resetButton;
    Button exitButton;

    BitmapFont arrowKeyFont;

    public BoardScreen(Woods aGame, Screen aScreen, int rows, int columns)
    {

        this.uiStage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(uiStage); //Without this, buttons and etc will not have their event listeners activated

        this.arrowKeyFont = new BitmapFont(Gdx.files.internal("monospace.fnt"));


        this.aScreen = aScreen;
        this.game = aGame;
        this.rows = rows;
        this.columns = columns;
        this.aShape = new ShapeRenderer();
        theCamera = new OrthographicCamera();
        theCamera.setToOrtho(true, 800, 480);

        int rightSideBuffer = 0;
        int bottomEdgeBuffer = 0;

        //Subtracting the rightSideBuffer from theCamera.viewportWidth or height will leave blank space on the right side or bottom side
        aBoardController = new BoardController(rows, columns, (theCamera.viewportWidth - rightSideBuffer) / columns, (theCamera.viewportHeight - bottomEdgeBuffer) / rows, 4);
        aBoardController.createArray();
        aBoardController.createPlayersDefaultLocation();

        adventureMusic = Gdx.audio.newMusic(Gdx.files.internal("brazilian.mp3"));
        adventureMusic.setLooping(true);
        conflict = false;
        stateOfGame = State.RUN;
        Skin someSkin = new Skin();

        resetButtonStyle = new Button.ButtonStyle();
        exitButtonStyle = new Button.ButtonStyle();

        resetTexture = new Texture(Gdx.files.internal("reset.png"));
        exitTexture = new Texture(Gdx.files.internal("exit.png"));
        exitRegion = new TextureRegion(exitTexture);
        resetRegion = new TextureRegion(resetTexture);
        someSkin.add("white", resetTexture);
        someSkin.add("black", exitTexture);


        resetButtonStyle.up = new TextureRegionDrawable(resetRegion);
        exitButtonStyle.up = new TextureRegionDrawable(exitRegion);
        resetButtonStyle.checked = someSkin.newDrawable("white", Color.DARK_GRAY);

        resetButtonStyle.over = someSkin.newDrawable("white", Color.LIME); //This adds a new drawable using the white skin and applying Color.Lime
        exitButtonStyle.over = someSkin.newDrawable("black", Color.CORAL);

        exitButton = new Button(exitButtonStyle);
        resetButton = new Button(resetButtonStyle);
        resetButton.setColor(Color.BROWN.r, Color.BROWN.g, Color.BROWN.b, 0.5f);
        exitButton.setColor(Color.CHARTREUSE.r, Color.CHARTREUSE.g, Color.CHARTREUSE.b, 0.5f);
        resetButton.setX(theCamera.viewportWidth - 150);
        resetButton.setY(10);
        exitButton.setY(10);
        exitButton.setX(10);

        exitButton.setHeight((float) exitTexture.getHeight() / 6);
        exitButton.setWidth((float) exitTexture.getWidth() / 6);
        resetButton.setHeight((float) resetTexture.getHeight() / 3);
        resetButton.setWidth((float) resetTexture.getWidth() / 3);
        uiStage.addActor(resetButton);
        uiStage.addActor(exitButton);

    }

    /**
     * Anything in this method will automatically start when this object screen opens
     */
    @Override
    public void show()
    {
         adventureMusic.play();
    }

    public boolean findCollisions()
    {
        return aBoardController.playerConflict();
    }

    @Override
    public void render(float delta)
    {
        //Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        //Gdx.gl.glClearColor(0, 0, 0, 0);
        ScreenUtils.clear(0, 0, 0.2f, 1);
        //Gdx.gl.glEnable(GL20.GL_BLEND);
        //Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        theCamera.update();
        //game.batch.setProjectionMatrix(theCamera.combined);
        aShape.setProjectionMatrix(theCamera.combined);
        aBoardController.drawBoard(aShape);
        //Gdx.gl.glDisable(GL20.GL_BLEND);

        //aBoardController.drawPlayers(aShape);
        game.batch.begin();
        game.font.setColor(1, 1, 0, 1.3f);
        game.font.draw(game.batch, "Total Moves -- " + aBoardController.totalPlayerMovements, 100, 150);
        this.arrowKeyFont.draw(game.batch, "Press Left or Right Arrow keys to speed up", 100, 100);
        game.batch.end();
        /*aShape.begin(ShapeRenderer.ShapeType.Filled);
        aShape.setColor(Color.FOREST);
        aShape.circle(0, 0, 30);
        aShape.end();*/
        /*game.batch.begin();
        resetButton.draw(game.batch, 20);
        game.batch.end();*/
        uiStage.act();
        uiStage.draw();
        Input anInput = Gdx.input;

        if (anInput.isKeyJustPressed(Input.Keys.SPACE))
        {
            if (stateOfGame == State.RUN)
            {
                stateOfGame = State.PAUSE;
            } else
            {
                stateOfGame = State.RUN;
            }
        }
        update();

    }

    public void update()
    {
        Input anInput = Gdx.input;

        if (anInput.isKeyPressed(Input.Keys.ESCAPE))
        {
            //ScreenUtils.clear(0, 0, 02.f, 1);
            this.game.setScreen(new MenuScreen(game));
            //hide();
        }

        if (stateOfGame == State.RUN)
        {
            if (anInput.isKeyPressed(Input.Keys.RIGHT))
            {
                aBoardController.increaseSpeed();
            }
            if (anInput.isKeyPressed(Input.Keys.LEFT))
            {
                aBoardController.decreaseSpeed();
            }
            aBoardController.updatePlayers();
        }

        if (findCollisions() && stateOfGame == State.RUN)
        {
            stateOfGame = State.STOPPED;
            adventureMusic.stop();
            //aBoardController.fade(aShape);
        }

        resetButton.addListener(new ChangeListener()
        {
            @Override
            public void changed(ChangeEvent event, Actor actor)
            {
                aBoardController.createArray();
                aBoardController.createPlayersDefaultLocation();
                stateOfGame = State.RUN;
                aBoardController.totalPlayerMovements = 0;
                //resetButton.setChecked(true);
                event.cancel();
            }
        });

        exitButton.addListener(new ChangeListener()
        {
            @Override
            public void changed(ChangeEvent event, Actor actor)
            {
                //Woods aWoods = new Woods();
                //aWoods.setScreen(new MenuScreen(aWoods));
                changeScreens();
            }
        });
    }

    private void changeScreens()
    {
        this.game.setScreen(new MenuScreen(game));
    }

    @Override
    public void resize(int width, int height)
    {

    }

    @Override
    public void pause()
    {

    }

    @Override
    public void resume()
    {

    }

    @Override
    public void hide()
    {
        this.dispose();
    }

    /**
     * Must dispose of game objects when finished, otherwise they can linger in the background
     */
    @Override
    public void dispose()
    {
        aShape.dispose();
        aScreen.dispose();
        adventureMusic.dispose();
        uiStage.dispose();
        //game.dispose();
    }
}


/*
Brazilian Street Fight by Punch Deck | https://soundcloud.com/punch-deck
Music promoted by https://www.chosic.com/free-music/all/
Creative Commons Attribution 3.0 Unported License
https://creativecommons.org/licenses/by/3.0/deed.en_US
 */