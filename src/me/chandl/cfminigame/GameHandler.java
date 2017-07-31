package me.chandl.cfminigame;

import me.chandl.cfminigame.minigame.Minigame;
import me.chandl.cfminigame.minigame.MinigamePlayer;
import me.chandl.cfminigame.minigame.MinigameState;
import me.chandl.cfminigame.minigame.PlayerState;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.List;

public class GameHandler implements Listener {

    private List<MinigamePlayer> playerList;
    private static GameHandler handler;
    private static Minigame currentMinigame;
    private static MinigameState currentState;

    private GameHandler(){
        currentMinigame = null;
        currentState = MinigameState.NO_GAME;
        playerList = new ArrayList<MinigamePlayer>();
    }

    public static GameHandler getHandler(){
        if(handler == null) {handler = new GameHandler();}
        return handler;
    }


    public static void startQueue(){
        currentState = MinigameState.IN_QUEUE;
    }

    public static void startMinigame(){
        currentState = MinigameState.IN_GAME;

        currentMinigame.start();
    }

    public static void stopMinigame(){
        currentState = MinigameState.NO_GAME;
        currentMinigame = null;

        currentMinigame.stop();
    }

    public boolean addPlayer(MinigamePlayer player){
        playerList.add(player);

        //make sure player limit is not reached
        if(playerList.size() < currentMinigame.getMaximumPlayers()){
            //Clear player's inventory
            player.clearItems();

            //teleport player to MG start location
            player.getPlayerObject().teleport(currentMinigame.getMap().getSpawnPoint());

            //give player MG starting items
            player.getPlayerObject().getInventory().setContents( currentMinigame.getMap().getStartingItems() );

            currentMinigame.onJoin(player);

            return true;

        }else {
            return false;
        }

    }

    public boolean removePlayer(MinigamePlayer player){

        if(playerList.remove(player)){
            player.setState(PlayerState.NOT_IN_GAME);
            player.loadItems();
            player.getPlayerObject().teleport(player.getBeforeMGPosition());

            currentMinigame.onLeave(player);

            return true;

        }else{
            return false;
        }
    }

    public List<MinigamePlayer> getPlayerList() {
        return playerList;
    }

    public static Minigame getCurrentMinigame(){
        return currentMinigame;
    }

    public static void setCurrentMinigame(Minigame minigame){
        currentMinigame = minigame;
    }
}
