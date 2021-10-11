package com.woods.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.*;

import static com.badlogic.gdx.Input.*;

/**
 * This implements a menu screen for the game.
 */
public class MenuScreen implements Screen
{
    Stage someStage; //libGDX object that stores game 'actors' to be drawn on the screen and manipulated
    Woods game; //Reference to the main game
    MenuController menuControl; //Creates and adds menu items...text fields and labels, etc

    OrthographicCamera camera;
    Viewport aViewport;
    Board aBoard;
    ShapeRenderer aShape;
    SpriteBatch aBatch;
    int rows;
    int columns;
    Texture lightningTexture;
    Texture rainTextureOne;
    Texture rainTextureTwo;
    Animation<TextureRegion> lightningAnimation;
    float animationStatetime;
    TextField rowTextField;
    TextField colTextField;

    TextField.TextFieldStyle rowStyle;
    Skin someSkin;

    String initialText, dialogue, message;
    boolean display;
    BitmapFont aFont;
    Table rootTable;
    TextButton startButton;
    Button exitButton;
    Button.ButtonStyle buttonStyle;
    Texture exitTexture;
    Background raindropsBackground;
    ImageButton imageButtonOfTree, imageOfBunny;
    Music forestMusic;


    public MenuScreen(Woods game)
    {
        this.forestMusic = Gdx.audio.newMusic(Gdx.files.internal("nightForest.mp3"));

        this.forestMusic.setLooping(true);
        this.aFont = new BitmapFont();
        this.aBatch = new SpriteBatch();
        this.game = game;
        this.aShape = new ShapeRenderer();
        this.camera = game.camera;
        this.rootTable = new Table();
        camera.setToOrtho(false);
        //camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        aViewport = game.aViewport;
        aViewport.apply();
        aBoard = new Board(50, 50, camera.viewportWidth / 50,
                camera.viewportHeight / 50);
        //aViewport.setScreenPosition(10, 10);

        someStage = new Stage(aViewport);

        this.columns = 10;
        this.rows = 10;
        lightningTexture = new Texture(Gdx.files.internal("lightning.png"));

        animationStatetime = 0f;

        raindropsBackground = new Background(game.backgroundTextures, 30, .05f, camera, 4, 4);

        createButtons();
        createTexture();
        addVisualTextures();

    }

    /**
     * Just a temporary method until a proper texture animation class is created
     */
    public void createTexture()
    {
        TextureRegion[] lightningFrames = new TextureRegion[5];

        TextureRegion[][] tempers = TextureRegion.split(lightningTexture, lightningTexture.getWidth() / 5, lightningTexture.getHeight());

        int index = 0;
        for (TextureRegion[] arrayOfRegions : tempers)
        {
            for (TextureRegion aRegion : arrayOfRegions)
            {
                lightningFrames[index] = aRegion;
                index++;
            }
        }

        lightningAnimation = new Animation<TextureRegion>(0.025f, lightningFrames);
    }

    @Override
    public void show()
    {
        Gdx.input.setInputProcessor(someStage);
        game.forestMusic.play();
        game.forestMusic.setVolume(0.1f);
        createListeners();

    }

