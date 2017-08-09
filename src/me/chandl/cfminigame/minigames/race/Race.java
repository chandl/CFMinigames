package me.chandl.cfminigame.minigames.race;

import me.chandl.cfminigame.database.MapConfig;
import me.chandl.cfminigame.minigame.core.Minigame;
import me.chandl.cfminigame.minigame.checkpoint.Checkpoint;
import org.bukkit.Material;

import java.util.LinkedList;

public class Race extends Minigame {
    private LinkedList<Checkpoint> checkPoints;
    private Material checkPointMaterial;

    public Race() {
        super();
    }

    @Override
    public void start() {
        System.out.println("This Minigame: " + this);
        System.out.println("Type: "  + this.getType());
        System.out.println("Map: "  + this.getMap().getName());
        if(MapConfig.loadConfig(getType(), getMap().getName())){
            checkPoints = (LinkedList<Checkpoint>) MapConfig.get("checkpoints");
            checkPointMaterial = (Material) MapConfig.get("material");
        }


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
