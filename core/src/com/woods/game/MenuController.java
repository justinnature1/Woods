package com.woods.game;

import com.badlogic.gdx.Screen;

/**
 * This class will eventually abstract much of the menu objects and menu screens and avoid code redundancy
 */
public class MenuController
{
    private final Screen aScreen;
    Woods aGame;


    public MenuController(Woods aGame, Screen aScreen)
    {
        this.aGame = aGame;
        this.aScreen = aScreen;
    }

}