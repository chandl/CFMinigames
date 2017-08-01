package me.chandl.cfminigame;

import me.chandl.cfminigame.database.MapConfig;
import me.chandl.cfminigame.minigame.MinigameType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;


public class TabCompleteHandler implements TabCompleter {

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
                                for(String map : MapConfig.getMapList(strings[1])){
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
