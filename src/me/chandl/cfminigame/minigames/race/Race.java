package me.chandl.cfminigame.minigames.race;

import me.chandl.cfminigame.CFMinigame;
import me.chandl.cfminigame.handler.GameHandler;
import me.chandl.cfminigame.database.CheckpointConfigStore;
import me.chandl.cfminigame.database.MapStore;
import me.chandl.cfminigame.minigames.race.checkpoint.RaceListener;
import me.chandl.cfminigame.minigame.core.Minigame;
import me.chandl.cfminigame.minigames.race.checkpoint.Checkpoint;
import me.chandl.cfminigame.minigame.player.MinigamePlayer;
import me.chandl.cfminigame.minigame.player.PlayerState;
import me.chandl.cfminigame.util.Message;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Date;

/**
 * Race Minigame
 *
 * @author Chandler me@cseverson.com
 * @version 1.0
 * @since Aug 20, 2017
 */
public class Race extends Minigame {

    //TODO Clean up methods, add more Documentation.


    private ArrayList<Checkpoint> checkPoints;
    private RaceListener raceListener;

    public Race() {
        super();
    }

    /**
     * A race is finished once all players have finished the race (All players spectating)
     */
    @Override
    protected boolean isGameFinished() {

        for(MinigamePlayer player : GameHandler.getHandler().getPlayerList()){
            if(player.getState() == PlayerState.IN_GAME) return false;
        }

        return true;
    }

    @Override
    public void onPlayerFinish(MinigamePlayer player) {
//        System.out.println("MINIGAMEPLAYER FINISHED THE RACE!");
        super.onPlayerFinish(player);
    }

    @Override
    public void onPlayerDamage(MinigamePlayer player) {

//            onPlayerDie(new PlayerDeathEvent(player.getPlayerObject(), null, 0 , "Fell in Game."), player);
//        System.out.println("PLAYER DAMAGE D: ");
//        player.getPlayerObject().damage(10);
        if(player.isAlive())
            player.getPlayerObject().setHealth(0);
    }

    public void onFall(MinigamePlayer player){
//        System.out.println("PLAYER ON BLOCK D: ");
//        player.getPlayerObject().damage();
        if(player.isAlive())
            player.getPlayerObject().setHealth(0);
    }

    public void onCheckpoint(MinigamePlayer player){
        ItemStack firework = new ItemStack(Material.FIREWORK);
        FireworkMeta fwMeta = (FireworkMeta) firework.getItemMeta();
        fwMeta.addEffects(FireworkEffect.builder().withColor(Color.RED).with(FireworkEffect.Type.BALL_LARGE).build());
        fwMeta.setPower(2);
        firework.setItemMeta(fwMeta);

        Message.player(player, "Reached Checkpoint " + (player.getProgress() + 1)  + " of " + checkPoints.size());
//        System.out.println("Player Progress: " + player.getProgress()+". Checkpoints: " + checkPoints.size());

        player.setProgress(player.getProgress() + 1);
        player.getPlayerObject().getInventory().addItem(firework);

        if(checkPoints.size() == player.getProgress()){
            onPlayerFinish(player);
        }
    }



    @Override
    public void start() {

        //Load correct shape for difficulty level...
        String shape = CheckpointConfigStore.loadPoint(this.getType(), this.getDifficultyLevel());

        //Load and Configure Checkpoints
        FileConfiguration config ;
        try {
             config = MapStore.getConfig(getType(), getMap().getName());

            checkPoints = (ArrayList<Checkpoint>) config.get("checkpoints");
            for(Checkpoint p : checkPoints){
                p.setShape(shape);
            }

        } catch (FileNotFoundException e) {
            System.err.println("ERROR: Could not Load Checkpoints.");
            e.printStackTrace();
            return;
        }


        //Spawn all of the checkpoints
        for(Checkpoint p : checkPoints ){
            p.spawn();
        }

        for(MinigamePlayer player : GameHandler.getHandler().getPlayerList()){
            //give players MG starting items
            player.getPlayerObject().getInventory().setContents( getMap().getStartingItems() );

            //Reset Player's Progress
            player.setProgress(0);

            player.setState(PlayerState.IN_GAME);

            player.setAlive(true);

            System.out.println("Setting Player Current Life Count to " + getMap().getMaxLifeCount());
            player.setCurrentLifeCount(getMap().getMaxLifeCount());
        }


        //Start Race Listener
        raceListener = new RaceListener();
        CFMinigame.plugin.registerListener(raceListener);
        raceListener.setCheckpoints(checkPoints);


        setStartTime(new Date());
    }

    @Override
    public void stop() {
        if(raceListener != null)
        {
            CFMinigame.plugin.unregisterListener(raceListener);
        }
        if(checkPoints != null && checkPoints.size() > 0){
            for(Checkpoint p : checkPoints){
                p.despawn();
            }
        }
    }

}
