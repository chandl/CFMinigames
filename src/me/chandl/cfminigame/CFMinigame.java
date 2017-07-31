package me.chandl.cfminigame;


import me.chandl.cfminigame.database.DatabaseConnector;
import me.chandl.cfminigame.minigame.Minigame;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;
import java.util.logging.Logger;

public class CFMinigame extends JavaPlugin{

    private DatabaseConnector connector;
    private Logger log;
    private static Minigame currentMinigame;

    @Override
    public void onEnable() {
        log = getLogger();
        log.info("onEnable called.");


        //Connect to database
        connector =  DatabaseConnector.getConnector("127.0.0.1", "cfMinigame", "root", "$tream1T", 3306);
        try {
            log.info("Opening Database Connection.");
            connector.openConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        //Handle Commands
        this.getCommand("mg").setExecutor(new CommandHandler());


        currentMinigame = null;
    }


    @Override
    public void onDisable() {
        log.info("OnDisable called.");
    }




    public static Minigame getCurrentMinigame(){
        return currentMinigame;
    }

    public static void setCurrentMinigame(Minigame minigame){
        currentMinigame = minigame;
    }

}
