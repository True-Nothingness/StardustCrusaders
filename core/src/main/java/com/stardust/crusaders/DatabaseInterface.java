package com.stardust.crusaders;

public interface DatabaseInterface {
    boolean insertScore(String name, int score);
    String[] getTopScores();
}
