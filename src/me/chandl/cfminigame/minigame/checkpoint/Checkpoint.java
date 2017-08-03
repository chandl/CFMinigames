package me.chandl.cfminigame.minigame.checkpoint;


import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


import static me.chandl.cfminigame.minigame.checkpoint.CheckpointArea.*;

public class Checkpoint {
    //Shape will be the shape of the checkpoint
    // Read in dynamically
    private CheckpointArea[][] shape;
    private Location spawnPoint;
    private HashMap<Location, CheckpointArea> pointLocations;
    private World world;


    /**
     * Creates a Checkpoint
     *
     * How To Make the Checkpoint Shape in Text:
     * Shape must be in a perfect square/rectangle shape (i.e. lines must be same number of characters long)
     * X = BLOCK
     * O = AIR
     * Y = Checkpoint HITBOX
     * e.g. Level 4 Rings:
     *          OOXXXOX
     *          OXYYYXO
     *          XYYYYYX
     *          XYYYYYX
     *          XYYYYYX
     *          OXYYYXO
     *          OOXXXOO
     *          XOOOOOO
     *
     *
     *
     * @param shape A text representation of the 2D checkpoint
     * @param location
     */
    public Checkpoint(String shape, Location location){
        this.spawnPoint = new Location(location.getWorld(), location.getX(), location.getY(), location.getZ());
        this.shape = readShape(shape);

        this.world = location.getWorld();
    }

    public CheckpointArea[][] readShape(String shape){
        String[] lines = shape.split("\n");

        pointLocations = new HashMap<>();

        CheckpointArea[][] out = new CheckpointArea[lines.length][lines[0].length()];
//        pointLocations = new Location[lines.length * lines[0].length()];

        Location spTmp = spawnPoint.clone();

        System.out.println("LOCATION: " + spawnPoint);
        int i = 0;
        for(String line : lines){
            int lineLen = line.length();
            for(int j = 0; j<lineLen; j++){

                switch(line.charAt(j)+"".toUpperCase()){
                    case "O":
                        out[i][j] = AIR;
                        break;
                    case "Y":
                        out[i][j] = HITBOX;
                        break;
                    case "X":
                        out[i][j] = BLOCK;
                        break;
                    default:
                        System.out.println("Error Loading map... Could not read shape! (Invalid Character) " + line.charAt(j));
                        break;
                }
                pointLocations.put(spTmp.clone(), out[i][j]);

                spTmp.add(1,0,0);
            }
            spTmp.subtract(lineLen, 1 ,0);

        }

        return out;
    }



    /**
     * Spawns the checkpoint object in the game.
     */
    public void spawn(Material spawnMaterial){


        System.out.println("Spawn called!");
        if(pointLocations == null || pointLocations.size() == 0) {
            System.out.println("pointLocations not found. Can't spawn Checkpoint");
            return ;
        }

        System.out.println("Spawn Locations");
        Iterator it = pointLocations.entrySet().iterator();
        while(it.hasNext()){
            Map.Entry pair = (Map.Entry)it.next();
            Location l = (Location) pair.getKey();

            switch((CheckpointArea)pair.getValue()){
                case HITBOX:

                case AIR:


                    l.getBlock().setType(Material.AIR);
                    break;

                case BLOCK:
                    l.getBlock().setType(spawnMaterial);
                    break;
            }
        }


    }



}
