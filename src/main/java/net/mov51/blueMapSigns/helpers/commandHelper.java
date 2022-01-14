package net.mov51.blueMapSigns.helpers;

import de.bluecolored.bluemap.api.BlueMapAPI;
import net.mov51.periderm.paper.logs.AspenLogHelper;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static net.mov51.blueMapSigns.BlueMapSigns.aspenChatHelper;
import static net.mov51.blueMapSigns.BlueMapSigns.aspenLogHelper;
import static net.mov51.blueMapSigns.helpers.tabCompleteHelper.whatCanRun;
import static net.mov51.blueMapSigns.markerHandlers.markerSetHandler.*;

public class commandHelper implements CommandExecutor {

    private static final String permPrefix = "BlueMapSigns.";
    public static final String CreateMarkerSetCommand = "CreateMarkerSet";
    public static final String CreateMarkerSetPerm = "CreateSet";

    public static final String DeleteMarkerSetCommand = "DeleteMarkerSet";
    public static final String DeleteMarkerSetPerm = "DeleteSet";

    public static final String ListMarkerSetsCommand = "ListMarkerSets";
    public static final String ListMarkerSetsPerm = "ListSets";


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if(isPlayer(sender)) {
            Player p = (Player) sender;
            if (args.length != 0) {
                switch (args[0]) {
                    case CreateMarkerSetCommand:
                        if (hasPermission(p, CreateMarkerSetPerm)) {
                            if (args.length == 2) {
                                BlueMapAPI.getInstance().ifPresentOrElse(api -> {
                                    if (createMarkerSet(api, args[1])) {
                                        aspenChatHelper.sendChat(p, "Marker set " + args[1] + " created!");
                                    } else {
                                        aspenChatHelper.sendChat(p, "Marker set " + args[1] + " was not created");
                                    }
                                }, () -> {
                                    //todo throw noAPI error
                                });
                            }
                        }
                        return true;
                    case DeleteMarkerSetCommand:
                        if (hasPermission(p, DeleteMarkerSetPerm)) {
                            if (args.length == 2) {
                                BlueMapAPI.getInstance().ifPresentOrElse(api -> {
                                    AspenMarkerSet set = getMarkerSetFromName(args[1]);
                                    if(set != null){
                                        if (deleteMarkerSet(api, set)) {
                                            aspenChatHelper.sendChat(p, "Marker set " + args[1] + " has been removed!");
                                        } else {
                                            aspenChatHelper.sendChat(p, "Marker set " + args[1] + " wasn't able to be deleted :(");
                                        }
                                    }else{
                                        aspenChatHelper.sendChat(p, "Marker set " + args[1] + " doesn't exist!");
                                    }
                                }, () -> {
                                    //todo throw noAPI error
                                });
                            }
                        }
                        return true;
                    case ListMarkerSetsCommand:
                        if (hasPermission(p, ListMarkerSetsPerm)) {
                            BlueMapAPI.getInstance().ifPresentOrElse(api ->
                                aspenChatHelper.sendChat(p, "The current marker sets loaded by blueMap-Signs are " + listMarkerSets())
                            , () -> {
                                //todo throw noAPI error
                            });
                        }
                        return true;
                    default:
                        boolean canRun = (hasPermission(p, CreateMarkerSetPerm) || hasPermission(p, DeleteMarkerSetPerm) || hasPermission(p, ListMarkerSetsPerm));
                        if(canRun){

                            String[] response = whatCanRun(p).toArray(new String[0]);
                            aspenChatHelper.sendChat(p, "Please run one of these commands ");
                            aspenChatHelper.sendChat(p, response);
                        }
                        return true;
                }
            } else {
                aspenChatHelper.sendChat(p, "Check out this wiki page on what this command can do!");
                aspenChatHelper.sendChat(p, "https://github.com/theAspenGrove/BlueMap-Signs/wiki/Commands");

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
