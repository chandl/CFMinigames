package me.chandl.cfminigame.minigames.race;

import me.chandl.cfminigame.database.CheckpointConfig;
import me.chandl.cfminigame.database.MapConfig;
import me.chandl.cfminigame.minigame.core.Minigame;
import me.chandl.cfminigame.minigame.checkpoint.Checkpoint;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.LinkedList;

public class Race extends Minigame {
    private ArrayList<Checkpoint> checkPoints;
    private Material checkPointMaterial;

    public Race() {
        super();
    }

    @Override
    public void start() {
        System.out.println("This Minigame: " + this);
        System.out.println("Type: "  + this.getType());
        System.out.println("Map: "  + this.getMap().getName());


        //Load correct shape for difficulty level...
        String shape = CheckpointConfig.loadPoint(this.getType(), this.getDifficultyLevel());


        //Load Checkpoints
        if(MapConfig.loadConfig(getType(), getMap().getName())){
            checkPoints = (ArrayList<Checkpoint>) MapConfig.get("checkpoints");
            for(Checkpoint p : checkPoints){
                System.out.println("Checkpoint List: " + p);
                p.setShape(shape);
                p.setMaterial(Material.GLASS);
            }

        }else{
            System.out.println("Could not Load Checkpoints D: ");
        }


        for(Checkpoint p : checkPoints ){
            p.spawn();
        }
    }

    @Override
    public void stop() {
        for(Checkpoint p : checkPoints){
            p.despawn();
        }
    }



}
