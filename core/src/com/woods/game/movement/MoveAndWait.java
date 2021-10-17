package com.woods.game.movement;

import com.woods.game.Player;

import java.util.Random;

/**
 * Author: Justin Keller
 * A Player randomly moves for a few turns and then waits for a few turns.
 */
public class MoveAndWait extends Move {

    Move randomMovement;
    Move noMovement;
    int movesSinceSwitch = 0;
    int movesToSwitch;
    Move currentMovement;

    public MoveAndWait(Player player) {
        super(player);
        Random random = new Random();
        movesToSwitch = random.nextInt(10) + 5;
        randomMovement = new Wander(player);
        noMovement = new Wait(player);
        currentMovement = randomMovement;
    }

    public void move(int rows, int columns) {
        if (movesSinceSwitch >= movesToSwitch) {
            if (currentMovement == randomMovement) {
                currentMovement = noMovement;
            } else {
                currentMovement = randomMovement;
            }
            movesSinceSwitch = 0;
        }
        currentMovement.move(rows, columns);
        movesSinceSwitch++;
    }
}
