package com.woods.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.loaders.AssetLoader;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.*;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import static com.badlogic.gdx.Input.*;

/**
 * This implements a menu screen for the game.
 */
public class MenuScreen implements Screen
{
    Stage someStage;
    Woods aGame;

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
    TextureAtlas fruitAtlas;
    Sprite bananaSprite;
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
    TextButton beginButton;
    Button exitButton;
    Button.ButtonStyle buttonStyle;
    Texture exitTexture;
    Background raindropsBackground;
    ImageButton imageButtonOfTree;


    final float WORLD_WIDTH = 100;
    final float WORLD_HEIGHT = 100;


    public MenuScreen(Woods aGame)
    {

        this.aFont = new BitmapFont();
        this.aBatch = new SpriteBatch();
        this.aGame = aGame;
        this.aShape = new ShapeRenderer();
        this.camera = new OrthographicCamera(WORLD_WIDTH, WORLD_HEIGHT);
        this.rootTable = new Table();
        camera.setToOrtho(false);
        //camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        aViewport = new StretchViewport(camera.viewportWidth, camera.viewportHeight, camera);
        aViewport.apply();
        //aViewport.setScreenPosition(10, 10);

        someStage = new Stage(aViewport);

        this.columns = 10;
        this.rows = 10;
        lightningTexture = new Texture(Gdx.files.internal("lightning.png"));
        rainTextureOne = new Texture(Gdx.files.internal("rain-0.png"));
        rainTextureTwo = new Texture(Gdx.files.internal("rain-1.png"));

        fruitAtlas = new TextureAtlas("fruit.txt");
        bananaSprite = fruitAtlas.createSprite("banana");
        animationStatetime = 0f;

        ArrayList<Texture> someTextures = new ArrayList<>();
        someTextures.add(rainTextureOne);
        someTextures.add(rainTextureTwo);

        raindropsBackground = new Background(aGame.backgroundTextures, 30, .05f, camera, 4, 4);

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
    }

