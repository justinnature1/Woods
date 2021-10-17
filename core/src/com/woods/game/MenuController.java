package com.woods.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;

/**
 * This class will eventually abstract much of the menu objects and menu screens and avoid code redundancy
 */
public class MenuController
{
    private final Screen aScreen;
    Woods game;
    private Group labelGroup; //Used for storing labels
    private Group textFieldGroup; //Used for storing text fields
    private Group buttonGroup; //Used for storing buttons
    private Group backgroundTreeImageGroup; //Used for storing some background images
    private Skin someSkin; //libGDX specific class that is used for styling labels, buttons...etc

    //The screenDimensions Board object will be used for screen dimensions
    //It will be used to convert World coordinates to pixel coordinates
    private Board screenDimensions;
    private int rows, columns, amountOfPlayers;
    private TextField rowTextField;
    private TextField colTextField;
    private TextField playerTextField;


    private Button exitButton, startButton, infoButton, backButton, okayButton, selectButton, normalButton;
    private ImageTextButton imageOfBunny, imageOfPig, imageOfCow;
    private Label welcomeLabel, rowLabel, columnLabel, playerLabel, gameInfoLabel, k2InfoLabel,
            threeToFiveInfoLabel, selectModeLabel;

    private final int WORLD_WIDTH = 50;
    private final int WORLD_HEIGHT = 50;
    private Label rowWarning;
    private Label colWarning;
    private Label playerWarning;

