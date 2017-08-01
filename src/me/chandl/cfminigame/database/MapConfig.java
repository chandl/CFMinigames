package me.chandl.cfminigame.database;

import me.chandl.cfminigame.minigame.MinigameMap;
import me.chandl.cfminigame.minigame.MinigameType;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.List;


public class MapConfig {

    private static File mapFile;
    private static FileConfiguration mapConfiguration;

    public static boolean loadConfig(MinigameType type, String mapName, int difficulty){

        mapFile = new File("plugins/CFMinigame/maps/" + type.toString() + "/" + mapName + "-" + difficulty + ".yml");

        if(!mapFile.exists()){
            return false;
        }else{
            mapConfiguration = YamlConfiguration.loadConfiguration(mapFile);
        }

        return true;
    }

    public static void createMap( File mapFile , MinigameMap map){
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
        mapConfiguration.set("baseScore", map.getBaseScore());
        mapConfiguration.set("startingItems", map.getStartingItems());

        saveMapFile();
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
