package com.woods.game;

/**
 * A interface to use with screens. Helps with the abstraction and organization of code with Screens
 */
public interface Menu
{
    /**
     * Assigns a background
     */
    void addBackground();
    /**
     * Assigns buttons to the screen
     */
    void addButtons();

    /**
     * Assembles a game menu. Place and assign all your default default buttons/text..etc here.
     */
    void assembleMenu();

    /**
     * Adds listeners to the screen
     */
    void addListeners();

    /**
     * Adds text labels to the screen
     */
    void addLabels();

    /**
     * Adds writable text fields to the screen
     */
    void addTextFields();

    /**
     * Removes labels
     * @return boolean
     */
    boolean removeLabels();
    boolean removeListeners();
}
