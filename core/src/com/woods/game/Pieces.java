package com.woods.game;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.ArrayList;

public class Pieces
{

    ArrayList<Block> totalPieces;

    //TODO Change
    public Pieces()
    {
        totalPieces = new ArrayList<>();
    }

    public ArrayList<Block> getTotalPieces()
    {
        return totalPieces;
    }

    public void addPiece(Block aPiece)
    {
        totalPieces.add(aPiece);
    }

    public void removePiece(Block aPiece)
    {
        totalPieces.remove(aPiece);
    }

    public void setTotalPieces(ArrayList<Block> totalPieces)
    {
        this.totalPieces = totalPieces;
    }

    public void draw(ShapeRenderer renderer)
    {
        for (Block aBlock : this.totalPieces)
        {
            aBlock.draw(renderer);
        }
    }

    public void fade(ShapeRenderer renderer)
    {
        for (Block aBlock : this.totalPieces)
        {
            aBlock.fade(renderer);
        }
    }
}
