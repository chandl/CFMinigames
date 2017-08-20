package me.chandl.cfminigame.minigames.snowballfight;

import me.chandl.cfminigame.GameHandler;
import me.chandl.cfminigame.minigame.core.Minigame;
import me.chandl.cfminigame.minigame.core.MinigameState;
import me.chandl.cfminigame.minigame.player.MinigamePlayer;
import me.chandl.cfminigame.util.Message;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;


public class SnowballListener implements Listener {

    private static SnowballListener instance;

    ItemStack snowball = new ItemStack(Material.SNOW_BALL);

    @EventHandler
    public void onProjectileLaunch(ProjectileLaunchEvent event) {
        if(event.getEntity().getShooter() instanceof Player) {
            Player player = (Player) event.getEntity().getShooter();
            player.getInventory().addItem(snowball);
        }
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent evt){
        Projectile p = evt.getEntity();
        if(!(p instanceof Snowball)) return;

        Entity damaged = evt.getHitEntity();
        if(!(damaged instanceof Player)) return;


        Player shooter = (Player) p.getShooter();
        if(findPlayer(shooter) == null) return;

        Player hit = (Player) damaged;

        if(hit.getHealth() == 0) return;
        MinigamePlayer plr = findPlayer(hit);
        if(plr == null) return;

        hit.setHealth(0);

        Message.playersInGame(String.format("%s%s %skilled %s%s %swith a snowball!", ChatColor.RED, shooter.getName(), ChatColor.WHITE, ChatColor.RED, hit.getName(), ChatColor.WHITE));
//        Message.player(hit, String.format("You have died. Remaining lives: %s / %s", plr.getCurrentLifeCount()-1, GameHandler.getHandler().getCurrentMinigame().getMap().getMaxLifeCount()));
    }

    @EventHandler (priority =  EventPriority.HIGHEST)
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
    public void onPlayerDeath(PlayerDeathEvent evt){

//        System.out.println("Player Death. Cause: " + evt.getDeathMessage());
        if(GameHandler.getHandler().getCurrentState() != MinigameState.IN_GAME){return;}
        Player p = evt.getEntity();
        MinigamePlayer mp = findPlayer(p);

        //Stop logic if player is not in minigame.
        if(!GameHandler.getHandler().getPlayerUUIDs().contains(p.getUniqueId())){return;}

        Minigame curr = GameHandler.getHandler().getCurrentMinigame();
        curr.onDie(evt, mp);
    }

    private SnowballListener(){}

    public static SnowballListener getListener(){
        if(instance == null){
            instance = new SnowballListener();
        }

        return instance;
    }

    private MinigamePlayer findPlayer(Player player){
        UUID playerId = player.getUniqueId();
        MinigamePlayer mp;
        /*if(playerStore.containsKey(playerId)){
            mp = playerStore.get(playerId);
        }else{
//            mp = new MinigamePlayer(player, false);
            mp = GameHandler.getHandler().getPlayer(playerId);
            playerStore.put(playerId, mp);
        }*/

        mp = GameHandler.getHandler().getPlayer(playerId);

        return mp;
    }

}
