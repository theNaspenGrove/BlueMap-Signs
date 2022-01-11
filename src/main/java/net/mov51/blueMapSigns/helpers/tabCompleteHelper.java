package net.mov51.blueMapSigns.helpers;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static net.mov51.blueMapSigns.BlueMapSigns.mainCommand;
import static net.mov51.blueMapSigns.helpers.commandHelper.*;

public class tabCompleteHelper implements TabCompleter {
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, Command command, @NotNull String s, String[] strings) {
        if(command.getName().equals(mainCommand)){
            if(isPlayer(sender)) {
                Player p = (Player) sender;
                return whatCanRun(p);
            }
        }
        return null;
    }

    public static List<String> whatCanRun(Player p){
        ArrayList<String> l = new ArrayList<>();
        if (hasPermission(p, CreateMarkerSetPerm)) l.add(CreateMarkerSetCommand);
        if (hasPermission(p, DeleteMarkerSetPerm)) l.add(DeleteMarkerSetCommand);
        if (hasPermission(p, ListMarkerSetsPerm)) l.add(ListMarkerSetsCommand);
        return l;
    }
}
