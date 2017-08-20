package me.chandl.cfminigame.handler;

import me.chandl.cfminigame.database.CheckpointConfigStore;
import me.chandl.cfminigame.database.MapStore;
import me.chandl.cfminigame.minigame.builder.MinigameBuilder;
import me.chandl.cfminigame.minigame.builder.MinigameBuilderStore;
import me.chandl.cfminigame.minigame.player.MinigamePlayerStore;
import me.chandl.cfminigame.minigame.core.Minigame;
import me.chandl.cfminigame.minigame.core.MinigameMap;
import me.chandl.cfminigame.minigame.core.MinigameState;
import me.chandl.cfminigame.minigame.core.MinigameType;
import me.chandl.cfminigame.minigame.player.MinigamePlayer;
import me.chandl.cfminigame.minigames.race.builder.RaceBuilder;
import me.chandl.cfminigame.minigames.snowballfight.builder.SnowballFightBuilder;
import me.chandl.cfminigame.util.Message;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;

/**
 * CommandHandler - a Bukkit {@link CommandExecutor} that controls all of the commands for this Plugin.
 *
 * @author Chandler me@cseverson.com
 * @version 1.0
 * @since Aug 19, 2017
 */
public class CommandHandler implements CommandExecutor {

    // List of commands that can be used. This is used for Tab-Completion
    public static final String[] commands = {"new", "start", "stop",
            "join", "leave", "help"};


    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        //Make sure command sender is player
        if(commandSender instanceof Player){
            Player sender = (Player) commandSender;

            switch(s.toLowerCase()){
                case "mg":
                    //Get MinigamePlayer object for Player.
                    MinigamePlayer player = MinigamePlayerStore.findPlayer(sender);

                    if(strings.length > 0){
                        switch(strings[0].toLowerCase()){

                            //Createsnowball Command - Used to create a quick Snowball Fight Game.
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

                                    FileConfiguration conf = MapStore.createMap(MinigameType.SNOWBALLFIGHT, name, testMap);

                                    MapStore.saveMapFile(conf, MapStore.getMapFile(MinigameType.SNOWBALLFIGHT, name));

                                    Message.player(player, "New SNOWBALL Map Created: " + name);
                                }else{
                                    Message.player(sender, "ERROR", "Sorry, you do not have the permissions required to perform that command.");
                                }
                                break;

                            //Build Command - Used in conjunction with 'New' command, serves as dialogue to create new Minigame Map.
                            case "build":
                                if(sender.hasPermission("CFMinigame.mg.admin")){
                                    MinigameBuilder builder = MinigameBuilderStore.getInstance().getMinigameBuilder(player);
                                    if(builder == null) Message.player(player, "ERROR", "You are not in the Minigame Build Mode. Use '/mg new' to start.");
                                    else builder.handleCommand(strings);
                                }else{
                                    Message.player(sender, "ERROR", "Sorry, you do not have the permissions required to perform that command.");
                                }

                                break;
                            //New Command - Used to create new Minigame Maps.
                            case "new":
                                if(sender.hasPermission("CFMinigame.mg.admin")) {
                                    if (strings.length < 2)
                                        Message.player(sender, "ERROR", "Usage /mg new [MinigameType]");
                                    else {
                                        mgNewMap(player, strings[1]);
                                    }
                                }else{
                                    Message.player(sender, "ERROR", "Sorry, you do not have the permissions required to perform that command.");
                                }
                                break;

                            //Start Command - Used to start a new Minigame.
                            case "start":
                                if(sender.hasPermission("CFMinigame.mg.start")) {
                                    player = MinigamePlayerStore.findResetPlayer(sender);
                                    if (strings.length != 4) {
                                        Message.player(sender, "ERROR", "Usage: /mg start [MinigameType] [Map] [Difficulty]");
                                    } else {
                                        mgStart(player, strings[1], strings[2], Integer.parseInt(strings[3]));
                                    }
                                }else{
                                    Message.player(sender, "ERROR", "Sorry, you do not have the permissions required to perform that command.");
                                }
                                break;

                            //Stop Command - Used to stop the Current Minigame.
                            case "stop":
                                if(sender.hasPermission("CFMinigame.mg.stop")) {
                                    mgStop(player);
                                }else{
                                    Message.player(sender, "ERROR", "Sorry, you do not have the permissions required to perform that command.");
                                }
                                break;

                            //Join Command - Used to Join the Current Minigame.
                            case "join":
                                if(sender.hasPermission("CFMinigame.mg.join")) {
                                    player = MinigamePlayerStore.findResetPlayer(sender);
                                    mgJoin(player);
                                }else{
                                    Message.player(sender, "ERROR", "Sorry, you do not have the permissions required to perform that command.");
                                }
                                break;

                            //Leave Command - Used to Leave the Current Minigame.
                            case "leave":
                                if(sender.hasPermission("CFMinigame.mg.leave")) {
                                    mgLeave(player);
                                }else{
                                    Message.player(sender, "ERROR", "Sorry, you do not have the permissions required to perform that command.");
                                }
                                break;

                                /* HIGHSCORE - FOR FUTURE USE

                            case "highscore":
                                if(sender.hasPermission("CFMinigame.mg.highscore")) {
                                    System.out.println("'mg highscore' command called");
                                }else{
                                    Message.player(sender, "ERROR", "Sorry, you do not have the permissions required to perform that command.");
                                }
                                break;*/

                            //Help Command - Used to show the available commands.
                            case "help":
                                if(sender.hasPermission("CFMinigame.mg.help")) {
                                    StringBuilder sb = new StringBuilder();
                                    sb.append("CFMinigame Commands:\n/mg");
                                    for (String cmd : commands)
                                        sb.append(" " + cmd + ",");
                                    Message.player(player, sb.toString().substring(0, sb.toString().length() - 1));
                                }else{
                                    Message.player(sender, "ERROR", "Sorry, you do not have the permissions required to perform that command.");
                                }
                                break;

                            case "ccpf":
                                if(sender.hasPermission("CFMinigame.mg.admin")) {
                                    createCheckpointFiles();
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

    /**
     * Called when a Player uses the /mg stop command.
     * Stops the current minigame.
     *
     * @param player The player that called /mg stop
     */
    private void mgStop(MinigamePlayer player){
        if(GameHandler.getHandler().getCurrentMinigame() != null && GameHandler.getHandler().getCurrentState() != MinigameState.NO_GAME){
            GameHandler.getHandler().stopMinigame();
            Message.allPlayers("INFO", "The Current Minigame was Stopped! Start a new one with '/mg start'!");
        }else{
            Message.player(player, "ERROR", "Minigame Could not be Stopped. No Game found");
        }
    }

    /**
     * Called when a Player uses the /mg start command.
     * Starts a new minigame and alerts players.
     *
     * @param player The player who is starting the minigame.
     * @param typeStr  The desired type of the minigame.
     * @param mapName The desired Map of the minigame.
     * @param difficulty The desired difficulty level of the minigame.
     */
    private void mgStart (MinigamePlayer player, String typeStr, String mapName, int difficulty){
        if(GameHandler.getHandler().startNewMinigame(player, typeStr, mapName, difficulty)){
            Message.allPlayers(String.format("New %s minigame started. Use '/mg join' to join the lobby!", GameHandler.getHandler().getCurrentMinigame().getType()));
        }else{
            System.out.println(String.format("[CFMinigame ERROR] Minigame lobby COULD NOT BE started! %s: type: %s, map: %s, difficulty %d", player.getPlayerObject().getName(), typeStr, mapName, difficulty));
        }
    }

    /**
     * Called when a Player uses the /mg join command.
     * Lets the player join the current minigame.
     *
     * @param player The player who is joining the Minigame.
     */
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

    /**
     * Called when a Player uses the /mg leave command.
     * Lets the player leave the current minigame.
     *
     * @param player The player who is leaving the Minigame.
     */
    private void mgLeave(MinigamePlayer player){
        if(GameHandler.getHandler().removePlayer(player)){
            Message.player(player, "Successfully Left Minigame!");
        }else{
            Message.player(player, "You were not in a Minigame. Cannot leave.");
        }
    }

    /**
     * Called whe a Player uses the /mg new command.
     * Puts the player in Build mode so the /mg build command can be used.
     *
     * @param player The Player who is building the new Minigame.
     * @param mgType The type of the minigame that is being built.
     */
    private void mgNewMap(MinigamePlayer player, String mgType){
        if(MinigameBuilderStore.getInstance().isBuilding(player)){
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

        MinigameBuilder builder;
        switch(type){
            case ELYTRARACE:
                Message.player(player, "In Minigame Build Mode! Type '/mg build status' to see Available Minigame Options.");
                builder = new RaceBuilder(player);
                MinigameBuilderStore.getInstance().setBuilding(player, builder);
                break;
            case SNOWBALLFIGHT:
                Message.player(player, "In Minigame Build Mode! Type '/mg build status' to see Available Minigame Options.");
                builder = new SnowballFightBuilder(player);
                MinigameBuilderStore.getInstance().setBuilding(player, builder);
                break;
            default:
                Message.player(player, "ERROR", "No Minigame Builder for Type '"+ mgType + "' found!" );
                break;

        }
    }

    /**
     * Self-Explanatory.
     */
    private void createCheckpointFiles(){
        CheckpointConfigStore.createCheckpointFile(new File("plugins/CFMinigame/checkpoints/ELYTRARACE-1.txt"), "OOXXXXXXXXXOO\n" +
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
        CheckpointConfigStore.createCheckpointFile(new File("plugins/CFMinigame/checkpoints/ELYTRARACE-2.txt"), "OOXXXXXXXOO\n" +
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
        CheckpointConfigStore.createCheckpointFile(new File("plugins/CFMinigame/checkpoints/ELYTRARACE-3.txt"), "OOXXXXXOO\n" +
                "OXYYYYYXO\n" +
                "XYYYYYYYX\n" +
                "XYYYYYYYX\n" +
                "XYYYYYYYX\n" +
                "XYYYYYYYX\n" +
                "XYYYYYYYX\n" +
                "OXYYYYYXO\n" +
                "OOXXXXXOO");
        CheckpointConfigStore.createCheckpointFile(new File("plugins/CFMinigame/checkpoints/ELYTRARACE-4.txt"), "OOXXXOX\n" +
                "OXYYYXO\n" +
                "XYYYYYX\n" +
                "XYYYYYX\n" +
                "XYYYYYX\n" +
                "OXYYYXO\n" +
                "OOXXXOO\n" +
                "XOOOOOO");
        CheckpointConfigStore.createCheckpointFile(new File("plugins/CFMinigame/checkpoints/ELYTRARACE-5.txt"), "OOXXXOO\n" +
                "OXYYYXO\n" +
                "XYYYYYX\n" +
                "XYYYYYX\n" +
                "XYYYYYX\n" +
                "OXYYYXO\n" +
                "OOXXXOO");
    }
}
