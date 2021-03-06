package me.chandl.cfminigame.minigame.player;

import me.chandl.cfminigame.database.PlayerConfig;
import me.chandl.cfminigame.database.PlayerStore;
import me.chandl.cfminigame.handler.PlayerHandler;
import me.chandl.cfminigame.minigame.core.Minigame;
import me.chandl.cfminigame.scores.HighScore;
import me.chandl.cfminigame.scores.WinCount;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * A wrapper class for the Minecraft Player object used throughout the Minigames.
 *
 * @author Chandler me@cseverson.com
 * @version 1.0
 * @since Aug 20, 2017
 */
public class MinigamePlayer {
    private Player player;
    private List<HighScore> highScores;
    private List<WinCount> winCount;
    private int currentLifeCount;
    private Location beforeMGPosition;
    private ItemStack[] beforeMGInventory;
    private Minigame currentGame;
    private PlayerConfig conf;
    private PlayerState state;
    private GameMode previousGamemode;
    private long gameTime;
    private Date lastDeath;
    private boolean alive;
    private PlayerHandler handler;

    //Keep track of the progress in each game. (e.g. Race: The last checkpoint that you have gone through)
    private int progress;

    @Override
    public String toString() {
        return "MinigamePlayer{" +
                "player=" + player +
                ", highScores=" + highScores +
                ", winCount=" + winCount +
                ", currentLifeCount=" + currentLifeCount +
                ", beforeMGPosition=" + beforeMGPosition +
                ", beforeMGInventory=" + Arrays.toString(beforeMGInventory) +
                ", currentGame=" + currentGame +
                ", conf=" + conf +
                ", state=" + state +
                ", previousGamemode=" + previousGamemode +
                '}';
    }

    public MinigamePlayer (Player p, boolean reset){
        player = p;
//        conf = new PlayerConfig(p);
        conf = PlayerStore.getConfig(p);
        handler = new PlayerHandler(this);

        if(conf == null || reset){ // if config doesn't exist
            conf = PlayerStore.createConfig(p);

            conf.set("uuid", player.getUniqueId().toString());

            beforeMGPosition = player.getLocation();
            conf.set("beforePosition", beforeMGPosition);

            beforeMGInventory = player.getInventory().getContents();
            conf.set("beforeInventory",beforeMGInventory);

            previousGamemode = player.getGameMode();
            conf.set("previousGamemode", previousGamemode.name());

            conf.saveUserFile();
        }else{
            beforeMGPosition = (Location) conf.get("beforePosition");
            List<ItemStack> items = (List<ItemStack>) conf.getList("beforeInventory");
            beforeMGInventory = items.toArray(new ItemStack[items.size()]);
            previousGamemode = GameMode.valueOf((String)conf.get("previousGamemode"));

//            System.out.println("PREVIOUS GAMEMODE LOADED: " + GameMode.valueOf((String)conf.get("previousGamemode")));
        }
    }

    public PlayerHandler getHandler() {
        return handler;
    }

    public void loadItems(){
        player.getInventory().setContents(beforeMGInventory);
    }

    public void clearItems(){
        beforeMGInventory = player.getInventory().getContents();
        conf.set("beforeInventory",beforeMGInventory);
        conf.saveUserFile();
        player.getInventory().clear();
    }

    public Location getBeforeMGPosition() {
        return beforeMGPosition;
    }

    public Player getPlayerObject(){return player;}

    public GameMode getPreviousGamemode() {
        return previousGamemode;
    }

    public PlayerState getState() {
        return state;
    }

    public void setState(PlayerState state) {
        this.state = state;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public int getCurrentLifeCount() {
        return currentLifeCount;
    }

    public void setCurrentLifeCount(int currentLifeCount) {
        this.currentLifeCount = currentLifeCount;
    }

    public long getGameTime() {
        return gameTime;
    }

    public void setGameTime(long gameTime) {
        this.gameTime = gameTime;
    }

    public Date getLastDeath() {
        return lastDeath;
    }

    public void setLastDeath(Date lastDeath) {
        this.lastDeath = lastDeath;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }
}
