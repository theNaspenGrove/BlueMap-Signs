package net.mov51.blueMapSigns.helpers;

import de.bluecolored.bluemap.api.BlueMapAPI;
import net.mov51.blueMapSigns.BlueMapSigns;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static net.mov51.blueMapSigns.BlueMapSigns.aspenLogHelper;

public class iconHelper {

    public static Map<String,pairHelper<String,BufferedImage>> icons = new HashMap<>();

    public static void makeData(){
        BlueMapAPI.getInstance().ifPresentOrElse(api -> {
            //code executed when the api is enabled (skipped if the api is not enabled)
            File f = BlueMapSigns.plugin.getDataFolder();
            Path fP = Paths.get(f.toPath() + "/icons");
            File fF = fP.toFile();
            ArrayList<String> madeIcons = new ArrayList<String>();


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

                                madeIcons.add(cleanName);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }else{
                            aspenLogHelper.sendLogWarning("File " + file.getName() + " is not a png image!");
                        }
                    }else{
                        aspenLogHelper.sendLogWarning("Directory " + file.getName() + " is not a file! Subdirectories are not currently searched!");
                    }
                }
                aspenLogHelper.sendLogInfo("Added icons " + formatArray(madeIcons));
            }
        }, () -> {
            //If api is not present, please tell me 😭
            aspenLogHelper.sendLogSevere("BlueMap API not present! Trying to initialize Icons in IconHelper!");
        });
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
