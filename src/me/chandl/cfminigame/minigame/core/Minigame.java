package me.chandl.cfminigame.minigame.core;

import me.chandl.cfminigame.handler.GameHandler;
import me.chandl.cfminigame.minigame.player.MinigamePlayer;
import me.chandl.cfminigame.minigame.player.PlayerState;
import me.chandl.cfminigame.util.Message;
import org.bukkit.ChatColor;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public abstract class Minigame {
    private Date startTime;
    private MinigameType type;
    private int maximumPlayers, minimumPlayers;
    private MinigameMap map;
    private int difficultyLevel;
    private List<MinigamePlayer> scoreboard;

    public Minigame(){}

    @Override
    public String toString(){
        String out = String.format("[%s] %s Minigame. Map: %s. Difficulty: %d. " +
                "Max Players: %d, Min Players: %d.", startTime.toString(),
                type,
                map.getName(),
                difficultyLevel,
                maximumPlayers,
                minimumPlayers);
        return out;
    }

    public abstract void start ();
    public abstract void stop ();


    public abstract void onPlayerDamage(MinigamePlayer player);


    public void onPlayerFinish(MinigamePlayer player){
        if(scoreboard == null) {
            scoreboard = new ArrayList<>();
        }

        Date finished = new Date();
        long time = (finished.getTime() - startTime.getTime()) / 1000;
        player.setGameTime(time);
        scoreboard.add(player);

        Message.playersInGame(player.getPlayerObject().getName() + " Finished the Minigame in position " + scoreboard.size() + "! Time: " + time + " seconds." );
        System.out.println(player.getPlayerObject().getName() + " Finished the Minigame in position " + scoreboard.size() + "! Time: " + time + " seconds.");
        player.setState(PlayerState.SPECTATING);
        player.getPlayerObject().teleport(getMap().getSpectatorPoint());

        if(GameHandler.getHandler().checkIfAllPlayersAreFinished()){
            Message.allPlayers("INFO", "The Current Minigame Has Finished! 1st Place was " + scoreboard.get(0).getPlayerObject().getName() + " at " + scoreboard.get(0).getGameTime() + " seconds.");
            System.out.println("The Current Minigame Has Finished! 1st Place was " + scoreboard.get(0).getPlayerObject().getName() + " at " + scoreboard.get(0).getGameTime() + " seconds.");

            GameHandler.getHandler().stopMinigame();
        }
    }



    public void onRespawn(PlayerRespawnEvent event, MinigamePlayer player){
        System.out.println("ONRESPAWN CALLED!!!");
        System.out.println("Player State: " + player.getState());
        //Respawn Players
        if(player.getState() == PlayerState.IN_GAME){
            event.setRespawnLocation(map.getSpawnPoint());
            //give player MG starting items
            player.getPlayerObject().getInventory().setContents( getMap().getStartingItems() );
        }else if(player.getState() == PlayerState.SPECTATING){
            event.setRespawnLocation(getMap().getSpectatorPoint());
        }
    }

    public void onDie(PlayerDeathEvent event, MinigamePlayer player){
//        player.getPlayerObject().getInventory().setContents(null);

        event.getDrops().clear();
        player.setProgress(0);
        if(player.getCurrentLifeCount() == 1){ //player just died for the last time
            player.setState(PlayerState.SPECTATING);
            Message.player(player, "You have Died and have used all of your lives. Spectating now...");

            if(GameHandler.getHandler().checkIfAllPlayersAreFinished()){
                if(scoreboard == null || scoreboard.size() == 0){
                    Message.allPlayers("INFO", "The Current Minigame has Finished! Nobody was able to complete it!!");
                }else{
                    Message.allPlayers("INFO", "The Current Minigame Has Finished! 1st Place was " + scoreboard.get(0).getPlayerObject().getName() + " at " + scoreboard.get(0).getGameTime() + " seconds.");
                }

                GameHandler.getHandler().stopMinigame();
            }
        }else{
            player.setCurrentLifeCount(player.getCurrentLifeCount() - 1);
            Message.player(player, ChatColor.DARK_RED + "You Have Died!"+ ChatColor.WHITE +" Remaining Lives: " + player.getCurrentLifeCount());
        }

    }

    /**
     * Default onJoin Method for Minigames.
     * Clears Player's Inventory, Saving previous inventory for after game.
     * Teleports Player to the Minigame Spawn Point
     * Gives Player the Minigame Starting items.
     * @param player
     */
    public void onJoin(MinigamePlayer player){
        switch(GameHandler.getHandler().getCurrentState()){
            case IN_GAME:
                //Clear player's inventory
                player.clearItems();

                player.setState(PlayerState.IN_GAME);

                //teleport player to MG spectator location
                player.getPlayerObject().teleport(getMap().getSpectatorPoint());

                //notify all players of someone new in the MG.
                Message.allPlayers(String.format("%s started spectating the minigame! [%d/%d Players]", player.getPlayerObject().getDisplayName(), GameHandler.getHandler().getPlayerList().size(), getMaximumPlayers()));

                //give player MG starting items
                player.getPlayerObject().getInventory().setContents( getMap().getStartingItems() );
                break;

            case IN_QUEUE:
                //Clear player's inventory
                player.clearItems();

                player.setState(PlayerState.IN_QUEUE);

                //teleport player to MG start location
                player.getPlayerObject().teleport(getMap().getSpawnPoint());

                //notify all players of someone new in the MG.
                Message.allPlayers(String.format("%s just joined the minigame! [%d/%d Players]", player.getPlayerObject().getDisplayName(), GameHandler.getHandler().getPlayerList().size(), getMaximumPlayers()));
                break;

            case NO_GAME:
            default:
                System.out.println("This ERROR should never show. If it does, ur fucked. ");
                break;
        }

    }

    public void onLeave(MinigamePlayer player){
        player.setState(PlayerState.NOT_IN_GAME);
        player.loadItems();
        player.getPlayerObject().teleport(player.getBeforeMGPosition());
        //notify all players of someone leaving the MG.
        if(GameHandler.getHandler().getPlayerList().size() != 0)
            Message.allPlayers(String.format("%s just left the minigame! [%d/%d Players]", player.getPlayerObject().getDisplayName(), GameHandler.getHandler().getPlayerList().size(), getMaximumPlayers()));
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public MinigameType getType() {
        return type;
    }

    public void setType(MinigameType type) {
        this.type = type;
    }

    public int getMaximumPlayers() {
        return maximumPlayers;
    }

    public void setMaximumPlayers(int maximumPlayers) {
        this.maximumPlayers = maximumPlayers;
    }

    public int getMinimumPlayers() {
        return minimumPlayers;
    }

    public void setMinimumPlayers(int minimumPlayers) {
        this.minimumPlayers = minimumPlayers;
    }

    public MinigameMap getMap() {
        return map;
    }

    public void setMap(MinigameMap map) {
//        System.out.println("In SetMap. Map: " + map);
        this.map = map;
    }

    public int getDifficultyLevel() {
        return difficultyLevel;
    }

    public void setDifficultyLevel(int difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }
}
