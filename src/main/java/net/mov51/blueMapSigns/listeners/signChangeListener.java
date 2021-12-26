package net.mov51.blueMapSigns.listeners;

import net.mov51.blueMapSigns.BlueMapSigns;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

import static net.mov51.blueMapSigns.helpers.chatHelper.sendMessage;
import static net.mov51.blueMapSigns.markerHandlers.poiHandler.markSignParser;

public class signChangeListener implements Listener {

    public static String signPrefix = BlueMapSigns.plugin.getConfig().getString("sign-prefix");

    @EventHandler(priority = EventPriority.MONITOR)
    public void onSignChange(SignChangeEvent e) {
        String[] SignText = e.getLines();
        Player p = e.getPlayer();
        Location l = e.getBlock().getLocation();

        if (SignText[0].equalsIgnoreCase(signPrefix)) {
            if(p.hasPermission("BlueMap-Signs.createPOI")){
                markSignParser(p,SignText,l);
            }else{
                sendMessage(p,"Sorry! You don't have permission to create a POI marker!");
            }
        }//else if(SignText[0].equalsIgnoreCase("[shape]")){
            //TODO implement shapes
        //}
    }
}
