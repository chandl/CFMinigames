package me.chandl.cfminigame.minigame.core;


import me.chandl.cfminigame.database.MapConfig;
import me.chandl.cfminigame.scores.HighScore;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

public class MinigameMap {
    private String name;
    private int maxLifeCount;

    private Location spawnPoint;
    private Location spectatorPoint;

    private long gameTimeLimit;
    private long queueTimeLimit;
    private long baseScore;
    private long[] difficultyMultipliers;

    private ItemStack[] startingItems;
    private List<HighScore> highScores;


    public static MinigameMap findMap(MinigameType type, String mapName){
        if(MapConfig.loadConfig(type, mapName)){//map exists
            String name = (String) MapConfig.get("mapName");
            int maxLife = (Integer) MapConfig.get("maxLifeCount");
            Location spawnPoint = (Location) MapConfig.get("spawnPoint");
            Location spectatorPoint = (Location) MapConfig.get("spectatorPoint");
            long gameTimeLimit = (Integer) MapConfig.get("gameTimeLimit");
            long baseScore = (Integer) MapConfig.get("baseScore");
            List<ItemStack> items = (List<ItemStack>) MapConfig.getList("startingItems");
            MinigameMap out = new MinigameMap(name, maxLife, spawnPoint, spectatorPoint, gameTimeLimit, baseScore, items.toArray(new ItemStack[items.size()]));
//            System.out.println("Could find map. " + out.toString());
            return out;
        }else{
//            System.out.println("Could not find map. Type " + type +". MapName: " + mapName);
            return null;
        }
    }

    @Override
    public String toString() {
        return "MinigameMap{" +
                "name='" + name + '\'' +
                ", maxLifeCount=" + maxLifeCount +
                ", spawnPoint=" + spawnPoint +
                ", spectatorPoint=" + spectatorPoint +
                ", gameTimeLimit=" + gameTimeLimit +
                ", queueTimeLimit=" + queueTimeLimit +
                ", baseScore=" + baseScore +
                ", difficultyMultipliers=" + Arrays.toString(difficultyMultipliers) +
                ", startingItems=" + Arrays.toString(startingItems) +
                ", highScores=" + highScores +
                '}';
    }

    public MinigameMap(){}

    public MinigameMap(String name, Location spawnPoint, Location spectatorPoint, long gameTimeLimit, long queueTimeLimit) {
        this.name = name;
        this.spawnPoint = spawnPoint;
        this.spectatorPoint = spectatorPoint;
        this.gameTimeLimit = gameTimeLimit;
        this.queueTimeLimit = queueTimeLimit;
    }

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

    public long getQueueTimeLimit() {
        return queueTimeLimit;
    }

    public long getBaseScore() {
        return baseScore;
    }
}
