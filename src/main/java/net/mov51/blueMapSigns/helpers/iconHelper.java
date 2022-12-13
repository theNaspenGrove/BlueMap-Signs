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
import static net.mov51.periderm.logs.Strings.formatArray;

public class iconHelper {

    public static Map<String,pairHelper<String,BufferedImage>> icons = new HashMap<>();

    public static void makeIconData(){
        BlueMapAPI.getInstance().ifPresentOrElse(api -> {
            //code executed when the api is enabled (skipped if the api is not enabled)
            File f = BlueMapSigns.plugin.getDataFolder();
            Path fP = Paths.get(f.toPath() + "/icons");
            File fF = fP.toFile();
            ArrayList<String> madeIcons = new ArrayList<>();

            File[] files = fF.listFiles();
            if (files != null) {
                if(files.length > 0){
                    for (File file : files) {
                        if (!file.isDirectory()) {
                            //Split the file name on the '.' to get both clean name and extension
                            String[] fileName = file.getName().split("\\.");
                            //check that the file extension is PNG by selecting the second part of the array
                            if(fileName[1].equalsIgnoreCase("png")){
                                //select the first part of the array to get a clean name for the file
                                String cleanName = fileName[0];
                                //create the path String
                                try {
                                    //Generate the Buffered image from the file
                                    BufferedImage image = ImageIO.read(file);
                                    //create the image and store its path using the BlueMap api
                                    String path = api.getWebApp().createImage(image,cleanName);
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
                }else{
                    aspenLogHelper.sendLogInfo("Added no icons!");
                    aspenLogHelper.sendLogInfo("If you'd like to add some, check out the wiki!");
                    aspenLogHelper.sendLogInfo("https://github.com/theAspenGrove/BlueMap-Signs/wiki/Icons");
                }

            }
        }, () -> {
            //If api is not present, please tell me 😭
            aspenLogHelper.sendLogSevere("BlueMap API not present! Trying to initialize Icons in IconHelper!");
        });
    }

}
