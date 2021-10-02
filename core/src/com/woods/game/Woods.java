package com.woods.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;

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
    BitmapFont font;
    Label.LabelStyle aLabelStyle;
    Array<Texture> backgroundTextures;

    /**
     * Basic creation method
     */
    @Override
    public void create()
    {
        batch = new SpriteBatch();
        backgroundTextures = new Array<>();
        addTextures();
        this.font = new BitmapFont();
        aLabelStyle = new Label.LabelStyle();
        aLabelStyle.font = new BitmapFont(Gdx.files.internal("monofnt.fnt"));
        img = new Texture("badlogic.jpg");
        this.font = new BitmapFont(Gdx.files.internal("monofnt.fnt"));
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
        img.dispose();
        for (Texture aTexture: backgroundTextures)
        {
            aTexture.dispose();
        }

    }

    private void addTextures()
    {
        Texture rain0 = new Texture(Gdx.files.internal("rain-0.png"));
        Texture rain1 = new Texture(Gdx.files.internal("rain-1.png"));
        backgroundTextures.add(rain0);
        backgroundTextures.add(rain1);
    }
}
