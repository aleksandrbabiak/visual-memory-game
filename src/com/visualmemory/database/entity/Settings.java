package com.visualmemory.database.entity;

/**
 * Created by Windows on 02.04.14.
 */
public class Settings {

   private int errorPause;
   private int showPause;
   private int durationGame;

    public void setErrorPause(int errorPause) {
        this.errorPause = errorPause;
    }

    public void setShowPause(int showPause) {
        this.showPause = showPause;
    }

    public void setDurationGame(int durationGame) {
        this.durationGame = durationGame;
    }

    public int getErrorPause() {
        return errorPause;
    }

    public int getShowPause() {
        return showPause;
    }

    public int getDurationGame() {
        return durationGame;
    }
}
