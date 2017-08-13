package me.chandl.cfminigame.minigames.race;

import me.chandl.cfminigame.GameHandler;
import me.chandl.cfminigame.database.CheckpointConfig;
import me.chandl.cfminigame.database.MapConfig;
import me.chandl.cfminigame.minigame.core.Minigame;
import me.chandl.cfminigame.minigame.checkpoint.Checkpoint;
import me.chandl.cfminigame.minigame.core.MinigameState;
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
//        System.out.println("This Minigame: " + this);
//        System.out.println("Type: "  + this.getType());
//        System.out.println("Map: "  + this.getMap().getName());

        //Load correct shape for difficulty level...
        String shape = CheckpointConfig.loadPoint(this.getType(), this.getDifficultyLevel());

        //Load and Configure Checkpoints
        if(MapConfig.loadConfig(getType(), getMap().getName())){
            checkPoints = (ArrayList<Checkpoint>) MapConfig.get("checkpoints");
            for(Checkpoint p : checkPoints){
//                System.out.println("Checkpoint List: " + p);
                p.setShape(shape);
            }

        }else{
            System.err.println("ERROR: Could not Load Checkpoints.");
        }

        //Spawn all of the checkpoints
        for(Checkpoint p : checkPoints ){
            p.spawn();
        }
    }

    @Override
    public void stop() {
        if(checkPoints != null && checkPoints.size() > 0){
            for(Checkpoint p : checkPoints){
                p.despawn();
            }
        }
    }



}
