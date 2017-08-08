package me.chandl.cfminigame.minigames.race;

import me.chandl.cfminigame.minigame.core.Minigame;
import me.chandl.cfminigame.minigame.checkpoint.Checkpoint;
import org.bukkit.Material;

import java.util.ArrayList;

public class Race extends Minigame {
    private ArrayList<Checkpoint> checkPoints;
    private Material checkPointMaterial;

    public Race(){}

    public Race(ArrayList<Checkpoint> checkPoints, Material checkpointMaterial) {
        this.checkPoints = checkPoints;
        this.checkPointMaterial = checkpointMaterial;
    }

    @Override
    public void start() {
        for(Checkpoint p : checkPoints ){
            p.spawn(checkPointMaterial);
        }
    }

    @Override
    public void stop() {
        for(Checkpoint p : checkPoints){
            p.despawn();
        }
    }



}
