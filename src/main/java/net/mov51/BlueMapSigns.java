package net.mov51;

import de.bluecolored.bluemap.api.BlueMapAPI;
import net.mov51.listeners.signBreakListener;
import net.mov51.listeners.signChangeListener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

import static net.mov51.helpers.iconHelper.makeData;

public final class BlueMapSigns extends JavaPlugin {

    public static org.bukkit.plugin.Plugin plugin = null;

    @Override
    public void onEnable() {
        plugin = this;

        File data = plugin.getDataFolder();

        if(!data.exists()){
            boolean wasCreate = data.mkdirs();
            if(wasCreate){
                System.out.println("Plugin data folder has been created!");
            }else{
                System.out.println("Plugin data folder was unable to be created!");
            }
        }

        BlueMapAPI.onEnable(api -> {
            //code executed when the api got enabled
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
