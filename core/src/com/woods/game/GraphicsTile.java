package com.woods.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

/**
 * This class creates a graphics tile class, which are simple rectangles. This class will scale depending on the number of rows
 * and columns
 */
public class GraphicsTile extends Block
{

    float width;
    float height;
    //Rectangle rect;

    public GraphicsTile(int xArrayLocation, int yArrayLocation, Color aColor)
    {
        super(xArrayLocation, yArrayLocation, aColor);
    }

    public GraphicsTile(int xArrayLocation, int yArrayLocation, Color aColor, float width, float height)
    {
        super(xArrayLocation, yArrayLocation, aColor);
        this.width = width;
        this.height = height;
        //rect = new Rectangle(xDrawLocation, yDrawLocation, width, yDrawLocation);
    }

    @Override
    public void fade(ShapeRenderer aShape)
    {

        //aShape.begin(ShapeRenderer.ShapeType.Line);
        aShape.setColor(color.r, color.g, color.b, 01.f);
        //color.set(color.r, color.g, color.b, 0.1f);
        //aShape.rect(xArrayLocation * width, yArrayLocation * height, width, height);
        //aShape.end();
    }

    /**
     * This simply draws a single graphics tile (rectangle)
     * @param aShape ShapeRenderer
     */
    @Override
    public void draw(ShapeRenderer aShape)
    {
        aShape.setColor(super.color);
        aShape.rect(xArrayLocation * width, yArrayLocation * height, width, height);
    }

    @Override
    public void draw(SpriteBatch aShape)
    {
        //ShapeRenderer aRenderer = new ShapeRenderer();
        //aRenderer.begin(ShapeRenderer.ShapeType.Line);
        //aRenderer.rect(xArrayLocation * width, yArrayLocation * height, width, height);

    }

    public String drawText()
    {
        return "x";
    }
}
