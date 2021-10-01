package com.woods.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.ArrayList;

/**
 * Texture Sprite Sheet class to merge multiple sprite sheets together
 */
public class Animations extends Actor
{
    //TODO Finish proper draw implementation and update method

    ArrayList<Texture> someTextures;
    TextureRegion[] animationFrames;
    Animation<TextureRegion> anAnimation;

    public Animations(ArrayList<Texture> textures, int size, float frameDuration)
    {
        //this.animationFrames = animationFrames;
        animationFrames = new TextureRegion[size];
        this.someTextures = textures;
        createTexture();
        anAnimation = new Animation<TextureRegion>(frameDuration, animationFrames);

    }

    /**
     * Goes through each sprite sheet in the List and adds each individual texture to the animation array
     */
    private void createTexture()
    {
        int index = 0;
        for (Texture aTexture : someTextures)
        {
            TextureRegion[][] tempers = TextureRegion.split(aTexture, aTexture.getWidth() / 4, aTexture.getHeight() / 4);

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
    public void draw(Batch batch, float parentAlpha)
    {
        super.draw(batch, parentAlpha);
    }
}
