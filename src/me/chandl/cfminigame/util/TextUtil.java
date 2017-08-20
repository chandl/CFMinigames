package me.chandl.cfminigame.util;

import org.bukkit.ChatColor;

/**
 * TextUtil - A utility class to format messages for CFMinigame dialogue.
 *
 * @author Chandler me@cseverson.com
 * @version 1.0
 * @since Aug 19, 2017
 */
public class TextUtil {

    /**
     * Formats a message with the [CFMinigame] Prefix.
     *
     * @param msg The message to send.
     * @return A String with "[CFMinigame]" prefixing the {@code msg} string. Formatted with color.
     */
    public static String formatMessage(String msg){
        return String.format("%s%s[CFMinigame] %s%s", ChatColor.BOLD, ChatColor.RED, ChatColor.WHITE, msg);
    }

    /**
     * Formats a message with the [CFMinigame] Prefix and a Status code.
     *
     * @param msg The message to send.
     * @param status The status code to include in the prefix.
     * @return A String with "[CFMinigame STATUS]" prefixing the {@code msg} string. Formatted with color.
     */
    public static String formatMessage(String status, String msg){
        return String.format("%s%s[CFMinigame %s] %s%s", ChatColor.BOLD, ChatColor.RED, status, ChatColor.WHITE, msg);
    }
}
