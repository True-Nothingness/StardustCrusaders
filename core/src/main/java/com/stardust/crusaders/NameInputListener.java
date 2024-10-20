package com.stardust.crusaders;

import com.badlogic.gdx.Input;

public class NameInputListener implements Input.TextInputListener {
    private SpaceShooterGame game; // Reference to the main game class
    private int score;    // Store the player's score

    // Constructor to pass the main game class and score
    public NameInputListener(SpaceShooterGame game, int score) {
        this.game = game;
        this.score = score;
    }

    @Override
    public void input(String text) {
        // Handle the name input here
        System.out.println("User entered: " + text);
        // Access the database interface from the game class
        DatabaseInterface db = game.getDatabaseInterface();
        db.insertScore(text, score);  // Save the player's name and score in the database
    }

    @Override
    public void canceled() {
        System.out.println("User canceled the input.");
    }
}
