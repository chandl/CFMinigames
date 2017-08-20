package me.chandl.cfminigame.scores;


import me.chandl.cfminigame.minigame.core.MinigameMap;
import me.chandl.cfminigame.minigame.player.MinigamePlayer;

/**
 * WinCount - For future use.
 */
public class WinCount {
    private MinigamePlayer player;
    private MinigameMap map;
    private long difficulty;
    private int winCount;

    public MinigamePlayer getPlayer() {
        return player;
    }

    public void setPlayer(MinigamePlayer player) {
        this.player = player;
    }

    public MinigameMap getMap() {
        return map;
    }

    public void setMap(MinigameMap map) {
        this.map = map;
    }

    public long getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(long difficulty) {
        this.difficulty = difficulty;
    }

    public int getWinCount() {
        return winCount;
    }

    public void setWinCount(int winCount) {
        this.winCount = winCount;
    }
}
