package net.mov51;

import net.mov51.listeners.signPlaceListener;
import org.bukkit.plugin.java.JavaPlugin;

public final class BlueMapSigns extends JavaPlugin {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new signPlaceListener(), this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
