package com.woods.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
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
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.*;


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
    Viewport aViewport;
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
    float totalTimesRan, totalMovements, totalPlayerMovements, average;

    BitmapFont arrowKeyFont;

    public BoardScreen(Woods aGame, MenuScreen aScreen, int rows, int columns)
    {


        this.arrowKeyFont = new BitmapFont(Gdx.files.internal("monospace.fnt"));


        this.aScreen = aScreen;
        this.game = aGame;
        this.rows = rows;
        this.columns = columns;
        this.aShape = new ShapeRenderer();
        theCamera = aScreen.camera;
        aViewport = aScreen.aViewport;
        this.uiStage = new Stage(aViewport);

        Gdx.input.setInputProcessor(uiStage); //Without this, buttons and etc will not have their event listeners activated


        theCamera.setToOrtho(false);

        int rightSideBuffer = 0;
        int bottomEdgeBuffer = 0;

        //Subtracting the rightSideBuffer from theCamera.viewportWidth or height will leave blank space on the right side or bottom side
        aBoardController = new BoardController(rows, columns, (theCamera.viewportWidth - rightSideBuffer) / columns, (theCamera.viewportHeight - bottomEdgeBuffer) / rows, 4);
        aBoardController.createArray();
        aBoardController.createPlayersDefaultLocation();

        adventureMusic = Gdx.audio.newMusic(Gdx.files.internal("brazilian.mp3"));
        //adventureMusic.setLooping(true);
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
        //adventureMusic.play();
        uiStage.addActor(exitButton);
    }

    /**
     * Anything in this method will automatically start when this object screen opens
     */
    @Override
    public void show()
    {
        //adventureMusic.play();
        aBoardController.getAdventureMusic().play();
    }

    public boolean findCollisions()
    {
        return aBoardController.playerConflict();
    }

    @Override
    public void render(float delta)
    {
        ScreenUtils.clear(0, 0, 0.2f, 1);

        //theCamera.update();
        aShape.setProjectionMatrix(theCamera.combined);
        aBoardController.drawBoard(aShape);
        aBoardController.drawPlayers(aShape);

        game.batch.begin();
        game.monoFont.setColor(1, 1, 0, 1.3f);
        game.monoFont.draw(game.batch, "Total Moves -- " + aBoardController.totalPlayerMovements, 50, theCamera.viewportHeight - 10);
        game.monoFont.draw(game.batch, "Average: " + average, 50, theCamera.viewportHeight - 40);
        this.arrowKeyFont.draw(game.batch, "Press Left to slow or Right Arrow increase speed", 50, 50);
        this.arrowKeyFont.draw(game.batch, "ESC to exit or Press R to reset", 30, 20);
        game.batch.end();

        game.batch.begin();
        this.arrowKeyFont.draw(game.batch, "Rows: " + rows, 50, theCamera.viewportHeight - 70);
        this.arrowKeyFont.draw(game.batch, "Columns: " + columns, 50, theCamera.viewportHeight - 90);
        game.batch.end();
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

    /**
     * Updates the state of the game (collisions and movement) and collects keyboard input
     */
    public void update()
    {
        Input anInput = Gdx.input;

        if (anInput.isKeyPressed(Input.Keys.ESCAPE))
        {
            //ScreenUtils.clear(0, 0, 02.f, 1);
            adventureMusic.stop();
            changeScreens();
            //this.game.setScreen(new MenuScreen(game));
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
            aBoardController.getAdventureMusic().play();
            aBoardController.updatePlayers();
        }

        if (findCollisions() && stateOfGame == State.RUN)
        {
            stateOfGame = State.STOPPED;
            adventureMusic.stop();
            totalTimesRan++;
            totalMovements += aBoardController.totalPlayerMovements;
            average = totalMovements / totalTimesRan;
            //aBoardController.fade(aShape);
        }

        if (anInput.isKeyPressed(Input.Keys.R))
        {
            resetBoard();
        }

        resetButton.addListener(new ChangeListener()
        {
            @Override
            public void changed(ChangeEvent event, Actor actor)
            {
                resetBoard();
                //resetButton.setChecked(true);
                event.cancel();
            }
        });

        exitButton.addListener(new ChangeListener()
        {
            @Override
            public void changed(ChangeEvent event, Actor actor)
            {
                changeScreens();
            }
        });
    }

    private void changeScreens()
    {
        stateOfGame = State.STOPPED;
        aBoardController.getAdventureMusic().stop();
        //this.dispose();
        this.game.setScreen(aScreen);
        //this.game.setScreen(aScreen);
    }

    @Override
    public void resize(int width, int height)
    {
        aViewport.update(width, height);
        uiStage.getViewport().update(width, height);
    }

    /**
     * Resets the board and player location to defaults
     */
    private void resetBoard()
    {
        aBoardController.createArray();
        aBoardController.createPlayersDefaultLocation();
        stateOfGame = State.RUN;
        aBoardController.totalPlayerMovements = 0;
        aBoardController.playerUpdateTime = 0.3f;
        aBoardController.getAdventureMusic().stop();
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
        adventureMusic.stop();
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