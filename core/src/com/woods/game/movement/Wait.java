package com.woods.game.movement;

import com.woods.game.Player;

/**
 * Author: Justin Keller
 * This class will result in a player not moving.
 */
public class Wait extends Move {

    public Wait(Player player) {
        super(player);
    }

    public void move(int rows, int columns){
        boolean playerMoved = true;
    }
}
