package net.mov51.helpers;

import de.bluecolored.bluemap.api.BlueMapAPI;
import de.bluecolored.bluemap.api.marker.MarkerAPI;
import net.mov51.BlueMapSigns;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class iconHelper {

    public static Map<String,String> icons = new HashMap<>();

    public static void makeData(){
        BlueMapAPI.getInstance().ifPresent(api -> {
            //code executed when the api is enabled (skipped if the api is not enabled)
            File f = BlueMapSigns.plugin.getDataFolder();

            File[] files = f.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        System.out.print("directory:");
                    } else {
                        String extension = getFileExtension(file.getName());
                        boolean isPng = extension.equalsIgnoreCase("png");
                        if(isPng){
                            String cleanName = file.getName().split("\\.")[0];

                            String path = null;
                            try {
                                BufferedImage image = ImageIO.read(file);
                                path = api.createImage(image,cleanName);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            icons.put(cleanName,path);
                            System.out.println("added " + cleanName);
                        }
                    }
                    try {
                        System.out.println(file.getCanonicalPath());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public static String getFileExtension(String fullName) {
        if(fullName != null){
            String fileName = new File(fullName).getName();
            int dotIndex = fileName.lastIndexOf('.');
            return (dotIndex == -1) ? "" : fileName.substring(dotIndex + 1);
        }else{
            return null;
        }
    }

}
