package com.game;

public class Game {

    private int score = 0;

    public void addPoints(int points) {
        score += points;
    }

    public int getScore() {
        return score;
    }

    public static void main(String[] args) {
        Game game = new Game();
        game.addPoints(10);
        game.addPoints(20);

        System.out.println("🎮 Welcome to Java Arcade Game!");
        System.out.println("Final Score: " + game.getScore());
    }
}