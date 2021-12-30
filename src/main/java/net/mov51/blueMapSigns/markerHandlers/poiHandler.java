package net.mov51.blueMapSigns.markerHandlers;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import static net.mov51.blueMapSigns.BlueMapSigns.aspenChatHelper;
import static net.mov51.blueMapSigns.helpers.BlueMapApiHelper.createMarkerPOI;
import static net.mov51.blueMapSigns.markerHandlers.markerSetHandler.defaultMarkerSetName;

public class poiHandler {
    public static void markSignParser(Player p, String[] Text, Location l){

        String name = p.getName();

        if(Text[1].length() == 0){
            aspenChatHelper.sendChat(p,"You're missing the marker name! Please place the name of your marker on line 2!");
        }else if(Text[2].length() == 0 && Text[3].length() == 0){
            aspenChatHelper.sendChat(p,"Ok "+name+"! I'll make a marker with the default icon and a name of \"" + Text[1] + "\"");
            //use overloaded method to create a marker with the default POI icon
            createMarkerPOI(Text[1], l,p);

        }else if(Text[3].length() == 0){
            aspenChatHelper.sendChat(p,"Ok "+name+"! I'll make a marker with the name of \""  + Text[1] + "\" and the icon \"" + Text[2] + "\"");
            //create a marker with the provided icon
            createMarkerPOI(Text[1],l,Text[2],defaultMarkerSetName,p);
        }else{
            //take the 4th line of the sign as a markerSet
            aspenChatHelper.sendChat(p,"Ok "+name+"! I'll make a marker with the name of \""  + Text[1] + "\" in marker set \"" + Text[3] + "\"");
            createMarkerPOI(Text[1],l,Text[2],Text[3],p);
        }
    }
}
