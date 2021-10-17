package com.woods.game.movement;

import com.woods.game.Player;

import java.util.Random;

/**
 * Author: Justin Keller
 * This class will result in the player randomly moving one space at a time
 */
public class Wander extends Move {

    public Wander(Player player) {
        super(player);
    }

    public void move(int rows, int columns){
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

        int x = player.getxArrayLocation();
        int y = player.getyArrayLocation();

        if (x + xVector >= 0 && x + xVector < columns)
        {
            if (y + yVector >= 0 && y + yVector < rows)
            {
                player.setxArrayLocation(x += xVector);
                player.setyArrayLocation(y += yVector);
            }
        }
    }
}
