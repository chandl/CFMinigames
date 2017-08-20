package me.chandl.cfminigame.minigame.builder;

import me.chandl.cfminigame.minigame.player.MinigamePlayer;

import java.util.*;

/**
 * Class to Keep Track of who is building a minigame.
 *
 * @author Chandler me@cseverson.com
 * @version 1.0
 * @since Aug 20, 2017
 */
public class MinigameBuilderStore {
    //Keep track of who is building a minigame.
    private static Map<UUID,MinigameBuilder> currentBuilders;
    private static MinigameBuilderStore instance;

    private MinigameBuilderStore(){
        currentBuilders = new HashMap<>();
        instance = this;
    }

    /**
     * Gets the instance of this Singleton class.
     *
     * @return The MinigameBuilderStore instance.
     */
    public static MinigameBuilderStore getInstance(){
        if(instance == null){
            return new MinigameBuilderStore();
        }else{
            return instance;
        }
    }

    /**
     * Checks if a specified player is currently building a minigame.
     *
     * @param player The player to check.
     * @return True if the player is currently building a minigame, False otherwise.
     */
    public boolean isBuilding(MinigamePlayer player){
        return currentBuilders.containsKey(player.getPlayerObject().getUniqueId());
    }

    /**
     * Sets that the specified player is now building a minigame.
     *
     * @param player The player that started building the minigame.
     * @param builder The MinigameBuilder object for the builder.
     * @return True if we could successfully set that the player is building a minigame, false otherwise.
     */
    public boolean setBuilding(MinigamePlayer player, MinigameBuilder builder){
        if(!isBuilding(player)){
            currentBuilders.put(player.getPlayerObject().getUniqueId(), builder);
            return true;
        }

        return false;
    }

    /**
     * Sets that the specified player is not building a minigame.
     *
     * @param player The player that stopped building the minigame.
     * @return True if we could successfully set that the player is not building a minigame anymore, false otherwise.
     */
    public boolean stopBuilding(MinigamePlayer player){
        if(isBuilding(player)){
            currentBuilders.remove(player.getPlayerObject().getUniqueId());
            return true;
        }else{
            return false;
        }
    }

    /**
     * Gets the {@link MinigameBuilder} object for the specified player.
     *
     * @param player The player that is building a minigame.
     * @return The {@link MinigameBuilder} of the Player if they are building a minigame, Null if the player is not building a minigame.
     */
    public MinigameBuilder getMinigameBuilder(MinigamePlayer player){
        if(isBuilding(player)){
            return currentBuilders.get(player.getPlayerObject().getUniqueId());
        }else{
            return null;
        }
    }

    /**
     * Get all players that are building a minigame.
     * @return The list of players that are building a minigame.
     */
    public List<MinigameBuilder> getAllBuilders(){
        return new ArrayList<>(currentBuilders.values());
    }


}
