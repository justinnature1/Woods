package com.woods.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.Viewport;

//TODO Add graphical characters to the Players class to be drawn

/**
 * This class will implement the Kindergarten gameplay for the screen
 */
public class KindergartenGamePlayBoard implements Screen
{

    /**
     * This will help set the 'state' of the game. Whether it is running or paused or etc.
     */
    public enum State
    {
        PAUSE,
        RUN,
        RESUME,
        STOPPED,
        FOUND
    }

    BoardController aBoardController;
    Woods game;
    ShapeRenderer aShape;
    int rows;
    int columns;
    Screen aScreen;
    State stateOfGame;
    Stage uiStage;
    Camera theCamera;
    Viewport aViewport;
    Skin someSkin;

    Button resetButton;
    Button exitButton;

    found foundFunc;
    statistics statisticsFunc;


    public KindergartenGamePlayBoard(final Woods aGame, MenuScreen aScreen, final int rows, final int columns)
    {
        this.aScreen = aScreen;
        this.game = aGame;
        this.rows = rows;
        this.columns = columns;
        this.aShape = new ShapeRenderer();
        theCamera = aScreen.camera;
        aViewport = aScreen.aViewport;
        this.uiStage = new Stage(aGame.aViewport);
        this.someSkin = new Skin();
        game.camera.setToOrtho(false);

        int rightSideBuffer = 0;
        int bottomEdgeBuffer = 0;

        //Subtracting the rightSideBuffer from theCamera.viewportWidth or height will leave blank space on the right side or bottom side
        aBoardController = new BoardController(aGame, rows, columns, (game.camera.viewportWidth - rightSideBuffer) / columns,
                (game.camera.viewportHeight - bottomEdgeBuffer) / rows, 4);
        aBoardController.createPlayersDefaultLocation();
        aBoardController.createArrayOfTextures(aGame.boardTextures);
        stateOfGame = State.RUN;

        Button.ButtonStyle exitButtonStyle = new Button.ButtonStyle();
        Texture exitTexture = game.menuTextures.get("Exit");
        someSkin.add("exit", exitTexture);
        TextureRegion exitRegion = new TextureRegion(exitTexture);
        exitButtonStyle.up = new TextureRegionDrawable(exitRegion);
        exitButtonStyle.over = someSkin.newDrawable("exit", Color.CORAL);
        exitButton = new Button(exitButtonStyle);
        exitButton.setWidth((float) exitTexture.getWidth() / 4);
        exitButton.setHeight((float) exitTexture.getHeight() / 4);
        exitButton.setColor(Color.CHARTREUSE.r, Color.CHARTREUSE.g, Color.CHARTREUSE.b, 0.8f);

        resetButton = game.buttons.get("reset");

        uiStage.addActor(resetButton);
        uiStage.addActor(exitButton);

        this.foundFunc = new found()
        {
            @Override
            public void drawCollision(SpriteBatch aBatch)
            {
                game.medievalFont.setColor(Color.MAGENTA.r, Color.MAGENTA.g, Color.MAGENTA.b, 1);
                game.medievalFont.draw(game.batch, "Players found eachother!", game.camera.viewportWidth / 2, game.camera.viewportHeight / 2, 20f, 1, true);
            }
        };

        this.statisticsFunc = new statistics()
        {
            @Override
            public void drawStatistics(SpriteBatch aBatch)
            {

                game.largeFont.setColor(Color.YELLOW.r, Color.YELLOW.g, Color.YELLOW.b, 1);

                game.largeFont.draw(game.batch, "Total Moves -- " + aBoardController.totalPlayerMovements,
                        1 * aBoardController.pixelBlockWidth, 9 * aBoardController.pixelBlockHeight);
                //TODO Make a larger font for rows/columns
                game.arrowKeyFont.draw(game.batch, "Rows: " + rows, 1 * aBoardController.pixelBlockWidth, 8.5f * aBoardController.pixelBlockHeight);
                game.arrowKeyFont.draw(game.batch, "Columns: " + columns, 1 * aBoardController.pixelBlockWidth, 8 * aBoardController.pixelBlockHeight);

            }
        };

    }

    /**
     * Anything in this method will automatically start when this object screen opens.
     * MUST put listeners for buttons/etc in here otherwise there will be processing delays.
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

    @Override
    public void render(float delta)
    {
        ScreenUtils.clear(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //theCamera.update();
        aShape.setProjectionMatrix(game.camera.combined);

        //Next few lines Draws Players and rectangles on board
        aShape.begin(ShapeRenderer.ShapeType.Line);
        aBoardController.drawBoard(aShape);
        aShape.setAutoShapeType(true);
        aShape.set(ShapeRenderer.ShapeType.Filled);
        aBoardController.drawConflict(aShape);
        aBoardController.drawPlayers(aShape);
        aShape.end();

        game.batch.begin(); //DO NOT Use too many being() and end() calls, this will flush the drawing buffer too much
        aBoardController.drawBoard(game.batch);
        aBoardController.drawDirections();
        aBoardController.drawStatistics(statisticsFunc);
        game.batch.end();

        uiStage.act();
        uiStage.draw();
        Input anInput = Gdx.input;

        //Will NOT unpause game if collision is found, must use reset button instead
        if (stateOfGame != State.FOUND && anInput.isKeyJustPressed(Input.Keys.SPACE))
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

        if (stateOfGame == State.FOUND)
        {
            game.batch.begin();
            aBoardController.drawCollision(foundFunc);
            game.batch.end();
        }

    }

    @Override
    public void resize(int width, int height)
    {
        game.aViewport.update(width, height);
        uiStage.getViewport().update(width, height);
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

    @Override
    public void dispose()
    {
        aShape.dispose();
        aScreen.dispose();
        uiStage.dispose();
        game.dispose();
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
            stateOfGame = State.FOUND;
            this.pause();
            //aBoardController.fade(aShape);
        }

        if (anInput.isKeyPressed(Input.Keys.R))
        {
            resetBoard();
        }
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

    /**
     * This will change the current screen back to the previous screen.
     */
    private void changeScreens()
    {
        stateOfGame = State.STOPPED;
        this.game.setScreen(aScreen);
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
}
