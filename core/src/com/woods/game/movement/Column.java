package com.woods.game.movement;

import com.woods.game.Player;

/**
 * Author: Justin Keller
 * This class moves a player through a column completely before moving over a row to traverse that column.
 */
public class Column extends Move {

    private int xVector = -1;
    private int yVector = -1;

    public Column(Player player) {
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
        boolean playerMoved = false;

        int x = player.getxArrayLocation();
        int y = player.getyArrayLocation();

        if (y + yVector < 0 || y + yVector >= rows) {
            yVector *= -1;
            if (x + xVector < 0 || x + xVector >= columns) {
                xVector *= -1;
            }
            player.setxArrayLocation(x+xVector); //Move up or down a row when the end of the row.
        } else {
            player.setyArrayLocation(y+yVector);
        }
    }
}
