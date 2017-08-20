package me.chandl.cfminigame.minigame.core;

import me.chandl.cfminigame.handler.GameHandler;
import me.chandl.cfminigame.minigame.player.MinigamePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.util.UUID;

/**
 * MinigameListener - A Wrapper Class for the Minecraft {@link Listener}.
 * Used for EventHandler actions in Minigames.
 */
public class MinigameListener implements Listener{

    /**
     * Player Respawn Handler - Called when a Player Respawns
     *
     * @param evt The Respawn Event
     */
    @EventHandler(priority =  EventPriority.HIGHEST)
    public void onPlayerRespawn(PlayerRespawnEvent evt){
        if(GameHandler.getHandler().getCurrentState() != MinigameState.IN_GAME){return;}
        Player p = evt.getPlayer();
        MinigamePlayer mp = findPlayer(p);
        //Stop logic if player is not in minigame.
        if(mp == null){return;}

        mp.setAlive(true);

        Minigame curr = GameHandler.getHandler().getCurrentMinigame();
        curr.onPlayerRespawn(evt, mp);
    }

    /**
     * Helper Method for MinigameListener Subclasses to find a {@link MinigamePlayer} object for a specified {@link Player}
     * @param player The player to find the MinigamePlayer object for.
     * @return The {@link MinigamePlayer} representation of the Player, or Null if the player is not found in the current minigame.
     */
    protected MinigamePlayer findPlayer(Player player){
        return GameHandler.getHandler().getPlayer(player.getUniqueId());
    }
}