    private void createButtons()
    {
        someSkin = new Skin();

        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        someSkin.add("white", new Texture(pixmap));
        someSkin.add("default", new BitmapFont());

        TextField.TextFieldStyle textFieldStyleThing = new TextField.TextFieldStyle();
        textFieldStyleThing.background = someSkin.newDrawable("white", Color.DARK_GRAY);
        textFieldStyleThing.font = aGame.medievalFont;
        textFieldStyleThing.fontColor = Color.WHITE;
        textFieldStyleThing.selection = someSkin.newDrawable("white", Color.CORAL);
        textFieldStyleThing.cursor = someSkin.newDrawable("white", Color.BLACK);
        textFieldStyleThing.focusedBackground = someSkin.newDrawable("white", Color.PURPLE);


        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = aGame.medievalFont;
        textButtonStyle.fontColor = Color.CHARTREUSE;
        textButtonStyle.up = someSkin.newDrawable("white", Color.FIREBRICK);
        textButtonStyle.down = someSkin.newDrawable("white", Color.BLACK);
        textButtonStyle.checked = someSkin.newDrawable("white", Color.BLUE);
        textButtonStyle.over = someSkin.newDrawable("white", Color.CYAN);

        buttonStyle = new Button.ButtonStyle();
        exitTexture = new Texture(Gdx.files.internal("exit.png"));
        TextureRegion exitRegion = new TextureRegion(exitTexture);
        someSkin.add("red", exitTexture); //Hashmap creation to access texture by the key of 'red'
        buttonStyle.up = new TextureRegionDrawable(exitRegion);
        buttonStyle.over = someSkin.newDrawable("red", Color.CYAN);
        exitButton = new Button(buttonStyle);
        exitButton.setColor(Color.CHARTREUSE.r, Color.CHARTREUSE.g, Color.CHARTREUSE.b, 0.5f);

        Texture slantedTreeTexture = aGame.menuTextures.get("SlantedTree");
        Image slantedTreeImage = new Image(slantedTreeTexture);
        slantedTreeImage.setSize(300, 300);
        slantedTreeImage.setX(camera.viewportWidth-400);
        slantedTreeImage.setY(camera.viewportHeight-450);


        someSkin.add("default", textButtonStyle);

        beginButton = new TextButton("START", textButtonStyle);

        someSkin.add("meow", textFieldStyleThing);

        rowStyle = new TextField.TextFieldStyle();
        rowStyle.font = aGame.medievalFont;

        //someSkin.add("default", rowStyle);
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = aGame.medievalFont;
        someSkin.add("default", labelStyle);

        rowTextField = new TextField(String.valueOf(rows), textFieldStyleThing);
        colTextField = new TextField(String.valueOf(columns), textFieldStyleThing);
        //rowTextField.setSize(2, 2);
        //rowStyle.background = someSkin.newDrawable();
        rowStyle.fontColor = Color.WHITE;
        //rowTextField.setText("Rows");

        Label rowLabel = new Label("Rows: ", someSkin);
        Label colLabel = new Label("Columns:  ", someSkin);
        Label welcome = new Label("Welcome to Wandering Woods", someSkin);
        Label clickToBegin = new Label("Click Tree to begin", someSkin);

        Texture treeTexture = aGame.menuTextures.get("DeadTree");
        TextureRegion treeRegion = new TextureRegion(treeTexture);
        ImageButton.ImageButtonStyle imageStyle = new ImageButton.ImageButtonStyle();
        Image treeImage = new Image(treeRegion);
        treeImage.setSize(400, 400);
        treeImage.setY(camera.viewportHeight-500);
        someSkin.add("tree", treeRegion);
        imageStyle.up = new TextureRegionDrawable(treeRegion);
        imageStyle.over = someSkin.newDrawable("tree", Color.RED);
        ImageButton imageButtonOfTree = new ImageButton(imageStyle);

        imageButtonOfTree.addListener(new ChangeListener()
        {
            @Override
            public void changed(ChangeEvent event, Actor actor)
            {
                aGame.setScreen(new BoardScreen(aGame, new MenuScreen(aGame), rows, columns));
            }
        });

        rootTable = new Table();
        rootTable.setY(camera.viewportHeight / 2);
        rootTable.setX(camera.viewportWidth / 2);
        rootTable.row().padBottom(100);

        rootTable.add(welcome).width(30).padRight(200);
        rootTable.row(); //Adds a new row
        rootTable.add(rowLabel);
        rootTable.add(rowTextField);
        rootTable.row();
        rootTable.add(colLabel);
        rootTable.add(colTextField);
        rootTable.row();
        rootTable.add(imageButtonOfTree).size(100, 100);
        rootTable.add(clickToBegin);
        exitButton.setWidth((float) exitTexture.getWidth() / 4);
        exitButton.setHeight((float) exitTexture.getHeight() / 4);

        someStage.addActor(slantedTreeImage);
        someStage.addActor(treeImage);
        someStage.addActor(rootTable);
        someStage.addActor(exitButton);
        someStage.addActor(rootTable);
        someStage.setViewport(aViewport);

        createListeners();
    }

    /**
     * Just adds some background textures to the main menu
     */
    private void addVisualTextures()
    {

        rootTable.row();
        rootTable.add(imageButtonOfTree).size(100, 100);

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
                        rows = Integer.parseInt(textField.getText());
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
                        columns = Integer.parseInt(textField.getText());
                        done = true;
                    } catch (Exception e)
                    {
                        System.err.println(e);
                        break;
                    }
                }
            }
        });

        beginButton.addListener(new ChangeListener()
        {
            @Override
            public void changed(ChangeEvent event, Actor actor)
            {
                aGame.setScreen(new BoardScreen(aGame, new MenuScreen(aGame), rows, columns));
            }
        });

        exitButton.addListener(new ChangeListener()
        {
            @Override
            public void changed(ChangeEvent event, Actor actor)
            {
                Gdx.app.exit();
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

        TextureRegion currentFrame = lightningAnimation.getKeyFrame(animationStatetime, true);

        aBatch.begin();
        aBatch.draw(currentFrame, 10, 10, 10, 10);
        aBatch.end();

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
            aGame.setScreen(new BoardScreen(aGame, this, rows, columns));
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
        fruitAtlas.dispose();
    }
}
