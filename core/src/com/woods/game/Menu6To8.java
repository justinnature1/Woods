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
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.ScreenUtils;

/**
 * Selection screen for the students BETWEEN kindergarten and 6-8
 */
public class Menu6To8 implements Screen, Menu {

    Menu6To8 menu6To8;
    Group labelGroup, textFieldGroup, buttonSelectionGroup, imageGroup, warningGroup, backgroundGroup;
    TextField rowTextField, colTextField;
    Button exitButton, startButton;
    Label rowLabel, colLabel;

    final Woods game;
    final MenuScreen aMenuScreen;
    ShapeRenderer aShape;
    BoardController aBoardController;
    MenuController aMenuController;
    Skin someSkin;
    Stage uiStage, backgroundStage;
    Board boardOfScreen; //Used for dimensions of screen
    Background rainDropsBackground;
    float animationStateTime;
    int rows, columns, players;

    public Menu6To8(Woods game, MenuScreen aMenuScreen, int rows, int columns) {
        menu6To8 = this;
        this.rows = rows; //Default rows
        this.columns = columns; //Default columns
        this.game = game;
        this.aMenuScreen = aMenuScreen;
        aShape = new ShapeRenderer();
        someSkin = new Skin();
        uiStage = new Stage(game.aViewport);
        backgroundStage = new Stage(game.aViewport);
        boardOfScreen = new Board(50, 50, game.camera.viewportWidth / 50,
                game.camera.viewportHeight / 50);
        rainDropsBackground = new Background(game.backgroundTextures, 30, .05f, game.camera, 4, 4);
        animationStateTime = 0f;
        aMenuController = new MenuController(game, this);
        labelGroup = new Group();
        textFieldGroup = new Group();
        buttonSelectionGroup = new Group();
        backgroundGroup = new Group();
        warningGroup = new Group();
        addBackground();
        addButtons();
        addTextFields();
        addLabels();
        assembleMenu();

        //aBoardController = new BoardController(game, rows, columns, game.camera.viewportWidth / columns, game.camera.viewportHeight / rows);
    }

    @Override
    public void addLabels() {
        rowLabel = aMenuController.getRowLabel();
        colLabel = aMenuController.getColumnLabel();
        labelGroup.addActor(rowLabel);
        labelGroup.addActor(colLabel);
    }

    @Override
    public void addTextFields() {
        rowTextField = aMenuController.getRowTextField();
        colTextField = aMenuController.getColTextField();
        textFieldGroup.addActor(rowTextField);
        textFieldGroup.addActor(colTextField);
    }

    @Override
    public void addBackground() {
        backgroundGroup = aMenuController.getBackgroundTreeImageGroup();
    }

    @Override
    public void addButtons() {
        startButton = aMenuController.getStartButton();
        exitButton = aMenuController.getExitButton();
        buttonSelectionGroup.addActor(exitButton);
        buttonSelectionGroup.addActor(startButton);
    }

    @Override
    public void assembleMenu() {
        backgroundStage.addActor(backgroundGroup);
        uiStage.addActor(textFieldGroup);
        uiStage.addActor(buttonSelectionGroup);
        uiStage.addActor(labelGroup);
        uiStage.addActor(exitButton);
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
    public void addListeners() {
        rowTextField.setTextFieldListener(new TextField.TextFieldListener() {
            @Override
            public void keyTyped(TextField textField, char c) {
                boolean done = false;

                while (!done) {
                    try {
                        int tempRows = Integer.parseInt(textField.getText());
                        if (tempRows >= 2 && tempRows <= 50) {
                            rows = tempRows;
                        }
                        done = true;
                    } catch (Exception e) {
                        System.err.println(e);
                        break;
                    }
                }
            }
        });


        colTextField.setTextFieldListener(new TextField.TextFieldListener() {
            @Override
            public void keyTyped(TextField textField, char c) {
                boolean done = false;

                while (!done) {
                    try {
                        int tempColumns = Integer.parseInt(textField.getText());
                        if (tempColumns >= 2 && tempColumns <= 50) {
                            columns = tempColumns;
                            done = true;
                        } else {
                            break;
                        }
                    } catch (Exception e) {
                        System.err.println(e);
                        break;
                    }
                }
            }
        });

        startButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.forestMusic.stop();
                game.setScreen(new BoardScreen(game, menu6To8, rows, columns));
            }
        });

        exitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(aMenuScreen);
            }
        });

    }

    private void update() {
        Input anInput = Gdx.input;

        if (anInput.isKeyPressed(Input.Keys.ESCAPE)) {
            game.setScreen(aMenuScreen);
        }

        if (aMenuController.getRows() < 2 || aMenuController.getRows() > 50) {
            warningGroup.addActor(aMenuController.getRowWarning());
        } else {
            warningGroup.removeActor(aMenuController.getRowWarning());
        }
    }

    private void addLabelsToStage() {
        uiStage.addActor(labelGroup);
    }

    private void addTextFieldsToStage() {
        uiStage.addActor(textFieldGroup);
    }

    private void addButtonsToStage() {
        uiStage.addActor(buttonSelectionGroup);
    }

    private void addImagesToStage() {
        uiStage.addActor(imageGroup);
    }

    private void addWarningLabelsToState() {
        uiStage.addActor(warningGroup);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(uiStage);
        game.forestMusic.play();
        addListeners();
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0.2f, 1); //Just clears the screen to the specified color
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); //Clears buffer
        aShape.setProjectionMatrix(game.camera.combined);
        animationStateTime += Gdx.graphics.getDeltaTime(); // getDeltaTime() is the amount of time since the last frame rendered

        rainDropsBackground.draw(game.batch, animationStateTime);
        backgroundStage.act();
        backgroundStage.draw();
        uiStage.act();
        uiStage.draw(); //Draws everything on the stage

        update();
    }

    /**
     * This adjusts and scales the screen if it is resized. Otherwise will look out of place and too small/big
     *
     * @param width  int
     * @param height int
     */
    @Override
    public void resize(int width, int height) {
        game.aViewport.update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
