package me.chandl.cfminigame.database;

import org.bukkit.entity.Player;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.UUID;

/**
 * Store of {@link PlayerConfig}s.
 *
 * @author Chandler me@cseverson.com
 * @version 1.0
 * @since Aug 20, 2017
 */
public class PlayerStore {
    private static HashMap<UUID, PlayerConfig> store;

    /**
     * Retrieves a PlayerConfig from the store.
     *
     * @param player The player to get the configuration object for.
     * @return The PlayerConfig for the specified player.
     */
    public static PlayerConfig getConfig(Player player){
        if(store == null) store = new HashMap<>();
        UUID playerId = player.getUniqueId();

        if(store.containsKey(playerId)){
            return store.get(playerId);
        }else{
            try {
                PlayerConfig config = new PlayerConfig(player);
                store.put(playerId, config);
                return config;

            } catch (FileNotFoundException e) {
                return null;
            }
        }
    }

    /**
     * Creates a configuration file for a specified player.
     * Used for new Minigame Players and for when a player joins or starts a new minigame.
     *
     * @param player The player to create a new Configuration for.
     * @return The new PlayerConfiguration for the specified player.
     */
    public static PlayerConfig createConfig(Player player){
        UUID playerId = player.getUniqueId();
        File playerFile = new File("plugins/CFMinigame/data/players" + playerId + ".yml");

        PlayerConfig config = new PlayerConfig(playerId, playerFile);
        config.refreshUser();

        if(store.containsKey(playerId)){
            store.replace(playerId, config);
        }else{
            store.put(playerId,config);
        }

        return config;
    }
}
