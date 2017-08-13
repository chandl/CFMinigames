package me.chandl.cfminigame;

import me.chandl.cfminigame.minigame.core.Minigame;
import me.chandl.cfminigame.minigame.core.MinigameMap;
import me.chandl.cfminigame.minigame.core.MinigameState;
import me.chandl.cfminigame.minigame.core.MinigameType;
import me.chandl.cfminigame.minigame.player.MinigamePlayer;
import me.chandl.cfminigame.minigame.player.PlayerState;
import me.chandl.cfminigame.util.Message;
import me.chandl.cfminigame.util.TextUtil;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

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


    private BukkitRunnable countdownTimer;
    private BukkitRunnable gameStartTimer;
    public void startQueue(){
        currentState = MinigameState.IN_QUEUE;
        Message.allPlayers( "The level " + currentMinigame.getDifficultyLevel() + " " + currentMinigame.getType() + " Minigame on " + currentMinigame.getMap().getName() +" will start in " + CFMinigame.DEFAULT_MAX_QUEUE_TIME + " seconds...");


        //Countdown Timer at 3 Seconds
        countdownTimer = new BukkitRunnable(){
            @Override
            public void run() {
                for(int i=3; i>0; i--){
                    Message.allPlayers( currentMinigame.getType()+": " + currentMinigame.getMap().getName() + " (Level "+ currentMinigame.getDifficultyLevel()+") starting in " +i+"...");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        System.out.println("[CFMinigame] Minigame Countdown Timer was Interruputed.");
                    }
                }

            }
        };

        countdownTimer.runTaskLaterAsynchronously(CFMinigame.plugin, 20 * (CFMinigame.DEFAULT_MAX_QUEUE_TIME-3 ));

        //Actual Timer to Start the Minigame after DEFAULT_MAX_QUEUE_TIME seconds
        gameStartTimer = new BukkitRunnable(){
            @Override
            public void run() {
                Message.playersInGame(currentMinigame.getType()+": " + currentMinigame.getMap().getName() + " (Level "+ currentMinigame.getDifficultyLevel()+") Starting Now!");
                System.out.println("[CFMinigame]" + currentMinigame.getType()+": " + currentMinigame.getMap().getName() + " (Level "+ currentMinigame.getDifficultyLevel()+") Starting Now!");
                startMinigame();
            }
        };
        gameStartTimer.runTaskLater(CFMinigame.plugin, 20 * CFMinigame.DEFAULT_MAX_QUEUE_TIME );
    }

    public void startMinigame(){
//        if(playerList.size() < currentMinigame.getMinimumPlayers()){
//            Message.allPlayers("ERROR", "Not Enough Players to Start the Minigame (Minimum "+currentMinigame.getMinimumPlayers()+"). Try again with '/mg start'");
//            stopMinigame();
//            return;
//        }
        currentState = MinigameState.IN_GAME;

        currentMinigame.start();

        for(MinigamePlayer player : playerList.values()){
            player.setState(PlayerState.IN_GAME);
        }
    }

    public void stopMinigame(){
        if(currentState == MinigameState.NO_GAME) return; //return if we are already stopping the minigame
        if(currentState == MinigameState.IN_QUEUE){
            gameStartTimer.cancel();
            countdownTimer.cancel();
        }

        currentState = MinigameState.NO_GAME;

        for(MinigamePlayer player : playerList.values()){
            removePlayer(player);
            player.setState(PlayerState.NOT_IN_GAME);
        }

        currentMinigame.stop();
        currentMinigame = null;
    }


    public boolean createMinigame(MinigamePlayer player, String typeStr, String mapName, int difficulty){
        if(currentMinigame != null) return false;

        MinigameType type = null;
        MinigameMap map;

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

        map = MinigameMap.findMap(type, mapName);
        if(map == null){
            player.getPlayerObject().sendMessage(TextUtil.formatMessage("ERROR", "No Mingame Map '" + mapName +"'. Could not create minigame lobby."));
            return false;
        }

//        System.out.println("Type: " + type);
//        System.out.println("Map: " + map);

        Minigame game = type.toMinigame();
        game.setType(type);
        game.setMaximumPlayers(CFMinigame.DEFAULT_MAX_PLAYERS);
        game.setMinimumPlayers(CFMinigame.DEFAULT_MIN_PLAYERS);
//        System.out.println("In createMinigame. Map: " + map);
        game.setMap(map);
        game.setDifficultyLevel(difficulty);
        game.setStartTime(new Date());

        currentMinigame = game;
        startQueue();
        getHandler().addPlayer(player);

        return true;
    }

    public boolean addPlayer(MinigamePlayer player){

        //make sure player limit is not reached
        if(playerList.size() < currentMinigame.getMaximumPlayers()){

            playerList.put(player.getPlayerObject().getUniqueId(), player);

            //Call minigame onJoin
            currentMinigame.onJoin(player);

//            System.out.println("AddPlayer: " + player);
            return true;
        }else {
            return false;
        }

    }

    public boolean removePlayer(MinigamePlayer player){

        if(playerList.containsKey(player.getPlayerObject().getUniqueId())){
            playerList.remove(player.getPlayerObject().getUniqueId());

//            System.out.println("Shit Player 1: " + player);
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
    public HashSet<UUID> getPlayerUUIDs(){
        return new HashSet<UUID>(playerList.keySet());
    }
}
