package me.chandl.cfminigame.minigame.checkpoint;

import me.chandl.cfminigame.GameHandler;
import me.chandl.cfminigame.minigame.core.Minigame;
import me.chandl.cfminigame.minigame.core.MinigameState;
import me.chandl.cfminigame.minigame.player.MinigamePlayer;
import me.chandl.cfminigame.minigames.race.Race;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;


public class RaceListener implements Listener {

    private ArrayList<Checkpoint> checkpoints;
    private HashMap<UUID, MinigamePlayer> playerStore;
    private static RaceListener instance;
    private RaceListener() {
        checkpoints = new ArrayList<>();
        playerStore = new HashMap<>();
    }

    public static RaceListener getListener(){
        if(instance == null){
            instance = new RaceListener();
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

        MinigamePlayer mp= findPlayer(p);

        int progress = mp.getProgress();
        if(checkpoints.size() <= progress){
            System.out.println("ERROR: Hitboxes smaller than progress!!!");
        }else{//Do Hitbox Detection Logic
//            System.out.println(checkpoints.get(progress).getSpawnPoint().distance(p.getLocation()));
            Checkpoint point = checkpoints.get(progress);
            Location playerLoc = p.getLocation();
            if(playerLoc.getY() < point.hitboxMaxY && playerLoc.getY() > point.hitboxMinY
                    && playerLoc.getZ() < point.hitboxMaxZ && playerLoc.getZ() > point.hitboxMinZ
                    && playerLoc.getX() < point.hitboxMaxX && playerLoc.getX() > point.hitboxMinX) {
                System.out.println(p.getName() + ": Passed Through Point " + progress);


                Minigame curr = GameHandler.getHandler().getCurrentMinigame();
                if(curr instanceof Race){
                    ((Race) curr).onCheckpoint(mp);
                }

            }
        }

        //Check if player has hit the ground (only if they are past first checkpoint)
        if(progress > 0){
            if(p.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() != Material.AIR){
                Minigame curr = GameHandler.getHandler().getCurrentMinigame();
                if(curr instanceof Race){
                    ((Race) curr).onFall(mp);
                }


            }
        }



    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent evt){
        if(GameHandler.getHandler().getCurrentState() != MinigameState.IN_GAME){return;}
        Player p = evt.getEntity();
        MinigamePlayer mp = findPlayer(p);
        //Stop logic if player is not in minigame.
        if(!GameHandler.getHandler().getPlayerUUIDs().contains(p.getUniqueId())){return;}

        Minigame curr = GameHandler.getHandler().getCurrentMinigame();
        curr.onDie(evt, mp);
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent evt){
        if(GameHandler.getHandler().getCurrentState() != MinigameState.IN_GAME){return;}
        Player p = evt.getPlayer();
        MinigamePlayer mp = findPlayer(p);
        //Stop logic if player is not in minigame.
        if(!GameHandler.getHandler().getPlayerUUIDs().contains(p.getUniqueId())){return;}

        Minigame curr = GameHandler.getHandler().getCurrentMinigame();
        curr.onRespawn(evt, mp);
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent evt){
        if(GameHandler.getHandler().getCurrentState() != MinigameState.IN_GAME){return;}

        Entity e = evt.getEntity();

        //Call onDamage if player takes fall damage.
        if(e instanceof Player && (evt.getCause() == EntityDamageEvent.DamageCause.FLY_INTO_WALL || evt.getCause() == EntityDamageEvent.DamageCause.FALL)){
            System.out.println("DAMAGE CAUSED. TYPE: " + evt.getCause());
            Player p = (Player) e;
            MinigamePlayer mp = findPlayer(p);
            //Stop logic if player is not in minigame.
            if(!GameHandler.getHandler().getPlayerUUIDs().contains(p.getUniqueId())){return;}

            Minigame curr = GameHandler.getHandler().getCurrentMinigame();
            curr.onPlayerDamage(mp);
        }
    }

    private MinigamePlayer findPlayer(Player player){
        UUID playerId = player.getUniqueId();
        MinigamePlayer mp;
        if(playerStore.containsKey(playerId)){
            mp = playerStore.get(playerId);
        }else{
//            mp = new MinigamePlayer(player, false);
            mp = GameHandler.getHandler().getPlayer(playerId);
            playerStore.put(playerId, mp);
        }

        return mp;
    }


    public void setCheckpoints(ArrayList<Checkpoint> checkpoints) {
        this.checkpoints = checkpoints;
    }
}
