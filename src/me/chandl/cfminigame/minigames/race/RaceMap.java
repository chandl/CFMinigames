package me.chandl.cfminigame.minigames.race;

import me.chandl.cfminigame.minigame.core.MinigameMap;
import me.chandl.cfminigame.minigame.checkpoint.Checkpoint;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import java.util.List;


public class RaceMap extends MinigameMap {
    private List<Checkpoint> checkpoints;

    public RaceMap(String name, int maxLifeCount, Location spawnPoint, Location spectatorPoint, long gameTimeLimit, long baseScore, ItemStack[] startingItems, List<Checkpoint> checkpoints) {
        super(name, maxLifeCount, spawnPoint, spectatorPoint, gameTimeLimit, baseScore, startingItems);
    }

    public List<Checkpoint> getCheckpoints() {
        return checkpoints;
    }
}
