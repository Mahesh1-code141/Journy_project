package com.game;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    @Test
    void testScoreCalculation() {
        Game game = new Game();
        game.addPoints(10);
        game.addPoints(15);

        assertEquals(25, game.getScore());
    }
}