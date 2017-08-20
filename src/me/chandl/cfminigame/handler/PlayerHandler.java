package me.chandl.cfminigame.handler;


import me.chandl.cfminigame.minigame.core.Minigame;
import me.chandl.cfminigame.minigame.player.MinigamePlayer;
import me.chandl.cfminigame.minigame.player.PlayerState;
import org.bukkit.event.player.PlayerRespawnEvent;

/**
 * PlayerHandler - A set of methods containing game logic specific to {@link MinigamePlayer}s
 *
 * @author Chandler me@cseverson.com
 * @version 1.0
 * @since Aug 20, 2017
 */
public class PlayerHandler {

    private MinigamePlayer player;

    public PlayerHandler(MinigamePlayer player) {
        this.player = player;
    }

    /**
     * Called when the player finishes the minigame.
     *
     * @param timeTaken The time taken to finish the minigame.
     */
    public void minigameFinishAction(long timeTaken){
        Minigame currMg = GameHandler.getHandler().getCurrentMinigame();
        player.setState(PlayerState.SPECTATING);
        player.getPlayerObject().teleport(currMg.getMap().getSpectatorPoint());
        player.setGameTime(timeTaken);
    }

    /**
     * Called when the player leaves the minigame.
     */
    public void minigameLeaveAction(){
        player.setState(PlayerState.NOT_IN_GAME);
        player.loadItems();
        player.getPlayerObject().teleport(player.getBeforeMGPosition());
        player.getPlayerObject().setGameMode(player.getPreviousGamemode());
    }

    /**
     * Called when the player respawns in the minigame.
     * @param respawnEvent The Minecraft Respawn Event, Used to set spawnpoints.
     */
    public void minigameRespawnAction(PlayerRespawnEvent respawnEvent){
        Minigame currMg = GameHandler.getHandler().getCurrentMinigame();
        if(player.getState() == PlayerState.IN_GAME){
            player.getPlayerObject().getInventory().setContents(currMg.getMap().getStartingItems());
            respawnEvent.setRespawnLocation(currMg.getMap().getSpawnPoint());
        }else if(player.getState() == PlayerState.SPECTATING){
            respawnEvent.setRespawnLocation(currMg.getMap().getSpectatorPoint());
        }
    }

    /**
     * Called when the player dies in the minigame.
     * Logic to check if player is completely dead, sets state to SPECTATING.
     */
    public void minigameDeathAction() {
        player.setProgress(0);
        if (player.getCurrentLifeCount() == 1) { //player just died for the last time
            player.setState(PlayerState.SPECTATING);
        } else {
            player.setCurrentLifeCount(player.getCurrentLifeCount() - 1);
        }
    }

    /**
     * Called when the player tries to join a minigame that is in progress.
     */
    public void minigameJoinInProgressAction(){
        Minigame currMg = GameHandler.getHandler().getCurrentMinigame();

        //Clear player's inventory
        player.clearItems();

        //Set player's state to IN_GAME
        player.setState(PlayerState.IN_GAME);

        //teleport player to MG spectator location
        player.getPlayerObject().teleport( currMg.getMap().getSpectatorPoint());

        //give player MG starting items
        player.getPlayerObject().getInventory().setContents( currMg.getMap().getStartingItems() );
    }

    /**
     * Called when the player tries to join a minigame that is in the queue stage.
     */
    public void minigameJoinInQueueAction(){
        Minigame currMg = GameHandler.getHandler().getCurrentMinigame();
        //Clear player's inventory
        player.clearItems();

        //Set player's state to IN_QUEUE
        player.setState(PlayerState.IN_QUEUE);

        //teleport player to MG start location
        player.getPlayerObject().teleport(currMg.getMap().getSpawnPoint());
    }

}
