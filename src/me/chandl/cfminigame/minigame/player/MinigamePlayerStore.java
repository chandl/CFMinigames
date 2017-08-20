package me.chandl.cfminigame.minigame.player;


import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MinigamePlayerStore {

    private static Map<UUID, MinigamePlayer> playerStore;

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
