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
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.*;

//TODO Make Screen Controller
/**
 * This will implement a 'Board screen' for the actual gameplay
 */
public class OldBoardScreen implements Screen
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
        FOUND,
    }

    public enum SelectionState
    {
        SELECT,
        DEFAULT
    }

    OrthographicCamera theCamera;
    Viewport aViewport;
    //Board aBoard;
    OldBoardController aBoardController;
    Woods game;
    ShapeRenderer aShape;
    int rows, columns, players;
    OldPlayer[] playerArray;
    Screen aScreen;
    State stateOfGame;
    SelectionState selectState;
    Stage uiStage;
    Group collisionStarsGroup;

    Button resetButton;
    Button exitButton;
    float totalTimesRan, totalMovements, average;
    Sprite aSprite;
    Skin someSkin;

    statistics statisticsFunc;
    found foundFunc;

    BitmapFont arrowKeyFont;

    public OldBoardScreen(final Woods aGame, Screen aScreen, final int rows, final int columns, int players, OldPlayer[] playerArray) throws CloneNotSupportedException
    {
        this.arrowKeyFont = new BitmapFont(Gdx.files.internal("monospace.fnt"));

        this.playerArray = playerArray;
        this.aScreen = aScreen;
        this.game = aGame;
        this.rows = rows;
        this.columns = columns;
        this.aShape = new ShapeRenderer();
        theCamera = aGame.camera;
        aViewport = aGame.aViewport;
        this.uiStage = new Stage(aViewport);
        someSkin = new Skin();
        this.collisionStarsGroup = new Group();

        int rightSideBuffer = 0;
        int bottomEdgeBuffer = 0;

        //Subtracting the rightSideBuffer from theCamera.viewportWidth or height will leave blank space on the right side or bottom side
        aBoardController = new OldBoardController(aGame, rows, columns, (game.camera.viewportWidth - rightSideBuffer) / columns,
                (game.camera.viewportHeight - bottomEdgeBuffer) / rows, players, playerArray);

        aBoardController.createArrayOfTextures(aGame.boardTextures);

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

        stateOfGame = State.RUN;
        selectState = SelectionState.SELECT;
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
                game.monoFont.setColor(1, 1, 0, 1.3f);
                game.medievalFont.draw(game.batch, "Total Moves -- " + aBoardController.totalPlayerMovements, 50, game.camera.viewportHeight - 10);
                game.monoFont.draw(game.batch, "Average: " + average, 50, game.camera.viewportHeight - 40);
                game.arrowKeyFont.draw(game.batch, "Rows: " + rows, 50, game.camera.viewportHeight - 70);
                game.arrowKeyFont.draw(game.batch, "Columns: " + columns, 50, game.camera.viewportHeight - 90);
            }
        };
    }

    public OldBoardScreen(final Woods aGame, Screen aScreen, final int rows, final int columns, int players)
    {
        this.arrowKeyFont = new BitmapFont(Gdx.files.internal("monospace.fnt"));

        this.aScreen = aScreen;
        this.game = aGame;
        this.rows = rows;
        this.columns = columns;
        this.aShape = new ShapeRenderer();
        theCamera = aGame.camera;
        aViewport = aGame.aViewport;
        this.uiStage = new Stage(aViewport);
        this.collisionStarsGroup = new Group();
        someSkin = new Skin();

        int rightSideBuffer = 0;
        int bottomEdgeBuffer = 0;

        //Subtracting the rightSideBuffer from theCamera.viewportWidth or height will leave blank space on the right side or bottom side
        aBoardController = new OldBoardController(aGame, rows, columns, (game.camera.viewportWidth - rightSideBuffer) / columns,
                (game.camera.viewportHeight - bottomEdgeBuffer) / rows, players);

        aBoardController.createPlayersDefaultLocation();
        aBoardController.createArrayOfTextures(aGame.boardTextures);

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

        stateOfGame = State.RUN;
        selectState = SelectionState.DEFAULT;
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
                game.monoFont.setColor(1, 1, 0, 1.3f);
                game.medievalFont.draw(game.batch, "Total Moves -- " + aBoardController.totalPlayerMovements, 50, game.camera.viewportHeight - 10);
                game.monoFont.draw(game.batch, "Average: " + average, 50, game.camera.viewportHeight - 40);
                game.arrowKeyFont.draw(game.batch, "Rows: " + rows, 50, game.camera.viewportHeight - 70);
                game.arrowKeyFont.draw(game.batch, "Columns: " + columns, 50, game.camera.viewportHeight - 90);
            }
        };
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
                try
                {
                    resetBoard();
                } catch (CloneNotSupportedException e)
                {
                    e.printStackTrace();
                }
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
        aShape.setProjectionMatrix(game.camera.combined);

        //Next few lines Draws Players and rectangles on board
        aShape.begin(ShapeRenderer.ShapeType.Line);
        aBoardController.drawBoard(aShape);
        aShape.setAutoShapeType(true);
        aShape.set(ShapeRenderer.ShapeType.Filled);
        aBoardController.drawConflict(aShape);
        aBoardController.drawPlayers(aShape);
        aShape.end();

        game.batch.begin();
        aBoardController.drawBoard(game.batch); //Draws textures on board
        aBoardController.drawDirections();
        this.arrowKeyFont.setColor(Color.MAGENTA);
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

        try
        {
            update();
        } catch (CloneNotSupportedException e)
        {
            e.printStackTrace();
        }

        if (stateOfGame == State.FOUND)
        {
            game.batch.begin();
            aBoardController.drawCollision(foundFunc);
            game.batch.end();
        }

    }

    /**
     * Updates the state of the game (collisions and movement) and collects keyboard input
     */
    public void update() throws CloneNotSupportedException
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

        if ( stateOfGame == State.RUN && findCollisions())
        {
            for (Animations anAnimations : aBoardController.conflictStars)
            {
                collisionStarsGroup.addActor(anAnimations);
            }
            uiStage.addActor(collisionStarsGroup);
            game.found.play();
            //stateOfGame = State.STOPPED;
            stateOfGame = State.FOUND;
            this.pause();
            totalTimesRan++;
            totalMovements += aBoardController.totalPlayerMovements; //Adds all the player movements so far in the game
            average = totalMovements / totalTimesRan;
            aBoardController.setAverage(average);
            //aBoardController.fade(aShape);
        }

        if (anInput.isKeyPressed(Input.Keys.R))
        {
            resetBoard();
        }

        if (anInput.isTouched())
        {
            //game.medievalFont.draw(game.batch, "hellos", 400, 400);
            System.out.println("meow");
            System.out.println(aViewport.unproject(new Vector2((anInput.getX()), anInput.getY())));
            /*sprite.setPosition(Gdx.input.getX() - sprite.getWidth()/2,
                    Gdx.graphics.getHeight() - Gdx.input.getY() - sprite.getHeight()/2);*/
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
        game.aViewport.update(width, height);
        uiStage.getViewport().update(width, height);
    }

    /**
     * Resets the board and player location to defaults
     */
    private void resetBoard() throws CloneNotSupportedException
    {
        if (selectState == SelectionState.DEFAULT)
        {
            aBoardController.createPlayersDefaultLocation();
            stateOfGame = State.RUN;
            aBoardController.totalPlayerMovements = 0;
            aBoardController.playerUpdateTime = 0.3f;
            aBoardController.clearCollisionStars(collisionStarsGroup, aBoardController.conflictStars);
            aBoardController.conflictStars.clear();

            this.pause();
        }
        else
        {
            aBoardController.setPlayersToOriginalLocation();
            stateOfGame = State.RUN;
            aBoardController.totalPlayerMovements = 0;
            aBoardController.playerUpdateTime = 0.3f;
            aBoardController.clearCollisionStars(collisionStarsGroup, aBoardController.conflictStars);
            aBoardController.conflictStars.clear();
            //collisionStarsGroup.removeActor(aBoardController.conflictStars.)
            this.pause();
        }
        //aBoardController.createArrayOfTextures(game.boardTextures);

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
        game.dispose();

    }
}