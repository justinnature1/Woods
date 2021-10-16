package com.woods.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.*;

/**
 * This is a Board controller class that controls the state of the game board.
 */
public class BoardController
{

    Woods game;
    BoardOfPieces tileBoard;
    Board playerBoard;
    ArrayList<Player> collidedLocations; //Used to remember location of player conflicts
    int numberOfRows;
    int numberOfColumns;
    float pixelBlockWidth;
    float pixelBlockHeight;
    int numberOfPlayers;
    Player[] aPlayers;
    String[] playerNames = {"Joel", "Chris", "Mary", "Lyra"};
    int totalPlayerMovements;
    float average;

    Music adventureMusic;

    float playerUpdateTime;
    float playerMovementTimer;


    public BoardController(Woods aGame, int numberOfRows, int numberOfColumns, float pixelBlockWidth, float pixelBlockHeight, int numberOfPlayers)
    {
        /*if (numberOfRows < 5 || numberOfColumns < 5)
        {
            throw new IllegalArgumentException("Number of rows or number of columns must be above 9");
        }
        if (numberOfPlayers < 2)
        {
            throw new IllegalArgumentException("Number of players must be above 1");
        }*/
        this.game = aGame;
        this.tileBoard = new BoardOfPieces(numberOfRows, numberOfColumns, pixelBlockWidth, pixelBlockHeight);
        this.playerBoard = new Board(numberOfRows, numberOfColumns, pixelBlockWidth, pixelBlockHeight);
        this.collidedLocations = new ArrayList<>();
        this.numberOfRows = numberOfRows;
        this.numberOfColumns = numberOfColumns;
        this.pixelBlockWidth = pixelBlockWidth;
        this.pixelBlockHeight = pixelBlockHeight;
        this.numberOfPlayers = numberOfPlayers;
        this.aPlayers = new Player[numberOfPlayers];
        this.totalPlayerMovements = 0;
        this.playerUpdateTime = .3f; //Will update player movement every .3 seconds of game delta time
        this.adventureMusic = Gdx.audio.newMusic(Gdx.files.internal("brazilian.mp3"));
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

        int amountOfPlayersLeftToAdd = 0;
        int xVector = numberOfColumns - 1; //To eliminate index out of bounds exception, subtract 1
        int yVector = numberOfRows - 1;
        int xArrayLocation = 0;
        int yArrayLocation = 0;
        Player aPlayer;

        //TODO Should fix the following loop, just use a simple equation to put players in corners instead
        //This default player creation will create players starting at the Northwest corner
        while (amountOfPlayersLeftToAdd < this.numberOfPlayers)
        {
            aPlayer = new Player(xArrayLocation, yArrayLocation, Color.FIREBRICK, pixelBlockWidth, pixelBlockHeight, "Meow");
            aPlayers[amountOfPlayersLeftToAdd] = aPlayer;
            //playerBoard.boardArray[yArrayLocation][xArrayLocation] = aPlayer;
            //tileBoard.getPiecesArray()[yArrayLocation][xArrayLocation].addPiece(aPlayer); Might use this later

            //NorthEast Corner
            if (xArrayLocation == 0 && yArrayLocation == 0)
            {
                xArrayLocation += xVector;
                yArrayLocation += yVector;
            }
            //NorthWest Corner
            else if (xArrayLocation == numberOfColumns - 1 && yArrayLocation == numberOfRows - 1)
            {
                xArrayLocation -= xVector;
            }
            //SouthEast Corner
            else if (xArrayLocation == 0 && yArrayLocation == numberOfRows - 1)
            {
                xArrayLocation += xVector;
                yArrayLocation -= yVector;
            }
            amountOfPlayersLeftToAdd++;
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
        ArrayList<Player> playerConflictArrayList = new ArrayList<>();

        for (Player aPlayer : aPlayers)
        {
            if (playerConflictArrayList.contains(aPlayer))
            {
                collidedLocations.add(new Player(aPlayer.xArrayLocation, aPlayer.yArrayLocation, new Color(Color.LIGHT_GRAY),
                        aPlayer.width, aPlayer.height, "found"));
                //Pieces somePieces = tileBoard.getPiecesArray()[aPlayer.yArrayLocation][aPlayer.xArrayLocation];
                //somePieces.addPiece(new Player(aPlayer.xArrayLocation, aPlayer.yArrayLocation, Color.SALMON, aPlayer.width, aPlayer.height, "found"));
                //aPlayer.color.set(Color.MAGENTA);
                return true;
            } else
            {
                playerConflictArrayList.add(aPlayer);
            }
        }
        return false;
    }

    /**
     * Draws all the previous and current conflict on the board
     */
    public void drawConflict(ShapeRenderer aRenderer)
    {
        for (Player aPlayer : collidedLocations)
        {
            aPlayer.draw(aRenderer);
        }
    }

    public void drawCollision(found foundFunction)
    {
        foundFunction.drawCollision(game.batch);
    }

    public void drawStatistics(statistics aStatFunction)
    {
        aStatFunction.drawStatistics(game.batch);
    }

    public void drawDirections()
    {
        game.medievalFont.setColor(1, 1, 0, 1f);
        game.medievalFont.draw(game.batch, "Press Left to slow or Right Arrow increase speed", game.camera.viewportWidth / 2 - 350, 50);
        game.medievalFont.draw(game.batch, "Press R to Reset", game.camera.viewportWidth - 250, 75);

        game.medievalFont.draw(game.batch, "ESC to exit", 0, 75);
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

    /**
     * Takes an array of textures and randomly places them on every block on the gameboard
     *
     * @param textureArray Texture[]
     */
    public void createArrayOfTextures(Texture[] textureArray)
    {
        Random aRan = new Random();
        Texture aTexture;
        int arrayTextureIndex;
        GraphicsTile aRectTile;
        TextureTile aTile;
        Pieces somePiece;

        for (int i = 0; i < tileBoard.getPiecesArray().length; i++)
        {
            for (int j = 0; j < tileBoard.getPiecesArray()[i].length; j++)
            {
                arrayTextureIndex = aRan.nextInt(10);
                aTexture = textureArray[4]; //Too many different textures on large super large boards will slow down HTML build, GWT is slow

                aTile = new TextureTile(j, i, pixelBlockWidth, pixelBlockHeight, Color.GRAY, aTexture);
                aRectTile = new GraphicsTile(j, i, Color.GRAY, pixelBlockWidth, pixelBlockHeight);
                somePiece = new Pieces();
                tileBoard.getPiecesArray()[i][j] = somePiece;
                somePiece.addPiece(aTile);
                somePiece.addPiece(aRectTile);

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

    /**
     * Draws each individual Piece on the board using the Pieces data structure.
     * Must be used between a SpriteBatch.Begin() and SpriteBatch.End()
     *
     * @param aBatch SpriteBatch
     */
    public void drawBoard(SpriteBatch aBatch)
    {
        for (int i = 0; i < tileBoard.getPiecesArray().length; i++)
        {
            for (int j = 0; j < tileBoard.getPiecesArray()[i].length; j++)
            {

                Pieces somePiece = tileBoard.getPiecesArray()[i][j];
                somePiece.draw(aBatch);
                //aBlock.draw(renderer);
            }
        }
    }

    /**
     * Draws each player that is located in the Players ArrayList
     * MUST be used between Sprit
     *
     * @param renderer
     */
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

    public Music getAdventureMusic()
    {
        return adventureMusic;
    }

    public float getAverage()
    {
        return average;
    }

    public void setAverage(float average)
    {
        this.average = average;
    }

    /**
     * Updates location of players per game delta time
     */
    public void updatePlayers()
    {
        playerMovementTimer += Gdx.graphics.getDeltaTime(); //Gets the time from last render

        if (playerMovementTimer >= playerUpdateTime) //Updates movement every .3 seconds
        {
            for (Player somePlayer : aPlayers)
            {
                int oldXArrayLocation = somePlayer.xArrayLocation;
                int oldYArrayLocation = somePlayer.yArrayLocation;
                boolean playerMoved = somePlayer.playerMovement(numberOfRows, numberOfColumns);
                /*if (playerMoved)
                {
                    //playerBoard.boardArray[somePlayer.yArrayLocation][somePlayer.xArrayLocation] = somePlayer;
                    //playerBoard.boardArray[oldYArrayLocation][oldXArrayLocation] = null;
                    //tileBoard.getPiecesArray()[oldYArrayLocation][oldXArrayLocation].removePiece(somePlayer);
                    //tileBoard.getPiecesArray()[somePlayer.yArrayLocation][somePlayer.xArrayLocation].addPiece(somePlayer);
                }
                assert somePlayer.yArrayLocation < numberOfRows;*/
            }
            totalPlayerMovements++;
            playerMovementTimer = 0;
        }
    }
}

interface found
{
    void drawCollision(SpriteBatch aBatch);
}

interface statistics
{
    void drawStatistics(SpriteBatch aBatch);
}