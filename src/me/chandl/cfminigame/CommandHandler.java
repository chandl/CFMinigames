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
                    return true;
            }

            return true;
        }

        return false;
    }


}
