package me.chandl.cfminigame;

import me.chandl.cfminigame.database.MapConfig;
import me.chandl.cfminigame.minigame.MinigameType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.*;


public class TabCompleteHandler implements TabCompleter {
    private static Date lastMapUpdate;
    private static ArrayList<String> mapNames;

    public static ArrayList<String> getMapList(String type){

        //If there have been no updates in 5 minutes...
        Date now = new Date();
        if(lastMapUpdate == null || now.getTime() - lastMapUpdate.getTime() >= 5 * 60 * 1000) {
            ArrayList<String> out = new ArrayList<>();
            File mapsPath = new File("plugins/CFMinigame/maps/" + type + "/");
            if(!mapsPath.exists()) return new ArrayList<>();


            File[] fList = mapsPath.listFiles();
            HashSet<String> maps = new HashSet<>();

            String fn, name;
            for(File file : fList){
                if(file.isFile()){//make sure it is not a directory
                    fn = file.getName();
                    System.out.println("Indexed File : " + fn);
                    name = fn.substring(0, fn.length() - 6);
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

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        List<String> out = new ArrayList<>();
        //Make sure command sender is player
        if (commandSender instanceof Player) {
            if (s.equalsIgnoreCase("mg")) {
                if (strings.length > 0) {
                    if(strings.length == 1){
                        out = Arrays.asList(CommandHandler.commands);
                        return out;
                    }

                    switch (strings[0]) {
                        case "start":

                            if (strings.length == 2) {
                                HashSet<String> types = new HashSet();

                                for(MinigameType type : MinigameType.values() ){
                                    if(type.toString() != null) types.add(type.toString());
                                }

                                out.addAll(types);

                                return out;
                            }
                            else if (strings.length == 3) {
                                for(String map : getMapList(strings[1])){
                                    out.add(map);
                                }

                                return out;
                            }
                            else if (strings.length == 4){
                                for(int i= CFMinigame.DEFAULT_MIN_DIFFICULTY; i <= CFMinigame.DEFAULT_MAX_DIFFICULTY; i++){
                                    out.add(i + "");
                                }
                                return out;
                            }
                            break;

                        default:
                            return new ArrayList<>();
                    }
                }
            }
        }
        return null;
    }
}
