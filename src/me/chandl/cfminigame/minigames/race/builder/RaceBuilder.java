package me.chandl.cfminigame.minigames.race.builder;

import me.chandl.cfminigame.minigame.core.MinigameMap;
import me.chandl.cfminigame.minigame.player.MinigamePlayer;
import me.chandl.cfminigame.minigame.builder.MinigameBuilder;
import me.chandl.cfminigame.minigame.builder.MinigameBuilders;
import me.chandl.cfminigame.minigame.checkpoint.Checkpoint;
import org.bukkit.Location;
import org.bukkit.Material;

import java.util.LinkedList;
import java.util.Queue;


public class RaceBuilder extends MinigameBuilder {
    private Queue<Checkpoint> points;
    private Location endPoint;
    private Integer maxLives;
    private Material pointMaterial;

    public RaceBuilder(MinigamePlayer builder) {
        super(builder);

    }

    @Override
    public MinigameMap createMap() {

        //TODO Validation on Required Fields (Checkpoints & Ending Location)

        //TODO Serialize Checkpoints
        //TODO Save map configuration file
        MinigameBuilders.stopBuilding(getBuilder());

        return null;
    }

    @Override
    public String getProgression() {
        StringBuilder sb = new StringBuilder(super.getProgression());

        //TODO add other Race fields.
        sb.append("TESTING TESTING 123");

        return sb.toString();
    }

    @Override
    public boolean handleCommand(String[] args) throws IllegalArgumentException{
        try {
            super.handleCommand(args);
            return true;
        }catch(IllegalArgumentException ie){
            switch(args[1]){
                //TODO Capture Checkpoints from Player
                case "point":
                    if(points == null) points = new LinkedList<>();
                    break;
                //TODO Capture Ending Point from Player
                case "endpoint":
                    break;
                //TODO Get Maximum Life Count.
                case "lives":
                    break;
                //TODO Get Checkpoint Material
            }
        }

        return true;
    }
}