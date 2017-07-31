package me.chandl.cfminigame.minigame;

import me.chandl.cfminigame.database.PlayerConfig;
import me.chandl.cfminigame.scores.HighScore;
import me.chandl.cfminigame.scores.WinCount;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import java.util.List;

public class MinigamePlayer {
    private Player player;
    private List<HighScore> highScores;
    private List<WinCount> winCount;
    private int currentLifeCount;
    private Location beforeMGPosition;
    private ItemStack[] beforeMGInventory;
    private Minigame currentGame;
    private PlayerConfig conf;

    public MinigamePlayer (Player p){
        player = p;
        conf = new PlayerConfig();

        if(!conf.loadConfig(player) ){ // if config doesn't exist
            conf.createUser(conf.getPlayerFile());


            conf.set("uuid", player.getUniqueId().toString());
            conf.set("beforePosition", player.getLocation());
            beforeMGInventory = player.getInventory().getContents();
            conf.set("beforeInventory",beforeMGInventory);

            conf.saveUserFile();
        }else{
            beforeMGPosition = (Location) conf.get("beforePosition");
            List<ItemStack> items = (List<ItemStack>) conf.getList("beforeInventory");
            beforeMGInventory = items.toArray(new ItemStack[items.size()]);
        }
    }

    public void loadItems(){
        player.getInventory().setContents(beforeMGInventory);
    }

    public void clearItems(){
        player.getInventory().clear();
    }

    public Location getBeforeMGPosition() {
        return beforeMGPosition;
    }
}
