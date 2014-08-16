package com.visualmemory.database.entity;

/**
 * Created by Windows on 02.04.14.
 */
public class GameResult {
    private String date;
    private int durationGame;
    private double kFactor;

    public GameResult(){}

    public GameResult(String date, int durationGame, double kFactor) {
        this.date = date;
        this.durationGame = durationGame;
        this.kFactor = kFactor;
    }


    public void setDate(String date) {
        this.date = date;
    }

    public void setDurationGame(int durationGame) {
        this.durationGame = durationGame;
    }

    public void setkFactor(double kFactor) {
        this.kFactor = kFactor;
    }


    public int getDurationGame() {
        return durationGame;
    }

    public String getDate() {
        return date;
    }

    public double getkFactor() {
        return kFactor;
    }



}
