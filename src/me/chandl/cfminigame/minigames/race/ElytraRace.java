package me.chandl.cfminigame.minigames.race;

import me.chandl.cfminigame.handler.GameHandler;
import me.chandl.cfminigame.minigame.player.MinigamePlayer;
import me.chandl.cfminigame.minigame.player.PlayerState;
import org.bukkit.GameMode;

/**
 * Elytra Race Minigame.
 *
 * @author Chandler me@cseverson.com
 * @version 1.0
 * @since Aug 20, 2017
 */
public class ElytraRace extends Race{


    @Override
    public void start() {

//        System.out.println("From ElytraRace. Map " + getMap());
        for(MinigamePlayer p : GameHandler.getHandler().getPlayerList()){

            if(p.getState() != PlayerState.SPECTATING)
                p.getPlayerObject().teleport(getMap().getSpawnPoint());

            p.getPlayerObject().setGameMode(GameMode.SURVIVAL);
            p.getPlayerObject().setHealth(20);
            p.getPlayerObject().setExhaustion(0);
        }
        super.start();


    }

    @Override
    public void stop() {
        super.stop();
    }

    @Override
    public void onPlayerLeave(MinigamePlayer player) {
        player.getPlayerObject().setGameMode(player.getPreviousGamemode());

        super.onPlayerLeave(player);
    }
}
