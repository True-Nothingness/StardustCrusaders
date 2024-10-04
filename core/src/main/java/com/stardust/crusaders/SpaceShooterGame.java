package com.stardust.crusaders;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

import java.util.Random;

public class SpaceShooterGame extends Game {

    public MainMenuScreen mainMenuScreen;
    public GameScreen gameScreen;
    public SettingsScreen settingsScreen;
    public HighscoreScreen highscoreScreen;
    public static Random random = new Random();
    BitmapFont fontItalic, fontRegular;

    @Override
    public void create() {
        FreeTypeFontGenerator fontItalicGen = new FreeTypeFontGenerator(Gdx.files.internal("EdgeOfTheGalaxyPosterItalic-x3o1m.otf"));
        FreeTypeFontGenerator.FreeTypeFontParameter fontItalicParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        fontItalicParameter.size = 106;
        fontItalicParameter.borderWidth = 3.6f;
        fontItalicParameter.color = new Color(255, 214, 0, 255);
        fontItalicParameter.borderColor = new Color(0, 0, 0, 0.3f);
        fontItalic = fontItalicGen.generateFont(fontItalicParameter);
        fontItalicGen.dispose();
        FreeTypeFontGenerator fontRegularGen = new FreeTypeFontGenerator(Gdx.files.internal("EdgeOfTheGalaxyRegular-OVEa6.otf"));
        FreeTypeFontGenerator.FreeTypeFontParameter fontRegularParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        fontRegularParameter.size = 64;
        fontRegularParameter.borderWidth = 3.6f;
        fontRegularParameter.color = new Color(255, 214, 0, 255);
        fontRegularParameter.borderColor = new Color(0, 0, 0, 0.3f);
        fontRegular = fontRegularGen.generateFont(fontRegularParameter);
        fontRegularGen.dispose();
        mainMenuScreen = new MainMenuScreen(this);
        gameScreen = new GameScreen(this);
        settingsScreen = new SettingsScreen(this);
        highscoreScreen = new HighscoreScreen(this);
        // Start with the Main Menu Screen
        setScreen(mainMenuScreen);
    }

    @Override
    public void dispose() {
        mainMenuScreen.dispose();
        gameScreen.dispose();
        settingsScreen.dispose();
        highscoreScreen.dispose();
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void resize(int width, int height) {
        gameScreen.resize(width, height);
    }
}
