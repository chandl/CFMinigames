package me.chandl.cfminigame.database;

import me.chandl.cfminigame.minigame.core.MinigameMap;
import me.chandl.cfminigame.minigame.core.MinigameType;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Store of Map Configurations.
 *
 * @author  Chandler me@cseverson.com
 * @version 1.0
 * @since Aug 20, 2017
 */
public class MapStore {
    /**
     * String in Map is TYPE/MAPNAME
     * FileConfiguration is the configuration for the TYPE-MAPNAME pair
     */
    private static Map<String, FileConfiguration> mapStore;

    /**
     * Gets a FileConfiguration for a MinigameMap through the MapStore.
     *
     * @param type The type of the minigame.
     * @param mapName  The name of the minigame map.
     * @return The FileConfiguration for the map.
     * @throws FileNotFoundException If the MapName is not found.
     */
    public static FileConfiguration getConfig(MinigameType type, String mapName) throws FileNotFoundException{
        if(mapStore == null) mapStore = new HashMap<>();
        String key = type.toString() + "/" + mapName;

        File mapFile = new File("plugins/CFMinigame/maps/" + key +".yml");

        if(!mapFile.exists()){
            throw new FileNotFoundException("Map file " + mapFile.getName() +" does not exist.");
        }else{
            if(mapStore.containsKey(mapFile)){
                return mapStore.get(key);
            }else{
                FileConfiguration config = YamlConfiguration.loadConfiguration(mapFile);
                mapStore.put(key , config);
                return config;
            }
        }
    }

    /**
     * Create a new MapConfiguration.
     *
     * @param type The type of the minigame.
     * @param mapName The name of the minigame.
     * @param map The {@link MinigameMap} object.
     * @return a new FileConfiguration for this new Map.
     */
    public static FileConfiguration createMap( MinigameType type, String mapName,  MinigameMap map){
        if(mapStore == null) mapStore = new HashMap<>();
        String key = type.toString() + "/" + mapName;

        File mapFile = new File("plugins/CFMinigame/maps/" + key +".yml");
        FileConfiguration mapConfiguration = YamlConfiguration.loadConfiguration(mapFile);
        saveMapFile(mapConfiguration, mapFile);

        mapConfiguration.set("mapName", map.getName());
        mapConfiguration.set("maxLifeCount", map.getMaxLifeCount());
        mapConfiguration.set("spawnPoint", map.getSpawnPoint());
        mapConfiguration.set("spectatorPoint", map.getSpectatorPoint());
        mapConfiguration.set("gameTimeLimit", map.getGameTimeLimit());
        mapConfiguration.set("queueTimeLimit", map.getQueueTimeLimit());
        mapConfiguration.set("baseScore", map.getBaseScore());
        mapConfiguration.set("startingItems", map.getStartingItems());

        saveMapFile(mapConfiguration, mapFile);

        return mapConfiguration;
    }

    /**
     * Finds a Map in the Map Store based on the minigame type and mapname.
     *
     * @param type The type of the minigame.
     * @param mapName The minigame map.
     * @return A {@link MinigameMap} object with the specified type and mapname.
     */
    public static MinigameMap findMap(MinigameType type, String mapName){

        FileConfiguration mapFileConfig = null;
        try {
            mapFileConfig = getConfig(type, mapName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }

        String name = (String) mapFileConfig.get("mapName");
        int maxLife = (Integer) mapFileConfig.get("maxLifeCount");
        Location spawnPoint = (Location) mapFileConfig.get("spawnPoint");
        Location spectatorPoint = (Location) mapFileConfig.get("spectatorPoint");
        long gameTimeLimit = (Integer) mapFileConfig.get("gameTimeLimit");
        long baseScore = (Integer) mapFileConfig.get("baseScore");
        List<ItemStack> items = (List<ItemStack>) mapFileConfig.getList("startingItems");
        MinigameMap out = new MinigameMap(name, maxLife, spawnPoint, spectatorPoint, gameTimeLimit, baseScore, items.toArray(new ItemStack[items.size()]));

        return out;
    }

    /**
     * Gets the Map File with a specified minigame type and map name.
     *
     * @param type The type of the minigame.
     * @param mapName The name of the minigame map.
     * @return The File where the minigame map configuration is.
     */
    public static File getMapFile(MinigameType type, String mapName){
        return getMapFile(type.toString() + "/" + mapName);
    }

    public static File getMapFile(String key){
        return new File("plugins/CFMinigame/maps/" + key + ".yml");
    }

    /**
     * Saves a map file and handles any exceptions.
     *
     * @param configuration The FileConfiguration of the map config.
     * @param mapFile The actual file where this information is saved.
     */
    public static void saveMapFile(FileConfiguration configuration, File mapFile){
        try {
            configuration.save(mapFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
