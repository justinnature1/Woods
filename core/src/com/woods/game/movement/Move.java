package com.woods.game.movement;

import com.woods.game.Player;

import java.util.Random;

public abstract class Move {

    Player player;

    protected static final Random aRandom = new Random();

    public Move(Player player) {
        this.player = player;
    }

    public String getMovementName() {
        String [] splitString = this.getClass().getSimpleName().split("(?=\\p{Upper})");
        StringBuilder stringBuilder = new StringBuilder();
        for (String substring: splitString) {
            if (stringBuilder.length() != 0) {
                stringBuilder.append(" ");
            }
            stringBuilder.append(substring);
        }
        return stringBuilder.toString();
    }

    public abstract void move(int rows, int columns);
}
