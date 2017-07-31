package me.chandl.cfminigame;


import me.chandl.cfminigame.minigame.Minigame;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public class CFMinigame extends JavaPlugin{

    private Logger log;
    private static Minigame currentMinigame;

    @Override
    public void onEnable() {
        log = getLogger();
        log.info("onEnable called.");


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
