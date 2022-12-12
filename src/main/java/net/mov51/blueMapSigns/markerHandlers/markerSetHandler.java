package net.mov51.blueMapSigns.markerHandlers;

import de.bluecolored.bluemap.api.BlueMapAPI;
import de.bluecolored.bluemap.api.markers.MarkerSet;
import net.mov51.blueMapSigns.BlueMapSigns;

import java.util.concurrent.atomic.AtomicBoolean;

import static net.mov51.blueMapSigns.BlueMapSigns.aspenLogHelper;



public class markerSetHandler {
    public static final String SignMarkerSetIDPrefix = "bmSigns";
    public static final String DefaultMarkerSetID = BlueMapSigns.plugin.getConfig().getString("default-sign-marker-set-ID") != null ?
            BlueMapSigns.plugin.getConfig().getString("default-sign-marker-set-ID") : "SIGN-MARKER-SET";
    public static String DefaultMarkerSetName = BlueMapSigns.plugin.getConfig().getString("default-sign-marker-set-name") != null ?
            BlueMapSigns.plugin.getConfig().getString("default-sign-marker-set-name") : "Sign Marker Set";
    private static MarkerSet markerSet;

    private static boolean markerSetExists = false;

    public static void createDefaultMarkerSets(BlueMapAPI api){
        if(markerSet == null){
            buildDefaultMarkerSet();
        }
        AtomicBoolean status = new AtomicBoolean(false);

        api.getMaps().forEach(BlueMapMap -> {
            if(!BlueMapMap.getMarkerSets().containsKey(DefaultMarkerSetID)){
                aspenLogHelper.sendLogInfo("Default MarkerSet not found on map " + BlueMapMap.getId() + "!");
                BlueMapMap.getMarkerSets().put(DefaultMarkerSetID, markerSet);
                if(BlueMapMap.getMarkerSets().containsKey(DefaultMarkerSetID)){
                    BlueMapMap.getMarkerSets().put(DefaultMarkerSetID, markerSet);
                    aspenLogHelper.sendLogWarning("Created default MarkerSet on map " + BlueMapMap.getId() + "!");
                }else {
                    aspenLogHelper.sendLogWarning("Failed to create default MarkerSet on map " + BlueMapMap.getId() + "!");
                    status.set(false);
                }
            }
        });
        markerSetExists = status.get();
    }

    private static void buildDefaultMarkerSet(){
        markerSet = MarkerSet.builder()
                .label(DefaultMarkerSetName)
                .toggleable(true)
                .build();
    }

    //These are non-API helper methods
}
