package net.mov51.blueMapSigns.helpers;

import net.mov51.blueMapSigns.BlueMapSigns;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class chatHelper {

    public static Logger logger = BlueMapSigns.plugin.getLogger();

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

    public static String formatArray(ArrayList<String> array) {
        StringBuilder out = new StringBuilder();
        for (int i = 0; i < array.size(); i++) {
            if(i != 0){
                out.append(", ").append(array.get(i));
            }else out.append(array.get(0));
        }
        return out.toString();
    }
}
