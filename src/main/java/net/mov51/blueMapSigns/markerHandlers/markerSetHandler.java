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
import static net.mov51.periderm.logs.Strings.formatArray;

public class markerSetHandler {

    public static List<AspenMarkerSet> AspenMarkerSets =new ArrayList<>();

    public static AspenMarkerSet getMarkerSetFromName(String markerSetName){
        for (AspenMarkerSet aspenMarkerSet : AspenMarkerSets) {
            if(aspenMarkerSet.getSetName().equalsIgnoreCase(markerSetName)){
                return aspenMarkerSet;
            }
        }
        return null;
    }

    public static boolean setExists(MarkerAPI markerAPI, AspenMarkerSet set){
        return markerAPI.getMarkerSet(set.getPrefixedSetID()).isPresent();
    }

    public static boolean setExists(MarkerAPI markerAPI, String ID){
        return markerAPI.getMarkerSet(ID).isPresent();
    }

    //These methods use the BlueMap API to generate their own MarkerAPI and save it. They are self-contained and can be called safely without having to save the api.
    public static boolean createMarkerSet(BlueMapAPI api, String MarkerSetName){
        AspenMarkerSet set = new AspenMarkerSet(api, MarkerSetName);
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
    }

    private static void createDefaultMarkerSet(BlueMapAPI api){
        MarkerAPI markerAPI = getMarkerAPI(api);
        if(!setExists(markerAPI, prefixSetID(DefaultMarkerSetID))){
            aspenLogHelper.sendLogInfo("created default MarkerSet!");
            createMarkerSet(api, DefaultMarkerSetID, DefaultMarkerSetName);
        }
        saveMarkerAPI(markerAPI);
    }

    public static boolean deleteMarkerSet(BlueMapAPI api, AspenMarkerSet set){
        //todo delete MarkerSet
        MarkerAPI markerAPI = getMarkerAPI(api);
        if(setExists(markerAPI, set.getPrefixedSetID())){
            if(markerAPI.removeMarkerSet(set.getPrefixedSetID())){
                AspenMarkerSets.remove(set);
                saveMarkerAPI(markerAPI);
                return true;
            }
        }else{
            aspenLogHelper.sendLogWarning("Marker set with Full ID '" + set.getPrefixedSetID() + "' was not loaded in BlueMap!");
        }
        aspenLogHelper.sendLogWarning("Could not remove MarkerSet with Full ID '" + set.getPrefixedSetID() + "'");
        saveMarkerAPI(markerAPI);
        return false;
    }

    public static void loadAllMarkerSets(BlueMapAPI api){
        createDefaultMarkerSet(api);
        MarkerAPI markerAPI = getMarkerAPI(api);
        for (MarkerSet SingleSet : markerAPI.getMarkerSets()) {
            if(isPrefixedID(SingleSet.getId())) {
                new AspenMarkerSet(removePrefixFromSetID(SingleSet.getId()),SingleSet.getLabel());
                aspenLogHelper.sendLogInfo("Loaded MarkerSet '" + SingleSet.getLabel() + "' with full ID '" + SingleSet.getId() + "'");
            }
        }
        saveMarkerAPI(markerAPI);
    }

    public static String listMarkerSets(){
        ArrayList<String> markerSets = new ArrayList<>();

        for (AspenMarkerSet aspenMarkerSet : AspenMarkerSets) {
            markerSets.add(aspenMarkerSet.MarkerSetName);
        }

        return formatArray(markerSets);
    }

    //These are non-API helper methods
    public static String markerSetNameToID(String MarkerSetName){
        return MarkerSetName.toUpperCase().replaceAll(" ", "-");
    }

    public static String prefixSetID(String MarkerSetID){
        return SignMarkerSetIDPrefix + "_" + MarkerSetID;
    }

    public static String removePrefixFromSetID(String MarkerSetID){
        return MarkerSetID.replaceAll(SignMarkerSetIDPrefix + "_", "");
    }

    public static boolean isPrefixedID(String MarkerSetID){
        return MarkerSetID.startsWith(SignMarkerSetIDPrefix + "_");
    }
}
