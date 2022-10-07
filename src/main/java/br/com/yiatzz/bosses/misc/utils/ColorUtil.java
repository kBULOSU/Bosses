package br.com.yiatzz.bosses.misc.utils;

import org.bukkit.ChatColor;

public class ColorUtil {

    public static String colorIt(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

}
