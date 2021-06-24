package net.mov51.helpers;

import de.bluecolored.bluemap.api.BlueMapAPI;
import net.mov51.BlueMapSigns;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import static net.mov51.helpers.chatHelper.*;

public class iconHelper {

    public static Map<String,pairHelper<String,BufferedImage>> icons = new HashMap<>();
    public static String signMarkerSetID = BlueMapSigns.plugin.getConfig().getString("sign-marker-set-ID");

    public static void makeData(){
        BlueMapAPI.getInstance().ifPresentOrElse(api -> {
            try {
                api.getMarkerAPI().createMarkerSet(signMarkerSetID);
                sendLogInfo("created markerSet " + signMarkerSetID);
            } catch (IOException e) {
                e.printStackTrace();
            }


            //code executed when the api is enabled (skipped if the api is not enabled)
            File f = BlueMapSigns.plugin.getDataFolder();
            Path fP = Paths.get(f.toPath() + "/icons");
            File fF = fP.toFile();


            File[] files = fF.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (!file.isDirectory()) {

                        //Split the file name on the . to get both clean name and extension
                        String[] fileName = file.getName().split("\\.");
                        //check that the file extension is PNG by selecting the second part of the array
                        if(fileName[1].equalsIgnoreCase("png")){
                            //select the first part of the array to get a clean name for the file
                            String cleanName = fileName[0];
                            //create the path String
                            try {
                                //Generate the Buffered image from the file
                                BufferedImage image = ImageIO.read(file);
                                //create the image and store it's path using the BlueMap api
                                String path = api.createImage(image,cleanName);
                                //add the new icon to the map for verification later.
                                icons.put(cleanName,new pairHelper<>(path,image));

                                sendLogInfo("added icon " + cleanName);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }else{
                            sendLogWarning("File " + file.getName() + " is not a png image!");
                        }
                    }else{
                        sendLogWarning("Directory " + file.getName() + " is not a file! Subdirectories are not currently searched!");
                    }
                }
            }
        }, () -> {
            //If api is not present, please tell me 😭
            sendLogSevere("BlueMap API not present! Trying to initialize Icons in IconHelper!");
        });
    }

}
