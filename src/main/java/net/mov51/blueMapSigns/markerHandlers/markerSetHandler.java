package net.mov51.blueMapSigns.markerHandlers;

import de.bluecolored.bluemap.api.BlueMapAPI;
import de.bluecolored.bluemap.api.BlueMapMap;
import de.bluecolored.bluemap.api.markers.MarkerSet;
import net.mov51.blueMapSigns.BlueMapSigns;

import java.util.ArrayList;
import java.util.List;

import static net.mov51.blueMapSigns.BlueMapSigns.aspenLogHelper;
public class markerSetHandler {
    public static List<AspenMarkers> aspenMarkers = new ArrayList<>();
    public static final String DefaultMarkerSetID = BlueMapSigns.plugin.getConfig().getString("default-sign-marker-set-ID") != null ?
            BlueMapSigns.plugin.getConfig().getString("default-sign-marker-set-ID") : "SIGN-MARKER-SET";
    public static String DefaultMarkerSetName = BlueMapSigns.plugin.getConfig().getString("default-sign-marker-set-name") != null ?
            BlueMapSigns.plugin.getConfig().getString("default-sign-marker-set-name") : "Sign Marker Set";

    public static void createDefaultMarkerSets(BlueMapAPI api){{
            api.getMaps().forEach(BlueMapMap -> {
                if(!BlueMapMap.getMarkerSets().containsKey(generateMarkerSetID(BlueMapMap))){
                    MarkerSet markerSet = MarkerSet.builder()
                            .label(DefaultMarkerSetName)
                            .toggleable(true)
                            .build();
                    aspenLogHelper.sendLogInfo("Default MarkerSet not found on map " + BlueMapMap.getId() + "!");
                    BlueMapMap.getMarkerSets().put(generateMarkerSetID(BlueMapMap), markerSet);
                    if(BlueMapMap.getMarkerSets().containsKey(generateMarkerSetID(BlueMapMap))){
                        BlueMapMap.getMarkerSets().put(generateMarkerSetID(BlueMapMap), markerSet);
                        aspenMarkers.add(new AspenMarkers(generateMarkerSetID(BlueMapMap), markerSet, BlueMapMap));
                        aspenLogHelper.sendLogWarning("Created default MarkerSet on map " + BlueMapMap.getId() + "!");
                    }else {
                        aspenLogHelper.sendLogWarning("Failed to create default MarkerSet on map " + BlueMapMap.getId() + "!");
                    }
                }
            });
        }
    }

    public static String generateMarkerSetID(BlueMapMap map) {
        return DefaultMarkerSetID + map.getId();
    }

    //These are non-API helper methods
}
