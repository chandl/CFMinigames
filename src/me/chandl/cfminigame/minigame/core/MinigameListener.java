package me.chandl.cfminigame.minigame.core;

import me.chandl.cfminigame.GameHandler;
import me.chandl.cfminigame.minigame.player.MinigamePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.util.UUID;


public class MinigameListener implements Listener{

    @EventHandler(priority =  EventPriority.HIGHEST)
    public void onPlayerRespawn(PlayerRespawnEvent evt){
        if(GameHandler.getHandler().getCurrentState() != MinigameState.IN_GAME){return;}
        Player p = evt.getPlayer();
        MinigamePlayer mp = findPlayer(p);
        //Stop logic if player is not in minigame.
        if(!GameHandler.getHandler().getPlayerUUIDs().contains(p.getUniqueId())){return;}

        Minigame curr = GameHandler.getHandler().getCurrentMinigame();
        curr.onRespawn(evt, mp);
    }

    protected MinigamePlayer findPlayer(Player player){
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
