package me.chandl.cfminigame.minigame;


import me.chandl.cfminigame.scores.HighScore;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class MinigameMap {
    private String name;

    private int id;
    private int maxLifeCount;

    private Location spawnPoint;
    private Location spectatorPoint;

    private long gameTimeLimit;
    private long baseScore;
    private long[] difficultyMultipliers;

    private List<ItemStack> startingItems;
    private List<HighScore> highScores;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMaxLifeCount() {
        return maxLifeCount;
    }

    public void setMaxLifeCount(int maxLifeCount) {
        this.maxLifeCount = maxLifeCount;
    }

    public Location getSpawnPoint() {
        return spawnPoint;
    }

    public void setSpawnPoint(Location spawnPoint) {
        this.spawnPoint = spawnPoint;
    }

    public Location getSpectatorPoint() {
        return spectatorPoint;
    }

    public void setSpectatorPoint(Location spectatorPoint) {
        this.spectatorPoint = spectatorPoint;
    }

    public long getGameTimeLimit() {
        return gameTimeLimit;
    }

    public void setGameTimeLimit(long gameTimeLimit) {
        this.gameTimeLimit = gameTimeLimit;
    }

    public long getBaseScore() {
        return baseScore;
    }

    public void setBaseScore(long baseScore) {
        this.baseScore = baseScore;
    }

    public long[] getDifficultyMultipliers() {
        return difficultyMultipliers;
    }

    public void setDifficultyMultipliers(long[] difficultyMultipliers) {
        this.difficultyMultipliers = difficultyMultipliers;
    }

    public List<ItemStack> getStartingItems() {
        return startingItems;
    }

    public void setStartingItems(List<ItemStack> startingItems) {
        this.startingItems = startingItems;
    }

    public List<HighScore> getHighScores() {
        return highScores;
    }

    public void setHighScores(List<HighScore> highScores) {
        this.highScores = highScores;
    }
}
