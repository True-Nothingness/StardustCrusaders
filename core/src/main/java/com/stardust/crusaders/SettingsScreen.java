package com.stardust.crusaders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class SettingsScreen implements Screen {
    private SpriteBatch batch;
    private Texture menuBGTexture;
    private Camera camera;
    private boolean sfxState, bgmState;
    final SpaceShooterGame game;
    private Label titleLabel,sfxLabel, bgmLabel, backLabel, difficultyLabel;
    private Stage stage;
    private SpaceShooterGame.MODE mode;

    SettingsScreen(final SpaceShooterGame game){
        this.game = game;
        camera = new OrthographicCamera();
        menuBGTexture = new Texture("bg.png");
        sfxState = game.sfxState;
        bgmState = game.bgmState;
        batch = new SpriteBatch();
        stage = new Stage();
        mode = game.mode;
        prepareLabel();
    }
    @Override
    public void show() {
        // Load assets here (fonts, textures)
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.draw(menuBGTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        updateLabel();
        batch.end();
        stage.act(delta);
        stage.draw();
    }

    public void prepareLabel(){
        Color customColor = new Color(0xFF, 0xD6, 0x00, 1f);
        titleLabel = new Label("OPTIONS", new Label.LabelStyle(game.fontItalic, customColor));
        GlyphLayout titleLayout = new GlyphLayout(game.fontItalic, titleLabel.getText());
        float titleWidth = titleLayout.width;
        titleLabel.setPosition((Gdx.graphics.getWidth() - titleWidth) / 2, 1800);
        sfxLabel = new Label("SFX: "+(sfxState ? "ON" : "OFF"), new Label.LabelStyle(game.fontRegular, customColor));
        sfxLabel.setPosition(100, 1500);
        sfxLabel.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                sfxState = !sfxState; // Toggle sfxState
                game.prefs.putBoolean("sfxState", sfxState);
                game.prefs.flush();
            }
        });
        bgmLabel = new Label("MODE: "+(bgmState ? "ON" : "OFF"), new Label.LabelStyle(game.fontRegular, customColor));
        bgmLabel.setPosition(100, 1300);
        bgmLabel.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                bgmState = !bgmState; // Toggle sfxState
                if (bgmState){
                    game.music.play();
                } else {
                    game.music.stop();
                }
                game.prefs.putBoolean("bgmState", bgmState);
                game.prefs.flush();
            }
        });
        difficultyLabel = new Label("MODE: "+mode.name(), new Label.LabelStyle(game.fontRegular, customColor));
        difficultyLabel.setPosition(100, 1100);
        difficultyLabel.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                if(game.mode.equals(SpaceShooterGame.MODE.MEDIUM)){
                    mode = SpaceShooterGame.MODE.HARD;
                    game.gameScreen.
                    game.prefs.putInteger("mode", 2);
                    game.prefs.flush();
                } else if (game.mode.equals(SpaceShooterGame.MODE.HARD)) {
                    mode = SpaceShooterGame.MODE.EASY;
                    game.prefs.putInteger("mode", 0);
                    game.prefs.flush();
                } else {
                    mode = SpaceShooterGame.MODE.MEDIUM;
                    game.prefs.putInteger("mode", 1);
                    game.prefs.flush();
                }
            }
        });
        backLabel = new Label("MAIN MENU", new Label.LabelStyle(game.fontRegular, customColor));
        backLabel.setPosition(100, 900);
        backLabel.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                game.setScreen(game.mainMenuScreen);
            }
        });
        stage.addActor(titleLabel);
        stage.addActor(sfxLabel);
        stage.addActor(bgmLabel);
        stage.addActor(difficultyLabel);
        stage.addActor(backLabel);
    }
    private void updateLabel(){
        sfxLabel.setText("SFX: " + (sfxState ? "ON" : "OFF"));
        bgmLabel.setText("BGM: " + (bgmState ? "ON" : "OFF"));
        difficultyLabel.setText("MODE: "+mode.name());
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
