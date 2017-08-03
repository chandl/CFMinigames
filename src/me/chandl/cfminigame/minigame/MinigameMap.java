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


    public static MinigameMap findMap(MinigameType type, String mapName, int difficulty){
        if(MapConfig.loadConfig(type, mapName, difficulty)){//map exists
            String name = (String) MapConfig.get("mapName");
            int maxLife = (Integer) MapConfig.get("maxLifeCount");
            Location spawnPoint = (Location) MapConfig.get("spawnPoint");
            Location spectatorPoint = (Location) MapConfig.get("spectatorPoint");
            long gameTimeLimit = (Integer) MapConfig.get("gameTimeLimit");
            long baseScore = (Integer) MapConfig.get("baseScore");
            List<ItemStack> items = (List<ItemStack>) MapConfig.getList("startingItems");
            return new MinigameMap(name, maxLife, spawnPoint, spectatorPoint, gameTimeLimit, baseScore, items.toArray(new ItemStack[items.size()]));
        }else{
            return null;
        }

    }

    public MinigameMap(){}
    public MinigameMap(String name, int maxLifeCount, Location spawnPoint, Location spectatorPoint, long gameTimeLimit, long baseScore, ItemStack[] startingItems) {
        this.name = name;
        this.maxLifeCount = maxLifeCount;
        this.spawnPoint = spawnPoint;
        this.spectatorPoint = spectatorPoint;
        this.gameTimeLimit = gameTimeLimit;
        this.baseScore = baseScore;
        this.startingItems = startingItems;
    }

    public Location getSpawnPoint() {
        return spawnPoint;
    }

    public ItemStack[] getStartingItems() {
        return startingItems;
    }

    public String getName() {
        return name;
    }

    public int getMaxLifeCount() {
        return maxLifeCount;
    }

    public Location getSpectatorPoint() {
        return spectatorPoint;
    }

    public long getGameTimeLimit() {
        return gameTimeLimit;
    }

    public long getBaseScore() {
        return baseScore;
    }
}
