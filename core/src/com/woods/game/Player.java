package com.woods.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.Random;

public class Player extends Block
{

    float width;
    float height;
    float radius;
    String name;
    Random aRandom;

    public Player(int xArrayLocation, int yArrayLocation, Color aColor, float width, float height, String name)
    {
        super(xArrayLocation, yArrayLocation, aColor);
        this.width = width;
        this.height = height;

        //Radius will be in center and must not exceed height or width of GraphicsTile
        if (width > height)
        {
            this.radius = height / 2;
        }
        else
        {
            this.radius = width / 2;
        }

        this.name = name;
        this.aRandom = new Random();
        //Rectangle aRect = new Rectangle((int)x, (int)y, (int)width, (int)height);
    }

    /**
     * Moves player location, based on x/y coordinate. Will attempt to move Up/Right/Down/Left/Diagonally at random
     *
     * @param rows    int Number of rows in board
     * @param columns int Number of Columns in board
     * @return
     */
    public boolean playerMovement(int rows, int columns)
    {
        boolean playerMoved = false;
        int xVector = 0;
        int yVector = 0;

        int direction = aRandom.nextInt(8);

        switch (direction)
        {
            //North
            case 0:
                yVector = -1;
                break;
            case 1:
                //East
                xVector = 1;
                break;
            case 2:
                //South
                yVector = 1;
                break;
            case 3:
                //West
                xVector = -1;
                break;
            case 4:
                //NorthEast
                xVector = 1;
                yVector = -1;
                break;
            case 5:
                //NorthWest
                xVector = -1;
                yVector = -1;
                break;
            case 6:
                //SouthEast
                xVector = 1;
                yVector = 1;
                break;
            case 7:
                //SouthWest
                xVector = -1;
                yVector = 1;
                break;
        }
        if (this.xArrayLocation + xVector >= 0 && this.xArrayLocation + xVector < columns)
        {
            if (this.yArrayLocation + yVector >= 0 && this.yArrayLocation + yVector < rows)
            {
                this.xArrayLocation += xVector;
                this.yArrayLocation += yVector;
                playerMoved = true;
            }
        }
        return playerMoved;
    }

    @Override
    public void draw(ShapeRenderer aShape)
    {

        //xDrawLocation + pixelBlockWidth / 2, yDrawLocation + pixelBlockHeight / 2
        aShape.begin(ShapeRenderer.ShapeType.Filled);
        aShape.setColor(color);
        float xDrawLocation = (xArrayLocation * width) + width / 2; //Must divide by 2 to make sure draw location is in middle of block
        float yDrawLocation = (yArrayLocation * height) + height / 2;
        aShape.circle(xDrawLocation, yDrawLocation, radius);
        aShape.end();
        //drawText();
    }

    /**
     * Draws a text version of a player and returns the first letter in uppercase.
     * Ex. "Paul" -> "P"
     *
     * @return String, first letter capatalized
     */
    public String drawText()
    {
        return name.substring(0, 1).toUpperCase();
    }

    public boolean checkCollision(Block anotherBlock)
    {
        if (!anotherBlock.equals(this))
        {
            return this.xArrayLocation == anotherBlock.xArrayLocation && this.yArrayLocation == anotherBlock.yArrayLocation;
        }
        else
        {
            return false;
        }
    }

    @Override
    public void fade(ShapeRenderer aShape)
    {
        color.set(color.r, color.g, color.b, 0.5f);
    }
}
