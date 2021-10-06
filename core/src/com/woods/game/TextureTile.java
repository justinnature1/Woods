package com.woods.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import org.w3c.dom.Text;

public class TextureTile extends Block
{

    Texture texture;
    TextureRegion textureRegion;
    float width;
    float height;

    public TextureTile(int xLocation, int yLocation, float width, float height, Color aColor, Texture aTexture)
    {
        super(xLocation, yLocation, aColor);
        this.texture = aTexture;
        this.height = height;
        this.width = width;
        this.textureRegion = new TextureRegion(texture);
    }

    private void processTexture()
    {
        textureRegion = new TextureRegion(texture);
    }

    @Override
    public void draw(ShapeRenderer aShape)
    {

    }

    public void draw(Batch aBatch)
    {
        aBatch.draw(textureRegion, xArrayLocation * width, yArrayLocation * height, width, height);
    }

    @Override
    public void draw(SpriteBatch aBatch)
    {
        aBatch.draw(textureRegion, xArrayLocation * width, yArrayLocation * height, width, height);
    }

    @Override
    public void fade(ShapeRenderer aShape)
    {

    }
}
