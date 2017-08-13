package me.chandl.cfminigame.minigames.race;

import me.chandl.cfminigame.GameHandler;
import me.chandl.cfminigame.minigame.player.MinigamePlayer;
import org.bukkit.GameMode;


public class ElytraRace extends Race{


    @Override
    public void start() {

//        System.out.println("From ElytraRace. Map " + getMap());
        for(MinigamePlayer p : GameHandler.getHandler().getPlayerList()){

            p.getPlayerObject().setGameMode(GameMode.SURVIVAL);
        }
        super.start();


    }

    @Override
    public void stop() {
        super.stop();
    }

    @Override
    public void onLeave(MinigamePlayer player) {
        player.getPlayerObject().setGameMode(player.getPreviousGamemode());

        super.onLeave(player);
    }
}
