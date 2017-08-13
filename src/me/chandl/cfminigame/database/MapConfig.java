package me.chandl.cfminigame.database;

import me.chandl.cfminigame.minigame.core.MinigameMap;
import me.chandl.cfminigame.minigame.core.MinigameType;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.List;


public class MapConfig {
    private static File mapFile;
    private static FileConfiguration mapConfiguration;

    //TODO Add File/FileConfiguration cache


    public static boolean loadConfig(MinigameType type, String mapName){
        mapFile = new File("plugins/CFMinigame/maps/" + type.toString() + "/" + mapName +".yml");

        if(!mapFile.exists()){
            System.out.println("Map File " + mapFile.getName() + " does not exist D:");
            return false;
        }else{

            mapConfiguration = YamlConfiguration.loadConfiguration(mapFile);
//            System.out.println("Configuration: " + mapConfiguration.saveToString());
        }

        return true;
    }

    public static FileConfiguration createMap( MinigameType type, String mapName,  MinigameMap map){
        mapFile = new File("plugins/CFMinigame/maps/" + type.toString() + "/" + mapName +".yml");
        mapConfiguration = YamlConfiguration.loadConfiguration(mapFile);
        try {
            mapConfiguration.save(mapFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        mapConfiguration.set("mapName", map.getName());
        mapConfiguration.set("maxLifeCount", map.getMaxLifeCount());
        mapConfiguration.set("spawnPoint", map.getSpawnPoint());
        mapConfiguration.set("spectatorPoint", map.getSpectatorPoint());
        mapConfiguration.set("gameTimeLimit", map.getGameTimeLimit());
        mapConfiguration.set("queueTimeLimit", map.getQueueTimeLimit());
        mapConfiguration.set("baseScore", map.getBaseScore());
        mapConfiguration.set("startingItems", map.getStartingItems());

        saveMapFile();

        return mapConfiguration;
    }


    public static void set(String path, Object value){
        mapConfiguration.set(path, value);
    }

    public static Object get (String path) {
        return mapConfiguration.get(path);
    }

    public static List<?> getList(String path){
        return mapConfiguration.getList(path);
    }

    public static FileConfiguration getMapConfiguration(){
        return mapConfiguration;
    }

    public static File getMapFile(){
        return mapFile;
    }

    public static void saveMapFile(){
        try {
            getMapConfiguration().save(getMapFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
