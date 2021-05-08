package net.mov51.listeners;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import static net.mov51.helpers.BlueMapApiHelper.removeMarkerPOI;

public class signBreakListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onSignBreak(BlockBreakEvent e) {
        Block b = e.getBlock();
        if (b.getState() instanceof Sign){
            Location l = b.getLocation();
            removeMarkerPOI(l);
        }
    }
}
