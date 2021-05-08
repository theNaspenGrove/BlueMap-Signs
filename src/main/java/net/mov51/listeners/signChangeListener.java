package net.mov51.listeners;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

import java.util.Locale;

import static net.mov51.signHandlers.markerHandler.markSignParser;

public class signChangeListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onSignChange(SignChangeEvent e) {
        String[] SignText = e.getLines();
        Player p = e.getPlayer();
        Location l = e.getBlock().getLocation();

        switch (SignText[0].toLowerCase(Locale.ROOT)) {
            case "[mark]":
                markSignParser(p,SignText,l);
                break;
            case "[shape]":

                break;

            default:
                break;
        }
    }
}
