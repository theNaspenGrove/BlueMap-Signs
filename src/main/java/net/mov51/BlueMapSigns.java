package net.mov51;

import de.bluecolored.bluemap.api.BlueMapAPI;
import net.mov51.listeners.signBreakListener;
import net.mov51.listeners.signChangeListener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

import static net.mov51.helpers.chatHelper.sendLogWarning;
import static net.mov51.helpers.iconHelper.makeData;

public final class BlueMapSigns extends JavaPlugin {

    public static org.bukkit.plugin.Plugin plugin = null;

    @Override
    public void onEnable() {
        plugin = this;

        File data = plugin.getDataFolder();
        String iconDataP = data + "/icons";
        File iconDataF = new File(iconDataP);

        plugin.saveDefaultConfig();

        if(!iconDataF.exists()){
            boolean wasCreate = iconDataF.mkdirs();
            if(wasCreate){
                sendLogWarning("Plugin data folder has been created!");
            }else{
                sendLogWarning("Plugin data folder was unable to be created!");
            }
        }

        //Wait for the BlueMap API to enable and then make image Data
        BlueMapAPI.onEnable(api -> {
            //Create and process icons
            makeData();
        });


        getServer().getPluginManager().registerEvents(new signChangeListener(), this);
        getServer().getPluginManager().registerEvents(new signBreakListener(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
