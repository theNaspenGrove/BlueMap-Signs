package net.mov51.signHandlers;

import org.bukkit.entity.Player;

import static net.mov51.helpers.chatHelper.sendMessage;

public class markerHandler {
    public static void markSignParser(Player p, String[] Text){

        String name = p.getName();

        if(Text[1].length() == 0){
            sendMessage(p,"You're missing the marker name! Please place the name of your marker on line 2!");
        }else if(Text[2].length() == 0){
            sendMessage(p,"Ok "+name+"! I'll make a marker with the default icon and a name of \"" + Text[1] + "\"");
        }else if(Text[3].length() == 0){
            sendMessage(p,"Ok "+name+"! I'll make a marker with the name of \""  + Text[1] + "\" and the icon \"" + Text[2] + "\"");
        }else{
            sendMessage(p,"I don't really need 4 Lines, but thank you "+name+"! I'll make a marker with the name of \""  + Text[1] + "\" and the icon \"" + Text[2] + "\".");
            sendMessage(p,"Your message \"" + Text[3] + "\" meant a lot to me, so I'll get that marker done asap!");
        }

    }
}
