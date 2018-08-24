package me.chandl.cfminigame.minigame.core;


import me.chandl.cfminigame.scores.HighScore;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

/**
 * Representation of a Map for a {@link Minigame}
 *
 * @author Chandler me@cseverson.com
 * @version 1.0
 * @since Aug 20, 2017
 */
public class MinigameMap {
    private String name;
    private int maxLifeCount;

    private Location spawnPoint;
    private Location spectatorPoint;

    private long gameTimeLimit;
    private long queueTimeLimit;
    private ItemStack[] startingItems;

    private FileConfiguration mapFileConfig;

    //For future use
    private long baseScore;
    private long[] difficultyMultipliers;
    private List<HighScore> highScores;


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
