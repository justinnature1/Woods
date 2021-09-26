package com.woods.game;

/**
 * A class that works like the Board class but has an array to hold more 'blocks or pieces' in a single tile
 */
public class BoardOfPieces
{

    float blockPixelWidth;
    float blockPixelHeight;
    int numberOfColumns;
    int numberOfRows;
    Pieces[][] piecesArray;

    public BoardOfPieces(int numberOfRows, int numberOfColumns, float blockPixelWidth, float blockPixelHeight)
    {
        this.blockPixelWidth = blockPixelWidth;
        this.blockPixelHeight = blockPixelHeight;
        this.numberOfRows = numberOfRows;
        this.numberOfColumns = numberOfColumns;

        piecesArray = new Pieces[numberOfRows][numberOfColumns];
    }

    public Pieces[][] getPiecesArray()
    {
        return piecesArray;
    }

    public void setPiecesArray(Pieces[][] piecesArray)
    {
        this.piecesArray = piecesArray;
    }
}
