package me.chandl.cfminigame;

import me.chandl.cfminigame.minigame.Minigame;
import me.chandl.cfminigame.minigame.MinigameMap;
import me.chandl.cfminigame.minigame.MinigamePlayer;
import me.chandl.cfminigame.minigame.MinigameType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class CommandHandler implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        //Make sure command sender is player
        if(commandSender instanceof Player){
            Player sender = (Player) commandSender;

            switch(s){
                case "mg":
                    sender.sendMessage("mg command called");
                    MinigamePlayer player = new MinigamePlayer(sender);

                    if(strings.length > 0){
                        switch(strings[0]){
                            case "new":
                                sender.sendMessage("'mg new' command called");

                                break;
                            case "publish":
                                sender.sendMessage("'mg publish' command called");
                                break;
                            case "start":
                                sender.sendMessage("'mg start' command called");
                                if(strings.length != 4){
                                    sender.sendMessage("[CFMinigame ERROR] Invalid number of arguments for the 'start' command.");
                                }else{
                                    mgStart(player, strings[1], strings[2], Integer.parseInt(strings[3]));
                                }
                                break;
                            case "stop":
                                sender.sendMessage("'mg stop' command called");
                                break;
                            case "join":
                                sender.sendMessage("'mg join' command called");
                                mgJoin(player);
                                break;
                            case "leave":
                                sender.sendMessage("'mg leave' command called");
                                mgLeave(player);
                                break;
                            case "highscore":
                                sender.sendMessage("'mg highscore' command called");
                                break;
                            case "help":
                                sender.sendMessage("'mg help' command called");
                                break;
                            default:
                                sender.sendMessage("CFMingame ERROR: '" + strings[0] +"' not a recognized command!");
                                return false;
                        }
                    }else{
                        sender.sendMessage("You must provide additional arguments to the 'mg' command. '/mg help' for more info.");
                    }

                    return true;
            }

            return true;
        }

        return false;
    }

    private void mgStart (MinigamePlayer player, String typeStr, String mapName, int difficulty){
        MinigameType type = null;
        MinigameMap map = null;

        for(MinigameType mgType : MinigameType.values()){
            if(typeStr.equalsIgnoreCase(mgType.toString())){
                type = mgType;
                break;
            }
        }

        if(type == null){
            player.getPlayerObject().sendMessage("[CFMinigame ERROR] No Mingame Type '" + typeStr +"'. Could not create minigame lobby.");
            return;
        }

        map = MinigameMap.findMap(type, mapName, difficulty);
        if(map == null){
            player.getPlayerObject().sendMessage("[CFMinigame ERROR] No Mingame Map '" + mapName +"'. Could not create minigame lobby.");
            return;
        }



    }

    private void mgJoin(MinigamePlayer player){
        Minigame currentMinigame = GameHandler.getCurrentMinigame();

        if(currentMinigame == null){
            player.getPlayerObject().sendMessage("No Minigame started. Start one with '/mg start'");
        }else{
            if(GameHandler.getHandler().addPlayer(player)){
                player.getPlayerObject().sendMessage("[CFMinigame] Successfully Joined Minigame!");
            }else{
                player.getPlayerObject().sendMessage("[CFMinigame ERROR] Maximum player count reached. Can't join.");
            }
        }
    }

    private void mgLeave(MinigamePlayer player){
        if(GameHandler.getHandler().removePlayer(player)){
            player.getPlayerObject().sendMessage("[CFMinigame] Successfully Left Minigame!");
        }else{
            player.getPlayerObject().sendMessage("[CFMinigame] You were not in a Minigame. Cannot leave.");
        }
    }


}
