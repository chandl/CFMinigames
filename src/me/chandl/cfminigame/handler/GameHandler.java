package me.chandl.cfminigame.handler;

import me.chandl.cfminigame.CFMinigame;
import me.chandl.cfminigame.ex.GameTypeNotFoundException;
import me.chandl.cfminigame.ex.MapNotFoundException;
import me.chandl.cfminigame.minigame.core.MinigameFactory;
import me.chandl.cfminigame.minigame.core.Minigame;
import me.chandl.cfminigame.minigame.core.MinigameState;
import me.chandl.cfminigame.minigame.player.MinigamePlayer;
import me.chandl.cfminigame.minigame.player.PlayerState;
import me.chandl.cfminigame.util.Message;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * GameHandler - The Singleton Main Game Handling Logic for one Minigame at a Time.
 *
 * @author Chandler me@cseverson.com
 * @version 1.0
 * @since Aug 19.2017
 */
public class GameHandler implements Listener {

    //List of Players currently in the Minigame.
    private static ConcurrentHashMap<UUID, MinigamePlayer> playerList;
    //Singleton variable
    private static GameHandler handler;
    //The current minigame that is in progress
    private static Minigame currentMinigame;
    //The current state of the game.
    private static MinigameState currentState;

    //BukkitRunnables used during the Queue and in the game to time events.
    private BukkitRunnable timerA;
    private BukkitRunnable timerB;


    /**
     * Constructor used to create a base GameHandler state.
     */
    private GameHandler(){
        currentMinigame = null;
        currentState = MinigameState.NO_GAME;
        playerList = new ConcurrentHashMap<>();
    }

    /**
     * Singleton method to return a single instance of this GameHandler.
     *
     * @return the {@code handler} variable (a new GameHandler).
     */
    public static GameHandler getHandler(){
        if(handler == null) {handler = new GameHandler();}
        return handler;
    }


