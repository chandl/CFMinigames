package me.chandl.cfminigame.minigame;

import me.chandl.cfminigame.scores.HighScore;
import me.chandl.cfminigame.scores.WinCount;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * Created by chandler on 7/30/17.
 */
public class MinigamePlayer {
    private Player player;
    private List<HighScore> highScores;
    private List<WinCount> winCount;
    private int currentLifeCount;
    private Location beforeMGPosition;
    private List<ItemStack> beforeMGInventory;

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public List<HighScore> getHighScores() {
        return highScores;
    }

    public void setHighScores(List<HighScore> highScores) {
        this.highScores = highScores;
    }

    public List<WinCount> getWinCount() {
        return winCount;
    }

    public void setWinCount(List<WinCount> winCount) {
        this.winCount = winCount;
    }

    public int getCurrentLifeCount() {
        return currentLifeCount;
    }

    public void setCurrentLifeCount(int currentLifeCount) {
        this.currentLifeCount = currentLifeCount;
    }

    public Location getBeforeMGPosition() {
        return beforeMGPosition;
    }

    public void setBeforeMGPosition(Location beforeMGPosition) {
        this.beforeMGPosition = beforeMGPosition;
    }

    public List<ItemStack> getBeforeMGInventory() {
        return beforeMGInventory;
    }

    public void setBeforeMGInventory(List<ItemStack> beforeMGInventory) {
        this.beforeMGInventory = beforeMGInventory;
    }
}
