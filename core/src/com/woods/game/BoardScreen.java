package com.woods.game;

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
import com.badlogic.gdx.math.Vector3;
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

        theCamera.setToOrtho(false);

        int rightSideBuffer = 0;
        int bottomEdgeBuffer = 0;

        //Subtracting the rightSideBuffer from theCamera.viewportWidth or height will leave blank space on the right side or bottom side
        aBoardController = new BoardController(rows, columns, (theCamera.viewportWidth - rightSideBuffer) / columns, (theCamera.viewportHeight - bottomEdgeBuffer) / rows, 4);

        aBoardController.createArrayOfTextures(aGame.boardTextures);
        aBoardController.createPlayersDefaultLocation();

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
        resetButton.setColor(Color.BROWN.r, Color.BROWN.g, Color.BROWN.b, 0.8f);
        exitButton.setColor(Color.CHARTREUSE.r, Color.CHARTREUSE.g, Color.CHARTREUSE.b, 0.8f);
        resetButton.setX(theCamera.viewportWidth - 110);

        exitButton.setHeight((float) exitTexture.getHeight() / 4);
        exitButton.setWidth((float) exitTexture.getWidth() / 4);
        resetButton.setHeight((float) resetTexture.getHeight() / 3);
        resetButton.setWidth((float) resetTexture.getWidth() / 3);
        uiStage.addActor(resetButton);
        uiStage.addActor(exitButton);
    }

    /**
     * Anything in this method will automatically start when this object screen opens.
     * Must put listeners for buttons/etc in here otherwise there will be processing delays.
     * DO NOT PUT Listeners in render()
     */
    @Override
    public void show()
    {
        Gdx.input.setInputProcessor(uiStage); //Without this, buttons and etc will not have their event listeners activated
        game.scaryMusic.play();
        game.scaryMusic.setVolume(0.1f);
        resetButton.addListener(new ChangeListener()
        {
            @Override
            public void changed(ChangeEvent event, Actor actor)
            {
                resetBoard();
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

    /**
     * Finds collisions among the players
     *
     * @return boolean
     */
    public boolean findCollisions()
    {
        return aBoardController.playerConflict();
    }


    @Override
    public void render(float delta)
    {
        ScreenUtils.clear(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //theCamera.update();
        aShape.setProjectionMatrix(theCamera.combined);

        //Next few lines Draws Players and rectangles on board
        aShape.begin(ShapeRenderer.ShapeType.Line);
        aBoardController.drawBoard(aShape);
        aShape.setAutoShapeType(true);
        aShape.set(ShapeRenderer.ShapeType.Filled);
        aBoardController.drawPlayers(aShape);
        aShape.end();

        game.batch.begin();
        aBoardController.drawBoard(game.batch); //Draws textures on board

        game.monoFont.setColor(1, 1, 0, 1.3f);
        game.medievalFont.draw(game.batch, "Total Moves -- " + aBoardController.totalPlayerMovements, 50, theCamera.viewportHeight - 10);
        game.monoFont.draw(game.batch, "Average: " + average, 50, theCamera.viewportHeight - 40);

        this.arrowKeyFont.setColor(Color.MAGENTA);

        game.medievalFont.setColor(1, 1, 0, 0.7f);
        game.medievalFont.draw(game.batch, "Press Left to slow or Right Arrow increase speed", theCamera.viewportWidth / 2 - 350, 50);
        game.medievalFont.draw(game.batch, "Press R to Reset", theCamera.viewportWidth - 250, 75);

        game.medievalFont.draw(game.batch, "ESC to exit", 0, 75);

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
        //TODO Write a pause text when pressing spacebar
        Input anInput = Gdx.input;

        if (anInput.isKeyPressed(Input.Keys.ESCAPE))
        {
            changeScreens();
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
            this.resume();
        }

        if (findCollisions() && stateOfGame == State.RUN)
        {
            game.found.play();
            stateOfGame = State.STOPPED;
            this.pause();
            totalTimesRan++;
            totalMovements += aBoardController.totalPlayerMovements;
            average = totalMovements / totalTimesRan;
            //aBoardController.fade(aShape);
        }

        if (anInput.isKeyPressed(Input.Keys.R))
        {
            resetBoard();
        }
    }

    /**
     * This will change the current screen back to the previous screen.
     */
    private void changeScreens()
    {
        stateOfGame = State.STOPPED;
        this.game.setScreen(aScreen);
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

        //aBoardController.createArrayOfTextures(game.boardTextures);
        aBoardController.createPlayersDefaultLocation();
        stateOfGame = State.RUN;
        aBoardController.totalPlayerMovements = 0;
        aBoardController.playerUpdateTime = 0.3f;
        this.pause();

    }

    @Override
    public void pause()
    {
        game.scaryMusic.pause();
    }

    @Override
    public void resume()
    {
        game.scaryMusic.play();
    }

    @Override
    public void hide()
    {
        game.scaryMusic.stop();
    }

    /**
     * Must dispose of game objects when finished, otherwise they can linger in the background
     */
    @Override
    public void dispose()
    {
        aShape.dispose();
        aScreen.dispose();
        uiStage.dispose();
        exitTexture.dispose();
        game.dispose();
    }
}


/*
Brazilian Street Fight by Punch Deck | https://soundcloud.com/punch-deck
Music promoted by https://www.chosic.com/free-music/all/
Creative Commons Attribution 3.0 Unported License
https://creativecommons.org/licenses/by/3.0/deed.en_US
 */