package net.mov51.helpers;

import com.flowpowered.math.vector.Vector3d;
import de.bluecolored.bluemap.api.BlueMapAPI;
import de.bluecolored.bluemap.api.marker.Marker;
import de.bluecolored.bluemap.api.marker.MarkerAPI;
import de.bluecolored.bluemap.api.marker.MarkerSet;
import de.bluecolored.bluemap.api.marker.POIMarker;
import org.bukkit.Location;
import org.bukkit.World;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

public class BlueMapApiHelper {

    public static void createMarkerPOI(String markerName, Location l,String icon){
        //get world to run loop om
        World world = l.getWorld();

        //loop through all maps on the given world and add a marker at the provided location.
        // Will perform a location check when that's opened in the API.
        BlueMapAPI.getInstance().get().getWorld(world.getUID()).ifPresent(blueWorld -> blueWorld.getMaps().forEach(map -> {
            //create marker api
            MarkerAPI api = getMarkerAPI();
            //create marker set
            MarkerSet SignMarkerSet = api.createMarkerSet("SIGN_MARKER_SET");
            //set the marker set label
            SignMarkerSet.setLabel("User created markers");
            //generate 3d vector to create poiMarker
            Vector3d markerPos = Vector3d.from(l.getX(), l.getY(), l.getZ());
            //create the marker ID
            String ID = generateMarkerID(l);
            //Create the marker Object
            POIMarker marker = SignMarkerSet.createPOIMarker(ID,map,markerPos);
            System.out.println("created marker " + ID);
            //set the marker label
            marker.setLabel(markerName);
            if(iconHelper.icons.containsKey(icon)){

                String iconPath = iconHelper.icons.get(icon);

                marker.setIcon(iconPath,0,0);
            }
            //save changes
            saveMarkerAPI(api);

        }));
    }

    //Method overload for optional default POI icon
    public static void createMarkerPOI(String markerName, Location l){
        createMarkerPOI(markerName,l,"default");
    }

    public static void removeMarkerPOI(Location l){
        //get world to run loop om
        World world = l.getWorld();

        //loop through all maps on the given world and add a marker at the provided location.
        // Will perform a location check when that's opened in the API.
        BlueMapAPI.getInstance().get().getWorld(world.getUID()).ifPresent(blueWorld -> blueWorld.getMaps().forEach(map -> {
            //create marker api
            MarkerAPI api = getMarkerAPI();
            //create marker set
            MarkerSet SignMarkerSet = api.createMarkerSet("SIGN_MARKER_SET");
            //set the marker set label
            SignMarkerSet.setLabel("User created markers");
            String ID = generateMarkerID(l);
            //remove the marker
            SignMarkerSet.removeMarker(ID);
            System.out.println("removed marker " + ID);

            //save changes
            saveMarkerAPI(api);
        }));
    }

    public static String generateMarkerID(Location l){
        //Generate the marker ID with the exact location of the sign and the world it's in. Since there can only ever be one sign in that spot, there shouldn't be any confusion if I use the generated ID to remove the marker when the sign is broken.
        return Objects.requireNonNull(l.getWorld()).getName() + "_" + l.getX() + "-" + l.getY() + "-" + l.getZ();
    }

    public static MarkerAPI getMarkerAPI(){
        try {
            return BlueMapAPI.getInstance().get().getMarkerAPI();
        } catch (IOException ignored) {
            return null;
        }
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
