package net.mov51.blueMapSigns.markerHandlers;

import org.bukkit.Location;
import org.bukkit.entity.Player;


import static net.mov51.blueMapSigns.BlueMapSigns.aspenChatHelper;
import static net.mov51.blueMapSigns.helpers.BlueMapApiHelper.createMarkerPOI;

public class poiHandler {
    public static void markSignParser(Player p, String[] Text, Location l){

        String name = p.getName();
        String MarkerName = Text[1];
        String Icon = Text[2];
        String MarkerSetName = Text[3];

        if(MarkerName.length() == 0){
            aspenChatHelper.sendChat(p,"You're missing the marker name! Please place the name of your marker on line 2!");
        }else if(Icon.length() == 0 && MarkerSetName.length() == 0){
            aspenChatHelper.sendChat(p,"Ok " + name + "! I'll make a marker with the default icon and a name of \"" + MarkerName + "\"");
            //use overloaded method to create a marker with the default POI icon
            createMarkerPOI(MarkerName, l);
        }else if(MarkerSetName.length() == 0) {
            aspenChatHelper.sendChat(p, "Ok " + name + "! I'll make a marker with the name of \"" + MarkerName + "\" and the icon \"" + Icon + "\"");
            //create a marker with the provided icon
            createMarkerPOI(MarkerName, l, Icon);
        }else{
            createMarkerPOI(MarkerName, l, Icon, MarkerSetName);
        }
    }
}
