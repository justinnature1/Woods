package com.woods.game;

import com.badlogic.gdx.Gdx;
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
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class InfoScreen implements Screen {

    Stage someStage;
    Woods aGame;
    Screen returnScreen;

    OrthographicCamera camera;
    Viewport aViewport;
    SpriteBatch aBatch;

    float animationStatetime;

    BitmapFont aFont;
    Table rootTable;
    TextField.TextFieldStyle rowStyle;
    Button backButton;
    Button.ButtonStyle buttonStyle;
    Texture backTexture;
    Background raindropsBackground;
    Music forestMusic;


    final float WORLD_WIDTH = 100;
    final float WORLD_HEIGHT = 100;
    public InfoScreen(Woods aGame, Screen returnScreen)
    {
        this.returnScreen = returnScreen;
        this.aGame = aGame;

        this.forestMusic = Gdx.audio.newMusic(Gdx.files.internal("nightForest.mp3"));
        this.forestMusic.setLooping(true);

        this.aFont = new BitmapFont();
        this.aBatch = new SpriteBatch();

        this.camera = new OrthographicCamera(WORLD_WIDTH, WORLD_HEIGHT);
        camera.setToOrtho(false);
        aViewport = new FitViewport(camera.viewportWidth, camera.viewportHeight, camera);
        aViewport.apply();

        someStage = new Stage(aViewport);

        animationStatetime = 0f;

        raindropsBackground = new Background(aGame.backgroundTextures, 30, .05f, camera, 4, 4);

        buttonStyle = new Button.ButtonStyle();
        backTexture = new Texture(Gdx.files.internal("back.png"));
        buttonStyle.up = new TextureRegionDrawable(backTexture);
        buttonStyle.over = new TextureRegionDrawable(backTexture).tint(Color.CYAN);
        backButton = new Button(buttonStyle);
        backButton.setHeight(50);
        backButton.setColor(Color.CHARTREUSE.r, Color.CHARTREUSE.g, Color.CHARTREUSE.b, 0.5f);

        Texture slantedTreeTexture = aGame.menuTextures.get("SlantedTree");
        Image slantedTreeImage = new Image(slantedTreeTexture);
        slantedTreeImage.setSize(300, 300);
        slantedTreeImage.setX(camera.viewportWidth-400);
        slantedTreeImage.setY(camera.viewportHeight-450);

        rowStyle = new TextField.TextFieldStyle();
        rowStyle.font = aGame.medievalFont;

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = aGame.medievalFont;

        rowStyle.fontColor = Color.WHITE;
        Label welcome = new Label("Welcome to Wandering Woods", labelStyle);

        Label labelKTo2 = new Label("K-2 Simulation:",labelStyle);
        labelKTo2.setAlignment(Align.topRight);

        Label textKTo2 = new Label("K-2 Description... Has really long text description that spans to the " +
                "second row and wraps as it should", labelStyle);
        textKTo2.setWrap(true);
        textKTo2.setAlignment(Align.topLeft);

        Label label3To5 = new Label("3-5 Simulation:",labelStyle);
        label3To5.setAlignment(Align.topRight);

        Label text3To5 = new Label("3-5 Description... Has really long text description that spans to the " +
                "second row and wraps as it should", labelStyle);
        text3To5.setWrap(true);
        text3To5.setAlignment(Align.topLeft);


        Label label6To8 = new Label("6-8 Simulation:",labelStyle);
        label6To8.setAlignment(Align.topRight);

        Label text6To8 = new Label("6-8 Description... Has really long text description that spans to the " +
                "second row and wraps as it should", labelStyle);
        text6To8.setWrap(true);
        text6To8.setAlignment(Align.topLeft);

        rootTable = new Table();
        rootTable.setFillParent(true);
        rootTable.row().padBottom(20);

        rootTable.add(welcome).expandX().colspan(2);
        rootTable.row().padBottom(20).padRight(75); //Adds a new row
        rootTable.add(labelKTo2).padRight(20).width(camera.viewportWidth*1/4).top().right();
        rootTable.add(textKTo2).width(camera.viewportWidth*3/4).top().left();
        rootTable.row().padBottom(20).padRight(75); //Adds a new row
        rootTable.add(label3To5).padRight(20).width(camera.viewportWidth*1/4).top().right();
        rootTable.add(text3To5).width(camera.viewportWidth*3/4).top().left();
        rootTable.row().padBottom(20).padRight(75); //Adds a new row
        rootTable.add(label6To8).padRight(20).width(camera.viewportWidth*1/4).top().right();
        rootTable.add(text6To8).width(camera.viewportWidth*3/4).top().left();

        backButton.setWidth((float) backTexture.getWidth() / 4);
        backButton.setHeight((float) backTexture.getHeight() / 5);

        someStage.addActor(slantedTreeImage);
        someStage.addActor(rootTable);
        someStage.addActor(backButton);
    }

    @Override
    public void show()
    {
        Gdx.input.setInputProcessor(someStage);
        aGame.forestMusic.play();
        aGame.forestMusic.setVolume(0.1f);
        createListeners();
    }

    /**
     * Creates listeners for the various textfields and buttons
     */
    private void createListeners()
    {

        //This button will exit the game in the main menu
        backButton.addListener(new ChangeListener()
        {
            @Override
            public void changed(ChangeEvent event, Actor actor)
            {
                aGame.setScreen(returnScreen);
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
        camera.update();
        aBatch.setProjectionMatrix(camera.combined);
        raindropsBackground.draw(aBatch, animationStatetime += delta);
        someStage.act();
        someStage.draw();
    }

    @Override
    public void resize(int width, int height) {
        someStage.getViewport().update(width, height);
        this.aViewport.update(width, height);
    }

    @Override
    public void pause() {
        aGame.forestMusic.stop();
    }

    @Override
    public void resume() {
        aGame.forestMusic.play();
    }

    @Override
    public void hide() {
        aGame.forestMusic.stop();
    }

    @Override
    public void dispose() {
        forestMusic.dispose();
        aGame.forestMusic.stop();
    }

}
