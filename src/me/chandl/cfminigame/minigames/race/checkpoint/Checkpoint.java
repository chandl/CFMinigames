package me.chandl.cfminigame.minigames.race.checkpoint;


import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.io.Serializable;
import java.util.*;


import static me.chandl.cfminigame.minigames.race.checkpoint.CheckpointArea.*;

/**
 * Class to represent a Checkpoint for a Race.
 *
 * @author Chandler me@cseverson.com
 * @version 1.0
 * @since Aug 20, 2017
 */
public class Checkpoint implements Serializable, ConfigurationSerializable {

    //This value will determine when you hit the checkpoint hitbox.
    // e.g. 2 means that you will hit the hitbox when 2 blocks in front or behind of the point.
    private static final double CHECKPOINT_HITBOX_EXTENSION = 2;
    //Shape will be the shape of the checkpoint
    // Read in dynamically
    private transient CheckpointArea[][] shape;
    private transient Location spawnPoint;
    private transient HashMap<Location, CheckpointArea> pointLocations;
    private transient HashMap<Location, Material> oldBlocks;
    private transient HashSet<Location> hitbox;

    public double hitboxMinX,hitboxMinY,hitboxMinZ,hitboxMaxX,hitboxMaxY,hitboxMaxZ;
    private String direction;
    private Material material;


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

