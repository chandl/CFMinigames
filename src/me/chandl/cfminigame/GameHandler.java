package me.chandl.cfminigame;

import me.chandl.cfminigame.minigame.*;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.List;

public class GameHandler implements Listener {


    private static List<MinigamePlayer> playerList;
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

        for(MinigamePlayer player : playerList){
            player.setState(PlayerState.IN_GAME);
        }
    }

    public static void stopMinigame(){
        currentState = MinigameState.NO_GAME;
        currentMinigame = null;

        currentMinigame.stop();
        for(MinigamePlayer player : playerList){
            player.setState(PlayerState.NOT_IN_GAME);
        }
    }

    public static boolean createMinigame(MinigamePlayer player, String typeStr, String mapName, int difficulty){
        if(currentMinigame != null) return false;

        MinigameType type = null;
        MinigameMap map = null;

        for(MinigameType mgType : MinigameType.values()){
            if(typeStr.equalsIgnoreCase(mgType.toString())){
                type = mgType;
                break;
            }
        }

        if(type == null){
            player.getPlayerObject().sendMessage("[CFMinigame ERROR] No Mingame Type '" + typeStr +"'. Could not create minigame lobby.");
            return false;
        }

        map = MinigameMap.findMap(type, mapName, difficulty);
        if(map == null){
            player.getPlayerObject().sendMessage("[CFMinigame ERROR] No Mingame Map '" + mapName +"'. Could not create minigame lobby.");
            return false;
        }

        Minigame game = type.toMinigame();
        game.setType(type);
        game.setMaximumPlayers(CFMinigame.DEFAULT_MAX_PLAYERS);
        game.setMinimumPlayers(CFMinigame.DEFAULT_MIN_PLAYERS);
        game.setQueueTimeLimit(CFMinigame.DEFAULT_MAX_QUEUE_TIME);
        game.setMap(map);
        game.setDifficultyLevel(difficulty);

        currentMinigame = game;
        startQueue();

        return true;
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
