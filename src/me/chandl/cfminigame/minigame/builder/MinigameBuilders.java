package me.chandl.cfminigame.minigame.builder;

import me.chandl.cfminigame.minigame.player.MinigamePlayer;

import java.util.*;


public class MinigameBuilders {
    //Keep track of who is building a minigame.
    private static Map<UUID,MinigameBuilder> currentBuilders;
    private static MinigameBuilders instance;

    private MinigameBuilders(){
        currentBuilders = new HashMap<>();
        instance = this;
    }

    public static MinigameBuilders getBuilders(){
        if(instance == null){
            return new MinigameBuilders();
        }else{
            return instance;
        }
    }

    public boolean isBuilding(MinigamePlayer player){
        return currentBuilders.containsKey(player.getPlayerObject().getUniqueId());
    }

    public boolean setBuilding(MinigamePlayer player, MinigameBuilder builder){
        if(!isBuilding(player)){
            currentBuilders.put(player.getPlayerObject().getUniqueId(), builder);
            return true;
        }

        return false;
    }

    public boolean stopBuilding(MinigamePlayer player){
        if(isBuilding(player)){
            currentBuilders.remove(player.getPlayerObject().getUniqueId());
            return true;
        }else{
            return false;
        }
    }

    public MinigameBuilder getMinigameBuilder(MinigamePlayer player){
        if(isBuilding(player)){
            return currentBuilders.get(player.getPlayerObject().getUniqueId());
        }else{
            return null;
        }
    }

    public List<MinigameBuilder> getAllBuilders(){
        return new ArrayList<>(currentBuilders.values());
    }


}
