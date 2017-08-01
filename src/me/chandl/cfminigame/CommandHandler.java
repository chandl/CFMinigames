package me.chandl.cfminigame;

import me.chandl.cfminigame.database.MapConfig;
import me.chandl.cfminigame.minigame.*;
import me.chandl.cfminigame.util.Message;
import me.chandl.cfminigame.util.TextUtil;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;


public class CommandHandler implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        //Make sure command sender is player
        if(commandSender instanceof Player){
            Player sender = (Player) commandSender;

            switch(s.toLowerCase()){
                case "mg":
                    System.out.println("mg command called");
                    MinigamePlayer player = new MinigamePlayer(sender);

                    if(strings.length > 0){
                        switch(strings[0]){
                            case "new":
                                System.out.println("'mg new' command called");

                                break;
                            case "publish":
                                System.out.println("'mg publish' command called");
                                break;
                            case "start":
                                System.out.println("'mg start' command called");
                                if(strings.length != 4){
                                    sender.sendMessage("[CFMinigame ERROR] Invalid number of arguments for the 'start' command.");
                                }else{
                                    mgStart(player, strings[1], strings[2], Integer.parseInt(strings[3]));
                                }
                                break;
                            case "stop":
                                System.out.println("'mg stop' command called");
                                mgStop(player);
                                break;
                            case "join":
                                System.out.println("'mg join' command called");
                                mgJoin(player);
                                break;
                            case "leave":
                                System.out.println("'mg leave' command called");
                                mgLeave(player);
                                break;
                            case "highscore":
                                System.out.println("'mg highscore' command called");
                                break;
                            case "help":
                                System.out.println("'mg help' command called");
                                break;
                            case "createrace":
                                ItemStack[] startingItems = new ItemStack[1];
                                startingItems[0] = new ItemStack(Material.ELYTRA);
                                MinigameMap testMap = new MinigameMap("testMap", 3, sender.getLocation(), sender.getLocation() , 1, 1, startingItems);
                                File testFile = new File("plugins/CFMinigame/maps/elytrarace/testMap-1.yml");
                                MapConfig.createMap(testFile, testMap);
                                break;
                            case "status":
                                System.out.println("MG STATUS:" + GameHandler.getHandler().getCurrentState());
                                if(GameHandler.getHandler().getCurrentState() != MinigameState.NO_GAME)
                                    System.out.println("MG: " + GameHandler.getHandler().getCurrentMinigame().toString());
                                break;
                            case "playerlist":
                                if(GameHandler.getHandler().getCurrentState() != MinigameState.NO_GAME){
                                    int i=1;
                                    for(MinigamePlayer p : GameHandler.getHandler().getPlayerList()){
                                        sender.sendMessage(TextUtil.formatMessage("Player " + (i++) +": " + p.getPlayerObject().getName()));
                                    }
                                }
                                break;
                            default:
                                Message.player(sender, "ERROR", "'" + strings[0] +"' is not a recognized command!");
                                return false;
                        }
                    }else{
                        Message.player(sender, "INFO", "You must provide additional arguments to the 'mg' command. '/mg help' for more info.");
                    }

                    return true;
            }

            return true;
        }

        return false;
    }

    private void mgStop(MinigamePlayer player){
        if(GameHandler.getHandler().getCurrentMinigame() != null && GameHandler.getHandler().getCurrentState() != MinigameState.NO_GAME){
            GameHandler.getHandler().stopMinigame();
            Message.allPlayers("INFO", "The Current Minigame was Stopped! Start a new one with '/mg start'!");
        }else{
            Message.player(player, "ERROR", "Minigame Could not be Stopped. No Game found");
        }
    }

    private void mgStart (MinigamePlayer player, String typeStr, String mapName, int difficulty){
        if(GameHandler.getHandler().createMinigame(player, typeStr, mapName, difficulty)){
            Message.allPlayers(String.format("New %s minigame started. Use '/mg join' to join the lobby!", GameHandler.getHandler().getCurrentMinigame().getType()));
        }else{
            System.out.println(String.format("[CFMinigame ERROR] Minigame lobby COULD NOT BE started! %s: type: %s, map: %s, difficulty %d", player.getPlayerObject().getName(), typeStr, mapName, difficulty));
        }
    }

    private void mgJoin(MinigamePlayer player){
        Minigame currentMinigame = GameHandler.getHandler().getCurrentMinigame();

        if(currentMinigame == null){
            Message.player(player, "No Minigame started. Start one with '/mg start'");
        }else if(GameHandler.getHandler().playerInGame(player)){
            Message.player(player, "ERROR", "You are already in the minigame lobby! Use '/mg leave' to leave.");

        }else{
            if(GameHandler.getHandler().addPlayer(player)){
                Message.player(player, "Successfully Joined Minigame!");
            }else{
                Message.player(player, "ERROR", "Maximum player count reached. Can't join.");
            }
        }
    }

    private void mgLeave(MinigamePlayer player){
        if(GameHandler.getHandler().removePlayer(player)){
            Message.player(player, "Successfully Left Minigame!");
        }else{
            Message.player(player, "You were not in a Minigame. Cannot leave.");
        }
    }


}
