package com.woods.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.loaders.AssetLoader;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.ScreenUtils;
import sun.font.TextRecord;

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
    TextureAtlas fruitAtlas;
    Sprite bananaSprite;
    Animation<TextureRegion> lightningAnimation;
    float animationStatetime;
    TextField rowTextField;

    String initialText, dialogue, message;
    boolean display;
    BitmapFont aFont;


    public MenuScreen(Woods aGame)
    {
        this.aFont = new BitmapFont();
        this.aBatch = new SpriteBatch();
        this.aGame = aGame;
        this.aShape = new ShapeRenderer();
        this.camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
        this.columns = 10;
        this.rows = 10;
        lightningTexture = new Texture(Gdx.files.internal("lightning.png"));
        fruitAtlas = new TextureAtlas("fruit.txt");
        bananaSprite = fruitAtlas.createSprite("banana");
        animationStatetime = 0f;
        createTexture();
    }

    /**
     * Just a temporary method until a proper texture animation class is created
     */
    public void createTexture()
    {
        TextureRegion[] lightningFrames = new TextureRegion[5];

        TextureRegion[][] tempers = TextureRegion.split(lightningTexture, lightningTexture.getWidth()/5, lightningTexture.getHeight());

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

    @Override
    public void render(float delta)
    {
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        animationStatetime += Gdx.graphics.getDeltaTime();
        ScreenUtils.clear(0, 0, 0.2f, 1);
        camera.update();
        aBatch.setProjectionMatrix(camera.combined);

        aBatch.begin();
        aFont.draw(aBatch, "Welcome to Random Movement Simulator", 100, 150);
        aBatch.end();
        aBatch.begin();
        aFont.draw(aBatch, "Press anywhere to begin", 100, 100);
        aBatch.end();

        TextureRegion currentFrame = lightningAnimation.getKeyFrame(animationStatetime, true);

        aBatch.begin();
        aBatch.draw(currentFrame, 50, 50);
        aBatch.end();

        //basicUI.render();

        this.update();

    }

    public void update()
    {
        Input anInput = Gdx.input;

        if (anInput.isTouched() || anInput.isKeyPressed(Keys.ENTER))
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
