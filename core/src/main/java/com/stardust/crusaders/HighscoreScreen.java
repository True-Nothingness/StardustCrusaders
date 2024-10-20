package com.stardust.crusaders;

import com.badlogic.gdx.Gdx;
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
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class HighscoreScreen implements Screen {
    private SpriteBatch batch;
    private Texture menuBGTexture;
    private Camera camera;
    final SpaceShooterGame game;
    private Stage stage;
    private Label titleLabel, backLabel;
    private Table table;
    private String[] topScores;
    HighscoreScreen(final SpaceShooterGame game) {
        this.game = game;
        camera = new OrthographicCamera();
        menuBGTexture = new Texture("highscore.png");
        batch = new SpriteBatch();
        stage = new Stage();
        table = new Table();
        table.setFillParent(true);  // Make the table fill the stage
    }

    @Override
    public void show() {
        // Load assets here (fonts, textures)
        Gdx.input.setInputProcessor(stage);
        showLeaderboard();
    }
    public void showLeaderboard() {
        table.clear();
        // Fetch the top 5 scores from the database
        topScores = game.getDatabaseInterface().getTopScores();
        Color customColor = new Color(0xFF, 0xD6, 0x00, 1f);
        titleLabel = new Label("LEADERBOARD", new Label.LabelStyle(game.fontItalic, customColor));
        GlyphLayout titleLayout = new GlyphLayout(game.fontItalic, titleLabel.getText());
        float titleWidth = titleLayout.width;
        float titleHeight = titleLayout.height;
        titleLabel.setPosition((Gdx.graphics.getWidth() - titleWidth) / 2, Gdx.graphics.getHeight() - titleHeight - 600);
        backLabel = new Label("MAIN MENU", new Label.LabelStyle(game.fontRegular, customColor));
        backLabel.setPosition((Gdx.graphics.getWidth() - backLabel.getWidth()) / 2, 600);
        backLabel.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                game.setScreen(game.mainMenuScreen);
            }
        });
        // Loop through the top scores and add them as labels
        for (int i = 0; i < topScores.length; i++) {
            if (topScores[i] != null) {
                // Split the name and score from the topScores string
                String[] scoreData = topScores[i].split(" - ");
                String playerName = scoreData[0];
                String playerScore = scoreData[1];

                // Create two labels: one for the name, one for the score
                Label nameLabel = new Label((i + 1) + ". " + playerName, new Label.LabelStyle(game.fontRegular, customColor));
                Label scoreLabel = new Label(playerScore, new Label.LabelStyle(game.fontRegular, customColor));
                nameLabel.setFontScale(1.3f);
                scoreLabel.setFontScale(1.3f);
                // Align the name to the left and the score to the right with padding between them
                table.add(nameLabel).expandX().left().padRight(100).padBottom(50);  // Expands to the left, pad 100 pixels to the right
                table.add(scoreLabel).expandX().right();              // Expands to the right
                table.pad(30);
                table.row();  // Move to the next row for the next score
            }
        }

        // Add the table to the stage
        stage.addActor(table);
        stage.addActor(titleLabel);
        stage.addActor(backLabel);
    }
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.draw(menuBGTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.end();
        stage.act(delta);
        stage.draw();
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
    }
}