    private Background rainDropsBackground;

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
        backgroundTreeImageGroup = new Group();
        createTextFields();
        createButtons();
        createImages();
        createLabels();
    }

    public void createLabels()
    {
        //Below this code sets the style for the labels
        Label.LabelStyle selectionLabelStyle = new Label.LabelStyle();
        selectionLabelStyle.font = game.medievalFont;
        someSkin.add("default", selectionLabelStyle);
        rowLabel = new Label("Rows: ", someSkin);
        columnLabel = new Label("Columns: ", someSkin);
        playerLabel = new Label("Number of Players: ", someSkin);
        selectModeLabel = new Label("Press Select to choose starting spots " +
                "or choose Normal for standard spots", someSkin);
        selectModeLabel.setPosition(screenDimensions.blockPixelWidth * 5,
                screenDimensions.blockPixelHeight * 30);

        Label.LabelStyle gameInfoStyleLabel = new Label.LabelStyle();
        gameInfoStyleLabel.font = game.medievalFont;
        someSkin.add("default", gameInfoStyleLabel);
        gameInfoLabel = new Label("This game will simulate players 'lost in the woods'.\n'" +
                "Players will wander in random directions until they meet eachother.\n" +
                "Click on a grade level for more information.", someSkin);
        gameInfoLabel.setWrap(false);
        gameInfoLabel.setAlignment(Align.center);

        k2InfoLabel = new Label("This game mode is made for the youngest age group.\n" +
                "Game will start with 4 players with 10 rows and 10 columns.", someSkin);
        k2InfoLabel.setWrap(false);
        k2InfoLabel.setAlignment(Align.center);

        threeToFiveInfoLabel = new Label("This game mode is made for grades 3-5.\n" +
                "Students will be able to choose the amount of rows and columns\n" +
                "And the amount of players, including a starting position if desired.", someSkin);
        threeToFiveInfoLabel.setWrap(false);
        threeToFiveInfoLabel.setAlignment(Align.center);

        Label.LabelStyle welcomeStyle = new Label.LabelStyle();
        welcomeStyle.font = game.largeFont;
        someSkin.add("default", welcomeStyle);
        welcomeLabel = new Label("Welcome to Wandering in the Woods Game", someSkin);

        Label.LabelStyle warningLabelStyle = new Label.LabelStyle();
        warningLabelStyle.font = game.monoFont;
        warningLabelStyle.fontColor = Color.RED;
        someSkin.add("default", warningLabelStyle);
        rowWarning = new Label("2-50 only!", someSkin);
        colWarning = new Label("2-50 only!", someSkin);
        playerWarning = new Label("2-4 only!", someSkin);

        //Below this code creates the labels and sets their position on screen
        rowLabel.setPosition(screenDimensions.blockPixelWidth * 19,
                screenDimensions.blockPixelHeight * 20);
        columnLabel.setPosition(screenDimensions.blockPixelWidth * 19,
                screenDimensions.blockPixelHeight * 17);
        welcomeLabel.setPosition(screenDimensions.blockPixelWidth * 10, screenDimensions.blockPixelHeight * 35);
        playerLabel.setPosition(screenDimensions.blockPixelWidth * 19 - playerLabel.getWidth() / 2,
                screenDimensions.blockPixelHeight * 14);
        rowWarning.setPosition(screenDimensions.blockPixelWidth * 32,
                screenDimensions.blockPixelHeight * 20);
        colWarning.setPosition(screenDimensions.blockPixelWidth * 19 + colWarning.getWidth(),
                screenDimensions.blockPixelHeight * 17);
        playerWarning.setPosition(screenDimensions.blockPixelWidth * 19 + playerWarning.getWidth(),
                screenDimensions.blockPixelHeight * 14);

        labelGroup.addActor(rowLabel);
        labelGroup.addActor(columnLabel);
        labelGroup.addActor(playerLabel);
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
     * NOT necessary to use anymore. Use menu interface to implement listeners on screens instead
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



        rowTextField.setTextFieldListener(rowListener);
        colTextField.setTextFieldListener(colListener);
        playerTextField.setTextFieldListener(playersListener);
        exitButton.addListener(exitListener);
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
        exitButton.setWidth((float) exitTexture.getWidth() / 3);
        exitButton.setHeight((float) exitTexture.getHeight() / 3);
        exitButton.setColor(Color.CHARTREUSE.r, Color.CHARTREUSE.g, Color.CHARTREUSE.b, 0.8f);

        Button.ButtonStyle normalButtonStyle = new Button.ButtonStyle();
        Texture normalTexture = game.menuTextures.get("Normal");
        someSkin.add("normal", normalTexture);
        TextureRegion normalRegion = new TextureRegion(normalTexture);
        normalButtonStyle.up = new TextureRegionDrawable(normalRegion);
        normalButtonStyle.over = someSkin.newDrawable("normal", Color.CORAL);
        normalButtonStyle.checked = someSkin.newDrawable("normal", Color.GRAY);
        normalButton = new Button(normalButtonStyle);
        normalButton.setWidth(screenDimensions.blockPixelWidth * 5);
        normalButton.setHeight(screenDimensions.blockPixelHeight * 4);
        normalButton.setX(screenDimensions.blockPixelWidth * 25);
        normalButton.setY(screenDimensions.blockPixelHeight * 9);

        Button.ButtonStyle selectButtonStyle = new Button.ButtonStyle();
        Texture selectTexture = game.menuTextures.get("Select");
        someSkin.add("select", selectTexture);
        TextureRegion selectRegion = new TextureRegion(selectTexture);
        selectButtonStyle.up = new TextureRegionDrawable(selectRegion);
        selectButtonStyle.over = someSkin.newDrawable("select", Color.CORAL);
        selectButtonStyle.checked = someSkin.newDrawable("select", Color.DARK_GRAY);
        selectButton = new Button(selectButtonStyle);
        selectButton.setWidth((float) selectTexture.getWidth() / 3);
        selectButton.setHeight((float) selectTexture.getHeight() / 3);
        selectButton.setX(screenDimensions.blockPixelWidth * 20);
        selectButton.setY(screenDimensions.blockPixelHeight * 9);
        selectButton.setColor(Color.BLUE);


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
        startButton.setX(screenDimensions.blockPixelWidth * 22);
        startButton.setY(screenDimensions.blockPixelHeight * 5);

        Button.ButtonStyle infoButtonStyle = new Button.ButtonStyle();
        Texture infoTexture = game.menuTextures.get("Info");
        TextureRegion infoRegion = new TextureRegion(infoTexture);
        someSkin.add("info", infoTexture);
        infoButtonStyle.up = new TextureRegionDrawable(infoRegion);
        infoButtonStyle.down = someSkin.newDrawable("info", Color.DARK_GRAY);
        infoButtonStyle.over = someSkin.newDrawable("info", Color.CORAL);
        infoButton = new Button(infoButtonStyle);
        infoButton.setSize(screenDimensions.blockPixelWidth * 4, screenDimensions.blockPixelHeight * 3);
        infoButton.setX(screenDimensions.blockPixelWidth * 50 - infoButton.getWidth());


        //Placing button data in Main Game will lead to a LARGE slowdown
        ImageTextButton.ImageTextButtonStyle pigButtonStyle = new ImageTextButton.ImageTextButtonStyle();
        //pigButtonStyle.fontColor = Color.RED;
        pigButtonStyle.font = game.largeFont;
        pigButtonStyle.overFontColor = Color.RED;
        TextureRegion bunnyRegion = new TextureRegion(game.menuTextures.get("SleepingBunny"));
        someSkin.add("bunny", bunnyRegion);
        pigButtonStyle.up = new TextureRegionDrawable(bunnyRegion);
        pigButtonStyle.over = someSkin.newDrawable("bunny", Color.CORAL);
        imageOfBunny = new ImageTextButton("K-2", pigButtonStyle);
        imageOfBunny.setX(5 * screenDimensions.blockPixelWidth);
        imageOfBunny.setY(8 * screenDimensions.blockPixelHeight);
        imageOfBunny.setSize(300, 200);

        Texture pigTexture = game.menuTextures.get("Pig");
        TextureRegion pigRegion = new TextureRegion(pigTexture);
        ImageTextButton.ImageTextButtonStyle pigStyle = new ImageTextButton.ImageTextButtonStyle();
        pigStyle.overFontColor = Color.RED;
        pigStyle.font = game.largeFont;
        Image pigImage = new Image(pigRegion);
        pigImage.setSize(screenDimensions.blockPixelWidth * 5, screenDimensions.blockPixelHeight * 5);
        pigImage.setX(25 * screenDimensions.blockPixelWidth);
        someSkin.add("pig", pigRegion);
        pigStyle.up = new TextureRegionDrawable(pigRegion);
        pigStyle.over = someSkin.newDrawable("pig", Color.CORAL);
        imageOfPig = new ImageTextButton("3-5", pigStyle);
        imageOfPig.setY(5 * screenDimensions.blockPixelHeight);
        imageOfPig.setX(15 * screenDimensions.blockPixelWidth);



        Button.ButtonStyle backStyle = new Button.ButtonStyle();
        Texture backTexture = new Texture(Gdx.files.internal("back.png"));
        backStyle.up = new TextureRegionDrawable(backTexture);
        backStyle.over = new TextureRegionDrawable(backTexture).tint(Color.CYAN);
        backButton = new Button(backStyle);
        backButton.setColor(Color.CHARTREUSE.r, Color.CHARTREUSE.g, Color.CHARTREUSE.b, 0.5f);
        backButton.setSize(5 * screenDimensions.blockPixelWidth,
                3 * screenDimensions.blockPixelHeight);

        Button.ButtonStyle okayStyle = new Button.ButtonStyle();
        Texture okayTexture = new Texture(Gdx.files.internal("okay2.png"));
        okayStyle.up = new TextureRegionDrawable(okayTexture);
        okayStyle.over = new TextureRegionDrawable(okayTexture).tint(Color.CYAN);
        okayButton = new Button(okayStyle);
        okayButton.setColor(Color.CHARTREUSE.r, Color.CHARTREUSE.g, Color.CHARTREUSE.b, 0.5f);
        okayButton.setSize(5 * screenDimensions.blockPixelWidth,
                3 * screenDimensions.blockPixelHeight);


        Texture cowTexture = game.menuTextures.get("Cow");
        TextureRegion cowRegion = new TextureRegion(cowTexture);
        ImageTextButton.ImageTextButtonStyle cowStyle = new ImageTextButton.ImageTextButtonStyle();
        cowStyle.overFontColor = Color.RED;
        cowStyle.fontColor = Color.BLACK;
        cowStyle.font = game.largeFont;
        Image cowImage = new Image(cowRegion);
        cowImage.setSize(screenDimensions.blockPixelWidth * 5, screenDimensions.blockPixelHeight * 5);
        cowImage.setX(25 * screenDimensions.blockPixelWidth);
        someSkin.add("cow", cowRegion);
        cowStyle.up = new TextureRegionDrawable(cowRegion);
        cowStyle.over = someSkin.newDrawable("cow", Color.CORAL);
        imageOfCow = new ImageTextButton("6-8", cowStyle);
        imageOfCow.setY(8 * screenDimensions.blockPixelHeight);
        imageOfCow.setX(28 * screenDimensions.blockPixelWidth);

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
        slantedTreeImage.setSize(screenDimensions.blockPixelWidth * 14, screenDimensions.blockPixelHeight * 22);
        slantedTreeImage.setScale(.9f);
        slantedTreeImage.setX(game.camera.viewportWidth - slantedTreeImage.getWidth()); //Subtract width to make sure it doesn't go off screen
        slantedTreeImage.setY(0);

        Texture deadTreeTexture = game.menuTextures.get("DeadTree");
        TextureRegion deadTreeRegion = new TextureRegion(deadTreeTexture);
        //ImageButton.ImageButtonStyle imageStyle = new ImageButton.ImageButtonStyle();
        Image deadTreeImage = new Image(deadTreeRegion);
        deadTreeImage.setSize(screenDimensions.blockPixelWidth * 10, screenDimensions.blockPixelHeight * 20);
        float yPositionDeadTree = screenDimensions.blockPixelHeight * 30;
        deadTreeImage.setY(yPositionDeadTree);
        someSkin.add("tree", deadTreeRegion);

        rainDropsBackground = new Background(game.backgroundTextures, 30,
                .05f, game.camera, 4, 4);

        backgroundTreeImageGroup.addActor(deadTreeImage);
        backgroundTreeImageGroup.addActor(slantedTreeImage);
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

    public Group getBackgroundTreeImageGroup()
    {
        return backgroundTreeImageGroup;
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

    public Button getExitButton()
    {
        return exitButton;
    }

    public void setExitButton(Button exitButton)
    {
        this.exitButton = exitButton;
    }

    public Button getStartButton()
    {
        return startButton;
    }

    public void setStartButton(Button startButton)
    {
        this.startButton = startButton;
    }

    public Button getInfoButton()
    {
        return infoButton;
    }

    public void setInfoButton(Button infoButton)
    {
        this.infoButton = infoButton;
    }

    public TextField getRowTextField()
    {
        return rowTextField;
    }

    public void setRowTextField(TextField rowTextField)
    {
        this.rowTextField = rowTextField;
    }

    public TextField getColTextField()
    {
        return colTextField;
    }

    public void setColTextField(TextField colTextField)
    {
        this.colTextField = colTextField;
    }

    public TextField getPlayerTextField()
    {
        return playerTextField;
    }

    public void setPlayerTextField(TextField playerTextField)
    {
        this.playerTextField = playerTextField;
    }

    public ImageTextButton getImageOfBunny()
    {
        return imageOfBunny;
    }

    public void setImageOfBunny(ImageTextButton imageOfBunny)
    {
        this.imageOfBunny = imageOfBunny;
    }

    public ImageTextButton getImageOfPig()
    {
        return imageOfPig;
    }

    public void setImageOfPig(ImageTextButton imageOfPig)
    {
        this.imageOfPig = imageOfPig;
    }

    public ImageTextButton getImageOfCow()
    {
        return imageOfCow;
    }

    public void setImageOfCow(ImageTextButton imageOfCow)
    {
        this.imageOfCow = imageOfCow;
    }

    public Label getWelcomeLabel()
    {
        return welcomeLabel;
    }

    public void setWelcomeLabel(Label welcomeLabel)
    {
        this.welcomeLabel = welcomeLabel;
    }

    public Label getRowLabel()
    {
        return rowLabel;
    }

    public void setRowLabel(Label rowLabel)
    {
        this.rowLabel = rowLabel;
    }

    public Label getColumnLabel()
    {
        return columnLabel;
    }

    public void setColumnLabel(Label columnLabel)
    {
        this.columnLabel = columnLabel;
    }

    public Label getPlayerLabel()
    {
        return playerLabel;
    }

    public void setPlayerLabel(Label playerLabel)
    {
        this.playerLabel = playerLabel;
    }

    public Button getBackButton()
    {
        return backButton;
    }

    public void setBackButton(Button backButton)
    {
        this.backButton = backButton;
    }

    public Background getRainDropsBackground()
    {
        return rainDropsBackground;
    }

    public void setRainDropsBackground(Background rainDropsBackground)
    {
        this.rainDropsBackground = rainDropsBackground;
    }

    public Label getGameInfoLabel()
    {
        return gameInfoLabel;
    }

    public void setGameInfoLabel(Label gameInfoLabel)
    {
        this.gameInfoLabel = gameInfoLabel;
    }

    public Label getK2InfoLabel()
    {
        return k2InfoLabel;
    }

    public Button getOkayButton()
    {
        return okayButton;
    }

    public Label getThreeToFiveInfoLabel()
    {
        return threeToFiveInfoLabel;
    }

    public Button getSelectButton()
    {
        return selectButton;
    }

    public Button getNormalButton()
    {
        return normalButton;
    }

    public Label getSelectModeLabel()
    {
        return selectModeLabel;
    }

    public Group getImageGroup()
    {
        return null;
    }
}