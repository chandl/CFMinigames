package me.chandl.cfminigame;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by chandler on 7/30/17.
 */
public class CommandHandler implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        //Make sure command sender is player
        if(commandSender instanceof Player){
            Player sender = (Player) commandSender;

            switch(s){
                case "mg":
                    sender.sendMessage("mg command called");

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
                                break;
                            case "stop":
                                sender.sendMessage("'mg stop' command called");
                                break;
                            case "join":
                                sender.sendMessage("'mg join' command called");
                                break;
                            case "highscore":
                                sender.sendMessage("'mg highscore' command called");
                                break;
                            default:
                                sender.sendMessage("ERROR: '" + strings[0] +"' not a recognized command!");
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


}
