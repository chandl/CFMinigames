package me.chandl.cfminigame.minigame.checkpoint;


import org.bukkit.Location;
import org.bukkit.Material;

import java.io.Serializable;
import java.util.*;


import static me.chandl.cfminigame.minigame.checkpoint.CheckpointArea.*;

public class Checkpoint implements Serializable{
    //Shape will be the shape of the checkpoint
    // Read in dynamically
    private transient CheckpointArea[][] shape;
    private transient Location spawnPoint;
    private int spawnPointX, spawnPointY, spawnPointZ;
    private transient HashMap<Location, CheckpointArea> pointLocations;
    private transient HashMap<Location, Material> oldBlocks;
    private transient HashSet<Location> hitbox;
    private String direction;


    @Override
    public String toString() {
        return "Checkpoint{" +
                "spawnPoint=" + spawnPoint +
                ", pointLocations=" + pointLocations +
                ", oldBlocks=" + oldBlocks +
                ", hitbox=" + hitbox +
                ", direction='" + direction + '\'' +
                '}';
    }

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
    public Checkpoint(String shape, Location location, float yaw){
        this.spawnPoint = new Location(location.getWorld(), location.getX(), location.getY(), location.getZ());
        this.direction = getDirectionFromYaw(yaw);
        this.shape = readShape(shape);
        this.hitbox = new HashSet<>();

//        System.out.println("Direction " + direction + ". Rot: " + rot);

    }

    public String getDirectionFromYaw(float yaw){
        String direction;
        double rot = (yaw - 90) % 360;
        if (rot < 0) {
            rot += 360.0;
        }
        if (0 <= rot && rot < 45) {
            direction = "W";

        } else if (45 <= rot && rot < 135) {
            direction = "N";

        } else if (135 <= rot && rot < 225) {
            direction = "E";

        } else if (225 <= rot && rot < 315) {
            direction = "S";

        } else if (315 <= rot && rot < 360.0) {
            direction = "W";
        } else {
            System.out.println("New Checkpoint - couldn't tell which direction player is facing. Defaulting to N");
            direction = "N";
        }

        return direction;
    }

    public CheckpointArea[][] readShape(String shape){
        String[] lines = shape.split("\n");

        pointLocations = new HashMap<>();

        CheckpointArea[][] out = new CheckpointArea[lines.length][lines[0].length()];
//        pointLocations = new Location[lines.length * lines[0].length()];

        Location spTmp = spawnPoint.clone();

        System.out.println("LOCATION: " + spawnPoint + spawnPoint.getDirection());
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

                switch(direction){
                    case "E":
                    case "W":
                        spTmp.add(0,0,1);
                        break;
                    case "N":
                    case "S":
                        spTmp.add(1,0,0);
                    default:
                        break;
                }

            }

            switch(direction){
                case "E":
                case "W":
                    spTmp.subtract(0, 1 ,lineLen);
                    break;
                case "N":
                case "S":
                    spTmp.subtract(lineLen, 1 ,0);
                default:
                    break;
            }


        }

        return out;
    }



    /**
     * Spawns the checkpoint object in the game.
     */
    public void spawn(Material spawnMaterial){

        if(oldBlocks == null) oldBlocks = new HashMap<>();

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
            oldBlocks.put(l, l.getBlock().getType());

            switch((CheckpointArea)pair.getValue()){
                case HITBOX:
                    l.getBlock().setType(Material.WEB);
                    hitbox.add(l);
                    break;
                case AIR:
                    l.getBlock().setType(Material.AIR);
                    break;

                case BLOCK:
                    l.getBlock().setType(spawnMaterial);
                    break;
            }
        }
    }

    public void despawn(){
        System.out.println("Despawn Called");
        if(oldBlocks == null || oldBlocks.size() == 0){
            System.out.println("olDBlocks not cound. Cannot despawn Checkpoints.");
            return ;
        }

        Iterator it = oldBlocks.entrySet().iterator();
        while(it.hasNext()){
            Map.Entry pair = (Map.Entry)it.next();
            Location l = (Location) pair.getKey();
            Material m = (Material) pair.getValue();

            l.getBlock().setType(m);
        }

        oldBlocks.clear();
        oldBlocks = null;
    }

    public HashMap<Location, Material> getOldBlocks() {
        return oldBlocks;
    }

    public HashSet<Location> getHitbox() {
        return hitbox;
    }
}
