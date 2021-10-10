package com.woods.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;

/**
 * Selection screen for the students BETWEEN kindergarten and 6-8
 */
public class MiddleScreen implements Screen
{

    Group someGroup;
    final Woods game;
    final MenuScreen aMenuScreen;
    ShapeRenderer aShape;
    BoardController aBoardController;
    Skin someSkin;
    Button exitButton;
    Stage uiStage;
    TextField rowTextField;
    TextField colTextField;
    Board boardOfScreen; //Used for dimensions of screen
    Group textFieldsAndPlayers; //Used for adding text fields and number of players text field
    Background rainDropsBackground;
    float animationStateTime;

    public MiddleScreen(Woods game, MenuScreen aMenuScreen, int rows, int columns)
    {
        this.game = game;
        this.aMenuScreen = aMenuScreen;
        aShape = new ShapeRenderer();
        someSkin = new Skin();
        uiStage = new Stage(game.aViewport);
        boardOfScreen = new Board(50, 50, game.camera.viewportWidth / 50,
                game.camera.viewportHeight / 50);
        someGroup = new Group();
        rainDropsBackground = new Background(game.backgroundTextures, 30, .05f, game.camera, 4, 4);
        animationStateTime = 0f;
        createButtons();



        //aBoardController = new BoardController(game, rows, columns, game.camera.viewportWidth / columns, game.camera.viewportHeight / rows);
    }

    private void createButtons()
    {

        //Defining another Exit button here. If you use a previously defined exit button from another object, it will keep the previous
        //listener definition. Removing the old listener would be ideal
        Button.ButtonStyle exitButtonStyle = new Button.ButtonStyle();
        Texture exitTexture = game.menuTextures.get("Exit");
        someSkin.add("exit", exitTexture);
        TextureRegion exitRegion = new TextureRegion(exitTexture);
        exitButtonStyle.up = new TextureRegionDrawable(exitRegion);
        exitButtonStyle.over = someSkin.newDrawable("exit", Color.CORAL);
        exitButton = new Button(exitButtonStyle);
        exitButton.setWidth((float) exitTexture.getWidth() / 4);
        exitButton.setHeight((float) exitTexture.getHeight() / 4);
        exitButton.setColor(Color.CHARTREUSE.r, Color.CHARTREUSE.g, Color.CHARTREUSE.b, 0.8f);

        rowTextField = game.textFields.get("Row");
        rowTextField.setX(boardOfScreen.blockPixelWidth* 25);
        rowTextField.setY(boardOfScreen.blockPixelHeight * 20);
        rowTextField.setText("2-50");
        someGroup.addActor(rowTextField);

        colTextField = game.textFields.get("Col");
        colTextField.setX(boardOfScreen.blockPixelWidth* 25);
        colTextField.setY(boardOfScreen.blockPixelHeight * 17);
        colTextField.setText("2-50");
        someGroup.addActor(colTextField);

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = game.medievalFont;
        someSkin.add("default", labelStyle);

        Label rows = new Label("Rows:", someSkin);
        rows.setPosition(boardOfScreen.blockPixelWidth * 19, boardOfScreen.blockPixelHeight* 20);
        Label columns = new Label("Columns:", someSkin);
        columns.setPosition(boardOfScreen.blockPixelWidth * 19, boardOfScreen.blockPixelHeight * 17);
        someGroup.addActor(rows);
        someGroup.addActor(columns);


        uiStage.addActor(someGroup);
        uiStage.addActor(exitButton);

    }

    @Override
    public void show()
    {
        Gdx.input.setInputProcessor(uiStage);

        //This button will exit the game in the main menu
        exitButton.addListener(new ChangeListener()
        {
            @Override
            public void changed(ChangeEvent event, Actor actor)
            {
                game.setScreen(new MenuScreen(game));
            }
        });
    }

    @Override
    public void render(float delta)
    {
        ScreenUtils.clear(0, 0, 0.2f, 1); //Just clears the screen to the specified color
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); //Clears buffer
        aShape.setProjectionMatrix(game.camera.combined);
        animationStateTime += Gdx.graphics.getDeltaTime(); // getDeltaTime() is the amount of time since the last frame rendered

        rainDropsBackground.draw(game.batch, animationStateTime);
        uiStage.act();
        uiStage.draw(); //Draws everything on the stage
    }

    @Override
    public void resize(int width, int height)
    {
        game.aViewport.update(width, height);
    }

    @Override
    public void pause()
    {

    }

    @Override
    public void resume()
    {

    }

    @Override
    public void hide()
    {

    }

    @Override
    public void dispose()
    {

    }
}
