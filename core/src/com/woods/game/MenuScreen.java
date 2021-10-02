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
import com.badlogic.gdx.utils.ScreenUtils;
import javafx.scene.control.Tab;
import sun.font.TextRecord;

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
    Background raindropsBackground;


    public MenuScreen(Woods aGame)
    {
        someStage = new Stage();
        Gdx.input.setInputProcessor(someStage);

        this.aFont = new BitmapFont();
        this.aBatch = new SpriteBatch();
        this.aGame = aGame;
        this.aShape = new ShapeRenderer();
        this.camera = new OrthographicCamera();
        this.rootTable = new Table();
        camera.setToOrtho(false, 800, 480);
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
        textFieldStyleThing.font = new BitmapFont();
        textFieldStyleThing.fontColor = Color.WHITE;
        textFieldStyleThing.selection = someSkin.newDrawable("white", Color.CORAL);
        textFieldStyleThing.cursor = someSkin.newDrawable("white", Color.BLACK);
        textFieldStyleThing.focusedBackground = someSkin.newDrawable("white", Color.PURPLE);

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = new BitmapFont();
        textButtonStyle.fontColor = Color.CHARTREUSE;
        textButtonStyle.up = someSkin.newDrawable("white", Color.FIREBRICK);
        textButtonStyle.down = someSkin.newDrawable("white", Color.BLACK);
        textButtonStyle.checked = someSkin.newDrawable("white", Color.BLUE);

        someSkin.add("default", textButtonStyle);

        beginButton = new TextButton("START", textButtonStyle);

        someSkin.add("meow", textFieldStyleThing);

        rowStyle = new TextField.TextFieldStyle();
        rowStyle.font = new BitmapFont();

        //someSkin.add("default", rowStyle);
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = new BitmapFont();
        someSkin.add("default", labelStyle);

        rowTextField = new TextField(String.valueOf(rows), textFieldStyleThing);
        colTextField = new TextField(String.valueOf(columns), textFieldStyleThing);
        //rowStyle.background = someSkin.newDrawable();
        rowStyle.fontColor = Color.WHITE;
        //rowTextField.setText("Rows");

        Label rowLabel = new Label("Rows: ", someSkin);
        Label colLabel = new Label("Columns:  ", someSkin);
        //Label welcomeLabel = new Label(Welcome)

        //rootTable.setFillParent(true);
        rootTable.add(rowLabel).pad(10);
        rootTable.add(rowTextField);
        rootTable.row();
        rootTable.add(colLabel).pad(10);
        rootTable.add(colTextField);
        rootTable.row();
        rootTable.add(beginButton).width(100).height(50).colspan(2);

        rootTable.setY(150);
        rootTable.setX(camera.viewportWidth / 2);

        someStage.addActor(rootTable);
        createListeners();
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

        aBatch.begin();
        aFont.draw(aBatch, "Welcome to Random Movement Simulator", camera.viewportWidth / 2 - 100, camera.viewportHeight / 2 + 100);
        aBatch.end();
        aBatch.begin();
        aFont.draw(aBatch, "Press START to begin", camera.viewportWidth / 2 - 100, camera.viewportHeight / 2 + 50);
        aBatch.end();

        TextureRegion currentFrame = lightningAnimation.getKeyFrame(animationStatetime, true);

        aBatch.begin();
        aBatch.draw(currentFrame, 50, 50);
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
