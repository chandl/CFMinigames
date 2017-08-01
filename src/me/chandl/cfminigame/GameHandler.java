package me.chandl.cfminigame;

import me.chandl.cfminigame.minigame.*;
import me.chandl.cfminigame.util.Message;
import me.chandl.cfminigame.util.TextUtil;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

public class GameHandler implements Listener {

    private static HashMap<UUID, MinigamePlayer> playerList;
    private static GameHandler handler;
    private static Minigame currentMinigame;
    private static MinigameState currentState;

    private GameHandler(){
        currentMinigame = null;
        currentState = MinigameState.NO_GAME;
        playerList = new HashMap<>();
    }


    public void startQueue(){
        currentState = MinigameState.IN_QUEUE;

        //Start Async Queue
    }

    public void startMinigame(){
        currentState = MinigameState.IN_GAME;

        currentMinigame.start();

        for(MinigamePlayer player : playerList.values()){
            player.setState(PlayerState.IN_GAME);
        }
    }

    public void stopMinigame(){
        currentState = MinigameState.NO_GAME;

        currentMinigame.stop();

        currentMinigame = null;

        for(MinigamePlayer player : playerList.values()){
            player.setState(PlayerState.NOT_IN_GAME);
        }
    }



    public boolean createMinigame(MinigamePlayer player, String typeStr, String mapName, int difficulty){
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
            player.getPlayerObject().sendMessage(TextUtil.formatMessage("ERROR", "No Mingame Type '" + typeStr +"'. Could not create minigame lobby."));
            return false;
        }

        map = MinigameMap.findMap(type, mapName, difficulty);
        if(map == null){
            player.getPlayerObject().sendMessage(TextUtil.formatMessage("ERROR", "No Mingame Map '" + mapName +"'. Could not create minigame lobby."));
            return false;
        }

        Minigame game = type.toMinigame();
        game.setType(type);
        game.setMaximumPlayers(CFMinigame.DEFAULT_MAX_PLAYERS);
        game.setMinimumPlayers(CFMinigame.DEFAULT_MIN_PLAYERS);
        game.setQueueTimeLimit(CFMinigame.DEFAULT_MAX_QUEUE_TIME);
        game.setMap(map);
        game.setDifficultyLevel(difficulty);
        game.setStartTime(new Date());

        currentMinigame = game;
        startQueue();
        getHandler().addPlayer(player);

        return true;
    }

    public boolean addPlayer(MinigamePlayer player){

        playerList.put(player.getPlayerObject().getUniqueId(), player);

        //make sure player limit is not reached
        if(playerList.size() < currentMinigame.getMaximumPlayers()){

            //Call minigame onJoin
            currentMinigame.onJoin(player);
            return true;
        }else {
            return false;
        }

    }

    public boolean removePlayer(MinigamePlayer player){

        if(playerList.containsKey(player.getPlayerObject().getUniqueId())){
            playerList.remove(player.getPlayerObject().getUniqueId());
            //Call minigame onLeave
            currentMinigame.onLeave(player);

            if(playerList.size() == 0){
                Message.allPlayers("All players left the current minigame! Use '/mg start' to start a new one!");
                stopMinigame();
            }
            return true;
        }else{
            return false;
        }
    }

    public boolean playerInGame(MinigamePlayer player){
        return playerList.containsKey(player.getPlayerObject().getUniqueId());
    }





    public static GameHandler getHandler(){
        if(handler == null) {handler = new GameHandler();}
        return handler;
    }

    public Minigame getCurrentMinigame(){
        return currentMinigame;
    }

    public void setCurrentMinigame(Minigame minigame){
        currentMinigame = minigame;
    }

    public MinigameState getCurrentState() {
        return currentState;
    }

    public void setCurrentState(MinigameState currentState) {
        GameHandler.currentState = currentState;
    }

    public ArrayList<MinigamePlayer> getPlayerList() {
        return new ArrayList<MinigamePlayer>(playerList.values());
    }
}
