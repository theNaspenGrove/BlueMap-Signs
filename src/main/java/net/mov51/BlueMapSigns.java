package net.mov51;

import net.mov51.listeners.signBreakListener;
import net.mov51.listeners.signChangeListener;
import org.bukkit.plugin.java.JavaPlugin;

public final class BlueMapSigns extends JavaPlugin {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new signChangeListener(), this);
        getServer().getPluginManager().registerEvents(new signBreakListener(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
