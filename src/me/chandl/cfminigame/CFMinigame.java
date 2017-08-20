package me.chandl.cfminigame;


import me.chandl.cfminigame.handler.CommandHandler;
import me.chandl.cfminigame.handler.GameHandler;
import me.chandl.cfminigame.handler.TabCompleteHandler;
import me.chandl.cfminigame.minigame.builder.MinigameBuilder;
import me.chandl.cfminigame.minigame.builder.MinigameBuilders;
import me.chandl.cfminigame.minigames.race.checkpoint.Checkpoint;
import me.chandl.cfminigame.minigame.core.MinigameListener;
import me.chandl.cfminigame.minigame.core.MinigameState;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.logging.Logger;

/**
 * CFMinigame - A Minecraft Minigame System
 *
 *
 * @author Chandler me@cseverson.com
 * @version 1.0
 * @since Aug 19, 2017
 *
 */
public class CFMinigame extends JavaPlugin{

    //Constants for the Minigame System.
    public static final int DEFAULT_MAX_PLAYERS = 16;
    public static final int DEFAULT_MIN_PLAYERS = 2;

    //Difficulty Levels
    public static final int DEFAULT_MIN_DIFFICULTY = 1;
    public static final int DEFAULT_MAX_DIFFICULTY = 5;

    //Maximum Queuing Time (Seconds)
    public static final long DEFAULT_MAX_QUEUE_TIME = 30;
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

    /**
     * Registers a Custom Listener with the Plugin Manager.
     * Usually called by a Minigame in its {@code start()} method.
     *
     * @param listener the {@link MinigameListener} that is used in conjunction with a specific {@link me.chandl.cfminigame.minigame.core.MinigameType}.
     */
    public void registerListener(MinigameListener listener){
        getServer().getPluginManager().registerEvents(listener, this);
    }

    /**
     * Unregisters a Custom Listener.
     * Usually called by a Minigame in its {@code stop()} method.
     *
     * @param listener the {@link MinigameListener} that is used in conjunction with a specific {@link me.chandl.cfminigame.minigame.core.MinigameType}
     */
    public void unregisterListener(MinigameListener listener){
        HandlerList.unregisterAll(listener);
    }


    @Override
    public void onDisable() {
        if(GameHandler.getHandler().getCurrentState() != MinigameState.NO_GAME){
            GameHandler.getHandler().stopMinigame();
        }

        for(MinigameBuilder builders : MinigameBuilders.getBuilders().getAllBuilders()){
            builders.stopBuilding();
        }
        log.info("OnDisable called.");
    }


}
