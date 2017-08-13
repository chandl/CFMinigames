package me.chandl.cfminigame;


import me.chandl.cfminigame.minigame.checkpoint.Checkpoint;
import me.chandl.cfminigame.minigame.checkpoint.RaceListener;
import me.chandl.cfminigame.minigame.core.MinigameState;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.logging.Logger;

public class CFMinigame extends JavaPlugin{
    public static final int DEFAULT_MAX_PLAYERS = 16;
    public static final int DEFAULT_MIN_PLAYERS = 2;
    public static final int DEFAULT_MIN_DIFFICULTY = 1;
    public static final int DEFAULT_MAX_DIFFICULTY = 5;

    //Maximum Queuing Time (Seconds)
    public static final long DEFAULT_MAX_QUEUE_TIME = 20;
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

        ConfigurationSerialization.registerClass(Checkpoint.class);
    }

    public void registerRaceHandler(){
        getServer().getPluginManager().registerEvents(RaceListener.getListener(), this);
    }

    public void unregisterRaceHandler(){
        HandlerList.unregisterAll(RaceListener.getListener());
    }


    @Override
    public void onDisable() {
        if(GameHandler.getHandler().getCurrentState() != MinigameState.NO_GAME){
            GameHandler.getHandler().stopMinigame();
        }
        log.info("OnDisable called.");
    }


}
