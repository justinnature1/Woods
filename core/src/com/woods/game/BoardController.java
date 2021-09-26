package com.woods.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.Random;

/**
 * This is a Board controller class that controls the state of the game board.
 */
public class BoardController
{

    BoardOfPieces tileBoard;
    Board playerBoard;
    int numberOfRows;
    int numberOfColumns;
    float pixelBlockWidth;
    float pixelBlockHeight;
    int numberOfPlayers;
    Player[] aPlayers;
    String[] playerNames = {"Joel", "Chris", "Mary", "Lyra"};
    int totalPlayerMovements;

    Music adventureMusic;

    float playerUpdateTime;
    float playerMovementTimer;

    public BoardController(int numberOfRows, int numberOfColumns, float pixelBlockWidth, float pixelBlockHeight, int numberOfPlayers)
    {
        if (numberOfRows < 5 || numberOfColumns < 5)
        {
            throw new IllegalArgumentException("Number of rows or number of columns must be above 9");
        }
        if (numberOfPlayers < 2)
        {
            throw new IllegalArgumentException("Number of players must be above 1");
        }

        this.tileBoard = new BoardOfPieces(numberOfRows, numberOfColumns, pixelBlockWidth, pixelBlockHeight);
        this.playerBoard = new Board(numberOfRows, numberOfColumns, pixelBlockWidth, pixelBlockHeight);
        this.numberOfRows = numberOfRows;
        this.numberOfColumns = numberOfColumns;
        this.pixelBlockWidth = pixelBlockWidth;
        this.pixelBlockHeight = pixelBlockHeight;
        this.numberOfPlayers = numberOfPlayers;
        this.aPlayers = new Player[numberOfPlayers];
        this.totalPlayerMovements = 0;
        this.playerUpdateTime = .3f; //Will update player movement every 3 seconds of game delta time
        this.playerMovementTimer = 0;

    }

    /**
     * Creates players at RANDOM locations on the board using Player objects and stores them in an array
     */
    public void createPlayersRandomLocations()
    {
        Random aRan = new Random();
        int currentPlayersAdded = 0;

        //This loop will add random players and makes sure that it doesn't add on the same location
        while (currentPlayersAdded < this.numberOfPlayers)
        {
            int xArrayLocation = aRan.nextInt(numberOfColumns);
            int yArrayLocation = aRan.nextInt(numberOfRows);

            if (playerBoard.boardArray[yArrayLocation][xArrayLocation] == null)
            {
                String aName = playerNames[currentPlayersAdded];
                Player aPlayer = new Player(xArrayLocation, yArrayLocation, Color.FIREBRICK, pixelBlockWidth, pixelBlockHeight, aName);
                aPlayers[currentPlayersAdded] = aPlayer;
                playerBoard.boardArray[yArrayLocation][xArrayLocation] = aPlayer;
                currentPlayersAdded++;
            }
        }
    }

    /**
     * Creates players at default locations. Which are the corners of the board.
     */
    public void createPlayersDefaultLocation() throws IllegalArgumentException
    {
        if (numberOfPlayers > 4)
        {
            throw new IllegalArgumentException("Too many players and not enough corners, for default corner locations");
        }

        int currentPlayersAdded = 0;
        int xVector = numberOfColumns - 1; //To eliminate index out of bounds exception, subtract 1
        int yVector = numberOfRows - 1;
        int xArrayLocation = 0;
        int yArrayLocation = 0;

        //This default player creation will create players starting at the Northwest corner
        while (currentPlayersAdded < this.numberOfPlayers)
        {
            Player aPlayer = new Player(xArrayLocation, yArrayLocation, Color.FIREBRICK, pixelBlockWidth, pixelBlockHeight, "Meow");
            aPlayers[currentPlayersAdded] = aPlayer;
            //playerBoard.boardArray[yArrayLocation][xArrayLocation] = aPlayer;
            tileBoard.getPiecesArray()[yArrayLocation][xArrayLocation].addPiece(aPlayer);
            currentPlayersAdded++;

            //SouthEast Corner
            if (xArrayLocation == 0 && yArrayLocation == 0)
            {
                xArrayLocation += xVector;
                yArrayLocation += yVector;
            }
            //SouthWest Corner
            else if (xArrayLocation == xVector && yArrayLocation == yVector)
            {
                xArrayLocation -= xVector;
            }
            //NorthEast Corner
            else
            {
                xArrayLocation += xVector;
                yArrayLocation -= yVector;
            }
        }
    }

