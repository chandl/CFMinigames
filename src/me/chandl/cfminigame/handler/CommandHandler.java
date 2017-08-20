package me.chandl.cfminigame.handler;

import me.chandl.cfminigame.database.CheckpointConfig;
import me.chandl.cfminigame.database.MapConfig;
import me.chandl.cfminigame.minigame.builder.MinigameBuilder;
import me.chandl.cfminigame.minigame.builder.MinigameBuilders;
import me.chandl.cfminigame.minigames.race.checkpoint.Checkpoint;
import me.chandl.cfminigame.minigame.core.Minigame;
import me.chandl.cfminigame.minigame.core.MinigameMap;
import me.chandl.cfminigame.minigame.core.MinigameState;
import me.chandl.cfminigame.minigame.core.MinigameType;
import me.chandl.cfminigame.minigame.player.MinigamePlayer;
import me.chandl.cfminigame.minigames.race.builder.RaceBuilder;
import me.chandl.cfminigame.util.Message;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.List;


public class CommandHandler implements CommandExecutor {

    public static final String[] commands = {"new", "start", "stop",
            "join", "leave", "help"};

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

                            case "createsnowball":
                                if(sender.hasPermission("CFMinigame.mg.admin")) {
                                    if(strings.length < 2){
                                        Message.player(player, "ERROR", "Usage: /mg createsnowball 'name' ");
                                        return false;
                                    }
                                    String name = "";

                                    for(int i=1; i<strings.length; i++){
                                        name += strings[i];
                                        if(i+1 < strings.length) name += "-";
                                    }

                                    System.out.println("createsnowball called");
                                    ItemStack[] startingItems = new ItemStack[1];
                                    startingItems[0] = new ItemStack(Material.SNOW_BALL, 16);

                                    MinigameMap testMap = new MinigameMap(name, 10, sender.getLocation(), sender.getLocation(), 1, 1, startingItems);

                                    FileConfiguration a = MapConfig.createMap(MinigameType.SNOWBALLFIGHT, name, testMap);

                                    MapConfig.saveMapFile();

                                    Message.player(player, "New SNOWBALL Map Created: " + name);
                                }else{
                                    Message.player(sender, "ERROR", "Sorry, you do not have the permissions required to perform that command.");
                                }
                                break;
                            case "build":
                                if(sender.hasPermission("CFMinigame.mg.admin")){
                                    MinigameBuilder builder = MinigameBuilders.getBuilders().getMinigameBuilder(player);
                                    if(builder == null) Message.player(player, "ERROR", "You are not in the Minigame Build Mode. Use '/mg new' to start.");
                                    else builder.handleCommand(strings);
                                }else{
                                    Message.player(sender, "ERROR", "Sorry, you do not have the permissions required to perform that command.");
                                }

                                break;
                            case "new":
                                if(sender.hasPermission("CFMinigame.mg.admin")) {
                                    System.out.println("'mg new' command called");
                                    if (strings.length < 2)
                                        Message.player(sender, "ERROR", "Usage /mg new [MinigameType]");
                                    else {
                                        mgNewMap(player, strings[1]);
                                    }
                                }else{
                                    Message.player(sender, "ERROR", "Sorry, you do not have the permissions required to perform that command.");
                                }
                                break;
                            case "publish":
                                if(sender.hasPermission("CFMinigame.mg.admin")) {
                                    System.out.println("'mg publish' command called");
                                }else{
                                    Message.player(sender, "ERROR", "Sorry, you do not have the permissions required to perform that command.");
                                }
                                break;
                            case "start":
                                if(sender.hasPermission("CFMinigame.mg.start")) {
                                    player = new MinigamePlayer(sender, true);
                                    System.out.println("'mg start' command called");
                                    if (strings.length != 4) {
                                        Message.player(sender, "ERROR", "Usage: /mg start [MinigameType] [Map] [Difficulty]");
                                    } else {
                                        mgStart(player, strings[1], strings[2], Integer.parseInt(strings[3]));
                                    }
                                }else{
                                    Message.player(sender, "ERROR", "Sorry, you do not have the permissions required to perform that command.");
                                }
                                break;
                            case "stop":
                                if(sender.hasPermission("CFMinigame.mg.stop")) {
                                    System.out.println("'mg stop' command called");
                                    mgStop(player);
                                }else{
                                    Message.player(sender, "ERROR", "Sorry, you do not have the permissions required to perform that command.");
                                }
                                break;

