package net.mov51.helpers;

import de.bluecolored.bluemap.api.BlueMapAPI;
import net.mov51.BlueMapSigns;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class iconHelper {

    public static Map<String,pairHelper<String,BufferedImage>> icons = new HashMap<>();

    public static void makeData(){
        BlueMapAPI.getInstance().ifPresent(api -> {
            //code executed when the api is enabled (skipped if the api is not enabled)
            File f = BlueMapSigns.plugin.getDataFolder();

            File[] files = f.listFiles();
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

                                //TODO use console log handler
                                System.out.println("added " + cleanName);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        });
    }

}
