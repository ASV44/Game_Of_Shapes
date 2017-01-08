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

    public List<String> countMoves = null;
    public List<String> timeAttack = null;
    public  List<String> timeChallenge = null;

    public String countMoves_dir = "Levels/countMoves.txt";
    public String timeAttack_dir = "Levels/timeAttack.txt";
    public String timeChallenge_dir = "Levels/timeChallenge.txt";

    public HighScores() {
        update_HighScores();
    }

    public List<String> read_HighScore(String ScoreList_name) throws IOException {
        FileHandle scoreList;
        List<String> scores = new ArrayList<String>();
        if(Gdx.files.local(ScoreList_name).exists()) {
            scoreList = Gdx.files.local(ScoreList_name);
        } else {
            scoreList = Gdx.files.internal(ScoreList_name);
        }
        BufferedReader bufferedReader = new BufferedReader(scoreList.reader());
        int i = 0;
        String score;

        try {
            while((score = bufferedReader.readLine()) !=  null) {
                Gdx.app.log("Score" + i,score);
                scores.add(score);
                i++;
            }
        }
        finally {
            bufferedReader.close();
        }

        return scores;
    }

    public void update_HighScores() {
        try {
            countMoves = read_HighScore("Levels/countMoves.txt");
            timeAttack = read_HighScore("Levels/timeAttack.txt");
            timeChallenge = read_HighScore("Levels/timeChallenge.txt");
        }
        catch(IOException exception) {
            Gdx.app.log("Exception","",exception);
        }
    }

    public void append_HighScore(String directory, int score) {
        FileHandle scoreList;

        if(!Gdx.files.local(directory).exists()) {
            scoreList = Gdx.files.local(directory);
            scoreList.writeString(String.valueOf(score), true);
        }
        else {
            scoreList = Gdx.files.local(directory);
            scoreList.writeString("\n" + String.valueOf(score), true);
        }
    }

    public void change_HighScore(String directory, int level, String score) {
        FileHandle scoreList;
        List<String> highScores = new ArrayList<String>();

        score = score.replaceAll(",",".");

        if(!Gdx.files.local(directory).exists()) {
            scoreList = Gdx.files.local(directory);
            scoreList.writeString(String.valueOf(score), true);
        }
        else {
            try { highScores = read_HighScore(directory); }
            catch (IOException exception) { Gdx.app.log("Exception","",exception); }
            if(directory.contains("Challenge"))
            {
                if(Float.parseFloat(highScores.get(level - 6)) == 0) { highScores.set(level - 6, score); }
                if(Float.parseFloat(score) < Float.parseFloat(highScores.get(level - 6))) { highScores.set(level - 6, score); }
            }
            if(Float.parseFloat(score) > Float.parseFloat(highScores.get(level - 6)) && !directory.contains("Challenge"))
            { highScores.set(level - 6, score); }
            scoreList = Gdx.files.local(directory);
            for(int i = 0; i < highScores.size(); i++) {
                if(i == 0 ) { scoreList.writeString(highScores.get(i),false);}
                else { scoreList.writeString("\n" + highScores.get(i),true); }
            }
        }
    }

    public String getDir(String mode) {
        if(mode.equals("Moves")) { return countMoves_dir; }
        if(mode.equals("Time1")) { return timeChallenge_dir; }
        if(mode.equals("Time")) { return  timeAttack_dir; }

        return null;
    }

    public List<String> getScores(String mode) {
        if(mode.equals("Moves")) { return countMoves; }
        if(mode.equals("Time1")) { return timeChallenge; }
        if(mode.equals("Time")) { return  timeAttack; }

        return null;
    }
}
