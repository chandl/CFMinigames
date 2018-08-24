package me.chandl.cfminigame.minigame.player;


import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Player Store for {@link MinigamePlayer}s
 *
 * @author Chandler me@cseverson.com
 * @version 1.0
 * @since Aug 20, 2017
 */
public class MinigamePlayerStore {

    private static Map<UUID, MinigamePlayer> playerStore;

    /**
     * Finds a MinigamePlayer player from the store.
     *
     * @param p The player to find the MinigamePlayer object for.
     * @return The {@link MinigamePlayer} object representing the Player
     */
    public static MinigamePlayer findPlayer(Player p){
        if(playerStore == null) playerStore = new HashMap<>();

        UUID playerId = p.getUniqueId();
        if(playerStore.containsKey(playerId)){
            return playerStore.get(playerId);
        }else{
            MinigamePlayer mgp = new MinigamePlayer(p, false);
            playerStore.put(playerId, mgp);
            return mgp;
        }
    }

    /**
     * Finds and Resets a player from the store.
     *
     * @param p The player to find and reset the MinigamePlayer object for.
     * @return The refreshed {@link MinigamePlayer} object representing the Player.
     */
    public static MinigamePlayer findResetPlayer(Player p){
        if(playerStore == null) playerStore = new HashMap<>();

        UUID playerId = p.getUniqueId();
        MinigamePlayer mgp = new MinigamePlayer(p, true);

        if(playerStore.containsKey(playerId)){
            playerStore.replace(playerId, mgp);
        }else{
            playerStore.put(playerId, mgp);
        }

        return mgp;
    }



}