                            case "join":
                                if(sender.hasPermission("CFMinigame.mg.join")) {
                                    player = new MinigamePlayer(sender, true);
                                    System.out.println("'mg join' command called");
                                    mgJoin(player);
                                }else{
                                    Message.player(sender, "ERROR", "Sorry, you do not have the permissions required to perform that command.");
                                }
                                break;
                            case "leave":
                                if(sender.hasPermission("CFMinigame.mg.leave")) {
                                    System.out.println("'mg leave' command called");
                                    mgLeave(player);
                                }else{
                                    Message.player(sender, "ERROR", "Sorry, you do not have the permissions required to perform that command.");
                                }
                                break;
                            case "highscore":
                                if(sender.hasPermission("CFMinigame.mg.highscore")) {
                                    System.out.println("'mg highscore' command called");
                                }else{
                                    Message.player(sender, "ERROR", "Sorry, you do not have the permissions required to perform that command.");
                                }
                                break;
                            case "help":
                                if(sender.hasPermission("CFMinigame.mg.help")) {
                                    System.out.println("'mg help' command called");
                                    StringBuilder sb = new StringBuilder();
                                    sb.append("CFMinigame Commands:\n/mg");
                                    for (String cmd : commands)
                                        sb.append(" " + cmd + ",");
                                    Message.player(player, sb.toString().substring(0, sb.toString().length() - 1));
                                }else{
                                    Message.player(sender, "ERROR", "Sorry, you do not have the permissions required to perform that command.");
                                }
                                break;

