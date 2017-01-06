package com.game.gameworld;

import com.game.shapes.HighScores;

import java.util.List;

/**
 * Created by hackintosh on 1/6/17.
 */

public class GameLevelsItems {
    private String gameMode;
    public List<Integer>  highScores;

    public GameLevelsItems(String gameMode, HighScores highScores) {
        this.gameMode = gameMode;
        chooseScores(gameMode, highScores);

    }

    public void update() {

    }

    public void chooseScores(String gameMode, HighScores highScores) {
        if(gameMode.equals("countMoves")) { this.highScores = highScores.countMoves; }

        if(gameMode.equals("timeAttack")) { this.highScores = highScores.timeAttack; }

        if(gameMode.equals("timeChallenge")) { this.highScores = highScores.timeChallenge; }
    }
}
