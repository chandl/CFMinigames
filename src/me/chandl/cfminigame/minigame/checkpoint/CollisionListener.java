package me.chandl.cfminigame.minigame.checkpoint;

import me.chandl.cfminigame.GameHandler;
import me.chandl.cfminigame.minigame.core.MinigameState;
import me.chandl.cfminigame.minigame.player.MinigamePlayer;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;


public class CollisionListener implements Listener {

    private ArrayList<Checkpoint> checkpoints;
    private HashMap<UUID, MinigamePlayer> playerStore;
    private static CollisionListener instance;
    private CollisionListener() {
        checkpoints = new ArrayList<>();
        playerStore = new HashMap<>();
    }

    public static CollisionListener getListener(){
        if(instance == null){
            instance = new CollisionListener();
        }
        return instance;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent evt){
        if(GameHandler.getHandler().getCurrentState() != MinigameState.IN_GAME){return;}
        Player p = evt.getPlayer();
        UUID playerId = p.getUniqueId();
        //Stop logic if player is not in minigame.
        if(!GameHandler.getHandler().getPlayerUUIDs().contains(playerId)){return;}

        MinigamePlayer mp;
        if(playerStore.containsKey(playerId)){
            mp = playerStore.get(playerId);
        }else{
            mp = new MinigamePlayer(p, false);
            playerStore.put(playerId, mp);
        }

        int progress = mp.getProgress();
        if(checkpoints.size() < progress){
            System.out.println("ERROR: Hitboxes smaller than progress!!!");
        }else{//Do Hitbox Detection Logic
//            System.out.println(checkpoints.get(progress).getSpawnPoint().distance(p.getLocation()));
            Checkpoint point = checkpoints.get(progress);
            Location playerLoc = p.getLocation();
            if(playerLoc.getY() < point.hitboxMaxY && playerLoc.getY() > point.hitboxMinY
                    && playerLoc.getZ() < point.hitboxMaxZ && playerLoc.getZ() > point.hitboxMinZ
                    && playerLoc.getX() < point.hitboxMaxX && playerLoc.getX() > point.hitboxMinX) {
                System.out.println("PASSED THROUGH POINT!!!");
            }

        }


    }

    public void setCheckpoints(ArrayList<Checkpoint> checkpoints) {
        this.checkpoints = checkpoints;
    }
}