    /**
     * Checks for player collisions using just array locations. Should check for collisions
     * after moving players
     *
     * @return boolean
     */
    public boolean playerConflict()
    {
        boolean answer = false;
        Player firstPlayer = aPlayers[0];

        for (int i = 1; i < aPlayers.length; i++)
        {
            Player anotherPlayer = aPlayers[i];
            answer = firstPlayer.checkCollision(anotherPlayer);
            if (answer)
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Creates an array of GraphicsTiles objects. Using the i and j values in the loops for x/y tile location
     */
    public void createArray()
    {
        for (int i = 0; i < tileBoard.getPiecesArray().length; i++)
        {
            //TODO Fix color, always adding alpha of 0.1f even on reset
            for (int j = 0; j < tileBoard.getPiecesArray()[i].length; j++)
            {
                GraphicsTile aTile = new GraphicsTile(j, i, Color.GRAY, pixelBlockWidth, pixelBlockHeight);
                Pieces somePiece = new Pieces();
                tileBoard.getPiecesArray()[i][j] = somePiece;
                somePiece.addPiece(aTile);
            }
        }
    }

    public void drawBoard(ShapeRenderer renderer)
    {
        for (int i = 0; i < tileBoard.getPiecesArray().length; i++)
        {
            for (int j = 0; j < tileBoard.getPiecesArray()[i].length; j++)
            {
                Pieces somePiece = tileBoard.getPiecesArray()[i][j];
                somePiece.draw(renderer);
                //aBlock.draw(renderer);
            }
        }
    }

    public void drawPlayers(ShapeRenderer renderer)
    {
        for (Player somePlayer : aPlayers)
        {
            somePlayer.draw(renderer);
        }
    }

    public void fade(ShapeRenderer renderer)
    {
        for (int i = 0; i < tileBoard.getPiecesArray().length; i++)
        {
            for (int j = 0; j < tileBoard.getPiecesArray()[i].length; j++)
            {
                Pieces somePiece = tileBoard.getPiecesArray()[i][j];
                somePiece.fade(renderer);
                //aBlock.draw(renderer);
            }
        }
    }

    public void decreaseSpeed()
    {
        playerUpdateTime += 0.01f;
    }

    public void increaseSpeed()
    {
        if ((playerUpdateTime - 0.01f) >= 0.0f)
        {
            playerUpdateTime -= 0.01f;
        }
    }

    /**
     * Updates location of players per game delta time
     */
    public void updatePlayers()
    {
        playerMovementTimer += Gdx.graphics.getDeltaTime();

        if (playerMovementTimer >= playerUpdateTime) //Updates movement every 3 seconds
        {
            for (Player somePlayer : aPlayers)
            {
                int oldXArrayLocation = somePlayer.xArrayLocation;
                int oldYArrayLocation = somePlayer.yArrayLocation;
                boolean playerMoved = somePlayer.playerMovement(numberOfRows, numberOfColumns);
                if (playerMoved)
                {
                    //playerBoard.boardArray[somePlayer.yArrayLocation][somePlayer.xArrayLocation] = somePlayer;
                    //playerBoard.boardArray[oldYArrayLocation][oldXArrayLocation] = null;
                    tileBoard.getPiecesArray()[oldYArrayLocation][oldXArrayLocation].removePiece(somePlayer);
                    tileBoard.getPiecesArray()[somePlayer.yArrayLocation][somePlayer.xArrayLocation].addPiece(somePlayer);
                }
                assert somePlayer.yArrayLocation < numberOfRows;
            }
            totalPlayerMovements++;
            playerMovementTimer = 0;
        }
    }
}