                            /*case "createrace":



                                if(sender.hasPermission("CFMinigame.mg.admin")) {
                                    if(strings.length < 2){
                                        Message.player(player, "ERROR", "Usage: /mg createrace 'racename' ");
                                        return false;
                                    }
                                    String name = "";

                                    for(int i=1; i<strings.length; i++){
                                        name += strings[i];
                                        if(i+1 < strings.length) name += "-";
                                    }

                                    System.out.println("createrace called");
                                    ItemStack[] startingItems = new ItemStack[1];
                                    startingItems[0] = new ItemStack(Material.ELYTRA);

                                    RaceMap testMap = new RaceMap(name, 10, sender.getLocation(), sender.getLocation(), 1, 1, startingItems, testPoints);

                                    FileConfiguration a = MapConfig.createMap(MinigameType.ELYTRARACE, name, testMap);
                                    LinkedList<Checkpoint> checkpointList = new LinkedList<>(testPoints);
                                    a.set("checkpoints", checkpointList);
                                    MapConfig.saveMapFile();

                                    Message.player(player, "New Map Created: " + name);

                                    for (Checkpoint pt : testPoints) {
                                        pt.despawn();
                                    }

                                    testPoints.clear();
                                }else{
                                    Message.player(sender, "ERROR", "Sorry, you do not have the permissions required to perform that command.");
                                }
                                break;
                            case "status":
                                if(sender.hasPermission("CFMinigame.mg.admin")) {
                                    System.out.println("MG STATUS:" + GameHandler.getHandler().getCurrentState());
                                    if (GameHandler.getHandler().getCurrentState() != MinigameState.NO_GAME)
                                        System.out.println("MG: " + GameHandler.getHandler().getCurrentMinigame().toString());
                                }else{
                                    Message.player(sender, "ERROR", "Sorry, you do not have the permissions required to perform that command.");
                                }
                                break;
                            case "playerlist":
                                if(sender.hasPermission("CFMinigame.mg.playerlist")) {
                                    if (GameHandler.getHandler().getCurrentState() != MinigameState.NO_GAME) {
                                        int i = 1;
                                        for (MinigamePlayer p : GameHandler.getHandler().getPlayerList()) {
                                            sender.sendMessage(TextUtil.formatMessage("Player " + (i++) + ": " + p.getPlayerObject().getName()));
                                        }
                                    }
                                }else{
                                    Message.player(sender, "ERROR", "Sorry, you do not have the permissions required to perform that command.");
                                }
                                break;*/
                            /*case "checkpoint":
                                if(sender.hasPermission("CFMinigame.mg.admin")) {
                                    if (testPoints == null) testPoints = new ArrayList<>();
                                    Location here = sender.getLocation();
                                    String cp = CheckpointConfig.loadPoint(MinigameType.ELYTRARACE, new Integer(strings[1]));


                                    Checkpoint point = new Checkpoint(cp, here, here.getYaw());
                                    Material mat = player.getPlayerObject().getInventory().getItemInMainHand().getType();
                                    point.setMaterial(mat);
                                    point.spawn();
                                    testPoints.add(point);
                                }else{
                                    Message.player(sender, "ERROR", "Sorry, you do not have the permissions required to perform that command.");
                                }
                                break;
                            case "despawncheckpoints":
                                if(sender.hasPermission("CFMinigame.mg.admin")) {
                                    for (Checkpoint pt : testPoints) {
                                        pt.despawn();
                                    }

                                    testPoints.clear();
                                }else{
                                    Message.player(sender, "ERROR", "Sorry, you do not have the permissions required to perform that command.");
                                }
                                break;
                            */
                            case "cpfile":
                                if(sender.hasPermission("CFMinigame.mg.admin")) {
                                    CheckpointConfig.createCheckpointFile(new File("plugins/CFMinigame/checkpoints/ELYTRARACE-1.txt"), "OOXXXXXXXXXOO\n" +
                                            "OXYYYYYYYYYXO\n" +
                                            "XYYYYYYYYYYYX\n" +
                                            "XYYYYYYYYYYYX\n" +
                                            "XYYYYYYYYYYYX\n" +
                                            "XYYYYYYYYYYYX\n" +
                                            "XYYYYYYYYYYYX\n" +
                                            "XYYYYYYYYYYYX\n" +
                                            "XYYYYYYYYYYYX\n" +
                                            "XYYYYYYYYYYYX\n" +
                                            "XYYYYYYYYYYYX\n" +
                                            "OXYYYYYYYYYXO\n" +
                                            "OOXXXXXXXXXOO");
                                    CheckpointConfig.createCheckpointFile(new File("plugins/CFMinigame/checkpoints/ELYTRARACE-2.txt"), "OOXXXXXXXOO\n" +
                                            "OXYYYYYYYXO\n" +
                                            "XYYYYYYYYYX\n" +
                                            "XYYYYYYYYYX\n" +
                                            "XYYYYYYYYYX\n" +
                                            "XYYYYYYYYYX\n" +
                                            "XYYYYYYYYYX\n" +
                                            "XYYYYYYYYYX\n" +
                                            "XYYYYYYYYYX\n" +
                                            "OXYYYYYYYXO\n" +
                                            "OOXXXXXXXOO\n");
                                    CheckpointConfig.createCheckpointFile(new File("plugins/CFMinigame/checkpoints/ELYTRARACE-3.txt"), "OOXXXXXOO\n" +
                                            "OXYYYYYXO\n" +
                                            "XYYYYYYYX\n" +
                                            "XYYYYYYYX\n" +
                                            "XYYYYYYYX\n" +
                                            "XYYYYYYYX\n" +
                                            "XYYYYYYYX\n" +
                                            "OXYYYYYXO\n" +
                                            "OOXXXXXOO");
                                    CheckpointConfig.createCheckpointFile(new File("plugins/CFMinigame/checkpoints/ELYTRARACE-4.txt"), "OOXXXOX\n" +
                                            "OXYYYXO\n" +
                                            "XYYYYYX\n" +
                                            "XYYYYYX\n" +
                                            "XYYYYYX\n" +
                                            "OXYYYXO\n" +
                                            "OOXXXOO\n" +
                                            "XOOOOOO");
                                    CheckpointConfig.createCheckpointFile(new File("plugins/CFMinigame/checkpoints/ELYTRARACE-5.txt"), "OOXXXOO\n" +
                                            "OXYYYXO\n" +
                                            "XYYYYYX\n" +
                                            "XYYYYYX\n" +
                                            "XYYYYYX\n" +
                                            "OXYYYXO\n" +
                                            "OOXXXOO");
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