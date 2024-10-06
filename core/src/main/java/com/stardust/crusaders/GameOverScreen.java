package com.stardust.crusaders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class GameOverScreen implements Screen {
    private SpriteBatch batch;
    private Texture menuBGTexture;
    private TextureAtlas buttonsAtlas;
    private ImageButton saveButton, resetButton, menuButton;
    private TextureRegion saveTextureRegion, resetTextureRegion, menuTextureRegion;
    private Camera camera;
    private Viewport viewport;
    private Label titleLabel, scoreLabel, nameLabel;
    private Stage stage;
    int score;
    final SpaceShooterGame game;
    //world parameters
    private final float WORLD_WIDTH = 72;
    private final float WORLD_HEIGHT = 128;
    GameOverScreen(SpaceShooterGame game){
        this.game = game;
        camera = new OrthographicCamera();
        viewport = new StretchViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        menuBGTexture = new Texture("bg2.png");
        batch = new SpriteBatch();
        stage = new Stage();
        //set up the texture atlas
        buttonsAtlas = new TextureAtlas("Buttons.atlas");
        //initialize texture regions
        saveTextureRegion = buttonsAtlas.findRegion("Save");
        resetTextureRegion = buttonsAtlas.findRegion("Repeat");
        menuTextureRegion = buttonsAtlas.findRegion("Menu");
        prepareLabel();
        prepareButtons();
    }
    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }
    public void prepareLabel(){
        Color customColor = new Color(0xFF, 0xD6, 0x00, 1f);
        titleLabel = new Label("GAME OVER", new Label.LabelStyle(game.fontItalic, customColor));
        GlyphLayout titleLayout = new GlyphLayout(game.fontItalic, titleLabel.getText());
        float textWidth = titleLayout.width;
        titleLabel.setPosition((Gdx.graphics.getWidth() - textWidth) / 2, 1800);
        scoreLabel = new Label("Your score: " + score, new Label.LabelStyle(game.fontRegular, customColor));
        GlyphLayout scoreLayout = new GlyphLayout(game.fontRegular, scoreLabel.getText());
        float scoreWidth = scoreLayout.width;
        scoreLabel.setPosition((Gdx.graphics.getWidth() - scoreWidth) / 2, 1500);
        stage.addActor(titleLabel);
        stage.addActor(scoreLabel);
    }
    public void prepareButtons(){
        TextureRegionDrawable resetDrawable = new TextureRegionDrawable(resetTextureRegion);
        resetButton = new ImageButton(resetDrawable);
        resetButton.setPosition((Gdx.graphics.getWidth() - resetButton.getWidth())/3, 900);
        resetButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                game.gameScreen.clearGameState();
                game.setScreen(game.gameScreen);
            }
        });
        TextureRegionDrawable menuDrawable = new TextureRegionDrawable(menuTextureRegion);
        menuButton = new ImageButton(menuDrawable);
        menuButton.setPosition((Gdx.graphics.getWidth() - menuButton.getWidth())*2/3, 900);
        menuButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                game.gameScreen.clearGameState();
                game.setScreen(game.mainMenuScreen);
            }
        });
        stage.addActor(resetButton);
        stage.addActor(menuButton);
    }
    public void updateScore(){
        scoreLabel.setText("Your score: " + score);
        GlyphLayout scoreLayout = new GlyphLayout(game.fontRegular, scoreLabel.getText());
        float scoreWidth = scoreLayout.width;
        scoreLabel.setPosition((Gdx.graphics.getWidth() - scoreWidth) / 2, 1500);
    }
    public void saveScore(){
        Color customColor = new Color(0xFF, 0xD6, 0x00, 1f);
        nameLabel = new Label("You made it to the top!\nInput your name: " + score, new Label.LabelStyle(game.fontRegular, customColor));
        GlyphLayout nameLayout = new GlyphLayout(game.fontItalic, nameLabel.getText());
        float nameWidth = nameLayout.width;
        nameLabel.setPosition((Gdx.graphics.getWidth() - nameWidth) / 2, 1300);
        TextureRegionDrawable saveDrawable = new TextureRegionDrawable(saveTextureRegion);
        saveButton = new ImageButton(saveDrawable);
        saveButton.setPosition((Gdx.graphics.getWidth() - saveButton.getWidth())/2, 1000);
        saveButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

            }
        });
        stage.addActor(nameLabel);
        stage.addActor(saveButton);
    }
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.draw(menuBGTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        updateScore();
        batch.end();
        stage.act(delta);
        stage.draw();
    }
    public void setScore(int score){
        this.score = score;
    }
    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
