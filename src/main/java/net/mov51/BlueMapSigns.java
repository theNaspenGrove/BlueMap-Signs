package net.mov51;

import de.bluecolored.bluemap.api.BlueMapAPI;
import net.mov51.helpers.commandHelper;
import net.mov51.helpers.tabCompleteHelper;
import net.mov51.listeners.signBreakListener;
import net.mov51.listeners.signChangeListener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Objects;

import static net.mov51.helpers.chatHelper.sendLogWarning;
import static net.mov51.helpers.iconHelper.makeData;

public final class BlueMapSigns extends JavaPlugin {

    public static org.bukkit.plugin.Plugin plugin = null;
    public static String mainCommand = "BlueMap-Signs";

    @Override
    public void onEnable() {
        plugin = this;

        File data = plugin.getDataFolder();
        String iconDataP = data + "/icons";
        File iconDataF = new File(iconDataP);

        plugin.saveDefaultConfig();

        Objects.requireNonNull(getCommand(mainCommand)).setExecutor(new commandHelper());
        Objects.requireNonNull(getCommand(mainCommand)).setTabCompleter(new tabCompleteHelper());

        if(!iconDataF.exists()){
            boolean wasCreate = iconDataF.mkdirs();
            if(wasCreate){
                sendLogWarning("Plugin data folder has been created!");
            }else{
                sendLogWarning("Plugin data folder was unable to be created!");
            }
        }

        //Wait for the BlueMap API to enable and then make image Data
        BlueMapAPI.onEnable(api -> makeData());

        getServer().getPluginManager().registerEvents(new signChangeListener(), this);
        getServer().getPluginManager().registerEvents(new signBreakListener(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
