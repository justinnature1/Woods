package com.woods.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.ScreenUtils;

/**
 * This is a screen that will enable students to select their position on the board
 */
public class SelectionScreen implements Screen, Menu
{
    public enum State
    {
        PAUSE,
        RUN,
        RESUME,
        STOPPED,
        FOUND,
        SELECTION,
        DONE
    }

    Woods game;
    Screen returnScreen;
    int rows, columns, players, playersToSelect;
    OldBoardController aBoardController;
    ShapeRenderer shapeRenderer;
    OldPlayer[] totalPlayersArray;
    State gameState;
    MenuController aMenuController;
    Button startButton, backButton;
    Group defaultButtons;
    Stage uiStage;

    public SelectionScreen(Woods game, Screen aScreen, int rows, int columns, int players)
    {
        game.camera.setToOrtho(false);
        this.game = game;
        this.returnScreen = aScreen;
        this.columns = columns;
        this.players = players;
        this.rows = rows;
        this.playersToSelect = players;
        this.shapeRenderer = new ShapeRenderer();
        aBoardController = new OldBoardController(game, rows, columns, game.camera.viewportWidth / columns,
                game.camera.viewportHeight / rows, players);
        aBoardController.createArrayOfTextures(game.boardTextures);
        this.totalPlayersArray = new OldPlayer[playersToSelect];
        gameState = State.SELECTION;
        aMenuController = new MenuController(game, this);
        this.uiStage = new Stage(game.aViewport);
        defaultButtons = new Group();
        addButtons();
        assembleMenu();
        addListeners();
    }

    @Override
    public void addBackground()
    {

    }

    @Override
    public void addButtons()
    {
        startButton = aMenuController.getStartButton();
        backButton = aMenuController.getBackButton();
        defaultButtons.addActor(backButton);
    }

    @Override
    public void assembleMenu()
    {
        uiStage.addActor(defaultButtons);
    }

    @Override
    public void addListeners()
    {
        backButton.addListener(new ChangeListener()
        {
            @Override
            public void changed(ChangeEvent event, Actor actor)
            {
                game.setScreen(returnScreen);
            }
        });

        startButton.addListener(new ChangeListener()
        {
            @Override
            public void changed(ChangeEvent event, Actor actor)
            {
                try
                {
                    game.setScreen(new OldBoardScreen(game, new SelectionScreen(game, returnScreen, rows, columns, players), rows, columns, players, totalPlayersArray));
                } catch (CloneNotSupportedException e)
                {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void addLabels()
    {

    }

    @Override
    public void addTextFields()
    {

    }

    @Override
    public boolean removeLabels()
    {
        return false;
    }

    @Override
    public boolean removeListeners()
    {
        return false;
    }

    @Override
    public void show()
    {
        Gdx.input.setInputProcessor(uiStage);
    }

    @Override
    public void render(float delta)
    {
        ScreenUtils.clear(0, 0, 0.2f, 1); //Just clears the screen to the specified color
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); //Clears buffer
        game.batch.setProjectionMatrix(game.camera.combined);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        aBoardController.drawBoard(shapeRenderer);
        shapeRenderer.end();

        update();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        OldBoardController.drawPlayers(shapeRenderer, totalPlayersArray);
        shapeRenderer.end();

        uiStage.act();
        uiStage.draw();
    }

    private void update()
    {
        Input anInput = Gdx.input;

        if (anInput.isTouched() && gameState == State.SELECTION)
        {
            float maxViewportWidth = game.aViewport.getScreenWidth();
            float maxViewportHeight = game.aViewport.getScreenHeight();

            int xLoc = anInput.getX();
            int yLoc = (int) maxViewportHeight - anInput.getY();

            float blackBarLeftWidth = game.aViewport.getLeftGutterWidth();
            float blackBarRightWidth = game.aViewport.getRightGutterWidth();
            float blackBarTopHeight = game.aViewport.getTopGutterHeight();

            float blockWidth = aBoardController.pixelBlockWidth;
            float blockHeight = aBoardController.pixelBlockHeight;
            /*float xArrayLocation = ((xLoc + blackBarRightWidth) / blockWidth);
            float yArrayLocation = (yLoc + blackBarTopHeight) / blockHeight;*/
            float yArrayLocation = yLoc / blockHeight;
            float xArrayLocation = xLoc / blockWidth;

            OldPlayer anotherPlayerToAdd = new OldPlayer((int) xArrayLocation, (int) yArrayLocation, Color.RED, blockWidth, blockHeight, "rawrs");

            boolean foundConflict = OldBoardController.playerConflict(totalPlayersArray, anotherPlayerToAdd);

            if (!foundConflict && playersToSelect > 0) //Does not find conflict, will add to total array of Players
            {
                totalPlayersArray[playersToSelect - 1] = anotherPlayerToAdd;
                --playersToSelect;

                if (playersToSelect == 0)
                {
                    gameState = State.DONE;
                }
            }
        }
        if (gameState == State.DONE)
        {
            defaultButtons.addActor(startButton);
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

    }

    @Override
    public void resume()
    {

    }

    @Override
    public void hide()
    {

    }

    @Override
    public void dispose()
    {

    }
}
