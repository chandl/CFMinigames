package me.chandl.cfminigame.minigames.race;

import me.chandl.cfminigame.minigame.core.MinigameMap;
import me.chandl.cfminigame.minigames.race.checkpoint.Checkpoint;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import java.util.List;


/**
 * RaceMap subclass of MinigameMap
 *
 * Contains list of Checkpoints on top of other MinigameMap objects.
 *
 * @author Chandler me@cseverson.com
 * @version 1.0
 * @since Aug 20, 2017
 */
public class RaceMap extends MinigameMap {
    private List<Checkpoint> checkpoints;

    public RaceMap(String name, int maxLifeCount, Location spawnPoint, Location spectatorPoint, long gameTimeLimit, long baseScore, ItemStack[] startingItems, List<Checkpoint> checkpoints) {
        super(name, maxLifeCount, spawnPoint, spectatorPoint, gameTimeLimit, baseScore, startingItems);
    }

    public List<Checkpoint> getCheckpoints() {
        return checkpoints;
    }
}
