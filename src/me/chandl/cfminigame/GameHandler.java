package me.chandl.cfminigame;

import me.chandl.cfminigame.minigame.MinigamePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.List;

/**
 * Created by chandler on 7/30/17.
 */
public class GameHandler implements Listener {

    private List<MinigamePlayer> playerList;


    public static void startQueue(){

    }

    public static void startMinigame(){

    }

    public void addPlayer(Player player){
        MinigamePlayer mgPlayer = new MinigamePlayer(player);

    }

    public List<MinigamePlayer> getPlayerList() {
        return playerList;
    }
}
