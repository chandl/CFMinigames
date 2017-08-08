package me.chandl.cfminigame.minigame.checkpoint;


import me.chandl.cfminigame.minigame.MinigameType;
import org.bukkit.Location;

import java.io.Serializable;

public class PersistentCheckpoint implements Serializable{

    //To Re-Create Location. Need X, Y, Z, and World
    private double x, y, z;

    //To get Accurate Position, Store Direction
    private String direction;

    //Keep type and difficulty to get the checkpoint type
    private MinigameType type;
    private int difficulty;

    public PersistentCheckpoint(Checkpoint checkpoint, MinigameType type, int difficulty) {
        Location loc = checkpoint.getSpawnPoint();
        this.x = loc.getX();
        this.y = loc.getY();
        this.z = loc.getZ();

        this.direction = checkpoint.getDirection();

        this.type = type;
        this.difficulty = difficulty;
    }

    @Override
    public String toString() {
        return "PersistentCheckpoint{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                ", direction='" + direction + '\'' +
                ", type=" + type +
                ", difficulty=" + difficulty +
                '}';
    }
}