    private void createButtons()
    {
        someSkin = new Skin();

        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        someSkin.add("white", new Texture(pixmap));
        someSkin.add("default", new BitmapFont());


        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = game.medievalFont;
        textButtonStyle.fontColor = Color.CHARTREUSE;
        textButtonStyle.up = someSkin.newDrawable("white", Color.FIREBRICK);
        textButtonStyle.down = someSkin.newDrawable("white", Color.BLACK);
        textButtonStyle.checked = someSkin.newDrawable("white", Color.BLUE);
        textButtonStyle.over = someSkin.newDrawable("white", Color.CYAN);

        buttonStyle = new Button.ButtonStyle();

        Texture slantedTreeTexture = game.menuTextures.get("SlantedTree");
        Image slantedTreeImage = new Image(slantedTreeTexture);
        slantedTreeImage.setSize(300, 300);
        slantedTreeImage.setX(camera.viewportWidth - 400);
        slantedTreeImage.setY(camera.viewportHeight - 450);

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


        someSkin.add("default", textButtonStyle);

        startButton = new TextButton("START", textButtonStyle);

        rowStyle = new TextField.TextFieldStyle();
        rowStyle.font = game.medievalFont;
        rowStyle.fontColor = Color.WHITE;

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = game.medievalFont;
        someSkin.add("default", labelStyle);

        Label rowLabel = new Label("Rows: ", someSkin);
        Label colLabel = new Label("Columns:  ", someSkin);
        Label kinderGartenLabel = new Label("K-2", someSkin);
        kinderGartenLabel.setPosition(10 * aBoard.blockPixelWidth, 5 * aBoard.blockPixelHeight);
        Label welcome = new Label("Welcome to Wandering Woods", someSkin);
        Label clickToBegin = new Label("Click Tree to begin", someSkin);
        Label cleanUI = new Label("This UI will be cleaned and re-positioned soon", someSkin);
        cleanUI.setPosition(5 * aBoard.blockPixelWidth, 45 * aBoard.blockPixelHeight);

        Texture treeTexture = game.menuTextures.get("DeadTree");
        TextureRegion treeRegion = new TextureRegion(treeTexture);
        ImageButton.ImageButtonStyle imageStyle = new ImageButton.ImageButtonStyle();
        Image treeImage = new Image(treeRegion);
        treeImage.setSize(400, 400);
        treeImage.setY(camera.viewportHeight - 500);
        someSkin.add("tree", treeRegion);
        imageStyle.up = new TextureRegionDrawable(treeRegion);
        imageStyle.over = someSkin.newDrawable("tree", Color.RED);
        imageButtonOfTree = new ImageButton(imageStyle);

        //Placing button data in Main Game will lead to a LARGE slowdown
        ImageButton.ImageButtonStyle gradeButtonStyle = new ImageButton.ImageButtonStyle();
        TextureRegion bunnyRegion = new TextureRegion(game.menuTextures.get("SleepingBunny"));
        someSkin.add("bunny", bunnyRegion);
        gradeButtonStyle.up = new TextureRegionDrawable(bunnyRegion);
        gradeButtonStyle.over = someSkin.newDrawable("bunny", Color.CORAL);
        imageOfBunny = new ImageButton(gradeButtonStyle);

        rowTextField = game.textFields.get("Row");
        rowTextField.setText("2-50");
        colTextField = game.textFields.get("Col");
        colTextField.setText("2-50");

        rootTable = new Table();
        rootTable.setFillParent(true);
        Table someTable = new Table();
        someTable.add(welcome);
        someTable.setFillParent(true);


        rootTable.add(welcome).fill().padBottom(100);
        rootTable.row().align(0);
        rootTable.add(rowLabel);
        rootTable.add(rowTextField);
        rootTable.row(); //Adds new row
        rootTable.add(colLabel);
        rootTable.add(colTextField);
        rootTable.row(); //Adds new row
        rootTable.add(imageButtonOfTree).size(100, 100);
        rootTable.add(clickToBegin);

        //sleepingBunny = aGame.imageButtons.get("SleepingBunny");
        imageOfBunny.setX(5 * aBoard.blockPixelWidth);
        imageOfBunny.setY(8 * aBoard.blockPixelHeight);
        imageOfBunny.setSize(300, 200);

        exitButton = game.buttons.get("exit");


        someStage.addActor(cleanUI);
        someStage.addActor(imageOfBunny);
        someStage.addActor(kinderGartenLabel);
        someStage.addActor(slantedTreeImage);
        someStage.addActor(treeImage);
        someStage.addActor(rootTable);
        someStage.addActor(exitButton);
        someStage.addActor(rootTable);
        someStage.setViewport(aViewport);
    }

    /**
     * Just adds some background textures to the main menu
     */
    private void addVisualTextures()
    {

        //someStage.addActor(treeImage);
    }

    /**
     * Creates listeners for the various textfields and buttons
     */
    private void createListeners()
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
                        }
                        else
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

        //This button will change to the gameplay screen
        startButton.addListener(new ChangeListener()
        {
            @Override
            public void changed(ChangeEvent event, Actor actor)
            {
                game.forestMusic.stop();
                game.setScreen(new BoardScreen(game, new MenuScreen(game), rows, columns, 4));
            }
        });

        //This button will exit the game in the main menu
        exitButton.addListener(game.exitScreenListener);

        imageButtonOfTree.addListener(new ChangeListener()
        {
            @Override
            public void changed(ChangeEvent event, Actor actor)
            {
                game.setScreen(new BoardScreen(game, new MenuScreen(game), rows, columns, 4));
            }
        });

        imageOfBunny.addListener(new ChangeListener()
        {
            @Override
            public void changed(ChangeEvent event, Actor actor)
            {
                game.forestMusic.stop();
                game.setScreen(new KindergartenGamePlayBoard(game, new MenuScreen(game), rows, columns));
            }
        });


    }

    /**
     * Draws to the screen, a rasterizer
     *
     * @param delta float
     */
    @Override
    public void render(float delta)
    {
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        animationStatetime += Gdx.graphics.getDeltaTime();
        ScreenUtils.clear(0, 0, 0.2f, 1);
        camera.update();
        aBatch.setProjectionMatrix(camera.combined);
        raindropsBackground.draw(aBatch, animationStatetime);

        someStage.act();
        someStage.draw();

        this.update();
    }

    /**
     * Gathers keyboard input, for now
     */
    public void update()
    {
        Input anInput = Gdx.input;

        if (anInput.isKeyPressed(Keys.B))
        {
            game.setScreen(new MiddleScreen(game, this, rows, columns));
        }
    }

    @Override
    public void resize(int width, int height)
    {
        someStage.getViewport().update(width, height);
        this.aViewport.update(width, height);
    }

    @Override
    public void pause()
    {
        game.forestMusic.play();
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
        forestMusic.dispose();
        game.forestMusic.stop();
        game.dispose();
    }
}
