package com.woods.game.movement;

import com.woods.game.Player;

import java.util.Random;

/**
 * Author: Justin Keller
 * This class moves a player through a row completely before moving up/down a column to traverse that row.
 */
public class Row extends Move {

    private int xVector = -1;
    private int yVector = -1;

    public Row(Player player) {
        super(player);
        int random = aRandom.nextInt(3);
        if (random == 0){
            xVector = 1;
        } else if (random == 1){
            yVector = 1;
        } else {
            xVector = 1;
            yVector = 1;
        }
    }

    public void move(int rows, int columns){
        int x = player.getxArrayLocation();
        int y = player.getyArrayLocation();

        if (x + xVector < 0 || x + xVector >= columns) {
            xVector *= -1;
            if (y + yVector < 0 || y + yVector >= rows) {
                yVector *= -1;
            }
            player.setyArrayLocation(y+yVector); //Move up or down a row when the end of the row.
        } else {
            player.setxArrayLocation(x+xVector);
        }
    }
}
