package com.woods.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Texture Sprite Sheet class to merge multiple sprite sheets together
 */
public class Animations extends Actor
{
    //TODO Finish proper draw implementation and update method

    Array<Texture> someTextures;
    TextureRegion[] animationFrames;
    TextureRegion currentRegion;
    Animation<TextureRegion> anAnimation;
    int columns, rows;
    float time = 0f;

    public Animations(Array<Texture> someTextures, int size, float frameDuration, int rows, int columns)
    {
        //this.animationFrames = animationFrames;
        animationFrames = new TextureRegion[size];
        this.someTextures = someTextures;
        this.rows = rows;
        this.columns = columns;
        createTexture();
        anAnimation = new Animation<TextureRegion>(frameDuration, animationFrames);
    }

    /**
     * Goes through each sprite sheet in the List and adds each individual texture to the animation array
     */
    private void createTexture()
    {
        int index = 0;
        for (Texture aTexture: someTextures)
        {
            TextureRegion[][] tempers = TextureRegion.split(aTexture, aTexture.getWidth() / rows, aTexture.getHeight() / columns);

            for (TextureRegion[] arrayOfRegions : tempers)
            {
                for (int i = 0; index < animationFrames.length && i < arrayOfRegions.length; i++)
                {
                    animationFrames[index] = arrayOfRegions[i];
                    index++;
                }
            }
        }
    }

    @Override
    public void act(float delta){
        super.act(delta);
        time += delta;

        currentRegion = anAnimation.getKeyFrame(time, true);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.draw(currentRegion, getX(), getY(), getWidth(), getHeight());
    }

    /**
     * Sets the location and size of an animation.
     * @param xLoc int - location on screen
     * @param yLoc int - location on screen
     * @param width float
     * @param height float
     */
    public void setLocationAndSize(int xLoc, int yLoc, float width, float height)
    {

        //Below sets the location of the animation. If a board is 2 rows x 30 columns for example, it will set the location in the middle of the largest width or height
        if (width >= (height * 2))
        {
            this.setX((xLoc * width) + width / 2 - this.getWidth());
            this.setY((yLoc * height));
        }
        else if (height >= (width * 2))
        {
            this.setX((xLoc * width));
            this.setY((yLoc * height) + height / 2 - this.getHeight());
        }
        else
        {
            this.setX((xLoc * width));
            this.setY((yLoc * height));
        }

        //This will set the height and width of the animation determined by the lesser value. So images do not get distorted.
        if (width > height)
        {
            this.setWidth(height * 1.5f);
            this.setHeight(height * 1.5f);
        }
        else
        {
            this.setWidth(width * 1.5f);
            this.setHeight(width * 1.5f);
        }
    }
}
