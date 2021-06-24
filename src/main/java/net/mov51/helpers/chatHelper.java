package net.mov51.helpers;

import net.mov51.BlueMapSigns;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.logging.Level;
import java.util.logging.Logger;

public class chatHelper {

    public static String chatPrefix = BlueMapSigns.plugin.getConfig().getString("chat-prefix");
    public static Logger logger = BlueMapSigns.plugin.getLogger();

    public static void sendMessage(Player p, String message){
        String colorPrefix = ChatColor.translateAlternateColorCodes('&', chatPrefix);

        p.sendMessage(colorPrefix + " " + message);
    }

    public static void sendLogInfo(String message){
        logger.log(Level.INFO, " " + message);
    }

    public static void sendLogWarning(String message){
        logger.log(Level.WARNING, " " + message);
    }

    public static void sendLogSevere(String message){
        logger.log(Level.SEVERE, " ---BlueMapSigns Fatal Error---");
        logger.log(Level.SEVERE, " " + message);
        logger.log(Level.SEVERE, " ---BlueMapSigns Fatal Error---");
    }

}
