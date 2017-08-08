package me.chandl.cfminigame.minigames.race.builder;

import me.chandl.cfminigame.minigame.MinigameMap;
import me.chandl.cfminigame.minigame.MinigamePlayer;
import me.chandl.cfminigame.minigame.builder.MinigameBuilder;
import me.chandl.cfminigame.minigame.builder.MinigameBuilders;
import me.chandl.cfminigame.minigame.checkpoint.Checkpoint;
import org.bukkit.Location;

import java.util.Queue;


public class RaceBuilder extends MinigameBuilder {
    private Queue<Checkpoint> points;
    private Location endPoint;

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
                //TODO Capture Ending Point from Player
                //TODO Get Maximum Life Count.
            }
        }

        return true;
    }
}
