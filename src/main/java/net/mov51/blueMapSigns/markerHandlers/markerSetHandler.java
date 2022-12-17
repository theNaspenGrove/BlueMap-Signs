package net.mov51.blueMapSigns.markerHandlers;

import de.bluecolored.bluemap.api.BlueMapAPI;
import de.bluecolored.bluemap.api.BlueMapMap;
import de.bluecolored.bluemap.api.gson.MarkerGson;
import de.bluecolored.bluemap.api.markers.MarkerSet;
import net.mov51.blueMapSigns.BlueMapSigns;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static net.mov51.blueMapSigns.BlueMapSigns.aspenLogHelper;
import static net.mov51.blueMapSigns.BlueMapSigns.markerDataFolder;

public class markerSetHandler {
    public static HashMap<String,AspenMarkers> aspenMarkers = new HashMap<>();
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
                    String markerSetID = generateMarkerSetID(BlueMapMap);
                    aspenLogHelper.sendLogInfo("Default MarkerSet not found on map " + BlueMapMap.getId() + "!");
                    BlueMapMap.getMarkerSets().put(markerSetID, markerSet);
                    if(BlueMapMap.getMarkerSets().containsKey(markerSetID)){
                        BlueMapMap.getMarkerSets().put(markerSetID, markerSet);
                        aspenMarkers.put(markerSetID,new AspenMarkers(markerSetID, markerSet, BlueMapMap));
                        aspenLogHelper.sendLogWarning("Created default MarkerSet on map " + BlueMapMap.getId() + "!");
                    }else {
                        aspenLogHelper.sendLogWarning("Failed to create default MarkerSet on map " + BlueMapMap.getId() + "!");
                    }
                }
            });
        }
    }

    public static void saveMarkerSets(){
        for(Map.Entry<String,AspenMarkers> aspenMarker: aspenMarkers.entrySet()){
            String markerSetID = aspenMarker.getValue().getMarkerSetID();
            MarkerSet markerSet = aspenMarker.getValue().getMap().getMarkerSets().get(markerSetID);
            if(markerSet != null){
                String markerSetPath = markerDataFolder + "/" + markerSetID + ".json";
                try (FileWriter writer = new FileWriter(markerSetPath)) {
                    MarkerGson.INSTANCE.toJson(markerSet, writer);
                } catch (IOException ex) {
                    // handle io-exception
                    ex.printStackTrace();
                }
            }
        }
    }

    public static void loadMarkerSets(BlueMapAPI api){
        if(aspenMarkers.isEmpty()){
            api.getMaps().forEach(map -> {
                MarkerSet MarkerSetToLoad = null;
                String path = markerDataFolder + "/" + generateMarkerSetID(map)+ ".json";
                File file = new File(path);
                if(file.exists()){
                    try (FileReader reader = new FileReader(path)) {
                        MarkerSetToLoad = MarkerGson.INSTANCE.fromJson(reader, MarkerSet.class);
                    } catch (IOException ex) {
                        // handle io-exception
                        ex.printStackTrace();
                    }
                    aspenMarkers.put(generateMarkerSetID(map), new AspenMarkers(generateMarkerSetID(map),MarkerSetToLoad,map));
                }else {
                    aspenLogHelper.sendLogWarning("No marker file found for map " + map.getId());
                }
            });
        }
        for(Map.Entry<String,AspenMarkers> aspenMarker: aspenMarkers.entrySet()){
            api.getMap(aspenMarker.getValue().getMap().getId()).ifPresent(map ->
                    map.getMarkerSets().put(aspenMarker.getValue().getMarkerSetID(),aspenMarker.getValue().getMarkerSet()));
        }
    }

    public static String generateMarkerSetID(BlueMapMap map) {
        return DefaultMarkerSetID + map.getId();
    }
}
