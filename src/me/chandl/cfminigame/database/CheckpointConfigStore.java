package me.chandl.cfminigame.database;

import me.chandl.cfminigame.minigame.core.MinigameType;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * Store of Checkpoint Configurations.
 *
 * @author Chandler me@cseverson.com
 * @version 1.0
 * @since Aug 20, 2017
 */
public class CheckpointConfigStore {
    private static File checkpointFile;
    private static Map<File, String> checkpointStore;

    /**
     * Loads a Checkpoint file and returns a String representation from a specified Minigame
     * Type and difficulty level. Keeps a store of Checkpoint Strings.
     *
     * @param type The type of the minigame.
     * @param difficulty The difficulty level of the minigame.
     * @return A String representation of a Checkpoint.
     */
    public static String loadPoint(MinigameType type, int difficulty){
        if(checkpointStore == null) checkpointStore = new HashMap<>();

        checkpointFile = new File("plugins/CFMinigame/checkpoints/" + type.toString()  + "-" + difficulty + ".txt");

        if(!checkpointFile.exists()){
            return null;
        }else{
            if(checkpointStore.containsKey(checkpointFile)){
                return checkpointStore.get(checkpointFile);
            }else{
                return readCheckpointFile(checkpointFile);
            }
        }
    }

    /**
     * Method to create a checkpoint file. Rarely used.
     * @deprecated
     */
    public static void createCheckpointFile( File pointFile , String pattern){

        try {
            pointFile.getParentFile().mkdirs();
            pointFile.createNewFile();
            FileWriter fw = new FileWriter(pointFile);
            fw.write(pattern);
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Reads a checkpoint file and return a String.
     *
     * @param pointFile The file that the Checkpoint is stored in.
     * @return The String representation of a Checkpoint.
     */
    private static String readCheckpointFile(File pointFile){
        try {
            String ln = new String(Files.readAllBytes(Paths.get(pointFile.toURI())));
            checkpointStore.put(pointFile, ln);
            return ln;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


}
