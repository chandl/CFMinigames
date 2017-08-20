package me.chandl.cfminigame.minigame.builder;


import me.chandl.cfminigame.minigame.core.MinigameMap;
import me.chandl.cfminigame.minigame.player.MinigamePlayer;
import me.chandl.cfminigame.util.Message;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

/**
 * Minigame Builder Class - Abstract class to help mods build a new minigame from specified requirements.
 *
 * <p>
 *     Base State Progression:
         1) Set name of mg
         2) Set Spawnpoint of MG
         3) Set Minimum Players
         4) Set Maximum Players
         5) Set Time Limit
         6) Set Queue Time Limit
         7) Minigame-Specific Additions (e.g. Checkpoints)
         8) Confirm
 * </p>
 *
 * @author Chandler me@cseverson.com
 * @version 1.0
 * @since Aug 20, 2017
 */
public abstract class MinigameBuilder {
    private MinigamePlayer builder;

    protected String name;
    protected Integer minPlayers, maxPlayers, timeLimit, queueLimit;
    protected Location spawnPoint, spectatorPoint;
    protected ItemStack[] startingItems;
    protected static final int DEFAULT_MIN_PLAYERS=2, DEFAULT_MAX_PLAYERS=16, DEFAULT_TIME_LIMIT=5, DEFAULT_QUEUE_LIMIT=20;


    //Constructors.
    public MinigameBuilder(MinigamePlayer builder) {
        this.builder = builder;
    }

    public MinigamePlayer getBuilder() {
        return builder;
    }


    public abstract MinigameMap createMap();

    /**
     * Updates the variables with their default values if they are not set.
     */
    protected void updateDefaults(){
        if(minPlayers == null) minPlayers = DEFAULT_MIN_PLAYERS;
        if(maxPlayers == null) maxPlayers = DEFAULT_MAX_PLAYERS;
        if(timeLimit == null) timeLimit = DEFAULT_TIME_LIMIT;
        if(queueLimit == null) queueLimit = DEFAULT_QUEUE_LIMIT;
        if(startingItems == null) startingItems = new ItemStack[0];
    }

    /**
     * Gets the current progression of the state of the new minigame.
     *
     * @return A formatted String list of the status of the steps to create the new minigame.
     */
    public String getProgression(){
        StringBuilder sb = new StringBuilder();

        sb.append("New Minigame Progression:\n");
        if(name == null) sb.append(ChatColor.RED + "* Minigame Name [/mg build name MinigameName] (REQUIRED)");
        else sb.append(ChatColor.GREEN + "* Minigame Name [" + name + "]");

        sb.append("\n");

        if(spawnPoint == null) sb.append(ChatColor.RED + "* Minigame Spawn Location [/mg build spawn] (sets to current location) (REQUIRED)");
        else sb.append(ChatColor.GREEN + "* Minigame Spawn Point [" + locationToString(spawnPoint) + "]");

        sb.append("\n");

        if(spectatorPoint == null) sb.append(ChatColor.RED + "* Minigame Spectator Location [/mg build spectate] (sets to current location) (REQUIRED)");
        else sb.append(ChatColor.GREEN + "* Minigame Spectator Location [" + locationToString(spectatorPoint) + "]");

        sb.append("\n");

        if(startingItems == null) sb.append(ChatColor.DARK_RED + "* Minigame Starting Items [/mg build items] (sets to your current inventory)");
        else sb.append(ChatColor.GREEN + "* Minigame Starting Items: " + startingItemsList());

        sb.append("\n");

        if(minPlayers == null) sb.append(ChatColor.DARK_RED + "* Minigame Minimum Players [/mg build minplayers 2] (Default 2)");
        else sb.append(ChatColor.GREEN + "* Minigame Minimum Players [" + minPlayers + "]");

        sb.append("\n");

        if(maxPlayers == null) sb.append(ChatColor.DARK_RED + "* Minigame Maximum Players [/mg build maxplayers 16] (Default 16)");
        else sb.append(ChatColor.GREEN + "* Minigame Maximum Players [" + maxPlayers + "]");

        sb.append("\n");

//        if(timeLimit == null) sb.append(ChatColor.DARK_RED + "* Minigame Time Limit [/mg build timelimit 5] (in minutes) (Default 5 minutes)");
//        else sb.append(ChatColor.GREEN + "* Minigame Time Limit [" + timeLimit + "]");

//        sb.append("\n");

//        if(queueLimit == null) sb.append(ChatColor.DARK_RED + "* Minigame Queue Time Limit [/mg build queuelimit 20] (in seconds) (Default 20 seconds)");
//        else sb.append(ChatColor.GREEN + "* Minigame Queue Time Limit [" + queueLimit + "]");

//        sb.append("\n");

        return sb.toString();
    }

    /**
     * Helper method to format Locations.
     *
     * @param loc The Minecraft Location to format.
     * @return A Formatted Location.
     */
    protected String locationToString(Location loc){
        return String.format("World: %s, X: %s, Y: %s, Z: %s", loc.getWorld().getName(), loc.getX(), loc.getY(), loc.getZ());
    }


