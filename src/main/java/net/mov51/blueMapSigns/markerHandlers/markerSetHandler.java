package net.mov51.blueMapSigns.markerHandlers;

import de.bluecolored.bluemap.api.BlueMapAPI;
import de.bluecolored.bluemap.api.marker.MarkerAPI;
import de.bluecolored.bluemap.api.marker.MarkerSet;
import net.mov51.blueMapSigns.helpers.AspenMarkerSet;

import java.util.ArrayList;
import java.util.List;

import static net.mov51.blueMapSigns.BlueMapSigns.aspenLogHelper;
import static net.mov51.blueMapSigns.helpers.AspenMarkerSet.*;
import static net.mov51.blueMapSigns.helpers.BlueMapApiHelper.getMarkerAPI;
import static net.mov51.blueMapSigns.helpers.BlueMapApiHelper.saveMarkerAPI;


public class markerSetHandler {

    public static List<AspenMarkerSet> AspenMarkerSets =new ArrayList<>();

    public static AspenMarkerSet getMarkerSetFromName(String markerSetName){
        for (AspenMarkerSet aspenMarkerSet : AspenMarkerSets) {
            if(aspenMarkerSet.markerSetName.equalsIgnoreCase(markerSetName)){
                return aspenMarkerSet;
            }
        }
        return null;
    }

    public static boolean setExists(MarkerAPI markerAPI, AspenMarkerSet set){
        return markerAPI.getMarkerSet(set.getSetID()).isPresent();
    }

    public static boolean setExists(MarkerAPI markerAPI, String ID){
        return markerAPI.getMarkerSet(signMarkerSetIDPrefix + "_" +ID).isPresent();
    }

    //These methods use the BlueMap API to generate their own MarkerAPI and save it. They are self-contained and can be called safely without having to save the api.
    public static boolean createMarkerSet(BlueMapAPI api, String MarkerSetName){
        AspenMarkerSet set = new AspenMarkerSet(api, null, MarkerSetName);
        MarkerAPI markerAPI = getMarkerAPI(api);
        if(setExists(markerAPI,set)){
            saveMarkerAPI(markerAPI);
            return true;
        }
        saveMarkerAPI(markerAPI);
        return false;
    }

    public static void createMarkerSet(BlueMapAPI api, String MarkerSetID, String MarkerSetName){
        new AspenMarkerSet(api, MarkerSetID, MarkerSetName);
        MarkerAPI markerAPI = getMarkerAPI(api);
        saveMarkerAPI(markerAPI);
    }

    private static void createDefaultMarkerSet(BlueMapAPI api){
        MarkerAPI markerAPI = getMarkerAPI(api);
        if(!setExists(markerAPI,defaultMarkerSetID)){
            aspenLogHelper.sendLogInfo("created default MarkerSet!");
            createMarkerSet(api,defaultMarkerSetID,defaultMarkerSetName);
        }
        saveMarkerAPI(markerAPI);
    }

    public static boolean deleteMarkerSet(BlueMapAPI api, String MarkerSetName){
        //todo delete markerset
        AspenMarkerSet set = new AspenMarkerSet(api, null, MarkerSetName);
        MarkerAPI markerAPI = getMarkerAPI(api);
        if(setExists(markerAPI,set)){
            saveMarkerAPI(markerAPI);
            return true;
        }
        saveMarkerAPI(markerAPI);
        return false;
    }

    public static void loadAllMarkerSets(BlueMapAPI api){
        createDefaultMarkerSet(api);
        MarkerAPI markerAPI = getMarkerAPI(api);
        for (MarkerSet SingleSet : markerAPI.getMarkerSets()) {
            String[] ArrayName = SingleSet.getId().split("_");
            aspenLogHelper.sendLogInfo("Loading MarkerSets!");
            if(ArrayName.length > 0 && ArrayName[0].equals(signMarkerSetIDPrefix)) {
                new AspenMarkerSet(SingleSet.getLabel(),SingleSet.getLabel());
                aspenLogHelper.sendLogInfo("Loaded MarkerSet " + SingleSet.getLabel());
            }
        }
        saveMarkerAPI(markerAPI);
    }

    public static String listMarkerSets(BlueMapAPI api){
        MarkerAPI markerAPI = getMarkerAPI(api);
        StringBuilder markerSetList = new StringBuilder();
        int pass = 0;
        for (MarkerSet SingleSet : markerAPI.getMarkerSets()) {
            String[] ArrayName = SingleSet.getId().split("_");
            if(ArrayName.length > 0 && ArrayName[0].equals(signMarkerSetIDPrefix)) {
                if(pass == 0){
                    markerSetList.insert(0, ", " + SingleSet.getLabel());
                    pass++;
                }else markerSetList.insert(0, SingleSet.getLabel());
            }
        }
        return markerSetList.toString();
    }

    //These are non-API helper methods
    public static String markerSetNameToID(String MarkerSetName){
        return signMarkerSetIDPrefix + "_" + MarkerSetName.replaceAll(" ", "-").toUpperCase();
    }

    public static String prefixSetID(String MarkerSetID){
        return signMarkerSetIDPrefix + "_" + MarkerSetID;
    }
}