    /**
     * Starts the Game Queue.
     *
     * Alerts players that the game will start and begins a countdown at T-3 seconds.
     */
    public void startQueue(){
        currentState = MinigameState.IN_QUEUE;

        //Alert players of the queue starting.
        Message.allPlayers( "The level " + currentMinigame.getDifficultyLevel() + " " + currentMinigame.getType() + " Minigame on " + currentMinigame.getMap().getName() +" will start in " + CFMinigame.DEFAULT_MAX_QUEUE_TIME + " seconds. Type '/mg join' to join!");


        //Runnable for a Countdown Timer at 3 Seconds
        timerA = new BukkitRunnable(){
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

        //Start countdown task three seconds before the game is supposed to start.
        timerA.runTaskLaterAsynchronously(CFMinigame.plugin, 20 * (CFMinigame.DEFAULT_MAX_QUEUE_TIME-3 ));

        //Actual Timer to Start the Minigame after the specified queue length
        timerB = new BukkitRunnable(){
            @Override
            public void run() {
                Message.playersInGame(currentMinigame.getType()+": " + currentMinigame.getMap().getName() + " (Level "+ currentMinigame.getDifficultyLevel()+") Starting Now!");
                System.out.println("[CFMinigame]" + currentMinigame.getType()+": " + currentMinigame.getMap().getName() + " (Level "+ currentMinigame.getDifficultyLevel()+") Starting Now!");
                startMinigame();
            }
        };
        timerB.runTaskLater(CFMinigame.plugin, 20 * CFMinigame.DEFAULT_MAX_QUEUE_TIME );
    }

    /**
     * Method to start the minigame.
     *
     * Updates the state of the current game to {@code IN_GAME} and calls the {@link Minigame} {@code start()} method.
     * Updates all players' states to {@code IN_GAME}
     */
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

    /**
     * Method to stop the current minigame.
     *
     * Updates the state of the game to {@code NO_GAME} and cancels any timers that may be running.
     * Removes all of the players from the minigame and returns them to where they were before starting, setting their state to {@code NOT_IN_GAME}.
     * Calls the {@code stop()} method of the current {@link Minigame}
     */
    public void stopMinigame(){
        if(currentState == MinigameState.NO_GAME) return; //return if we are already stopping the minigame
        if(currentState == MinigameState.IN_QUEUE){
            timerB.cancel();
            timerA.cancel();
        }

        currentState = MinigameState.NO_GAME;

        for(MinigamePlayer player : playerList.values()){
            removePlayer(player);
            player.setState(PlayerState.NOT_IN_GAME);
        }

        currentMinigame.stop();
        currentMinigame = null;
    }


    /**
     * Starts a new Minigame based on the type of the minigame, the map name, and the difficulty.
     *
     *
     * @param startingPlayer The player that requested to start the minigame.
     * @param typeOfMinigame The type of the minigame {@link me.chandl.cfminigame.minigame.core.MinigameType}.
     * @param mapName The {@link me.chandl.cfminigame.minigame.core.MinigameMap} that should be played on.
     * @param difficulty The difficulty level of the Minigame.
     *
     * @return True if the minigame was started successfully, false if there were errors.
     */
    public boolean startNewMinigame(MinigamePlayer startingPlayer, String typeOfMinigame, String mapName, int difficulty){
        if(currentMinigame != null) return false;

        try {
            currentMinigame = MinigameFactory.newMinigame(typeOfMinigame,mapName,difficulty);
        } catch (GameTypeNotFoundException e) {
            Message.player(startingPlayer, "ERROR", "No Mingame Type '" + typeOfMinigame +"'. Could not create minigame lobby.");
            return false;
        } catch (MapNotFoundException e) {
            Message.player(startingPlayer, "ERROR", "No Mingame Map '" + mapName +"'. Could not create minigame lobby.");
            return false;
        }

        startQueue();
        getHandler().addPlayer(startingPlayer);
        return true;
    }

    /**
     * Adds a new Player to the current minigame and updates the Player's state.
     *
     * @param player The player that is joining the game.
     * @return True if the player join was successful, false otherwise.
     */
    public boolean addPlayer(MinigamePlayer player){

        //make sure player limit is not reached
        if(playerList.size() < currentMinigame.getMaximumPlayers()){

            playerList.put(player.getPlayerObject().getUniqueId(), player);
            currentMinigame.onPlayerJoin(player);

            if(getCurrentState() == MinigameState.IN_QUEUE){
                player.setState(PlayerState.IN_QUEUE);
            }else if(getCurrentState() == MinigameState.IN_GAME){
                player.setState(PlayerState.IN_GAME);
            }
            return true;
        }else {
            return false;
        }

    }

    /**
     * Removes a player from the current minigame and updates their state..
     *
     * @param player The player that is leaving the minigame.
     * @return True if the player successfully left the minigame, false otherwise.
     */
    public boolean removePlayer(MinigamePlayer player){
        if(playerList.containsKey(player.getPlayerObject().getUniqueId())){
            playerList.remove(player.getPlayerObject().getUniqueId());

            //Call minigame onPlayerLeave
            currentMinigame.onPlayerLeave(player);

            if(playerList.size() == 0){
                Message.allPlayers("All players left the current minigame! Use '/mg start' to start a new one!");
                stopMinigame();
            }
            return true;
        }else{
            return false;
        }
    }

    /**
     * Checks if all players in the current minigame are spectating.
     *
     * @return True if all of the current players in the minigame are spectating.
     */
    public boolean checkIfAllPlayersAreFinished(){
        for(MinigamePlayer p : GameHandler.getHandler().getPlayerList()){
            if(p.getState() != PlayerState.SPECTATING) return false;
        }

        return true;
    }

    /**
     * Checks if a specified player is in the current minigame.
     *
     * @param player The player to check for.
     * @return True if the player is in the current minigame.
     */
    public boolean playerInGame(MinigamePlayer player){
        return playerList.containsKey(player.getPlayerObject().getUniqueId());
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

    /**
     * Retrieves a {@link MinigamePlayer} representation of a specified {@link UUID} playerId.
     *
     * @param playerId The {@link UUID} unique id for the Minecraft {@link org.bukkit.entity.Player} object.
     * @return a {@link MinigamePlayer} wrapper for the specified playerId, or {@code null} if the player is not in the current minigame.
     */
    public static MinigamePlayer getPlayer(UUID playerId){
        if(playerList.containsKey(playerId)){
            return playerList.get(playerId);
        }else{
            return null;
        }
    }

    public ArrayList<MinigamePlayer> getPlayerList() {
        return new ArrayList<MinigamePlayer>(playerList.values());
    }
    public HashSet<UUID> getPlayerUUIDs(){
        return new HashSet<UUID>(playerList.keySet());
    }
}
