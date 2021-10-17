package com.woods.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.woods.game.movement.*;

import java.util.*;

public class Player extends Block
{

    float width;
    float height;
    float radius;
    String name;
    Random aRandom;
    Move move;
    boolean createdByUser;
    Queue <Move> movementQueue = new LinkedList<>();
    Queue <Color> colorQueue = new LinkedList<>();
    private int previousX = 0;
    private int previousY = 0;

    public Player(int xArrayLocation, int yArrayLocation, Color aColor, float width, float height, boolean createdByUser)
    {
        super(xArrayLocation, yArrayLocation, aColor);
        this.width = width;
        this.height = height;

        //Radius will be in center and must not exceed height or width of GraphicsTile
        if (width > height)
        {
            this.radius = height / 2;
        } else
        {
            this.radius = width / 2;
        }

        createQueues(5);

        this.createdByUser = createdByUser;
        this.move = new Wander(this); //Create Players that begin moving the normal way
        //Rectangle aRect = new Rectangle((int)x, (int)y, (int)width, (int)height);
    }

    /**
     * Creates a clone of the player.
     * @param playerToClone
     */
    public Player(Player playerToClone) {
        super(playerToClone.xArrayLocation, playerToClone.yArrayLocation, playerToClone.color);
        this.width = playerToClone.width;
        this.height = playerToClone.height;
        this.radius = playerToClone.radius;
        this.name = playerToClone.name;
        this.createdByUser = playerToClone.createdByUser;
        try{
            this.move = playerToClone.move.getClass().getConstructor(Player.class).newInstance(this);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            this.move = new Wander(this);
        }
        createQueues(playerToClone.movementQueue.size());
    }


    /**
     * Moves player location, based on x/y coordinate. Will attempt to move Up/Right/Down/Left/Diagonally at random
     *
     * @param rows    int Number of rows in board
     * @param columns int Number of Columns in board
     * @return
     */
    public void playerMovement(int rows, int columns)
    {
        this.previousX = getxArrayLocation();
        this.previousY = getyArrayLocation();
        move.move(rows, columns);
    }

    public void createQueues(int queueSize){
        switch (queueSize){
            case 5:
                movementQueue.add(new Row(this));
                colorQueue.add(Color.ORANGE);
            case 4:
                movementQueue.add(new Column(this));
                colorQueue.add(Color.YELLOW);
            case 3:
                movementQueue.add(new Wait(this));
                colorQueue.add(Color.GREEN);
            case 2:
                movementQueue.add(new MoveAndWait(this));
                colorQueue.add(Color.BLUE);
            case 1:
                movementQueue.add(new Teleport(this));
                colorQueue.add(Color.PURPLE);
        }
    }

    public boolean hasNextMovement(){
        boolean hasNext = false;
        if (movementQueue.size()>0) {
            hasNext = true;
        }
        return hasNext;
    }

    public void nextMovement(){
        setMove(movementQueue.poll());
        setColor(colorQueue.poll());
    }

    @Override
    public void draw(ShapeRenderer aShape)
    {
        aShape.setColor(color);
        float xDrawLocation = (xArrayLocation * width) + width / 2; //Must divide by 2 to make sure draw location is in middle of block
        float yDrawLocation = (yArrayLocation * height) + height / 2;
        aShape.circle(xDrawLocation, yDrawLocation, radius);
    }

    public void setMove(Move move){
        this.move = move;
    }

    public int getPreviousX() {
        return previousX;
    }

    public int getPreviousY() {
        return previousY;
    }

    @Override
    public void draw(SpriteBatch aShape)
    {

    }

    /**
     * Draws a description of the movement of the Player if the player is creaed by the user.
     */
    public void drawText(BitmapFont bitmapFont, SpriteBatch batch) {
        //double scale = Math.pow(.5, width);
        bitmapFont.getData().setScale(.5f, .5f);
        if (this.createdByUser) {
            bitmapFont.draw(batch, move.getMovementName(), (xArrayLocation * width) + ((width - 80) / 2),
                    (yArrayLocation * height) + height / 2, 80, 1, true);
        }
    }

    public boolean checkCollision(Block anotherBlock)
    {
        if (!anotherBlock.equals(this))
        {
            return this.xArrayLocation == anotherBlock.xArrayLocation && this.yArrayLocation == anotherBlock.yArrayLocation;
        } else
        {
            return false;
        }
    }

    @Override
    protected Object clone() throws CloneNotSupportedException
    {
        Player newPlayer = new Player(xArrayLocation, yArrayLocation, Color.RED, width, height, name);
        return newPlayer;
    }

    @Override
    public void fade(ShapeRenderer aShape)
    {
        color.set(color.r, color.g, color.b, 0.5f);
    }
}
