package me.chandl.cfminigame.minigame;

import me.chandl.cfminigame.GameHandler;
import me.chandl.cfminigame.util.Message;
import me.chandl.cfminigame.util.TextUtil;

import java.util.Date;

public abstract class Minigame {
    private Date startTime;
    private MinigameType type;
    private int maximumPlayers, minimumPlayers;
    private long queueTimeLimit;
    private MinigameMap map;
    private int difficultyLevel;

    @Override
    public String toString(){
        String out = String.format("[%s] %s Minigame. Map: %s. Difficulty: %d. " +
                "Max Players: %d, Min Players: %d.", startTime.toString(), type, map.getName(),
                difficultyLevel, maximumPlayers, minimumPlayers);
        return out;
    }

    public abstract void start ();
    public abstract void stop ();

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

                //teleport player to MG spectator location
                player.getPlayerObject().teleport(GameHandler.getHandler().getCurrentMinigame().getMap().getSpectatorPoint());

                //notify all players of someone new in the MG.
                Message.allPlayers(String.format("%s started spectating the minigame! [%d/%d Players]", player.getPlayerObject().getDisplayName(), GameHandler.getHandler().getPlayerList().size(), getMaximumPlayers()));
                break;

            case IN_QUEUE:
                //Clear player's inventory
                player.clearItems();

                //teleport player to MG start location
                player.getPlayerObject().teleport(GameHandler.getHandler().getCurrentMinigame().getMap().getSpawnPoint());

                //give player MG starting items
                player.getPlayerObject().getInventory().setContents( GameHandler.getHandler().getCurrentMinigame().getMap().getStartingItems() );

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

    public long getQueueTimeLimit() {
        return queueTimeLimit;
    }

    public void setQueueTimeLimit(long queueTimeLimit) {
        this.queueTimeLimit = queueTimeLimit;
    }

    public MinigameMap getMap() {
        return map;
    }

    public void setMap(MinigameMap map) {
        this.map = map;
    }

    public int getDifficultyLevel() {
        return difficultyLevel;
    }

    public void setDifficultyLevel(int difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }
}
