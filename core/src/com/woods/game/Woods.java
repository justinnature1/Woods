package com.woods.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

/**
 * Main class for the game
 * Name: Robert Zafaripour
 * This game will simply simulate a collision from several players on a tile board.
 * Framework: libGDX
 */
public class Woods extends Game
{
    SpriteBatch batch;
    Texture img;
    BitmapFont monoFont;
    BitmapFont medievalFont;
    BitmapFont arrowKeyFont;
    BitmapFont largeFont;
    Label.LabelStyle aLabelStyle;
    Array<Texture> backgroundTextures;
    HashMap<String, Texture> menuTextures;
    HashMap<String, Button> buttons;
    HashMap<String, ImageButton> imageButtons;
    HashMap<String, TextField> textFields;
    HashMap<String, Label.LabelStyle> labelStyles;
    Texture[] boardTextures;
    Music forestMusic;
    Music scaryMusic;
    Sound found;
    OrthographicCamera camera;
    Viewport aViewport;
    Skin someSkin;
    EventListener exitScreenListener;

    final float WORLD_WIDTH = 100;
    final float WORLD_HEIGHT = 100;

    /**
     * Basic creation method
     */
    @Override
    public void create()
    {
        someSkin = new Skin();
        batch = new SpriteBatch();
        backgroundTextures = new Array<>();
        buttons = new HashMap<>();
        imageButtons = new HashMap<>();
        menuTextures = new HashMap<>();
        textFields = new HashMap<>();
        boardTextures = new Texture[10];
        this.camera = new OrthographicCamera(WORLD_WIDTH, WORLD_HEIGHT); //Sets the game size, the full width/height is the entire length in pixels
        camera.setToOrtho(false);
        aViewport = new FitViewport(camera.viewportWidth, camera.viewportHeight, camera);
        aViewport.apply();

        addTextures();
        this.monoFont = new BitmapFont();
        this.medievalFont = new BitmapFont(Gdx.files.internal("leela.fnt"));
        this.arrowKeyFont = new BitmapFont(Gdx.files.internal("monospace.fnt"));
        this.largeFont = new BitmapFont(Gdx.files.internal("leelaLarge.fnt"));
        aLabelStyle = new Label.LabelStyle();
        aLabelStyle.font = new BitmapFont(Gdx.files.internal("monofnt.fnt"));
        this.monoFont = new BitmapFont(Gdx.files.internal("monofnt.fnt"));
        this.forestMusic = Gdx.audio.newMusic(Gdx.files.internal("nightForest.mp3"));
        this.scaryMusic = Gdx.audio.newMusic(Gdx.files.internal("scary.mp3"));
        this.found = Gdx.audio.newSound(Gdx.files.internal("found.wav"));
        createButtons();
        createMenuButtons();
        createListeners();
        this.setScreen(new MenuScreen(this));

    }

    @Override
    public void render()
    {
        super.render();
    }

    /**
     * Each class that extends the game class must have a dispose method to get rid of objects
     */
    @Override
    public void dispose()
    {
        batch.dispose();
        for (Texture aTexture : backgroundTextures)
        {
            aTexture.dispose();
        }
        forestMusic.dispose();
        monoFont.dispose();
        medievalFont.dispose();
        arrowKeyFont.dispose();
        for (int i = 0; i < boardTextures.length; i++)
        {
            boardTextures[i].dispose();
        }
        for (Map.Entry<String, Texture> entry : menuTextures.entrySet())
        {
            entry.getValue().dispose();
        }
        found.dispose();

    }

    private void addTextures()
    {
        backgroundTextures.add(new Texture(Gdx.files.internal("rain-0.png")));
        backgroundTextures.add(new Texture(Gdx.files.internal("rain-1.png")));

        menuTextures.put("SlantedTree", new Texture(Gdx.files.internal("slantedTree.png")));
        menuTextures.put("DeadTree", new Texture(Gdx.files.internal("deadTree.png")));
        menuTextures.put("Reset", new Texture(Gdx.files.internal("reset.png")));
        menuTextures.put("Exit", new Texture(Gdx.files.internal("exit.png")));
        menuTextures.put("Start", new Texture(Gdx.files.internal("start.png")));
        menuTextures.put("Bunny", new Texture(Gdx.files.internal("bunny.png")));
        menuTextures.put("SleepingBunny", new Texture(Gdx.files.internal("sleepingBunny.png")));
        menuTextures.put("Info", new Texture(Gdx.files.internal("info.png")));
        menuTextures.put("Pig", new Texture(Gdx.files.internal("pig.png")));
        menuTextures.put("Back", new Texture(Gdx.files.internal("back.png")));
        menuTextures.put("Okay", new Texture(Gdx.files.internal("okay2.png")));

        boardTextures[0] = new Texture(Gdx.files.internal("Tree_Pine_00.png"), true);
        boardTextures[1] = new Texture(Gdx.files.internal("Tree_Pine_01.png"), true);
        boardTextures[2] = new Texture(Gdx.files.internal("Tree_Pine_02.png"), true);
        boardTextures[3] = new Texture(Gdx.files.internal("Tree_Pine_03.png"), true);
        boardTextures[4] = new Texture(Gdx.files.internal("Tree_Pine_04.png"), true);
        boardTextures[5] = new Texture(Gdx.files.internal("Tree_Pine_Snow_00.png"), true);
        boardTextures[6] = new Texture(Gdx.files.internal("Tree_Pine_Snow_01.png"), true);
        boardTextures[7] = new Texture(Gdx.files.internal("Tree_Pine_Snow_02.png"), true);
        boardTextures[8] = new Texture(Gdx.files.internal("Tree_Pine_Snow_03.png"), true);
        boardTextures[9] = new Texture(Gdx.files.internal("Tree_Pine_Snow_04.png"), true);

        for (int i = 0; i < boardTextures.length; i++)
        {
            boardTextures[i].setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.Linear);
        }

