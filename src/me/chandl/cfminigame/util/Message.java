package me.chandl.cfminigame.util;

import me.chandl.cfminigame.handler.GameHandler;
import me.chandl.cfminigame.minigame.player.MinigamePlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * Message - A Utility Class to send Messages to Specific Players
 *
 * <p>
 *     Prefixes messages with [CFMinigame] and has options for sending messages to
 *     Specific Players, All Players, and All Players in the Current Minigame.
 * </p>
 *
 * @author Chandler me@cseverson.com
 * @version 1.0
 * @since Aug 19, 2017
 *
 */
public class Message {

    /**
     * Sends a formatted message to a specific {@link MinigamePlayer}.
     *
     * @param player The player to send the message to.
     * @param message The message to include.
     */
    public static void player(MinigamePlayer player, String message){
        player.getPlayerObject().sendMessage(TextUtil.formatMessage(message));
    }

    /**
     * Sends a formatted message with a Status code to a specific {@link MinigamePlayer}.
     *
     * @param player The player to send the message to.
     * @param status The status code to show in the message.
     * @param message The message to include.
     */
    public static void player(MinigamePlayer player, String status, String message){
        player.getPlayerObject().sendMessage(TextUtil.formatMessage(status, message));
    }

    /**
     * Sends a formatted message to a specific {@link Player}.
     *
     * @param player The player to send the message to.
     * @param message The message to include.
     */
    public static void player(Player player, String message){
        player.sendMessage(TextUtil.formatMessage(message));
    }

    /**
     * Sends a formatted message with a Status code to a specific {@link Player}.
     *
     * @param player The player to send the message to.
     * @param status The status code to show in the message.
     * @param message The message to include.
     */
    public static void player(Player player, String status, String message){
        player.sendMessage(TextUtil.formatMessage(status, message));
    }

    /**
     * Sends a formatted message to all {@link Player}s in the server.
     *
     * @param message The message to send to all players.
     */
    public static void allPlayers(String message){
        for(Player p : Bukkit.getServer().getOnlinePlayers()){
            p.sendMessage(TextUtil.formatMessage(message));
        }
    }

    /**
     * Sends a formatted message with a status code to all {@link Player}s in the server.
     *
     * @param message The message to send to all players.
     * @param status The status code to include in the message.
     */
    public static void allPlayers(String status, String message){
        for(Player p : Bukkit.getServer().getOnlinePlayers()){
            p.sendMessage(TextUtil.formatMessage(status, message));
        }
    }

    /**
     * Sends a formatted message to all players in the server.
     *
     * @param message The message to send to all players.
     */
    public static void playersInGame(String message){
        for(MinigamePlayer p : GameHandler.getHandler().getPlayerList()){
            p.getPlayerObject().sendMessage(TextUtil.formatMessage(message));
        }
    }


}
