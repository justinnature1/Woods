package com.woods.game;

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
    void assembleMenu();
    void addListeners();
    void addLabels();
    void addTextFields();
    boolean removeLabels();
    boolean removeListeners();
}
