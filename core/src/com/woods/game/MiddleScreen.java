package com.woods.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.ScreenUtils;

import javax.swing.*;

/**
 * Selection screen for the students BETWEEN kindergarten and 6-8
 */
public class MiddleScreen implements Screen
{

    public enum State
    {
        PAUSE,
        RUN,
        RESUME,
        STOPPED,
        FOUND,
        SELECTION
    }

    State gameState; //Used for setting the state of the game, paused, running, selection screen...etc
    Group labelGroup;
    Group textFieldGroup;
    Group buttonGroup;
    Group imageGroup;
    Group warningGroup;
    final Woods game;
    final MenuScreen aMenuScreen;
    ShapeRenderer aShape;
    BoardController aBoardController;
    MenuController aMenuController;
    Skin someSkin;
    Stage uiStage;
    Board boardOfScreen; //Used for dimensions of screen
    Background rainDropsBackground;
    float animationStateTime;
    int rows, columns, players;

    public MiddleScreen(Woods game, MenuScreen aMenuScreen, int rows, int columns)
    {
        this.rows = 10; //Default rows
        this.columns = 10; //Default columns
        this.players = 4; //Default Players
        this.game = game;
        this.aMenuScreen = aMenuScreen;
        aShape = new ShapeRenderer();
        someSkin = new Skin();
        uiStage = new Stage(game.aViewport);
        boardOfScreen = new Board(50, 50, game.camera.viewportWidth / 50,
                game.camera.viewportHeight / 50);
        rainDropsBackground = new Background(game.backgroundTextures, 30, .05f, game.camera, 4, 4);
        animationStateTime = 0f;
        aMenuController = new MenuController(game, this);
        aMenuController.createTextFields();
        aMenuController.createLabels();
        aMenuController.createButtons();
        aMenuController.createImages();
        buttonGroup = aMenuController.getButtonGroup();
        labelGroup = aMenuController.getLabelGroup();
        textFieldGroup = aMenuController.getTextFieldGroup();
        imageGroup = aMenuController.getImageGroup();
        warningGroup = new Group();

        addButtonsToStage();
        addLabelsToStage();
        addTextFieldsToStage();
        addImagesToStage();
        addWarningLabelsToState();
        gameState = State.SELECTION;
        //aBoardController = new BoardController(game, rows, columns, game.camera.viewportWidth / columns, game.camera.viewportHeight / rows);
    }

    private void update()
    {
        Input anInput = Gdx.input;

        if (gameState == State.SELECTION)
        {
            if (anInput.isKeyPressed(Input.Keys.ESCAPE))
            {
                game.setScreen(aMenuScreen);
            }

            if (aMenuController.getRows() < 2 || aMenuController.getRows() > 50)
            {
                warningGroup.addActor(aMenuController.getRowWarning());
            }
            else
            {
                warningGroup.removeActor(aMenuController.getRowWarning());
            }
        }
    }

    private void addLabelsToStage()
    {
        uiStage.addActor(labelGroup);
    }

    private void addTextFieldsToStage()
    {
        uiStage.addActor(textFieldGroup);
    }

    private void addButtonsToStage()
    {
        uiStage.addActor(buttonGroup);
    }

    private void addImagesToStage()
    {
        uiStage.addActor(imageGroup);
    }

    private void addWarningLabelsToState()
    {
        uiStage.addActor(warningGroup);
    }

    @Override
    public void show()
    {
        Gdx.input.setInputProcessor(uiStage);
        game.forestMusic.play();
        aMenuController.addListeners();

    }

    @Override
    public void render(float delta)
    {
        ScreenUtils.clear(0, 0, 0.2f, 1); //Just clears the screen to the specified color
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); //Clears buffer
        aShape.setProjectionMatrix(game.camera.combined);
        animationStateTime += Gdx.graphics.getDeltaTime(); // getDeltaTime() is the amount of time since the last frame rendered

        rainDropsBackground.draw(game.batch, animationStateTime);
        uiStage.act();
        uiStage.draw(); //Draws everything on the stage

        update();
    }

    /**
     * This adjusts and scales the screen if it is resized. Otherwise will look out of place and too small/big
     * @param width int
     * @param height int
     */
    @Override
    public void resize(int width, int height)
    {
        game.aViewport.update(width, height);
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
