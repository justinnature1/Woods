package com.woods.game.movement;

import com.woods.game.Player;

/**
 * Author: Justin Keller
 * This class will result in the player randomly moving one space at a time
 */
public class Teleport extends Move {

    public Teleport(Player player) {
        super(player);
    }

    public void move(int rows, int columns){
        boolean playerMoved = true;

        int newX = aRandom.nextInt(columns);
        int newY = aRandom.nextInt(rows);

        player.setxArrayLocation(newX);
        player.setyArrayLocation(newY);

    }
}
