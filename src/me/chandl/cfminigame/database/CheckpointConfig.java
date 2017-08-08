package me.chandl.cfminigame.database;

import me.chandl.cfminigame.minigame.core.MinigameType;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;


public class CheckpointConfig {
    private static File checkpointFile;
    private static HashMap<File, String> checkpointConfigCache;

    public static String loadPoint(MinigameType type, int difficulty){
        if(checkpointConfigCache == null) checkpointConfigCache = new HashMap<>();


        checkpointFile = new File("plugins/CFMinigame/checkpoints/" + type.toString()  + "-" + difficulty + ".txt");

        if(!checkpointFile.exists()){
            return null;
        }else{
            if(checkpointConfigCache.containsKey(checkpointFile)){
                return checkpointConfigCache.get(checkpointFile);
            }else{
                return readCheckpointFile(checkpointFile);
            }
        }
    }

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

    private static String readCheckpointFile(File pointFile){
        System.out.println("Reading Checkpoint File " + pointFile);
        try {
            String ln = new String(Files.readAllBytes(Paths.get(pointFile.toURI())));
            checkpointConfigCache.put(pointFile, ln);
            return ln;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


}
