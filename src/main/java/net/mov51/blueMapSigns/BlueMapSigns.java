package net.mov51.blueMapSigns;

import de.bluecolored.bluemap.api.BlueMapAPI;
import net.mov51.blueMapSigns.helpers.commandHelper;
import net.mov51.blueMapSigns.helpers.tabCompleteHelper;
import net.mov51.blueMapSigns.listeners.signBreakListener;
import net.mov51.blueMapSigns.listeners.signChangeListener;
import net.mov51.periderm.paper.AspenChatHelper;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Objects;

import static net.mov51.blueMapSigns.helpers.chatHelper.sendLogWarning;
import static net.mov51.blueMapSigns.helpers.iconHelper.makeData;

public final class BlueMapSigns extends JavaPlugin {

    public static org.bukkit.plugin.Plugin plugin = null;
    public static AspenChatHelper aspenChatHelper;
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

        String ConfigChatPrefix = BlueMapSigns.plugin.getConfig().getString("chat-prefix");
        aspenChatHelper = new AspenChatHelper((ConfigChatPrefix != null) ? ConfigChatPrefix : "BlueMap-Signs");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
