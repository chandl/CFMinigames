package me.chandl.cfminigame.minigames.race;

import me.chandl.cfminigame.CFMinigame;
import me.chandl.cfminigame.GameHandler;
import me.chandl.cfminigame.database.CheckpointConfig;
import me.chandl.cfminigame.database.MapConfig;
import me.chandl.cfminigame.minigame.checkpoint.RaceListener;
import me.chandl.cfminigame.minigame.core.Minigame;
import me.chandl.cfminigame.minigame.checkpoint.Checkpoint;
import me.chandl.cfminigame.minigame.player.MinigamePlayer;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.ArrayList;

public class Race extends Minigame {
    private ArrayList<Checkpoint> checkPoints;

    public Race() {
        super();
    }


    @Override
    public void onPlayerFinish(MinigamePlayer player) {
//        System.out.println("MINIGAMEPLAYER FINISHED THE RACE!");
        super.onPlayerFinish(player);
    }

    @Override
    public void onPlayerDamage(MinigamePlayer player) {


//            onDie(new PlayerDeathEvent(player.getPlayerObject(), null, 0 , "Fell in Game."), player);
        System.out.println("PLAYER DAMAGE D: ");
        player.getPlayerObject().setHealth(0);

    }

    public void onCheckpoint(MinigamePlayer player){
        player.setProgress(player.getProgress() + 1);

        if(checkPoints.size() == player.getProgress()){
            onPlayerFinish(player);
        }
    }

    public void onFall(MinigamePlayer player){
        System.out.println("PLAYER ON BLOCK D: ");
        player.getPlayerObject().setHealth(0);
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

        for(MinigamePlayer player : GameHandler.getHandler().getPlayerList()){
            //give players MG starting items
            player.getPlayerObject().getInventory().setContents( getMap().getStartingItems() );
        }


        //Start Race Listener
        CFMinigame.plugin.registerRaceHandler();
        RaceListener.getListener().setCheckpoints(checkPoints);

    }

    @Override
    public void stop() {
        CFMinigame.plugin.unregisterRaceHandler();
        if(checkPoints != null && checkPoints.size() > 0){
            for(Checkpoint p : checkPoints){
                p.despawn();
            }
        }
    }

}
