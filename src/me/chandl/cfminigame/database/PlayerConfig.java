package me.chandl.cfminigame.database;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class PlayerConfig {

    private UUID playerId;
    private File playerFile;
    private FileConfiguration configuration;

    public boolean loadConfig(Player player){
        playerId = player.getUniqueId();

        playerFile = new File("plugins/CFMinigame/data/" + playerId + ".yml");

        if(!playerFile.exists()){
            return false;
        }else{
            configuration = YamlConfiguration.loadConfiguration(playerFile);
        }

        return true;
    }

    public void createUser( File playerFile){
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
