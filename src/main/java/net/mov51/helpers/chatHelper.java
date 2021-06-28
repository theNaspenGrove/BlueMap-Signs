package net.mov51.helpers;

import net.mov51.BlueMapSigns;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.logging.Level;
import java.util.logging.Logger;

public class chatHelper {

    public static String chatPrefix = BlueMapSigns.plugin.getConfig().getString("chat-prefix");
    public static Logger logger = BlueMapSigns.plugin.getLogger();

    public static void sendMessage(Player p, String[] messages){
        String colorPrefix = ChatColor.translateAlternateColorCodes('&', chatPrefix);
        for (String message : messages)
        {
            p.sendMessage(colorPrefix + " " + message);
        }
    }

    public static void sendMessage(Player p, String message){
        String colorPrefix = ChatColor.translateAlternateColorCodes('&', chatPrefix);

        p.sendMessage(colorPrefix + " " + message);
    }

    public static void sendLogInfo(String message){
        logger.log(Level.INFO, " " + message);
    }

    public static void sendLogWarning(String[] message){
        logger.log(Level.WARNING, " --BlueMapSigns Warning--");
        for (String log : message)
        {
            logger.log(Level.WARNING, " " + log);
        }
        logger.log(Level.WARNING, " --BlueMapSigns Warning--");
    }

    public static void sendLogWarning(String message){
        String[] oneLineWarning = new String[] {message};
        sendLogWarning(oneLineWarning);
    }


    public static void sendLogSevere(String[] message){
        logger.log(Level.SEVERE, " ---BlueMapSigns Fatal Error--");
        for (String log : message)
        {
            logger.log(Level.SEVERE, " " + log);
        }
        logger.log(Level.SEVERE, " ---BlueMapSigns Fatal Error---");
    }

    public static void sendLogSevere(String message){
        String[] oneLineSevere = new String[] {message};
        sendLogSevere(oneLineSevere);
    }

}
