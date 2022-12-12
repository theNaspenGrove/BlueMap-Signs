package net.mov51.blueMapSigns;

import de.bluecolored.bluemap.api.BlueMapAPI;
import net.mov51.blueMapSigns.listeners.signBreakListener;
import net.mov51.blueMapSigns.listeners.signChangeListener;
import net.mov51.periderm.chat.AspenChatHelper;
import net.mov51.periderm.logs.AspenLogHelper;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.logging.Logger;

import static net.mov51.blueMapSigns.helpers.iconHelper.makeData;

public final class BlueMapSigns extends JavaPlugin {

    public static org.bukkit.plugin.Plugin plugin = null;
    public static AspenChatHelper aspenChatHelper;
    public static AspenLogHelper aspenLogHelper;
    public static Logger blueLogger;
    public static String mainCommand = "BlueMap-Signs";

    @Override
    public void onEnable() {
        plugin = this;

        String ConfigChatPrefix = BlueMapSigns.plugin.getConfig().getString("chat-prefix");
        aspenChatHelper = new AspenChatHelper((ConfigChatPrefix != null) ? ConfigChatPrefix : "BlueMap-Signs");

        blueLogger = BlueMapSigns.plugin.getLogger();
        aspenLogHelper = new AspenLogHelper(blueLogger,"BlueMap-Signs");

        File data = plugin.getDataFolder();
        String iconDataP = data + "/icons";
        File iconDataF = new File(iconDataP);

        plugin.saveDefaultConfig();

        if(!iconDataF.exists()){
            boolean wasCreate = iconDataF.mkdirs();
            if(wasCreate){
                aspenLogHelper.sendLogWarning("Plugin data folder has been created!");
            }else{
                aspenLogHelper.sendLogWarning("Plugin data folder was unable to be created!");
            }
        }

        //Wait for the BlueMap API to enable
        BlueMapAPI.onEnable(api -> {
            //make image Data
            makeData();
            //load marker sets currently controlled by BlueMap signs
        });

        getServer().getPluginManager().registerEvents(new signChangeListener(), this);
        getServer().getPluginManager().registerEvents(new signBreakListener(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
