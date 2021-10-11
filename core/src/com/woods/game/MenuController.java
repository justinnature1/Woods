package com.woods.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import java.util.HashMap;

/**
 * This class will eventually abstract much of the menu objects and menu screens and avoid code redundancy
 */
public class MenuController
{
    private final Screen aScreen;
    Woods game;
    Group labelGroup; //Used for storing labels
    Group textFieldGroup; //Used for storing text fields
    Group buttonGroup; //Used for storing buttons
    Group imageGroup; //Used for storing some background images
    Skin someSkin; //libGDX specific class that is used for styling labels, buttons...etc

    //The screenDimensions Board object will be used for screen dimensions
    //It will be used to convert World coordinates to pixel coordinates
    Board screenDimensions;
    int rows, columns, amountOfPlayers;
    private TextField rowTextField;
    private TextField colTextField;
    private TextField playerTextField;
    private Button exitButton, startButton;

    private final int WORLD_WIDTH = 50;
    private final int WORLD_HEIGHT = 50;
    private Label rowWarning;
    private Label colWarning;
    private Label playerWarning;

    public MenuController(Woods game, Screen aScreen)
    {
        this.rows = 10;
        this.columns = 10;
        this.amountOfPlayers = 4;
        this.game = game;
        this.aScreen = aScreen;
        labelGroup = new Group();
        someSkin = new Skin();
        screenDimensions = new Board(WORLD_HEIGHT, WORLD_WIDTH, game.camera.viewportWidth / WORLD_WIDTH,
                game.camera.viewportHeight / WORLD_HEIGHT);
        textFieldGroup = new Group();
        buttonGroup = new Group();
        labelGroup = new Group();
        imageGroup = new Group();
    }

    public void createLabels()
    {
        //Below this code sets the style for the labels
        Label.LabelStyle selectionLabelStyle = new Label.LabelStyle();
        selectionLabelStyle.font = game.medievalFont;
        someSkin.add("default", selectionLabelStyle);
        Label rows = new Label("Rows: ", someSkin);
        Label columns = new Label("Columns: ", someSkin);
        Label players = new Label("Number of Players: ", someSkin);

        Label.LabelStyle warningLabelStyle = new Label.LabelStyle();
        warningLabelStyle.font = game.monoFont;
        warningLabelStyle.fontColor = Color.RED;
        someSkin.add("default", warningLabelStyle);
        rowWarning = new Label("2-50 only!", someSkin);
        colWarning = new Label("2-50 only!", someSkin);
        playerWarning = new Label("2-4 only!", someSkin);

        //Below this code creates the labels and sets their position on screen
        rows.setPosition(screenDimensions.blockPixelWidth * 19,
                screenDimensions.blockPixelHeight * 20);
        columns.setPosition(screenDimensions.blockPixelWidth * 19,
                screenDimensions.blockPixelHeight * 17);
        players.setPosition(screenDimensions.blockPixelWidth * 19 - players.getWidth() / 2,
                screenDimensions.blockPixelHeight * 14);
        rowWarning.setPosition(screenDimensions.blockPixelWidth * 32,
                screenDimensions.blockPixelHeight * 20);
        colWarning.setPosition(screenDimensions.blockPixelWidth * 19 + colWarning.getWidth(),
                screenDimensions.blockPixelHeight * 17);
        playerWarning.setPosition(screenDimensions.blockPixelWidth * 19 + playerWarning.getWidth(),
                screenDimensions.blockPixelHeight * 14);

        labelGroup.addActor(rows);
        labelGroup.addActor(columns);
        labelGroup.addActor(players);
    }

    public void createTextFields()
    {
        //This is necessary for colors to get rendered properly within the text fields
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        someSkin.add("white", new Texture(pixmap));
        someSkin.add("default", new BitmapFont());

        //Below this code sets the style of the text fields
        TextField.TextFieldStyle textFieldStyleThing = new TextField.TextFieldStyle();
        textFieldStyleThing.background = someSkin.newDrawable("white", Color.DARK_GRAY);
        textFieldStyleThing.font = game.medievalFont;
        textFieldStyleThing.fontColor = Color.WHITE;
        textFieldStyleThing.selection = someSkin.newDrawable("white", Color.CORAL);
        textFieldStyleThing.cursor = someSkin.newDrawable("white", Color.BLACK);
        textFieldStyleThing.focusedBackground = someSkin.newDrawable("white", Color.PURPLE);

        //Below this code defines the actual text fields and their position on the screen
        rowTextField = new TextField("2-50", textFieldStyleThing);
        colTextField = new TextField("2-50", textFieldStyleThing);
        playerTextField = new TextField("2-4", textFieldStyleThing);
        rowTextField.setX(screenDimensions.blockPixelWidth * 25);
        rowTextField.setY(screenDimensions.blockPixelHeight * 20);
        colTextField.setX(screenDimensions.blockPixelWidth * 25);
        colTextField.setY(screenDimensions.blockPixelHeight * 17);
        playerTextField.setX(screenDimensions.blockPixelWidth * 25);
        playerTextField.setY(screenDimensions.blockPixelHeight * 14);

        //Below code just adds various textfields to the textField group
        textFieldGroup.addActor(rowTextField);
        textFieldGroup.addActor(colTextField);
        textFieldGroup.addActor(playerTextField);
    }

