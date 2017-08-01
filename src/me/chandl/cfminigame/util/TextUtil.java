package me.chandl.cfminigame.util;

import org.bukkit.ChatColor;

public class TextUtil {

    public static String formatMessage(String msg){
        return String.format("%s%s[CFMinigame] %s%s", ChatColor.BOLD, ChatColor.RED, ChatColor.WHITE, msg);
    }

    public static String formatMessage(String status, String msg){
        return String.format("%s%s[CFMinigame %s] %s%s", ChatColor.BOLD, ChatColor.RED, status, ChatColor.WHITE, msg);
    }
}
