package com.woods.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;

public class Pieces
{

    Array<Block> totalPieces;

    //TODO Change
    public Pieces()
    {
        totalPieces = new Array<>();
    }

    public Array<Block> getTotalPieces()
    {
        return totalPieces;
    }

    public void addPiece(Block aPiece)
    {
        totalPieces.add(aPiece);
    }

    public void removePiece(Block aPiece)
    {
        totalPieces.removeValue(aPiece,true);
    }

    public void setTotalPieces(Array<Block> totalPieces)
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

    public void draw(SpriteBatch aBatch)
    {
        for (Block aBlock : this.totalPieces)
        {
            aBlock.draw(aBatch);
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
