package me.chandl.cfminigame.minigames.snowballfight;

import me.chandl.cfminigame.CFMinigame;
import me.chandl.cfminigame.GameHandler;
import me.chandl.cfminigame.minigame.core.Minigame;
import me.chandl.cfminigame.minigame.core.MinigameType;
import me.chandl.cfminigame.minigame.player.MinigamePlayer;
import me.chandl.cfminigame.minigame.player.PlayerState;
import me.chandl.cfminigame.util.Message;
import org.bukkit.GameMode;
import org.bukkit.event.entity.PlayerDeathEvent;

public class SnowballFight extends Minigame {
    private SnowballListener snowballListener;

    public SnowballFight() {
        this.setType(MinigameType.SNOWBALLFIGHT);
        this.setMaximumPlayers(16);
        this.setMinimumPlayers(2);

    }

    @Override
    public void start() {
        for(MinigamePlayer p : GameHandler.getHandler().getPlayerList()){
            p.setCurrentLifeCount(getMap().getMaxLifeCount());
            p.getPlayerObject().setGameMode(GameMode.SURVIVAL);
            p.getPlayerObject().getInventory().setContents( getMap().getStartingItems() );
        }
        snowballListener = new SnowballListener();
        CFMinigame.plugin.registerListener(snowballListener);
    }

    @Override
    public void stop() {
        if(snowballListener != null)
        {
            CFMinigame.plugin.unregisterListener(snowballListener);
        }
    }

    @Override
    public void onPlayerDamage(MinigamePlayer player) {

    }

    @Override
    public void onDie(PlayerDeathEvent event, MinigamePlayer player) {
        super.onDie(event, player);

        if(player.getCurrentLifeCount() == 0){
            int gameFinished = 0;
            for(MinigamePlayer p : GameHandler.getHandler().getPlayerList()){
                if(p.getState() == PlayerState.IN_GAME) gameFinished++;
            }

            if(gameFinished == 1){
                Message.allPlayers("INFO", "The Current Snowball Minigame Has Finished. 1st Place was " + player.getPlayerObject().getName());
                GameHandler.getHandler().stopMinigame();
            }
        }
    }
}
