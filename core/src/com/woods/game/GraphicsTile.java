package com.woods.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

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

    @Override
    public void draw(ShapeRenderer aShape)
    {
        aShape.begin(ShapeRenderer.ShapeType.Line);
        aShape.setColor(super.color);
        aShape.rect(xArrayLocation * width, yArrayLocation * height, width, height);
        aShape.end();
    }

    public String drawText()
    {
        return "x";
    }
}