    /**
     * Adds listeners to the various listener objects, such as textfields and buttons
     */
    public void addListeners()
    {
        TextField.TextFieldListener rowListener = new TextField.TextFieldListener()
        {
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
        };

        TextField.TextFieldListener colListener = new TextField.TextFieldListener()
        {
            public void keyTyped(TextField textField, char c)
            {
                boolean done = false;

                while (!done)
                {
                    try
                    {
                        int tempCols = Integer.parseInt(textField.getText());
                        if (tempCols >= 2 && tempCols <= 50)
                        {
                            columns = tempCols;
                        }
                        done = true;
                    } catch (Exception e)
                    {
                        System.err.println(e);
                        break;
                    }
                }
            }
        };

        TextField.TextFieldListener playersListener = new TextField.TextFieldListener()
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
                            amountOfPlayers = tempPlayers;
                        }
                        done = true;
                    } catch (Exception e)
                    {
                        System.err.println(e);
                        break;
                    }
                }
            }
        };

        ChangeListener exitListener = new ChangeListener()
        {
            @Override
            public void changed(ChangeEvent event, Actor actor)
            {
                game.setScreen(aScreen);
            }
        };

        ChangeListener startListener = new ChangeListener()
        {
            @Override
            public void changed(ChangeEvent event, Actor actor)
            {
                game.setScreen(new BoardScreen(game, aScreen, rows, columns, amountOfPlayers));
            }
        };

        rowTextField.setTextFieldListener(rowListener);
        colTextField.setTextFieldListener(colListener);
        playerTextField.setTextFieldListener(playersListener);
        exitButton.addListener(exitListener);
        startButton.addListener(startListener);
    }

    /**
     * Creates buttons for the screen. Such as a exit button and etc.
     */
    public void createButtons()
    {
        //Below code creates the style and location of a clickable button
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

        Button.ButtonStyle startButtonStyle = new Button.ButtonStyle();
        Texture startTexture = game.menuTextures.get("Start");
        someSkin.add("start", startTexture);
        TextureRegion startRegion = new TextureRegion(startTexture);
        startButtonStyle.up = new TextureRegionDrawable(startRegion);
        startButtonStyle.over = someSkin.newDrawable("start", Color.CORAL);
        startButton = new Button(startButtonStyle);
        startButton.setWidth((float) exitTexture.getWidth() / 2);
        startButton.setHeight((float) exitTexture.getHeight() / 2);
        startButton.setColor(Color.CHARTREUSE.r, Color.CHARTREUSE.g, Color.CHARTREUSE.b, 0.9f);
        startButton.setX(screenDimensions.blockPixelWidth * WORLD_WIDTH / 2);
        startButton.setY(screenDimensions.blockPixelHeight * 10);

        buttonGroup.addActor(startButton);
        buttonGroup.addActor(exitButton);
    }

    /**
     * Creates background images and some basic graphics
     */
    public void createImages()
    {
        int heightOfTree = 300;
        int widthOfTree = 300;
        Texture slantedTreeTexture = game.menuTextures.get("SlantedTree");
        Image slantedTreeImage = new Image(slantedTreeTexture);
        slantedTreeImage.setSize(widthOfTree, heightOfTree);
        slantedTreeImage.setX(screenDimensions.blockPixelWidth * WORLD_WIDTH - widthOfTree); //Subtract width to make sure it doesn't go off screen
        slantedTreeImage.setY(0);

        imageGroup.addActor(slantedTreeImage);
    }

    public Group getTextFieldGroup()
    {
        return textFieldGroup;
    }

    public Group getLabelGroup()
    {
        return labelGroup;
    }

    public Group getButtonGroup()
    {
        return buttonGroup;
    }

    public Group getImageGroup()
    {
        return imageGroup;
    }

    public Label getRowWarning()
    {
        return rowWarning;
    }

    public Label getColWarning()
    {
        return colWarning;
    }

    public Label getPlayerWarning()
    {
        return playerWarning;
    }

    public int getRows()
    {
        return rows;
    }

    public int getColumns()
    {
        return columns;
    }
}