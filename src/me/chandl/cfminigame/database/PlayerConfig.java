package me.chandl.cfminigame.database;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**
 * Player Configuration Representation
 *
 * @author Chandler me@cseverson.com
 * @version 1.0
 * @since Aug 20, 2017
 */
public class PlayerConfig {

    private UUID playerId;
    private File playerFile;
    private FileConfiguration configuration;

    /**
     * Creates a PlayerConfig with a playerId and playerFile
     * Used when updating a player from PlayerStore.
     *
     * @param playerId The Unique Player ID of the player.
     * @param playerFile The file that the player configuration is saved in.
     */
    public PlayerConfig(UUID playerId, File playerFile) {
        this.playerId = playerId;
        this.playerFile = playerFile;
    }

    /**
     * Creates a new PlayerConfig for a specified Player
     *
     * @param player The player to find the Configuration for.
     * @throws FileNotFoundException If the Player has not been saved yet.
     */
    public PlayerConfig(Player player) throws FileNotFoundException{
        playerId = player.getUniqueId();

        playerFile = new File("plugins/CFMinigame/data/players/" + playerId + ".yml");

        if(!playerFile.exists()){
            throw new FileNotFoundException("PlayerFile " + playerFile.getName() + " not found.");
        }else{
            configuration = YamlConfiguration.loadConfiguration(playerFile);
        }
    }

    /**
     * Refreshes a PlayerConfig, deleting the current configuration file and creating a new one.
     */
    public void refreshUser(){
        if(playerFile == null){
            System.out.println("PlayerConfig.createUser() called but playerFile is null!");
            return;
        }
        configuration = YamlConfiguration.loadConfiguration(playerFile);
        try {
            configuration.save(playerFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void set(String path, Object value){
        configuration.set(path, value);
    }

    public Object get (String path) {
        return configuration.get(path);
    }

    public List<?> getList(String path){
        return configuration.getList(path);
    }

    public FileConfiguration getConfiguration(){
        return configuration;
    }

    public File getPlayerFile(){
        return playerFile;
    }

    public void saveUserFile(){
        try {
            getConfiguration().save(getPlayerFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
