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

    public List<Integer> countMoves = null;
    public List<Integer> timeAttack = null;
    public  List<Integer> timeChallenge = null;

    public String countMoves_dir = "Levels/countMoves.txt";
    public String timeAttack_dir = "Levels/timeAttack.txt";
    public String timeChallenge_dir = "Levels/timeChallenge.txt";

    public HighScores() {
        update_HighScores();
    }

    public List<Integer> read_HighScore(String ScoreList_name) throws IOException {
        FileHandle scoreList;
        List<Integer> scores = new ArrayList<Integer>();
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
                scores.add(Integer.parseInt(score));
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

    public void change_HighScore(String directory, int level, int score) {
        FileHandle scoreList;
        List<Integer> highScores = new ArrayList<Integer>();

        if(!Gdx.files.local(directory).exists()) {
            scoreList = Gdx.files.local(directory);
            scoreList.writeString(String.valueOf(score), true);
        }
        else {
            try { highScores = read_HighScore(directory); }
            catch (IOException exception) { Gdx.app.log("Exception","",exception); }
            if(score > highScores.get(level - 6)) { highScores.set(level - 6, score); }
            scoreList = Gdx.files.local(directory);
            for(int i = 0; i < highScores.size(); i++) {
                if(i == 0 ) { scoreList.writeString(String.valueOf(highScores.get(i)),false);}
                else { scoreList.writeString("\n" + String.valueOf(highScores.get(i)),true); }
            }
        }
    }

    public String getDir(String mode) {
        if(mode.equals("Moves")) { return countMoves_dir; }
        if(mode.equals("Time1")) { return timeChallenge_dir; }
        if(mode.equals("Time")) { return  timeAttack_dir; }

        return null;
    }
}
