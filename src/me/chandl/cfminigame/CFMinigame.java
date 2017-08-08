package me.chandl.cfminigame;


import me.chandl.cfminigame.minigame.core.MinigameState;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.logging.Logger;

public class CFMinigame extends JavaPlugin{
    public static final int DEFAULT_MAX_PLAYERS = 16;
    public static final int DEFAULT_MIN_PLAYERS = 2;
    public static final int DEFAULT_MIN_DIFFICULTY = 1;
    public static final int DEFAULT_MAX_DIFFICULTY = 5;

    //Maximum Queuing Time (Seconds)
    public static final long DEFAULT_MAX_QUEUE_TIME = 60;
    private Logger log;
    public static CFMinigame plugin;


    @Override
    public void onEnable() {
        this.plugin = this;
        log = getLogger();
        log.info("onEnable called.");

        //Handle Commands
        this.getCommand("mg").setExecutor(new CommandHandler());
        this.getCommand("mg").setTabCompleter(new TabCompleteHandler());

        GameHandler.getHandler().setCurrentMinigame(null);
        GameHandler.getHandler().setCurrentState(MinigameState.NO_GAME);
    }


    @Override
    public void onDisable() {
        log.info("OnDisable called.");
    }


}
