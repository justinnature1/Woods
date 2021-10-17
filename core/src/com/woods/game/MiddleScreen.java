package com.woods.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.ScreenUtils;

/**
 * Selection screen for the students BETWEEN kindergarten and 6-8
 */
public class MiddleScreen implements Screen, Menu
{

    public enum State
    {
        PAUSE,
        RUN,
        RESUME,
        STOPPED,
        FOUND,
        SELECTION,
        RANDOM,
        DEFAULT
    }

    MiddleScreen returnScreen;
    State gameState; //Used for setting the state of the game, paused, running, selection screen...etc
    Group labelGroup, textFieldGroup, defaultButtons, imageGroup, warningGroup, backgroundGroup;
    TextField rowTextField, colTextField, playerTextField;
    Button exitButton, startButton, selectButton, normalButton;
    Label rowLabel, colLabel, playerLabel, selectModeLabel;

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
    ButtonGroup<Button> buttonGroup;


    public MiddleScreen(Woods game, MenuScreen aMenuScreen, int rows, int columns)
    {
        this.returnScreen = this;
        this.rows = 10; //Default rows
        this.columns = 10; //Default columns
        this.players = 4; //Default Players
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
        defaultButtons = new Group();
        backgroundGroup = new Group();
        warningGroup = new Group();
        buttonGroup = new ButtonGroup<>();
        buttonGroup.setMaxCheckCount(1);
        buttonGroup.uncheckAll();

        addBackground();
        addButtons();
        addTextFields();
        addLabels();
        assembleMenu();

        gameState = State.DEFAULT;
        //aBoardController = new BoardController(game, rows, columns, game.camera.viewportWidth / columns, game.camera.viewportHeight / rows);
    }

    @Override
    public void addLabels()
    {
        rowLabel = aMenuController.getRowLabel();
        colLabel = aMenuController.getColumnLabel();
        playerLabel = aMenuController.getPlayerLabel();
        selectModeLabel = aMenuController.getSelectModeLabel();

        labelGroup.addActor(selectModeLabel);
        labelGroup.addActor(rowLabel);
        labelGroup.addActor(colLabel);
        labelGroup.addActor(playerLabel);
    }

    @Override
    public void addTextFields()
    {
        rowTextField = aMenuController.getRowTextField();
        colTextField = aMenuController.getColTextField();
        playerTextField = aMenuController.getPlayerTextField();
        textFieldGroup.addActor(rowTextField);
        textFieldGroup.addActor(colTextField);
        textFieldGroup.addActor(playerTextField);
    }

    @Override
    public void addBackground()
    {
        backgroundGroup = aMenuController.getBackgroundTreeImageGroup();
    }

    @Override
    public void addButtons()
    {
        startButton = aMenuController.getStartButton();
        exitButton = aMenuController.getExitButton();
        selectButton = aMenuController.getSelectButton();
        normalButton = aMenuController.getNormalButton();

        Table buttonTable = new Table();
        buttonTable.add(normalButton, selectButton);

        buttonGroup.add(selectButton);
        buttonGroup.add(normalButton);
        buttonGroup.uncheckAll();
        defaultButtons.addActor(normalButton);
        defaultButtons.addActor(exitButton);
        defaultButtons.addActor(startButton);
        defaultButtons.addActor(selectButton);
    }

    @Override
    public void assembleMenu()
    {
        backgroundStage.addActor(backgroundGroup);
        uiStage.addActor(textFieldGroup);
        uiStage.addActor(defaultButtons);
        uiStage.addActor(labelGroup);
        uiStage.addActor(exitButton);
    }

    @Override
    public void addListeners()
    {
        rowTextField.setTextFieldListener(new TextField.TextFieldListener()
        {
            @Override
            public void keyTyped(TextField textField, char c)
            {
                boolean done = false;

                while (!done)
                {
                    try
                    {
                        int tempRows = Integer.parseInt(textField.getText());
                        if (tempRows >= 2 && tempRows <= 50)
                        {
                            rows = tempRows;
                        }
                        done = true;
                    } catch (Exception e)
                    {
                        System.err.println(e);
                        break;
                    }
                }
            }
        });


        colTextField.setTextFieldListener(new TextField.TextFieldListener()
        {
            @Override
            public void keyTyped(TextField textField, char c)
            {
                boolean done = false;

                while (!done)
                {
                    try
                    {
                        int tempColumns = Integer.parseInt(textField.getText());
                        if (tempColumns >= 2 && tempColumns <= 50)
                        {
                            columns = tempColumns;
                            done = true;
                        } else
                        {
                            break;
                        }
                    } catch (Exception e)
                    {
                        System.err.println(e);
                        break;
                    }
                }
            }
        });

        startButton.addListener(new ChangeListener()
        {
            @Override
            public void changed(ChangeEvent event, Actor actor)
            {
                Button aButton = buttonGroup.getChecked();

                if (aButton == normalButton)
                {
                    game.setScreen(new BoardScreen(game, returnScreen, rows, columns, players));
                }
                else
                {
                    game.setScreen(new SelectionScreen(game, returnScreen, rows, columns, players));
                }

            }
        });

        exitButton.addListener(new ChangeListener()
        {
            @Override
            public void changed(ChangeEvent event, Actor actor)
            {
                game.setScreen(aMenuScreen);
            }
        });

        playerTextField.setTextFieldListener(new TextField.TextFieldListener()
        {
            public void keyTyped(TextField textField, char c)
            {
                boolean done = false;

                while (!done)
                {
                    try
                    {
                        int tempPlayers = Integer.parseInt(textField.getText());
                        if (tempPlayers >= 2 && tempPlayers <= 4)
                        {
                            players = tempPlayers;
                        }
                        done = true;
                    } catch (Exception e)
                    {
                        System.err.println(e);
                        break;
                    }
                }
            }
        });

        selectButton.addListener(new ChangeListener()
        {
            @Override
            public void changed(ChangeEvent event, Actor actor)
            {

            }
        });

        /*selectButton.addListener(new ChangeListener()
        {
            @Override
            public void changed(ChangeEvent event, Actor actor)
            {
                if (gameState == State.DEFAULT)
                {
                    gameState = State.SELECTION;
                }
                else
                {
                    gameState = State.DEFAULT;
                }
            }
        });*/
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
            } else
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
        uiStage.addActor(defaultButtons);
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
        addListeners();
    }

    @Override
    public void render(float delta)
    {
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
