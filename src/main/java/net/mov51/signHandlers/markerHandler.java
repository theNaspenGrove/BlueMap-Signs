package net.mov51.signHandlers;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import static net.mov51.helpers.BlueMapApiHelper.createMarkerPOI;
import static net.mov51.helpers.chatHelper.sendMessage;

public class markerHandler {
    public static void markSignParser(Player p, String[] Text, Location l){

        String name = p.getName();

        if(Text[1].length() == 0){
            //TODO output help message with each line
            sendMessage(p,"You're missing the marker name! Please place the name of your marker on line 2!");
        }else if(Text[2].length() == 0){
            sendMessage(p,"Ok "+name+"! I'll make a marker with the default icon and a name of \"" + Text[1] + "\"");
            createMarkerPOI(Text[1], l);

        }else if(Text[3].length() == 0){
            //TODO send location, name, and icon to API string 1 location icon
            sendMessage(p,"Ok "+name+"! I'll make a marker with the name of \""  + Text[1] + "\" and the icon \"" + Text[2] + "\"");
            createMarkerPOI(Text[1],l,Text[2]);
        }else{
            //TODO send location, name and icon
            //can't send map because I can't get the name of the map to the user easily
            //map handling will have to be done automatically when the API exposes the map bounds
            sendMessage(p,"I don't really need 4 Lines, but thank you "+name+"! I'll make a marker with the name of \""  + Text[1] + "\" and the icon \"" + Text[2] + "\".");
            sendMessage(p,"Your message \"" + Text[3] + "\" meant a lot to me, so I'll get that marker done asap!");
        }
    }
}
