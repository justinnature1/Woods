package com.woods.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Array;

import java.util.HashMap;

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
    Label.LabelStyle aLabelStyle;
    Array<Texture> backgroundTextures;
    HashMap<String, Texture> menuTextures;
    Texture[] boardTextures;
    Music forestMusic;
    Music scaryMusic;
    Sound found;

    /**
     * Basic creation method
     */
    @Override
    public void create()
    {
        batch = new SpriteBatch();
        backgroundTextures = new Array<>();
        menuTextures = new HashMap<>();
        boardTextures = new Texture[10];


        addTextures();
        this.monoFont = new BitmapFont();
        this.medievalFont = new BitmapFont(Gdx.files.internal("leela.fnt"));
        aLabelStyle = new Label.LabelStyle();
        aLabelStyle.font = new BitmapFont(Gdx.files.internal("monofnt.fnt"));
        img = new Texture("badlogic.jpg");
        this.monoFont = new BitmapFont(Gdx.files.internal("monofnt.fnt"));
        this.forestMusic = Gdx.audio.newMusic(Gdx.files.internal("nightForest.mp3"));
        this.scaryMusic = Gdx.audio.newMusic(Gdx.files.internal("scary.mp3"));
        this.found = Gdx.audio.newSound(Gdx.files.internal("found.wav"));
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
        for (Texture aTexture : backgroundTextures)
        {
            aTexture.dispose();
        }
        forestMusic.dispose();
        monoFont.dispose();
        medievalFont.dispose();
        for (int i = 0; i < boardTextures.length; i++)
        {
            boardTextures[i].dispose();
        }
        found.dispose();

    }

    private void addTextures()
    {
        Texture rain0 = new Texture(Gdx.files.internal("rain-0.png"));
        Texture rain1 = new Texture(Gdx.files.internal("rain-1.png"));
        backgroundTextures.add(rain0);
        backgroundTextures.add(rain1);

        Texture deadTree = new Texture(Gdx.files.internal("deadTree.png"));
        Texture slantedTree = new Texture(Gdx.files.internal("slantedTree.png"));
        menuTextures.put("SlantedTree", slantedTree);
        menuTextures.put("DeadTree", deadTree);

        boardTextures[0] = new Texture(Gdx.files.internal("Tree_Pine_00.png"));
        boardTextures[1] = new Texture(Gdx.files.internal("Tree_Pine_01.png"));
        boardTextures[2] = new Texture(Gdx.files.internal("Tree_Pine_02.png"));
        boardTextures[3] = new Texture(Gdx.files.internal("Tree_Pine_03.png"));
        boardTextures[4] = new Texture(Gdx.files.internal("Tree_Pine_04.png"));
        boardTextures[5] = new Texture(Gdx.files.internal("Tree_Pine_Snow_00.png"));
        boardTextures[6] = new Texture(Gdx.files.internal("Tree_Pine_Snow_01.png"));
        boardTextures[7] = new Texture(Gdx.files.internal("Tree_Pine_Snow_02.png"));
        boardTextures[8] = new Texture(Gdx.files.internal("Tree_Pine_Snow_03.png"));
        boardTextures[9] = new Texture(Gdx.files.internal("Tree_Pine_Snow_04.png"));

        Texture blueTile = new Texture(Gdx.files.internal("blueTile.png"));
    }
}
