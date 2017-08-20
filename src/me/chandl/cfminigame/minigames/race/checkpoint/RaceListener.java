package me.chandl.cfminigame.minigames.race.checkpoint;

import me.chandl.cfminigame.CFMinigame;
import me.chandl.cfminigame.handler.GameHandler;
import me.chandl.cfminigame.minigame.core.Minigame;
import me.chandl.cfminigame.minigame.core.MinigameListener;
import me.chandl.cfminigame.minigame.core.MinigameState;
import me.chandl.cfminigame.minigame.player.MinigamePlayer;
import me.chandl.cfminigame.minigames.race.Race;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FireworkExplodeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

/**
 * Subclassed MinigameListener for Race Minigames.
 *
 * @author Chandler me@cseverson.com
 * @version 1.0
 * @since Aug 20, 2017
 */
public class RaceListener extends MinigameListener {

    //TODO Clean up methods, add more Documentation.

    private ArrayList<Checkpoint> checkpoints;
    private HashMap<UUID, MinigamePlayer> playerStore;
    private Set<Firework> fireworks;

    public RaceListener() {
        checkpoints = new ArrayList<>();
        playerStore = new HashMap<>();
        fireworks = new HashSet<>();
    }

    @EventHandler(ignoreCancelled = true)
    public void onExplosion(FireworkExplodeEvent event) {
        final Firework firework = event.getEntity();
        fireworks.add(firework);
        new BukkitRunnable() {
            @Override
            public void run() {
                fireworks.remove(firework);
            }
        }.runTaskLater(CFMinigame.plugin, 5);
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
                if(curr instanceof Race ){
                    ((Race) curr).onFall(mp);
                }


            }
        }

    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent evt){

        System.out.println("Player Death. Cause: " + evt.getDeathMessage());
        if(GameHandler.getHandler().getCurrentState() != MinigameState.IN_GAME){return;}
        Player p = evt.getEntity();
        MinigamePlayer mp = findPlayer(p);

        if(mp == null)return;

//        if(!mp.isAlive()) return;

        //Stop logic if player is not in minigame.
        if(!GameHandler.getHandler().getPlayerUUIDs().contains(p.getUniqueId())){return;}

        Minigame curr = GameHandler.getHandler().getCurrentMinigame();
        curr.onPlayerDie(evt, mp);
    }



    @EventHandler(ignoreCancelled = true)
    public void onPlayerDamage(EntityDamageEvent evt){
        if(GameHandler.getHandler().getCurrentState() != MinigameState.IN_GAME){return;}

        Entity e = evt.getEntity();

        if(e instanceof Player){
            for(Entity entity : evt.getEntity().getNearbyEntities(5,5,5)){
                if(!(entity instanceof Firework)){
                    continue;
                }
                if(fireworks.contains(entity)){
                    evt.setCancelled(true);
                    return;
                }
            }
        }else{return;}

        Player p = (Player) e;
        MinigamePlayer mp = findPlayer(p);
        //Stop logic if player is not in minigame.
        if(mp == null){return;}

        if(!mp.isAlive()){
            System.out.println("NOT ALIVE");
            evt.setCancelled(true);
        }

        //Call onDamage if player takes fall damage.
        if(e instanceof Player && (evt.getCause() == EntityDamageEvent.DamageCause.FLY_INTO_WALL || evt.getCause() == EntityDamageEvent.DamageCause.FALL)){
            System.out.println("DAMAGE CAUSED. TYPE: " + evt.getCause());

            Minigame curr = GameHandler.getHandler().getCurrentMinigame();
            curr.onPlayerDamage(mp);
        }
    }




    public void setCheckpoints(ArrayList<Checkpoint> checkpoints) {
        this.checkpoints = checkpoints;
    }
}
