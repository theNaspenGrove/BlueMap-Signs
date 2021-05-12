package net.mov51.helpers;

import com.flowpowered.math.vector.Vector3d;
import de.bluecolored.bluemap.api.BlueMapAPI;
import de.bluecolored.bluemap.api.marker.MarkerAPI;
import de.bluecolored.bluemap.api.marker.MarkerSet;
import de.bluecolored.bluemap.api.marker.POIMarker;
import net.mov51.BlueMapSigns;
import org.bukkit.Location;
import org.bukkit.World;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class BlueMapApiHelper {

    public static String signMarkerSetID = BlueMapSigns.plugin.getConfig().getString("sign-marker-set-ID");
    public static String signMarkerSetName = BlueMapSigns.plugin.getConfig().getString("sign-marker-set-name");

    public static void createMarkerPOI(String markerName, Location l,String icon){
        //get world to run loop om
        World world = l.getWorld();

        //loop through all maps on the given world and add a marker at the provided location.
        // Will perform a location check when that's opened in the API.
        BlueMapAPI.getInstance().ifPresent(api -> api.getWorld(world != null ? world.getUID() : null).ifPresent(blueWorld -> blueWorld.getMaps().forEach(map -> {
            //create marker api
            MarkerAPI markerAPI = getMarkerAPI(api);
            assert markerAPI != null;
            //create marker set
            MarkerSet SignMarkerSet = markerAPI.getMarkerSet(signMarkerSetID).orElse(markerAPI.createMarkerSet(signMarkerSetID));
            SignMarkerSet.setLabel(signMarkerSetName);
            //generate 3d vector to create poiMarker and update location to be the center of the block
            Vector3d markerPos = Vector3d.from(l.getX() + .5, l.getY() + .5, l.getZ() + .5);
            //create the marker ID
            String ID = generateMarkerID(l);
            //Create the marker Object
            POIMarker marker = SignMarkerSet.createPOIMarker(ID, map, markerPos);
            System.out.println("created marker " + ID);
            //set the marker label
            marker.setLabel(markerName);
            if (iconHelper.icons.containsKey(icon)) {
                pairHelper<String, BufferedImage> pair = iconHelper.icons.get(icon);
                String iconPath = pair.getFirst();
                BufferedImage image = pair.getSecond();

                int x = image.getHeight() / 2;
                int y = image.getWidth() / 2;
                marker.setIcon(iconPath, x, y);
            }
            //save changes
            saveMarkerAPI(markerAPI);
        })));
    }


    //Method overload for optional default POI icon
    public static void createMarkerPOI(String markerName, Location l){
        createMarkerPOI(markerName,l,"");
    }

    public static void removeMarkerPOI(Location l) {
        //get world to run loop om
        World world = l.getWorld();

        //loop through all maps on the given world and add a marker at the provided location.
        // Will perform a location check when that's opened in the API.
        BlueMapAPI.getInstance().ifPresent(api -> api.getWorld(world != null ? world.getUID() : null).ifPresent(blueWorld -> blueWorld.getMaps().forEach(map -> {
            //create marker api
            MarkerAPI markerAPI = getMarkerAPI(api);
            assert markerAPI != null;
            //create marker set
            MarkerSet SignMarkerSet = markerAPI.getMarkerSet(signMarkerSetID).orElse(markerAPI.createMarkerSet(signMarkerSetID));
            //set the marker set label
            SignMarkerSet.setLabel(signMarkerSetName);
            String ID = generateMarkerID(l);
            //remove the marker
            SignMarkerSet.removeMarker(ID);
            System.out.println("removed marker " + ID);

            //save changes
            saveMarkerAPI(markerAPI);
        })));
    }

    public static String generateMarkerID(Location l){
        //Generate the marker ID with the exact location of the sign and the world it's in. Since there can only ever be one sign in that spot, there shouldn't be any confusion if I use the generated ID to remove the marker when the sign is broken.
        return Objects.requireNonNull(l.getWorld()).getName() + "_" + l.getX() + "-" + l.getY() + "-" + l.getZ();
    }

    public static MarkerAPI getMarkerAPI(BlueMapAPI api){
        try {
            return api.getMarkerAPI();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void saveMarkerAPI(MarkerAPI api){
        //Save the changes made to the API as long as the marker JSON is currently working.
        try {
            api.save();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