    public Checkpoint(Map<String, Object> map){
        double x,y,z;
        x = (Double) map.get("x");
        y = (Double) map.get("y");
        z = (Double) map.get("z");
        String w = (String) map.get("world");
        World world = Bukkit.getWorld(w);
        spawnPoint = new Location(world, x,y,z,0,0);
        direction = (String) map.get("direction");
//        material = Material.GLASS;
        material = Material.getMaterial((String)map.get("material"));
        hitbox = new HashSet<>();
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

    public void setShape(String shape){
        this.shape = readShape(shape);
    }

    private CheckpointArea[][] readShape(String shape){
        String[] lines = shape.split("\n");

        pointLocations = new HashMap<>();

        CheckpointArea[][] out = new CheckpointArea[lines.length][lines[0].length()];
//        pointLocations = new Location[lines.length * lines[0].length()];

        Location spTmp = spawnPoint.clone();

        int xSize = lines.length;
        int ySize = lines[0].length();

        // Move checkpoint to the center of the player
        switch(direction){
            case "E":
            case "W":
                if(xSize%2 != 0){ //Shift X-Value
                    spTmp.subtract(0, 0, (xSize +1 ) / 2);
//                    System.out.println(String.format("Subtracting %d to Z", (xSize+1)/2));
                }else{
                    spTmp.subtract(0, 0, xSize / 2);
//                    System.out.println(String.format("Subtracting %d to Z", (xSize/2)));
                }
                break;
            case "N":
            case "S":
                if(xSize%2 != 0){ //Shift X-Value
                    spTmp.subtract((xSize +1 ) / 2, 0, 0);
//                    System.out.println(String.format("Subtracting %d to X", (xSize+1)/2));
                }else{
                    spTmp.subtract(xSize / 2, 0, 0);
//                    System.out.println(String.format("Subtracting %d to X", (xSize/2)));
                }
                break;
        }


        //Shift the Y-Value
        if(ySize % 2 != 0){
            spTmp.add( 0, (ySize + 1) / 2, 0);
//            System.out.println(String.format("Adding %d to Y", (ySize+1)/2));
        }else{
            spTmp.add(0,ySize/2,0);
//            System.out.println(String.format("Adding %d to Y", (ySize/2)));
        }

//        System.out.println("LOCATION: " + spawnPoint + spawnPoint.getDirection());
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
     * Reads the Hitbox, generating a minimum and maximum set of X,Y,Z Coordinates.
     */
    private void readHitbox(){
        double minX = Double.MAX_VALUE, maxX=-Double.MAX_VALUE, minY=Double.MAX_VALUE,maxY=-Double.MAX_VALUE, minZ=Double.MAX_VALUE, maxZ=-Double.MAX_VALUE;
        for(Location loc : hitbox){

            double x = loc.getX(), y = loc.getY(), z = loc.getZ();
//            System.out.println(String.format("(%f,%f,%f) | (%f,%f,%f) | (%f, %f, %f)",x,y,z,minX,minY,minZ,maxX,maxY,maxZ));
//            System.out.println("X " + x + ". MaxX: " + maxX);
            if(x < minX){
                minX = x;
//                System.out.println("New Min X: " + minX);
            }
            if(x > maxX) {
                maxX = x;
//                System.out.println("New Max X: " + maxX);
            }

            if(y < minY) {
                minY = y;
//                System.out.println("New Min Y: " + minY);
            }
            if(y > maxY) {
                maxY = y;
//                System.out.println("New Max Y: " + maxY);
            }

            if(z < minZ) {
                minZ = z;
//                System.out.println("New Min Z: " + minZ);
            }
            if(z > maxZ) {
                maxZ = z;
//                System.out.println("New Max Z: " + maxZ);
            }
        }



        this.hitboxMaxX = Math.round(maxX);
        this.hitboxMaxY = Math.round(maxY);
        this.hitboxMaxZ = Math.round(maxZ);

        this.hitboxMinX = Math.round(minX);
        this.hitboxMinY = Math.round(minY);
        this.hitboxMinZ = Math.round(minZ);

        if(direction.equals("N") || direction.equals("S")){
            if(hitboxMaxZ < 0){
                hitboxMaxZ -= CHECKPOINT_HITBOX_EXTENSION;
            }else{
                hitboxMaxZ += CHECKPOINT_HITBOX_EXTENSION;
            }

            if(hitboxMinZ < 0){
                hitboxMinZ += CHECKPOINT_HITBOX_EXTENSION;
            }else{
                hitboxMinZ -=CHECKPOINT_HITBOX_EXTENSION;
            }

        }else{
            if(hitboxMaxX < 0){
                hitboxMaxX -= CHECKPOINT_HITBOX_EXTENSION;
            }else{
                hitboxMaxX += CHECKPOINT_HITBOX_EXTENSION;
            }

            if(hitboxMinX < 0 ){
                hitboxMinX += CHECKPOINT_HITBOX_EXTENSION;
            }else{
                hitboxMinX -= CHECKPOINT_HITBOX_EXTENSION;
            }
        }

//        System.out.println(String.format("Max X: %f, Max Y: %f, Max Z: %f. Min X: %f, Min Y: %f, Min Z: %f", hitboxMaxX, hitboxMaxY, hitboxMaxZ, hitboxMinX, hitboxMinY, hitboxMinZ));
    }

    /**
     * Spawns the checkpoint object in the game.
     */
    public void spawn(){

        if(oldBlocks == null) oldBlocks = new HashMap<>();

//        System.out.println("Spawn called!");
        if(pointLocations == null || pointLocations.size() == 0) {
            System.out.println("pointLocations not found. Can't spawn Checkpoint");
            return ;
        }

//        System.out.println("Spawn Locations");
        Iterator it = pointLocations.entrySet().iterator();
        while(it.hasNext()){
            Map.Entry pair = (Map.Entry)it.next();
            Location l = (Location) pair.getKey();
            oldBlocks.put(l, l.getBlock().getType());

            switch((CheckpointArea)pair.getValue()){
                case HITBOX:
                    l.getBlock().setType(Material.AIR);
                    hitbox.add(l);
                    break;
                case AIR:
                    l.getBlock().setType(Material.AIR);
                    break;

                case BLOCK:
                    l.getBlock().setType(material);
                    break;
            }
        }

        //Read Hitbox Locations - Find Min through Max Locations
        readHitbox();
    }

    public void despawn(){
//        System.out.println("Despawn Called");
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

    public Location getSpawnPoint() {
        return spawnPoint;
    }

    public String getDirection() {
        return direction;
    }

    @Override
    public Map<String, Object> serialize() {
        HashMap<String, Object> out = new HashMap<>();
//        out.put("location", spawnPoint);
        out.put("world", spawnPoint.getWorld().getName());
        out.put("x", spawnPoint.getX());
        out.put("y", spawnPoint.getY());
        out.put("z", spawnPoint.getZ());
        out.put("direction", direction);
        out.put("material", material.toString());
//        out.put("material", material.toString());
        return out;
    }

//    //Used for configuration deserialization.
//    public static Checkpoint deserialize(HashMap<String, Object> map){
//
//        Location location = map.containsKey("location")? (Location)map.get("location"): null;
//        String direction = map.containsKey("direction")? (String)map.get("direction"): null;
//
//        System.out.println("Deserialize called. Location: " + location + ". Direction + " + direction);
////        Material mat = map.containsKey("material")? (Material) map.get("material"): null;
//        return new Checkpoint(location, direction);
//    }

    public void setMaterial(Material material) {
        this.material = material;
    }
}



//Checkpoint Hitbox Boundaries:
//For each Checkpoint - find Min X,Y,Z
//IN Logic, Check if player is between min and max