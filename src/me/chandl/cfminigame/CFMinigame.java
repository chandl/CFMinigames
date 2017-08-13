package me.chandl.cfminigame;


import me.chandl.cfminigame.minigame.checkpoint.Checkpoint;
import me.chandl.cfminigame.minigame.checkpoint.CollisionListener;
import me.chandl.cfminigame.minigame.core.MinigameState;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.logging.Logger;

public class CFMinigame extends JavaPlugin{
    public static final int DEFAULT_MAX_PLAYERS = 16;
    public static final int DEFAULT_MIN_PLAYERS = 2;
    public static final int DEFAULT_MIN_DIFFICULTY = 1;
    public static final int DEFAULT_MAX_DIFFICULTY = 5;

    //Maximum Queuing Time (Seconds)
    public static final long DEFAULT_MAX_QUEUE_TIME = 5;
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
        getServer().getPluginManager().registerEvents(CollisionListener.getListener(), this);
        ConfigurationSerialization.registerClass(Checkpoint.class);
    }


    @Override
    public void onDisable() {
        if(GameHandler.getHandler().getCurrentState() != MinigameState.NO_GAME){
            GameHandler.getHandler().stopMinigame();
        }
        log.info("OnDisable called.");
    }


}