    /**
     * Custom Command Handler for Minigame Building.
     * All commands are prefixed with /mg build
     *
     * @param args The argments after /mg build ...
     * @return True if the command processed successfully, false if there were any errors.
     * @throws IllegalArgumentException When the command was not found.
     */
    public boolean handleCommand(String[] args) throws IllegalArgumentException{
        if(args.length < 2) {
            Message.player(builder, "ERROR", "Invalid builder command");
            return false;
        }
        switch(args[1].toLowerCase()){
            case "stop":
                MinigameBuilderStore.getInstance().stopBuilding(builder);
                Message.player(builder, "INFO", "You have left build mode. ");
                break;
            case "status":
                String msg = getProgression();

                msg += "\n\n" + ChatColor.WHITE + ChatColor.BOLD + "To Confirm These Settings and Create the Minigame, type /mg build confirm";
                Message.player(builder, msg);
                break;
            case "confirm":
                if(confirmCreate()){
                    updateDefaults();
                    createMap();
                }
                break;
            case "spawn":
                Location pLoc = builder.getPlayerObject().getLocation();
                Message.player(builder, "Spawn Point set to " + locationToString(pLoc));
                spawnPoint = pLoc.clone();
                break;
            case "spectate":
                pLoc = builder.getPlayerObject().getLocation();
                Message.player(builder, "Spectator Point set to " + locationToString(pLoc));
                spectatorPoint = pLoc.clone();
                break;
            case "name":
                if(args.length < 3) {
                    Message.player(builder, "ERROR", "Minigame Name Requred!");
                    return false;
                }
                String theName = "";
                for(int i=2; i < args.length; i++){
                    theName += args[i];
                    if(i+1 < args.length) theName += "-";
                }
                Message.player(builder, "Set minigame name to " + theName);
                name = theName;

                break;
            case "minplayers":
                if(args.length < 3) {
                    Message.player(builder, "ERROR", "Minigame Minimum Player Count Required!");
                    return false;
                }

                try{
                    int num = Integer.parseInt(args[2]);
                    Message.player(builder, "Set Minimum Player Count to " + num);
                    minPlayers = num;
                }catch(NumberFormatException nfe){
                    Message.player (builder, "ERROR", "Invalid Minimum Player Count: " + args[2]);
                    minPlayers = null;
                    return false;
                }
                break;
            case "maxplayers":
                if(args.length < 3) {
                    Message.player(builder, "ERROR", "Minigame Maximum Player Count Required!");
                    return false;
                }

                try{
                    int num = Integer.parseInt(args[2]);
                    Message.player(builder, "Set Maximum Player Count to " + num);
                    maxPlayers = num;
                }catch(NumberFormatException nfe){
                    Message.player (builder, "ERROR", "Invalid Maximum Player Count: " + args[2]);
                    maxPlayers = null;
                    return false;
                }
                break;
            case "timelimit":
                if(args.length < 3) {
                    Message.player(builder, "ERROR", "Minigame Time Limit Required!");
                    return false;
                }

                try{
                    int num = Integer.parseInt(args[2]);
                    Message.player(builder, "Set Time Limit to " + num);
                    timeLimit = num;
                }catch(NumberFormatException nfe){
                    Message.player (builder, "ERROR", "Invalid Time Limit: " + args[2]);
                    timeLimit = null;
                    return false;
                }
                break;
            case "queuelimit":
                if(args.length < 3) {
                    Message.player(builder, "ERROR", "Minigame Queue Time Limit Required!");
                    return false;
                }

                try{
                    int num = Integer.parseInt(args[2]);
                    Message.player(builder, "Set Queue Time Limit to " + num);
                    queueLimit = num;
                }catch(NumberFormatException nfe){
                    Message.player (builder, "ERROR", "Invalid Queue Time Limit: " + args[2]);
                    queueLimit = null;
                    return false;
                }
                break;
            case "items":
                startingItems = builder.getPlayerObject().getInventory().getContents();

                Message.player(builder, "Starting Items Set to " + startingItemsList());
                break;
            default:
                throw new IllegalArgumentException("MG Build Command Not Found: " + args[1]);
        }

        return true;
    }

    /**
     * Confirms that all required fields are set before being able to create the minigame.
     *
     * @return True if all required fields are filled, False otherwise.
     */
    protected boolean confirmCreate(){
        if(name == null){
            Message.player(builder, "ERROR", "You Must Supply a Name for the Minigame. [/mg build name MinigameName]");
            return false;
        }

        if(spawnPoint == null){
            Message.player(builder, "ERROR", "You Must Supply a Spawn Point for the Minigame. [/mg build spawn]");
            return false;
        }

        if(spectatorPoint == null){
            Message.player(builder, "ERROR", "You Must Supply a Spectator Point for the Minigame. [/mg build spectate]");
            return false;
        }

        return true;
    }

    /**
     * Helper method to get the List of Starting Items and the number of each.
     * @return A List of Starting Items in a String representation.
     */
    private String startingItemsList(){
        StringBuilder sb = new StringBuilder();
        for(ItemStack i : startingItems){
            if(i != null)
                sb.append(i.getType()).append(" [").append(i.getAmount()).append("], ");
        }

        String out = sb.toString();
        return out.substring(0,out.length()-2); //remove comma from last entry
    }

    /**
     * Called when the MingameBuilder stops building a new minigame.
     */
    public void stopBuilding(){
        MinigameBuilderStore.getInstance().stopBuilding(getBuilder());
    }
}
