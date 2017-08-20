package me.chandl.cfminigame.minigames.race.builder;

import me.chandl.cfminigame.database.CheckpointConfig;
import me.chandl.cfminigame.database.MapConfig;
import me.chandl.cfminigame.minigame.core.MinigameMap;
import me.chandl.cfminigame.minigame.core.MinigameType;
import me.chandl.cfminigame.minigame.player.MinigamePlayer;
import me.chandl.cfminigame.minigame.builder.MinigameBuilder;
import me.chandl.cfminigame.minigames.race.checkpoint.Checkpoint;
import me.chandl.cfminigame.minigames.race.RaceMap;
import me.chandl.cfminigame.util.Message;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;


public class RaceBuilder extends MinigameBuilder {
    private Queue<Checkpoint> points = null;
    private Integer maxLives;
    private static final int DEFAULT_MAX_LIVES = 5;
    private static final Material DEFAULT_CHECKPOINT_MATERIAL = Material.GLASS;

    public RaceBuilder(MinigamePlayer builder) {
        super(builder);

    }

    @Override
    public MinigameMap createMap() {

        if(maxLives == null) maxLives = DEFAULT_MAX_LIVES;
        if(startingItems.length == 0){
            startingItems = new ItemStack[1];
            startingItems[0] = new ItemStack(Material.ELYTRA);
        }

        List<Checkpoint> pts = new LinkedList<>(points);

        RaceMap map = new RaceMap(name, maxLives, spawnPoint, spectatorPoint, new Long(timeLimit), 1, startingItems, pts);
        FileConfiguration conf = MapConfig.createMap(MinigameType.ELYTRARACE, name, map);
        conf.set("checkpoints", pts);
        MapConfig.saveMapFile();

        Message.player(getBuilder(), "New Map Created: " + name);
        System.out.println("CFMinigame. New Map Created: " + map + ". Created by " + getBuilder().getPlayerObject().getName());
        stopBuilding();

        return null;
    }

    @Override
    public void stopBuilding() {
        if(points!= null){
            for(Checkpoint point : points){
                point.despawn();
            }
        }
        super.stopBuilding();
    }

    @Override
    public String getProgression() {
        StringBuilder sb = new StringBuilder(super.getProgression());

        int pointCount = (points == null)? 0 : points.size();
        if(pointCount == 0) sb.append(ChatColor.RED + "* Minigame Checkpoints [/mg build point] (creates a new point) (REQUIRED)");
        else sb.append(ChatColor.GREEN + "* Minigame Checkpoint Count: " + pointCount);

        sb.append("\n");

        if(maxLives == null) sb.append(ChatColor.DARK_RED + "* Minigame Maximum Life Count [/mg build maxlives 5] (Default: 5)");
        else sb.append(ChatColor.GREEN + "* Minigame Maximum Life Count: " + maxLives);

        sb.append("\n");

        return sb.toString();
    }

    @Override
    public boolean handleCommand(String[] args) throws IllegalArgumentException{
        try {
            super.handleCommand(args);
            return true;
        }catch(IllegalArgumentException ie){
            switch(args[1]){
                case "point":
                    if(points == null) points = new LinkedList<>();

                    Location here = getBuilder().getPlayerObject().getLocation();
                    String cp = CheckpointConfig.loadPoint(MinigameType.ELYTRARACE, 1);

                    Checkpoint point = new Checkpoint(cp, here, here.getYaw());
                    Material mat = getBuilder().getPlayerObject().getInventory().getItemInMainHand().getType();
                    if(mat == Material.AIR) mat  = DEFAULT_CHECKPOINT_MATERIAL;
                    point.setMaterial(mat);
                    point.spawn();

                    points.add(point);
                    break;

                case "maxlives":
                    if(args.length < 2){
                        Message.player(getBuilder(), "ERROR", "You must supply the number of maximum lives. [e.g. /mg build maxlives 5]");
                        break;
                    }

                    try{
                        int lives = Integer.parseInt(args[2]);
                        maxLives = lives;
                        Message.player(getBuilder(), "Maximum Lives set to " + maxLives);
                    }catch(NumberFormatException nfe) {
                        Message.player(getBuilder(), "ERROR", "Invalid count of maximum lives. Expecting an integer amount.");
                        break;
                    }

                    break;

            }
        }

        return true;
    }

    @Override
    protected boolean confirmCreate() {
        if (!super.confirmCreate()) return false;

        if(points == null || points.size() == 0){
            Message.player(getBuilder(), "ERROR", "You Must Supply Checkpoints for the Minigame. [/mg build point] to create one.");
            return false;
        }

        return true;
    }
}
