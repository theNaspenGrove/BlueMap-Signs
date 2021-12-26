package net.mov51.blueMapSigns.helpers;

import com.flowpowered.math.vector.Vector3d;
import de.bluecolored.bluemap.api.BlueMapAPI;
import de.bluecolored.bluemap.api.marker.MarkerAPI;
import de.bluecolored.bluemap.api.marker.MarkerSet;
import de.bluecolored.bluemap.api.marker.POIMarker;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

import static net.mov51.blueMapSigns.helpers.chatHelper.*;
import static net.mov51.blueMapSigns.markerHandlers.markerSetHandler.*;

public class BlueMapApiHelper {



    public static void createMarkerPOI(String markerName, Location l, String icon, String MarkerSetName, Player p){
        //get world to run loop om
        World world = l.getWorld();

        //loop through all maps on the given world and add a marker at the provided location.
        // Will perform a location check when that's opened in the API.
        BlueMapAPI.getInstance().ifPresentOrElse(api -> api.getWorld(world != null ? world.getUID() : null).ifPresent(blueWorld -> blueWorld.getMaps().forEach(map -> {
            //create marker api
            MarkerAPI markerAPI = getMarkerAPI(api);
            if (markerAPI != null) {
                //get marker set
                MarkerSet SignMarkerSet = getMarkerSetByName(markerAPI,MarkerSetName);
                if(SignMarkerSet != null) {
                    //generate 3d vector to create poiMarker and update location to be the center of the block
                    Vector3d markerPos = Vector3d.from(l.getX() + .5, l.getY() + .5, l.getZ() + .5);
                    //create the marker ID
                    String ID = generateMarkerID(l);
                    //Create the marker Object and set it's label
                    POIMarker marker = SignMarkerSet.createPOIMarker(ID, map, markerPos);
                    marker.setLabel(markerName);
                    //todo change to info logger
                    System.out.println("created marker " + ID);
                    if (iconHelper.icons.containsKey(icon)) {
                        pairHelper<String, BufferedImage> pair = iconHelper.icons.get(icon);
                        String iconPath = pair.getFirst();
                        BufferedImage image = pair.getSecond();

                        int x = image.getHeight() / 2;
                        int y = image.getWidth() / 2;
                        marker.setIcon(iconPath, x, y);
                    }
                }else sendMessage(p,"That Marker Set doesn't exist!");
                //save changes
                saveMarkerAPI(markerAPI);
            }
        })), () -> {
            //If api is not present, please tell me 😭
            //todo throw noAPI error
            sendLogSevere("BlueMap API not present! Trying to initialize Icons in IconHelper!");
        } );
    }


    //Method overload for optional default POI icon
    public static void createMarkerPOI(String markerName, Location l,Player p){
        createMarkerPOI(markerName,l,"",defaultMarkerSetName,p);
    }

    public static void removeMarkerPOI(Location l) {
        //get world to run loop om
        World world = l.getWorld();

        //loop through all maps on the given world and add a marker at the provided location.
        // Will perform a location check when that's opened in the API.
        BlueMapAPI.getInstance().ifPresentOrElse(api -> api.getWorld(world != null ? world.getUID() : null).ifPresent(blueWorld -> blueWorld.getMaps().forEach(map -> {
            //create marker api
            MarkerAPI markerAPI = getMarkerAPI(api);
            assert markerAPI != null;
            String ID = generateMarkerID(l);
            //remove the marker

            for (MarkerSet SingleSet : markerAPI.getMarkerSets()) {
                String[] ArrayName = SingleSet.getId().split("_");
                if(ArrayName.length > 0 && ArrayName[0].equals(signMarkerSetIDPrefix)){
                    markerAPI.getMarkerSet(SingleSet.getId()).ifPresentOrElse(TheMarkerSet -> {
                        if(TheMarkerSet.getMarker(ID).isPresent()){
                            if(TheMarkerSet.removeMarker(ID)){
                                sendLogInfo("Marker " + ID + " was safely removed");
                            }else{
                                sendLogWarning(new String[]{"Marker " + ID + " was unable to be removed!", "Tried to remove the marker from the " + TheMarkerSet.getLabel() + " marker set."});
                            }
                        }
                    },() -> sendLogWarning("Marker set " + SingleSet.getId() + " didn't work"));
                }else{
                    sendLogInfo("Marker set " + SingleSet.getId() + " with ID prefix " + ArrayName[0] + " failed verification!");
                }
            }

            //save changes
            saveMarkerAPI(markerAPI);
        })), () -> {
            //If api is not present, please tell me 😭
            sendLogSevere("BlueMap API not present! Trying to initialize Icons in IconHelper!");
        } );
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
