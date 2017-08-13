package me.chandl.cfminigame;

import me.chandl.cfminigame.database.CheckpointConfig;
import me.chandl.cfminigame.database.MapConfig;
import me.chandl.cfminigame.minigame.builder.MinigameBuilder;
import me.chandl.cfminigame.minigame.builder.MinigameBuilders;
import me.chandl.cfminigame.minigame.checkpoint.Checkpoint;
import me.chandl.cfminigame.minigame.core.Minigame;
import me.chandl.cfminigame.minigame.core.MinigameState;
import me.chandl.cfminigame.minigame.core.MinigameType;
import me.chandl.cfminigame.minigame.player.MinigamePlayer;
import me.chandl.cfminigame.minigames.race.RaceMap;
import me.chandl.cfminigame.minigames.race.builder.RaceBuilder;
import me.chandl.cfminigame.util.Message;
import me.chandl.cfminigame.util.TextUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class CommandHandler implements CommandExecutor {

    public static final String[] commands = {"new", "publish", "start", "stop",
            "join", "leave", "highscore", "help", "status", "playerlist"};

    private List<Checkpoint> testPoints;

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        //Make sure command sender is player
        if(commandSender instanceof Player){
            Player sender = (Player) commandSender;

            switch(s.toLowerCase()){
                case "mg":
//                    System.out.println("mg command called");
                    MinigamePlayer player = new MinigamePlayer(sender, false);

                    if(strings.length > 0){
                        switch(strings[0].toLowerCase()){
                            case "build":
                                MinigameBuilder builder = MinigameBuilders.getBuilders().getMinigameBuilder(player);
                                if(builder == null) Message.player(player, "ERROR", "You are not in the Minigame Build Mode. Use '/mg new' to start.");
                                else builder.handleCommand(strings);
                                break;
                            case "new":
                                System.out.println("'mg new' command called");
                                if(strings.length < 2) Message.player(sender, "ERROR", "Usage /mg new [MinigameType]");
                                else{
                                    mgNewMap(player, strings[1]);
                                }
                                break;
                            case "publish":
                                System.out.println("'mg publish' command called");
                                break;
                            case "start":
                                player = new MinigamePlayer(sender, true);
                                System.out.println("'mg start' command called");
                                if(strings.length != 4){
                                    Message.player(sender, "ERROR", "Usage: /mg start [MinigameType] [Map] [Difficulty]");
                                }else{
                                    mgStart(player, strings[1], strings[2], Integer.parseInt(strings[3]));
                                }
                                break;
                            case "stop":
                                System.out.println("'mg stop' command called");
                                mgStop(player);
                                break;

                            case "join":
                                player = new MinigamePlayer(sender, true);
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
                                StringBuilder sb = new StringBuilder();
                                sb.append("CFMinigame Commands:\n/mg");
                                for(String cmd : commands)
                                    sb.append(" "+cmd+",");
                                Message.player(player, sb.toString().substring(0,sb.toString().length()-1));

                                break;

                            case "createrace":
                                System.out.println("createrace called");
                                ItemStack[] startingItems = new ItemStack[1];
                                startingItems[0] = new ItemStack(Material.ELYTRA);
                                RaceMap testMap = new RaceMap("testMap2", 3, sender.getLocation(), sender.getLocation() , 1, 1, startingItems, testPoints);

                                FileConfiguration a = MapConfig.createMap(MinigameType.ELYTRARACE, "testMap2", testMap);
                                LinkedList<Checkpoint> checkpointList = new LinkedList<>(testPoints);
                                a.set("checkpoints", checkpointList);
                                MapConfig.saveMapFile();

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
                            case "checkpoint":
                                if(testPoints == null) testPoints = new ArrayList<>();
//                                String testPoint = "OOXXXOX\n" +
//                                        "OXYYYXO\n" +
//                                        "XYYYYYX\n" +
//                                        "XYYYYYX\n" +
//                                        "XYYYYYX\n" +
//                                        "OXYYYXO\n" +
//                                        "OOXXXOO\n" +
//                                        "XOOOOOO";
                                Location here = sender.getLocation();
                                String cp = CheckpointConfig.loadPoint(MinigameType.ELYTRARACE, new Integer(strings[1]));



                                Checkpoint point = new Checkpoint(cp, here, here.getYaw());
                                Material mat = player.getPlayerObject().getInventory().getItemInMainHand().getType();
                                point.setMaterial(mat);
                                point.spawn();
                                testPoints.add(point);
                                break;
                            case "despawncheckpoints":
                                for(Checkpoint pt : testPoints){
                                    pt.despawn();
                                }

                                testPoints.clear();
                                break;
                            case "cpfile":
                                CheckpointConfig.createCheckpointFile(new File("plugins/CFMinigame/checkpoints/ELYTRARACE-4.txt"), "OOXXXOX\n" +
                                        "OXYYYXO\n" +
                                        "XYYYYYX\n" +
                                        "XYYYYYX\n" +
                                        "XYYYYYX\n" +
                                        "OXYYYXO\n" +
                                        "OOXXXOO\n" +
                                        "XOOOOOO");
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
//        System.out.println("MGStart Player: " + player);
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

    public void mgNewMap(MinigamePlayer player, String mgType){
        if(MinigameBuilders.getBuilders().isBuilding(player)){
            Message.player(player, "ERROR", "You are already in build mode. Type '/mg build stop' to exit.");
            return ;
        }

        MinigameType type = null;
        for(MinigameType t : MinigameType.values()){
            if(mgType.equalsIgnoreCase(t.toString())){
                type = t;
                break;
            }
        }

        if(type == null) {
            Message.player(player, "ERROR", "Minigame Type '"+ mgType + "' not found!" );
            return ;
        }

        switch(type){
            case ELYTRARACE:
                Message.player(player, "In Minigame Build Mode! Type '/mg build status' to see Available Minigame Options.");
                MinigameBuilder builder = new RaceBuilder(player);
                MinigameBuilders.getBuilders().setBuilding(player, builder);
                break;
            default:
                Message.player(player, "ERROR", "No Minigame Builder for Type '"+ mgType + "' found!" );
                break;

        }
    }
}
