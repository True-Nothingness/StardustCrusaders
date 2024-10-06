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

public class PauseScreen implements Screen {
    private SpriteBatch batch;
    private Texture menuBGTexture;
    private TextureAtlas buttonsAtlas;
    private Camera camera;
    private Viewport viewport;
    private Label titleLabel;
    private Stage stage;
    private ImageButton resumeButton, resetButton, menuButton;
    private TextureRegion resumeTextureRegion, resetTextureRegion, menuTextureRegion;
    final SpaceShooterGame game;
    //world parameters
    private final float WORLD_WIDTH = 72;
    private final float WORLD_HEIGHT = 128;
    PauseScreen(SpaceShooterGame game){
        this.game = game;
        camera = new OrthographicCamera();
        viewport = new StretchViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        menuBGTexture = new Texture("bg2.png");
        batch = new SpriteBatch();
        stage = new Stage();
        //set up the texture atlas
        buttonsAtlas = new TextureAtlas("Buttons.atlas");
        //initialize texture regions
        resumeTextureRegion = buttonsAtlas.findRegion("Play");
        resetTextureRegion = buttonsAtlas.findRegion("Repeat");
        menuTextureRegion = buttonsAtlas.findRegion("Menu");
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.draw(menuBGTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        prepareLabel();
        prepareButtons();
        batch.end();
        stage.act(delta);
        stage.draw();
    }
    public void prepareLabel(){
        Color customColor = new Color(0xFF, 0xD6, 0x00, 1f);
        titleLabel = new Label("PAUSED", new Label.LabelStyle(game.fontItalic, customColor));
        GlyphLayout titleLayout = new GlyphLayout(game.fontItalic, titleLabel.getText());
        float textWidth = titleLayout.width;
        titleLabel.setPosition((Gdx.graphics.getWidth() - textWidth) / 2, 1800);
        stage.addActor(titleLabel);
    }
    public void prepareButtons(){
        TextureRegionDrawable resumeDrawable = new TextureRegionDrawable(resumeTextureRegion);
        resumeButton = new ImageButton(resumeDrawable);
        resumeButton.setPosition((Gdx.graphics.getWidth() - resumeButton.getWidth())/2, 1400);
        resumeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.gameScreen.resume();
                game.setScreen(game.gameScreen);
            }
        });
        TextureRegionDrawable resetDrawable = new TextureRegionDrawable(resetTextureRegion);
        resetButton = new ImageButton(resetDrawable);
        resetButton.setPosition((Gdx.graphics.getWidth() - resetButton.getWidth())/2, 1100);
        resetButton.addListener(new ClickListener(){
           @Override
           public void clicked(InputEvent event, float x, float y){
               game.gameScreen.clearGameState();
               game.setScreen(game.gameScreen);
           }
        });
        TextureRegionDrawable menuDrawable = new TextureRegionDrawable(menuTextureRegion);
        menuButton = new ImageButton(menuDrawable);
        menuButton.setPosition((Gdx.graphics.getWidth() - menuButton.getWidth())/2, 800);
        menuButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                game.gameScreen.clearGameState();
                game.setScreen(game.mainMenuScreen);
            }
        });
        stage.addActor(resumeButton);
        stage.addActor(resetButton);
        stage.addActor(menuButton);
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
        stage.dispose();
        batch.dispose();
    }
}
