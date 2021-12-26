package net.mov51.blueMapSigns.listeners;

import org.bukkit.Location;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import static net.mov51.blueMapSigns.helpers.BlueMapApiHelper.removeMarkerPOI;

public class signBreakListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onSignBreak(BlockBreakEvent e) {
        Block b = e.getBlock();
        if (b.getState() instanceof Sign){
            Location l = b.getLocation();
            removeMarkerPOI(l);
        }
        indirectBreak(b);
    }


    private static void indirectBreak(Block b){
        for (BlockFace f : BlockFace.values()) {
            if (Tag.SIGNS.isTagged(b.getRelative(f).getType())) {
                Location l = b.getRelative(f).getLocation();
                removeMarkerPOI(l);
            }
        }
    }
}
