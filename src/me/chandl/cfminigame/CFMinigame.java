package me.chandl.cfminigame;


import org.bukkit.plugin.java.JavaPlugin;
import java.util.logging.Logger;

public class CFMinigame extends JavaPlugin{
    public static final int DEFAULT_MAX_PLAYERS = 16;
    public static final int DEFAULT_MIN_PLAYERS = 2;
    public static final long DEFAULT_MAX_QUEUE_TIME = 1;
    private Logger log;


    @Override
    public void onEnable() {
        log = getLogger();
        log.info("onEnable called.");

        //Handle Commands
        this.getCommand("mg").setExecutor(new CommandHandler());

        GameHandler.setCurrentMinigame(null);
    }


    @Override
    public void onDisable() {
        log.info("OnDisable called.");
    }


}
