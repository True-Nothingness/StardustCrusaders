package com.stardust.crusaders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class MainMenuScreen implements Screen {
    private SpriteBatch batch;
    private Texture menuBGTexture;
    private Camera camera;
    private Viewport viewport;
    private Label titleLabel, startLabel, optLabel, hsLabel, quitLabel;
    private Stage stage;
    private Music menu;
    final SpaceShooterGame game;
    private boolean bgmState;
    //world parameters
    private final float WORLD_WIDTH = 72;
    private final float WORLD_HEIGHT = 128;
    MainMenuScreen(final SpaceShooterGame game) {
        this.game = game;
        camera = new OrthographicCamera();
        viewport = new StretchViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        menuBGTexture = new Texture("bg.png");
        bgmState = game.bgmState;
        menu = game.menu;
        batch = new SpriteBatch();
        stage = new Stage();
    }
    @Override
    public void show() {
        // Load assets here (fonts, textures)
        if (bgmState){
            menu.play();
        }
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.draw(menuBGTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        prepareLabel();
        batch.end();
        stage.act(delta);
        stage.draw();
    }

    public void prepareLabel(){
        Color customColor = new Color(0xFF, 0xD6, 0x00, 1f);
        titleLabel = new Label("STARDUST CRUSADERS", new Label.LabelStyle(game.fontItalic, customColor));
        GlyphLayout titleLayout = new GlyphLayout(game.fontItalic, titleLabel.getText());
        float textWidth = titleLayout.width;
        titleLabel.setPosition((Gdx.graphics.getWidth() - textWidth) / 2, 1800);
        startLabel = new Label("START GAME", new Label.LabelStyle(game.fontRegular, customColor));
        startLabel.setPosition(100, 1500);
        startLabel.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                if (menu.isPlaying()){
                    menu.stop();
                }
                game.setScreen(game.gameScreen);
            }
        });
        optLabel = new Label("OPTIONS", new Label.LabelStyle(game.fontRegular, customColor));
        optLabel.setPosition(100, 1300);
        optLabel.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                game.setScreen(game.settingsScreen);
            }
        });
        hsLabel = new Label("HIGHSCORE", new Label.LabelStyle(game.fontRegular, customColor));
        hsLabel.setPosition(100, 1100);
        hsLabel.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                game.setScreen(game.highscoreScreen);
            }
        });
        quitLabel = new Label("QUIT GAME", new Label.LabelStyle(game.fontRegular, customColor));
        quitLabel.setPosition(100, 900);
        quitLabel.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                Gdx.app.exit();
            }
        });
        stage.addActor(titleLabel);
        stage.addActor(startLabel);
        stage.addActor(optLabel);
        stage.addActor(hsLabel);
        stage.addActor(quitLabel);
    }
    @Override
    public void resize(int width, int height) {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        // Dispose assets here
        batch.dispose();
        stage.dispose();
    }
}
