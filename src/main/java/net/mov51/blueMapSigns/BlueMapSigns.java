package net.mov51.blueMapSigns;

import de.bluecolored.bluemap.api.BlueMapAPI;
import net.mov51.blueMapSigns.listeners.signBreakListener;
import net.mov51.blueMapSigns.listeners.signChangeListener;
import net.mov51.periderm.chat.AspenChatHelper;
import net.mov51.periderm.logs.AspenLogHelper;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.logging.Logger;

import static net.mov51.blueMapSigns.helpers.iconHelper.makeIconData;
import static net.mov51.blueMapSigns.markerHandlers.markerSetHandler.*;

public final class BlueMapSigns extends JavaPlugin {

    public static org.bukkit.plugin.Plugin plugin = null;
    public static AspenChatHelper aspenChatHelper;
    public static AspenLogHelper aspenLogHelper;
    public static Logger blueLogger;
    public static File markerDataFolder;
    public static File iconDataFolder;

    @Override
    public void onEnable() {
        plugin = this;

        //check if there isn't a chat prefix defined, use a default one.
        String ConfigChatPrefix = BlueMapSigns.plugin.getConfig().getString("chat-prefix");
        aspenChatHelper = new AspenChatHelper((ConfigChatPrefix != null) ? ConfigChatPrefix : "BlueMap-Signs");

        //get plugin data folder for use with icon/marker folders
        File dataFolder = BlueMapSigns.plugin.getDataFolder();
        blueLogger = BlueMapSigns.plugin.getLogger();
        aspenLogHelper = new AspenLogHelper(blueLogger,"BlueMap-Signs");
        //save configuration file
        plugin.saveDefaultConfig();

        //generate icon data folder location
        String iconDataP = dataFolder + "/icons";
        iconDataFolder = new File(iconDataP);

        //check for and create icon data folder
        if(!iconDataFolder.exists()){
            boolean wasCreate = iconDataFolder.mkdirs();
            if(wasCreate){
                aspenLogHelper.sendLogWarning("Plugin icon data folder has been created!");
            }else{
                aspenLogHelper.sendLogWarning("Plugin icon data folder was unable to be created!");
            }
        }


        //generate marker data folder location
        String markerDataPath = dataFolder + "/marker-sets/";
        markerDataFolder = new File(markerDataPath);

        //check for and create icon data folder
        if(!markerDataFolder.exists()){
            boolean wasCreate = markerDataFolder.mkdirs();
            if(wasCreate){
                aspenLogHelper.sendLogWarning("Plugin marker data folder has been created!");
            }else{
                aspenLogHelper.sendLogWarning("Plugin marker data folder was unable to be created!");
            }
        }

        //create and load plugin data on BlueMap load
        BlueMapAPI.onEnable(api -> {
            makeIconData(api);
            loadMarkerSets(api);
            createDefaultMarkerSets(api);
        });

        getServer().getPluginManager().registerEvents(new signChangeListener(), this);
        getServer().getPluginManager().registerEvents(new signBreakListener(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        saveMarkerSets();
    }
}