        Texture blueTile = new Texture(Gdx.files.internal("blueTile.png"));
    }

    private void createMenuButtons()
    {
        ImageButton.ImageButtonStyle gradeButtonStyle = new ImageButton.ImageButtonStyle();
        TextureRegion bunnyRegion = new TextureRegion(menuTextures.get("SleepingBunny"));
        Image bunnyImage = new Image(bunnyRegion);
        someSkin.add("bunny", bunnyRegion);
        gradeButtonStyle.up = new TextureRegionDrawable(bunnyRegion);
        gradeButtonStyle.over = someSkin.newDrawable("bunny", Color.CORAL);
        ImageButton imageButtonOfBunny = new ImageButton(gradeButtonStyle);

        imageButtons.put("SleepingBunny", imageButtonOfBunny);
    }

    private void createButtons()
    {

        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        someSkin.add("white", new Texture(pixmap));
        someSkin.add("default", new BitmapFont());

        TextField.TextFieldStyle textFieldStyleThing = new TextField.TextFieldStyle();
        textFieldStyleThing.background = someSkin.newDrawable("white", Color.DARK_GRAY);
        textFieldStyleThing.font = medievalFont;
        textFieldStyleThing.fontColor = Color.WHITE;
        textFieldStyleThing.selection = someSkin.newDrawable("white", Color.CORAL);
        textFieldStyleThing.cursor = someSkin.newDrawable("white", Color.BLACK);
        textFieldStyleThing.focusedBackground = someSkin.newDrawable("white", Color.PURPLE);
        TextField rowTextField = new TextField("2-50", textFieldStyleThing);
        TextField colTextField = new TextField("2-50", textFieldStyleThing);

        textFields.put("Row", rowTextField);
        textFields.put("Col", colTextField);

        Button.ButtonStyle resetButtonStyle = new Button.ButtonStyle();
        Button.ButtonStyle exitButtonStyle = new Button.ButtonStyle();
        Button.ButtonStyle infoButtonStyle = new Button.ButtonStyle();

        Texture infoTexture = menuTextures.get("Info");
        Texture resetTexture = menuTextures.get("Reset");
        Texture exitTexture = menuTextures.get("Exit");
        TextureRegion exitRegion = new TextureRegion(exitTexture);
        TextureRegion resetRegion = new TextureRegion(resetTexture);
        TextureRegion infoRegion = new TextureRegion(infoTexture);
        someSkin.add("reset", resetTexture);
        someSkin.add("black", exitTexture);
        someSkin.add("info", infoTexture);


        resetButtonStyle.up = new TextureRegionDrawable(resetRegion);
        exitButtonStyle.up = new TextureRegionDrawable(exitRegion);
        infoButtonStyle.up = new TextureRegionDrawable(infoRegion);

        resetButtonStyle.down = someSkin.newDrawable("reset", Color.DARK_GRAY);
        resetButtonStyle.over = someSkin.newDrawable("reset", Color.FIREBRICK); //This adds a new drawable using the white skin and applying Color.Lime
        infoButtonStyle.down = someSkin.newDrawable("info", Color.DARK_GRAY);
        infoButtonStyle.over = someSkin.newDrawable("info", Color.CORAL);
        exitButtonStyle.over = someSkin.newDrawable("black", Color.CORAL);
        Button exitButton = new Button(exitButtonStyle);
        Button resetButton = new Button(resetButtonStyle);
        Button infoButton = new Button(exitButtonStyle);
        resetButton.setColor(Color.BROWN.r, Color.BROWN.g, Color.BROWN.b, 0.8f);
        exitButton.setColor(Color.CHARTREUSE.r, Color.CHARTREUSE.g, Color.CHARTREUSE.b, 0.8f);
        resetButton.setX(camera.viewportWidth - 110);

        exitButton.setHeight((float) exitTexture.getHeight() / 4);
        exitButton.setWidth((float) exitTexture.getWidth() / 4);
        resetButton.setHeight((float) resetTexture.getHeight() / 3);
        resetButton.setWidth((float) resetTexture.getWidth() / 3);
        buttons.put("reset", resetButton);
        buttons.put("exit", exitButton);
    }

    private void createListeners()
    {
        exitScreenListener = new ChangeListener()
        {
            @Override
            public void changed(ChangeEvent event, Actor actor)
            {
                Gdx.app.exit();
            }
        };

    }
}
