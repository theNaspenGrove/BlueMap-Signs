package net.mov51.helpers;

import de.bluecolored.bluemap.api.BlueMapAPI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static net.mov51.helpers.chatHelper.sendMessage;
import static net.mov51.markerHandlers.markerSetHandler.*;

public class commandHelper implements CommandExecutor {

    private static final String permPrefix = "BlueMapSigns.";
    public static final String CreateMarkerSetCommand = "CreateMarkerSet";
    public static final String CreateMarkerSetPerm = "CreateSet";

    public static final String DeleteMarkerSetCommand = "DeleteMarkerSet";
    public static final String DeleteMarkerSetPerm = "DeleteSet";

    public static final String ListMarkerSetsCommand = "ListMarkerSets";
    public static final String ListMarkerSetsPerm = "ListSets";


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(isPlayer(sender)) {
            Player p = (Player) sender;
            if (args.length != 0) {
                switch (args[0]) {
                    case CreateMarkerSetCommand:
                        if (hasPermission(p, CreateMarkerSetPerm)) {
                            if (args.length == 2) {
                                BlueMapAPI.getInstance().ifPresentOrElse(api -> {
                                    if (createMarkerSetByName(api, args[1])) {
                                        sendMessage(p, "Marker set " + args[1] + " created!");
                                    } else {
                                        sendMessage(p, "Marker set " + args[1] + " already existed!");
                                    }
                                }, () -> {
                                    //todo throw noAPI error
                                });
                            }
                        }
                        return true;
                    case DeleteMarkerSetCommand:
                        //todo delete marker set
                        if (hasPermission(p, DeleteMarkerSetPerm)) {
                            if (args.length == 2) {
                                BlueMapAPI.getInstance().ifPresentOrElse(api -> {
                                    if (deleteMarkerSetByName(api, args[1])) {
                                        sendMessage(p, "Marker set " + args[1] + " has been removed!");
                                    } else {
                                        sendMessage(p, "Marker set " + args[1] + " doesn't exist!");
                                    }
                                }, () -> {
                                    //todo throw noAPI error
                                });
                            }
                        }
                        return true;
                    case ListMarkerSetsCommand:
                        //todo list markerSet
                        if (hasPermission(p, ListMarkerSetsPerm)) {
                            BlueMapAPI.getInstance().ifPresentOrElse(api ->
                                    sendMessage(p,"The current marker sets loaded by blueMap-Signs are " + listMarkerSets(api)), () -> {
                                //todo throw noAPI error
                            });
                        }
                        return true;
                    default:
                        //todo explain what can be do
                        sendMessage(p, "You broke it!");
                        return true;
                }
            } else {
                sendMessage(p, "Please run /BlueMap-Signs help to for help!");
            }
        }
        return false;
    }

    public static boolean isPlayer(CommandSender s){
        return s instanceof Player;
    }

    public static boolean hasPermission(Player p, String perm){
        return p.hasPermission(permPrefix+perm);
    }
}
