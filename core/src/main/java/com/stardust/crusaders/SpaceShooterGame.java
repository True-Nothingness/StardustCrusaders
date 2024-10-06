package com.stardust.crusaders;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
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
    public PauseScreen pauseScreen;
    public GameOverScreen gameoverScreen;
    public static Random random = new Random();
    BitmapFont fontItalic, fontRegular;
    Music music, menu;
    Sound death, laser, power;
    boolean bgmState, sfxState = true;
    Preferences prefs;
    enum MODE{
        EASY,
        MEDIUM,
        HARD
    }
    MODE mode;
    int modeNumber = 1;
    @Override
    public void create() {
        music = Gdx.audio.newMusic(Gdx.files.internal("music/bgm.ogg"));
        menu = Gdx.audio.newMusic(Gdx.files.internal("music/menu.ogg"));
        death = Gdx.audio.newSound(Gdx.files.internal("music/death.ogg"));
        laser = Gdx.audio.newSound(Gdx.files.internal("music/laser.ogg"));
        power = Gdx.audio.newSound(Gdx.files.internal("music/power.ogg"));
        prefs = Gdx.app.getPreferences("My Preferences");
        bgmState = prefs.getBoolean("bgmState");
        sfxState = prefs.getBoolean("sfxState");
        modeNumber = prefs.getInteger("mode");
        switch (modeNumber){
            case 0:
                mode = MODE.EASY;
                break;
            case 1:
                mode = MODE.MEDIUM;
                break;
            case 2:
                mode = MODE.HARD;
                break;
            default:
                break;
        }
        FreeTypeFontGenerator fontItalicGen = new FreeTypeFontGenerator(Gdx.files.internal("EdgeOfTheGalaxyPosterItalic-x3o1m.otf"));
        FreeTypeFontGenerator.FreeTypeFontParameter fontItalicParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        fontItalicParameter.size = 110;
        fontItalicParameter.borderWidth = 3.6f;
        fontItalicParameter.color = new Color(255, 214, 0, 255);
        fontItalicParameter.borderColor = new Color(0, 0, 0, 0.3f);
        fontItalic = fontItalicGen.generateFont(fontItalicParameter);
        fontItalicGen.dispose();
        FreeTypeFontGenerator fontRegularGen = new FreeTypeFontGenerator(Gdx.files.internal("EdgeOfTheGalaxyRegular-OVEa6.otf"));
        FreeTypeFontGenerator.FreeTypeFontParameter fontRegularParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        fontRegularParameter.size = 70;
        fontRegularParameter.borderWidth = 3.6f;
        fontRegularParameter.color = new Color(255, 214, 0, 255);
        fontRegularParameter.borderColor = new Color(0, 0, 0, 0.3f);
        fontRegular = fontRegularGen.generateFont(fontRegularParameter);
        fontRegularGen.dispose();
        mainMenuScreen = new MainMenuScreen(this);
        gameScreen = new GameScreen(this);
        settingsScreen = new SettingsScreen(this);
        highscoreScreen = new HighscoreScreen(this);
        pauseScreen = new PauseScreen(this);
        gameoverScreen = new GameOverScreen(this);
        music.setLooping(true);
        music.setVolume(0.05f);
        menu.setLooping(true);
        menu.setVolume(0.3f);
        // Start with the Main Menu Screen
        setScreen(mainMenuScreen);
    }

    @Override
    public void dispose() {
        mainMenuScreen.dispose();
        gameScreen.dispose();
        settingsScreen.dispose();
        highscoreScreen.dispose();
        pauseScreen.dispose();
        gameoverScreen.dispose();
        if (music != null) {
            music.dispose(); // Dispose of the music when the game is done
        }
        laser.dispose();
        death.dispose();
        power.dispose();
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
