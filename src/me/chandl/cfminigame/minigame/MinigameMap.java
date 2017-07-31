package me.chandl.cfminigame.minigame;


import me.chandl.cfminigame.database.MapConfig;
import me.chandl.cfminigame.scores.HighScore;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class MinigameMap {
    private String name;

    private int maxLifeCount;

    private Location spawnPoint;
    private Location spectatorPoint;

    private long gameTimeLimit;
    private long baseScore;
    private long[] difficultyMultipliers;

    private ItemStack[] startingItems;
    private List<HighScore> highScores;
    private int difficulty;



    //CONFIGURATION LOCATIONS
    //CFMinigame/maps/TYPE/name-difficulty

    public static MinigameMap findMap(MinigameType type, String mapName, int difficulty){
        //Look through map configuration files
        //Find if map exists with this name.

        if(MapConfig.loadConfig(type, mapName, difficulty)){//map exists
            String name = (String) MapConfig.get("mapName");
            int maxLife = (Integer) MapConfig.get("maxLifeCount");
            Location spawnPoint = (Location) MapConfig.get("spawnPoint");
            Location spectatorPoint = (Location) MapConfig.get("spectatorPoint");
            long gameTimeLimit = (Long) MapConfig.get("gameTimeLimit");
            long baseScore = (Long) MapConfig.get("baseScore");
            List<ItemStack> items = (List<ItemStack>) MapConfig.getList("startingItems");
            return new MinigameMap(name, maxLife, spawnPoint, spectatorPoint, gameTimeLimit, baseScore, items.toArray(new ItemStack[items.size()]), difficulty);
        }else{
            return null;
        }

    }

    private MinigameMap(String name, int maxLifeCount, Location spawnPoint, Location spectatorPoint, long gameTimeLimit, long baseScore, ItemStack[] startingItems, int difficulty) {
        this.name = name;
        this.maxLifeCount = maxLifeCount;
        this.spawnPoint = spawnPoint;
        this.spectatorPoint = spectatorPoint;
        this.gameTimeLimit = gameTimeLimit;
        this.baseScore = baseScore;
        this.startingItems = startingItems;
        this.difficulty = difficulty;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public ItemStack[] getStartingItems() {
        return startingItems;
    }

    public void setStartingItems(ItemStack[] startingItems) {
        this.startingItems = startingItems;
    }

    public List<HighScore> getHighScores() {
        return highScores;
    }

    public void setHighScores(List<HighScore> highScores) {
        this.highScores = highScores;
    }
}
