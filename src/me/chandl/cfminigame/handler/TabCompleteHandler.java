package me.chandl.cfminigame.handler;

import me.chandl.cfminigame.CFMinigame;
import me.chandl.cfminigame.minigame.core.MinigameType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.*;

/**
 * TabCompleteHandler - A Class that Handles Bukkit {@link TabCompleter} Tab Completion
 */
public class TabCompleteHandler implements TabCompleter {
    private static Date lastMapUpdate;
    private static ArrayList<String> mapNames;

    /**
     * Gets the list of MinigameMaps that are saved on the server.
     * Uses a timed-cache for updating the list.
     * After 5 minutes, the list will be refreshed if someone tab-completes.
     *
     * @param type The Minigame Type.
     * @return An {@link ArrayList} of the available map names in String form.
     */
    private static ArrayList<String> getMapList(String type){
        Date now = new Date();

        //If there have been no updates in 5 minutes...
        if(lastMapUpdate == null || now.getTime() - lastMapUpdate.getTime() >= 5 * 60 * 1000) {
            ArrayList<String> out = new ArrayList<>();
            File mapsPath = new File("plugins/CFMinigame/maps/" + type + "/");
            if(!mapsPath.exists()) return new ArrayList<>();


            File[] fList = mapsPath.listFiles();
            HashSet<String> maps = new HashSet<>();

            String fn, name;
            for(File file : fList){
                if(file.isFile() && !file.getName().equalsIgnoreCase(".DS_Store")){//make sure it is not a directory or .DS_STORE
                    fn = file.getName();
                    name = fn.split(".yml")[0];
                    System.out.println("[CFMinigame] TabCompleteHandler - getMapList - Indexed File : " + fn + " ( " + name + ")");
                    maps.add(name);
                }
            }
            out.addAll(maps);
            mapNames = out;
            lastMapUpdate = new Date();

            return out;
        } else {
            return mapNames;
        }
    }

    //Tab Completion Handler
    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        String startsWith = "";
        List<String> out = new ArrayList<>();
        //Make sure command sender is player
        if (commandSender instanceof Player) {
            if (s.equalsIgnoreCase("mg")) {
                if (strings.length > 0) {
                    if(strings.length == 1){
                        startsWith = strings[0];

                        List<String> tmp = Arrays.asList(CommandHandler.commands);

                        for(String cmd : tmp){
                            if(cmd.startsWith(startsWith.toLowerCase())) out.add(cmd);
                        }
                        return out;
                    }

                    switch (strings[0]) {
                        case "start":

                            if (strings.length == 2) {
                                startsWith = strings[1];
                                HashSet<String> types = new HashSet();

                                for(MinigameType type : MinigameType.values() ){
                                    if(type.toString() != null && type.toString().startsWith(startsWith.toUpperCase())) types.add(type.toString());
                                }

                                out.addAll(types);

                                return out;
                            }
                            else if (strings.length == 3) {
                                startsWith = strings[2];
                                for(String map : getMapList(strings[1])){
                                    if(map.startsWith(startsWith))
                                        out.add(map);
                                }

                                return out;
                            }
                            else if (strings.length == 4){
                                for(int i = CFMinigame.DEFAULT_MIN_DIFFICULTY; i <= CFMinigame.DEFAULT_MAX_DIFFICULTY; i++){
                                    out.add(i + "");
                                }
                                return out;
                            }
                            break;
                        case "new":
                            HashSet<String> types = new HashSet();

                            for(MinigameType type : MinigameType.values() ){
                                startsWith = strings[1];
                                if(type.toString() != null && type.toString().startsWith(startsWith.toUpperCase())) {
                                    types.add(type.toString());
                                }
                            }

                            out.clear();
                            out.addAll(types);

                            return out;

                        default:
                            return new ArrayList<>();
                    }
                }
            }
        }
        return null;
    }
}
