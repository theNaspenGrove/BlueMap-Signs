package net.mov51.helpers;

import net.mov51.BlueMapSigns;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class iconHelper {

    public static void makeData(){
        File f = BlueMapSigns.plugin.getDataFolder();

        File[] files = f.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    System.out.print("directory:");
                } else {
                    System.out.print("     file:");
                }
                try {
                    System.out.println(file.getCanonicalPath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

}
