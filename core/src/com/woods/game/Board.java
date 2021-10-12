package com.woods.game;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Board
{
    float blockPixelWidth;
    float blockPixelHeight;
    int numberOfColumns;
    int numberOfRows;
    int numberOfWrittenBlocks;

    //TODO Change boardArray to Pieces class
    Block[][] boardArray;

    public Board(int numberOfRows, int numberOfColumns, float blockPixelWidth, float blockPixelHeight)
    {
        this.blockPixelWidth = blockPixelWidth;
        this.blockPixelHeight = blockPixelHeight;
        this.numberOfRows = numberOfRows;
        this.numberOfColumns = numberOfColumns;
        this.numberOfWrittenBlocks = 0;
        boardArray = new Block[numberOfRows][numberOfColumns];
        //this.createArray();
    }

    public void add(Block aBlock, int row, int column)
    {
        remove(aBlock, row, column);
    }

    public boolean remove(Block aBlock, int row, int column)
    {
        if (boardArray[row][column] != null && boardArray[row][column].equals(aBlock))
        {
            boardArray[row][column] = null;
            numberOfWrittenBlocks--;
            return true;
        }
        else
        {
            return false;
        }
    }

    public void draw(ShapeRenderer renderer)
    {
        for (int i = 0; i < boardArray.length; i++)
        {
            for (int j = 0; j < boardArray[i].length; j++)
            {
                //Block aBlock = somePieces[i][j];
                //aBlock.draw(renderer);
            }
        }
    }

    public Block[][] getBoardArray()
    {
        return boardArray;
    }

    public void setBoardArray(Block[][] boardArray)
    {
        this.boardArray = boardArray;
    }
}
