package com.woods.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.*;

import static com.badlogic.gdx.Input.*;

/**
 * This implements a menu screen for the game.
 */
public class MenuScreen implements Screen, Menu
{
    Stage stageUI; //libGDX object that stores game 'actors' to be drawn on the screen and manipulated
    Stage backgroundStage;
    Woods game; //Reference to the main game
    MenuController menuControl; //Creates and adds menu items...text fields and labels, etc
    Group backgroundGroup, buttonGroup;
    MenuScreen currentScreen;

    OrthographicCamera camera;
    Viewport aViewport;
    Board aBoard;
    ShapeRenderer aShape; //Draws shapes
    SpriteBatch aBatch; //Draws sprites in batches
    int rows, columns;
    float animationStatetime;

    BitmapFont aFont;
    Table rootTable;
    Button exitButton;
    Background raindropsBackground;
    ImageTextButton imageOfPig, imageOfBunny, imageOfCow;
    Music forestMusic;
    private Button infoButton;

    public MenuScreen(Woods game)
    {
        this.currentScreen = this;
        this.forestMusic = Gdx.audio.newMusic(Gdx.files.internal("nightForest.mp3"));
        this.menuControl = new MenuController(game, this);
        this.backgroundGroup = new Group();
        this.buttonGroup = new Group();
        this.forestMusic.setLooping(true);
        this.aFont = new BitmapFont();
        this.aBatch = new SpriteBatch();
        this.game = game;
        this.aShape = new ShapeRenderer();
        this.camera = game.camera;
        this.rootTable = new Table();
        camera.setToOrtho(false);
        //aViewport = game.aViewport;
        //aViewport.apply();
        aBoard = new Board(50, 50, camera.viewportWidth / 50,
                camera.viewportHeight / 50);

        stageUI = new Stage(game.aViewport);
        backgroundStage = new Stage(game.aViewport);

        this.columns = 10;
        this.rows = 10;

        animationStatetime = 0f; //Current animation time for background moving texture

        raindropsBackground = new Background(game.backgroundTextures, 30, .05f, camera, 4, 4);
        addBackground();
        addButtons();
        assembleMenu();
    }

    @Override
    public void show()
    {
        Gdx.input.setInputProcessor(stageUI);
        game.forestMusic.play();
        game.forestMusic.setVolume(0.1f);
        addListeners();
    }

    @Override
    public void addLabels()
    {

    }

    @Override
    public void addTextFields()
    {

    }

    /**
     * Creates listeners for the various textfields and buttons
     */
    @Override
    public void addListeners()
    {

        //This button will exit the game in the main menu
        exitButton.addListener(new ChangeListener()
        {
            @Override
            public void changed(ChangeEvent event, Actor actor)
            {
                Gdx.app.exit();
            }
        });

        //This button will take you to the 6to8 menu screen
        imageOfCow.addListener(new ChangeListener()
        {
            @Override
            public void changed(ChangeEvent event, Actor actor)
            {
                game.setScreen(new Menu6To8(game, new MenuScreen(game), 10, 10));
                game.getScreen().pause();
            }
        });

        imageOfPig.addListener(new ChangeListener()
        {
            @Override
            public void changed(ChangeEvent event, Actor actor)
            {
                game.setScreen(new MiddleScreen(game, new MenuScreen(game), 10, 10));
            }
        });

        //Starts the K to 2 Game with 2 players in opposite corners
        imageOfBunny.addListener(new ChangeListener()
        {
            @Override
            public void changed(ChangeEvent event, Actor actor)
            {
                //game.forestMusic.stop();
                game.setScreen(new BoardScreen(game, new MenuScreen(game), rows, columns, 2));
            }
        });

        infoButton.addListener(new ChangeListener()
        {
            @Override
            public void changed(ChangeEvent event, Actor actor)
            {
                game.setScreen(new InfoScreen(game, currentScreen));

            }
        });
    }

    /**
     * Adds background to the background Group
     */
    @Override
    public void addBackground()
    {
        Label welcomeLabel = menuControl.getWelcomeLabel();
        backgroundGroup = menuControl.getBackgroundTreeImageGroup();
        backgroundGroup.addActor(welcomeLabel);
    }

    /**
     * Adds buttons to the button Group
     */
    @Override
    public void addButtons()
    {
        exitButton = menuControl.getExitButton();
        infoButton = menuControl.getInfoButton();
        imageOfBunny = menuControl.getImageOfBunny();
        imageOfPig = menuControl.getImageOfPig();
        imageOfCow = menuControl.getImageOfCow();
        buttonGroup.addActor(imageOfPig);
        buttonGroup.addActor(imageOfBunny);
        buttonGroup.addActor(imageOfCow);
        buttonGroup.addActor(exitButton);
        buttonGroup.addActor(infoButton);
    }

    /**
     * Adds buttons, textures and etc to the stage
     */
    @Override
    public void assembleMenu()
    {
        backgroundGroup.toBack();
        buttonGroup.toFront();
        backgroundStage.addActor(backgroundGroup);
        stageUI.addActor(buttonGroup);
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
        animationStatetime += Gdx.graphics.getDeltaTime(); //Delta time is the amount of time since the last frame was rendered
        ScreenUtils.clear(0, 0, 0.2f, 1);
        camera.update();
        aBatch.setProjectionMatrix(game.camera.combined);
        raindropsBackground.draw(aBatch, animationStatetime);

        backgroundStage.act();
        backgroundStage.draw();
        stageUI.act();
        stageUI.draw();

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
        game.aViewport.update(width, height);
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
