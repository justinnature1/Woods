package com.woods.game;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import java.util.HashMap;
import java.util.Random;

/**
 * Class for implementing background animations
 */
public class Background
{
    //TODO Change this class to use a Board data structure to determine dimensions of a background or just grab the screen dimensions from the viewport
    Animations backgroundAnim;
    int rows, columns;
    Array<Texture> someTextures;
    Camera aCamera;
    float width, height;


    public Background(Array<Texture> someTextures, int size, float frameDuration, Camera aCamera, int rows, int columns)
    {
        this.aCamera = aCamera;
        this.rows = rows;
        this.columns = columns;
        backgroundAnim = new Animations(someTextures, size, frameDuration, rows, columns);
        this.someTextures = someTextures;

        getDimensionsTwo();
    }

    public void draw(SpriteBatch aBatch, float animationStateTime)
    {
        /*
        Random aRan = new Random();
        for (int i = 0; i < rows; i++)
        {
            for (int j = 0; j < columns; j++)
            {
                TextureRegion currentFrame = backgroundAnim.anAnimation.getKeyFrame(animationStateTime, true);
                aBatch.begin();
                aBatch.draw(currentFrame, width * j, height * i, width, height);
                aBatch.end();


            }
            float animationRandomizer = aRan.nextFloat() * 0.01f;
            animationStateTime += animationRandomizer;
        }*/

        for (int i = 0; i < rows; i++)
        {
            for (int j = 0; j < columns; j++)
            {
                TextureRegion currentFrame = backgroundAnim.anAnimation.getKeyFrame(animationStateTime, true);
                aBatch.begin();
                aBatch.draw(currentFrame, j * width, i * height);
                aBatch.end();
            }
        }
    }

    private void getDimensions()
    {
        width = aCamera.viewportWidth / columns;
        height = aCamera.viewportHeight / rows;
    }

    /**
     * Determines the size of the background animation. Rows and Columns determines how often it should be drawn. Could also just use a Board class to determine
     * dimensions.
     */
    private void getDimensionsTwo()
    {
        width = backgroundAnim.anAnimation.getKeyFrame(0.1f).getRegionWidth();
        height = backgroundAnim.anAnimation.getKeyFrame(0.1f).getRegionHeight();
        columns = Math.round(aCamera.viewportWidth / width) + 1;
        rows = Math.round(aCamera.viewportHeight / height) + 1;
    }


}
