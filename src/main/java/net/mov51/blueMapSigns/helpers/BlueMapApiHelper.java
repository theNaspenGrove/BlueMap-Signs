package net.mov51.blueMapSigns.helpers;

import de.bluecolored.bluemap.api.BlueMapAPI;
import de.bluecolored.bluemap.api.BlueMapMap;
import de.bluecolored.bluemap.api.markers.POIMarker;
import org.bukkit.Location;
import org.bukkit.World;

import java.awt.image.BufferedImage;
import java.util.Objects;

import static net.mov51.blueMapSigns.markerHandlers.markerSetHandler.*;

public class BlueMapApiHelper {

    public static void createMarkerPOI(String markerName, Location l, String icon){
        //get world to run loop on
        World world = l.getWorld();
        BlueMapAPI.getInstance().ifPresent(api -> {
            POIMarker marker = POIMarker.builder()
                    .label(markerName)
                    .position(l.getBlockX(), l.getBlockY(), l.getBlockZ())
                    .build();
            if (!icon.isEmpty() && iconHelper.icons.containsKey(icon)) {
                pairHelper<String, BufferedImage> pair = iconHelper.icons.get(icon);
                String iconPath = pair.getFirst();
                BufferedImage image = pair.getSecond();

                int x = image.getHeight() / 2;
                int y = image.getWidth() / 2;
                marker.setIcon(iconPath, x, y);
            }

            api.getWorld(world).ifPresent(BlueWorld -> {
                for (BlueMapMap map : BlueWorld.getMaps()) {
                    map.getMarkerSets().get(generateMarkerSetID(map)).put(generateMarkerID(l), marker);
                }
            });
        });
    }


    //Method overload for optional default POI icon
    public static void createMarkerPOI(String markerName, Location l){
        createMarkerPOI(markerName,l,"");
    }

    public static void removeMarkerPOI(Location l) {

        BlueMapAPI.getInstance().flatMap(api -> api.getWorld(l.getWorld())).ifPresent(BlueWorld -> {
            for (BlueMapMap map : BlueWorld.getMaps()) {
                map.getMarkerSets().get(generateMarkerSetID(map)).remove(generateMarkerID(l));
            }
        });
    }

    public static String generateMarkerID(Location l){
        //Generate the marker ID with the exact location of the sign and the world it's in. Since there can only ever be one sign in that spot, there shouldn't be any confusion if I use the generated ID to remove the marker when the sign is broken.
        return Objects.requireNonNull(l.getWorld()).getName() + "_" + l.getX() + "-" + l.getY() + "-" + l.getZ();
    }



}
