package me.chandl.cfminigame.util;

import me.chandl.cfminigame.CFMinigame;
import me.chandl.cfminigame.GameHandler;
import me.chandl.cfminigame.minigame.MinigamePlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * Created by chandler on 7/31/17.
 */
public class Message {

    public static void player(MinigamePlayer player, String message){
        player.getPlayerObject().sendMessage(TextUtil.formatMessage(message));
    }
    public static void player(MinigamePlayer player, String status, String message){
        player.getPlayerObject().sendMessage(TextUtil.formatMessage(status, message));
    }

    public static void player(Player player, String message){
        player.sendMessage(TextUtil.formatMessage(message));
    }
    public static void player(Player player, String status, String message){
        player.sendMessage(TextUtil.formatMessage(status, message));
    }

    public static void allPlayers(String message){
        for(Player p : Bukkit.getServer().getOnlinePlayers()){
            p.sendMessage(TextUtil.formatMessage(message));
        }
    }

    public static void allPlayers(String status, String message){
        for(Player p : Bukkit.getServer().getOnlinePlayers()){
            p.sendMessage(TextUtil.formatMessage(status, message));
        }
    }
    public static void playersInGame(String message){
        for(MinigamePlayer p : GameHandler.getHandler().getPlayerList()){
            p.getPlayerObject().sendMessage(TextUtil.formatMessage(message));
        }
    }
}
