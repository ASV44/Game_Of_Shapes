package com.game.shapes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hackintosh on 1/5/17.
 */

public class HighScores {

    public List<Integer> countMoves = new ArrayList<Integer>();
    public List<Integer> timeAttack = new ArrayList<Integer>();
    public  List<Integer> timeChallenge = new ArrayList<Integer>();

    public HighScores() {
        try {
            read_HighScore("Levels/countMoves.txt",countMoves);
            read_HighScore("Levels/timeAttack.txt",timeAttack);
            read_HighScore("Levels/timeChallenge.txt",timeChallenge);
        }
        catch(IOException exception) {
            Gdx.app.log("Exception","",exception);
        }
    }

    public void read_HighScore(String ScoreList_name, List<Integer> scores) throws IOException {
        FileHandle scoreList = Gdx.files.internal(ScoreList_name);
        BufferedReader bufferedReader = new BufferedReader(scoreList.reader());
        int i = 0;
        String score;

        try {
            while((score = bufferedReader.readLine()) !=  null) {
                scores.add(Integer.parseInt(score));
                i++;
            }
        }
        finally {
            bufferedReader.close();
        }
    }
}
