package me.chandl.cfminigame.minigame.builder;


import me.chandl.cfminigame.minigame.core.MinigameMap;
import me.chandl.cfminigame.minigame.player.MinigamePlayer;
import me.chandl.cfminigame.util.Message;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

public abstract class MinigameBuilder {
    private MinigamePlayer builder;

    private String name;
    private Integer minPlayers, maxPlayers, timeLimit, queueLimit;
    private Location spawnPoint, spectatorPoint;
    private ItemStack[] startingItems;

    //State Progression

    //  1) Set name of mg
    //  2) Set Spawnpoint of MG
    //  3) Set Minimum Players
    //  4) Set Maximum Players
    //  5) Set Time Limit
    //  6) Set Queue Time Limit
    //  7) Minigame-Specific Additions (e.g. Checkpoints)
    //  8) Confirm

    public MinigameBuilder(MinigamePlayer builder) {
        this.builder = builder;
    }

    public MinigamePlayer getBuilder() {
        return builder;
    }


    public String getProgression(){
        StringBuilder sb = new StringBuilder();


        sb.append("New Minigame Progression:\n");
        if(name == null) sb.append(ChatColor.DARK_RED + "* Minigame Name [/mg build name MinigameName]");
        else sb.append(ChatColor.GREEN + "* Minigame Name [" + name + "]");

        sb.append("\n");

        if(minPlayers == null) sb.append(ChatColor.DARK_RED + "* Minigame Minimum Players [/mg build minplayers 2]");
        else sb.append(ChatColor.GREEN + "* Minigame Minimum Players [" + minPlayers + "]");

        sb.append("\n");

        if(maxPlayers == null) sb.append(ChatColor.DARK_RED + "* Minigame Maximum Players [/mg build maxplayers 16]");
        else sb.append(ChatColor.GREEN + "* Minigame Maximum Players [" + maxPlayers + "]");

        sb.append("\n");

        if(timeLimit == null) sb.append(ChatColor.DARK_RED + "* Minigame Time Limit [/mg build timelimit 5] (in minutes)");
        else sb.append(ChatColor.GREEN + "* Minigame Time Limit [" + timeLimit + "]");

        sb.append("\n");

        if(queueLimit == null) sb.append(ChatColor.DARK_RED + "* Minigame Queue Time Limit [/mg build queuelimit 1] (in minutes)");
        else sb.append(ChatColor.GREEN + "* Minigame Queue Time Limit [" + queueLimit + "]");

        sb.append("\n");

        if(spawnPoint == null) sb.append(ChatColor.DARK_RED + "* Minigame Spawn Location [/mg build spawn] (sets to current location)");
        else sb.append(ChatColor.GREEN + "* Minigame Spawn Point [" + locationToString(spawnPoint) + "]");

        sb.append("\n");

        if(spectatorPoint == null) sb.append(ChatColor.DARK_RED + "* Minigame Spectator Location [/mg build spectate] (sets to current location)");
        else sb.append(ChatColor.GREEN + "* Minigame Spectator Location [" + locationToString(spectatorPoint) + "]");

        sb.append("\n");

        if(startingItems == null) sb.append(ChatColor.DARK_RED + "* Minigame Starting Items [/mg build items] (sets to your current inventory)");
        else sb.append(ChatColor.GREEN + "* Minigame Starting Items: " + startingItemsList());

        sb.append("\n");

        return sb.toString();
    }

    private String locationToString(Location loc){
        return String.format("World: %s, X: %s, Y: %s, Z: %s", loc.getWorld().getName(), loc.getX(), loc.getY(), loc.getZ());
    }

    public abstract MinigameMap createMap();

    public boolean handleCommand(String[] args) throws IllegalArgumentException{
        if(args.length < 2) {
            Message.player(builder, "ERROR", "Invalid builder command");
            return false;
        }
        switch(args[1]){
            case "status":
                String msg = getProgression();

                msg += "\n\n" + ChatColor.WHITE + ChatColor.BOLD + "To Confirm These Settings and Create the Minigame, type /mg build confirm";
                Message.player(builder, msg);
                break;
            case "confirm":
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

                createMap();
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

    private String startingItemsList(){
        StringBuilder sb = new StringBuilder();
        for(ItemStack i : startingItems){
            if(i != null)
                sb.append(i.getType()).append(" [").append(i.getAmount()).append("], ");
        }

        String out = sb.toString();
        return out.substring(0,out.length()-2); //remove comma from last entry
    }
}