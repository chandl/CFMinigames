package me.chandl.cfminigame.scores;

import me.chandl.cfminigame.minigame.core.Minigame;
import me.chandl.cfminigame.minigame.player.MinigamePlayer;

/**
 * Created by chandler on 7/30/17.
 */
public class HighScore {
    private Minigame minigame;
    private MinigamePlayer player;
    private long score;

    public Minigame getMinigame() {
        return minigame;
    }

    public void setMinigame(Minigame minigame) {
        this.minigame = minigame;
    }

    public MinigamePlayer getPlayer() {
        return player;
    }

    public void setPlayer(MinigamePlayer player) {
        this.player = player;
    }

    public long getScore() {
        return score;
    }

    public void setScore(long score) {
        this.score = score;
    }
}
