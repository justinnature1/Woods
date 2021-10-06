package com.woods.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
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
    Array<Texture> boardTextures;

    /**
     * Basic creation method
     */
    @Override
    public void create()
    {
        batch = new SpriteBatch();
        backgroundTextures = new Array<>();
        menuTextures = new HashMap<>();
        boardTextures = new Array<>();


        addTextures();
        this.monoFont = new BitmapFont();
        this.medievalFont = new BitmapFont(Gdx.files.internal("leela.fnt"));
        aLabelStyle = new Label.LabelStyle();
        aLabelStyle.font = new BitmapFont(Gdx.files.internal("monofnt.fnt"));
        img = new Texture("badlogic.jpg");
        this.monoFont = new BitmapFont(Gdx.files.internal("monofnt.fnt"));
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

        Texture deadTree = new Texture(Gdx.files.internal("deadTree.png"));
        Texture slantedTree = new Texture(Gdx.files.internal("slantedTree.png"));
        menuTextures.put("SlantedTree", slantedTree);
        menuTextures.put("DeadTree", deadTree);

        Texture tree1 = new Texture(Gdx.files.internal("Tree_Pine_00.png"));
        Texture tree2 = new Texture(Gdx.files.internal("Tree_Pine_01.png"));
        Texture tree3 = new Texture(Gdx.files.internal("Tree_Pine_02.png"));
        Texture tree4 = new Texture(Gdx.files.internal("Tree_Pine_03.png"));
        Texture tree5 = new Texture(Gdx.files.internal("Tree_Pine_04.png"));
        Texture tree6 = new Texture(Gdx.files.internal("Tree_Pine_Snow_00.png"));
        Texture tree7 = new Texture(Gdx.files.internal("Tree_Pine_Snow_01.png"));
        Texture tree8 = new Texture(Gdx.files.internal("Tree_Pine_Snow_02.png"));
        Texture tree9 = new Texture(Gdx.files.internal("Tree_Pine_Snow_03.png"));
        Texture tree10 = new Texture(Gdx.files.internal("Tree_Pine_Snow_04.png"));

        boardTextures.add(tree1, tree2, tree3, tree4);
        boardTextures.add(tree5, tree6, tree7, tree8);
        boardTextures.add(tree9, tree10);
        Texture blueTile = new Texture(Gdx.files.internal("blueTile.png"));

    }
}
