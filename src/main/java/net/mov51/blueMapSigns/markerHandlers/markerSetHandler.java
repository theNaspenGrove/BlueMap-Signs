package net.mov51.blueMapSigns.markerHandlers;

import de.bluecolored.bluemap.api.BlueMapAPI;
import de.bluecolored.bluemap.api.marker.MarkerAPI;
import de.bluecolored.bluemap.api.marker.MarkerSet;
import net.mov51.blueMapSigns.BlueMapSigns;

import static net.mov51.blueMapSigns.helpers.BlueMapApiHelper.getMarkerAPI;
import static net.mov51.blueMapSigns.helpers.BlueMapApiHelper.saveMarkerAPI;


public class markerSetHandler {

    public static String defaultMarkerSetName = BlueMapSigns.plugin.getConfig().getString("sign-marker-set-name");
    public static String defaultMarkerSetID = BlueMapSigns.plugin.getConfig().getString("sign-marker-set-ID");
    public static String signMarkerSetIDPrefix = "bmSigns";


    //These methods use the MarkerAPI and need to have the API saved by the method that called them.
    public static MarkerSet getMarkerSetByName(MarkerAPI api, String MarkerSetName){
        return getMarkerSetByID(api,MarkerSetName);
    }

    public static MarkerSet getMarkerSetByID(MarkerAPI api, String MarkerSetName){
        return api.getMarkerSet(getPrefixedMarkerIdByName(MarkerSetName)).orElse(null);
    }

    public static Boolean doesMarkerSet(MarkerAPI markerAPI, String markerSetName){
        String markerSetId = getPrefixedMarkerIdByName(markerSetName);

        for (MarkerSet SingleSet : markerAPI.getMarkerSets()) {
            if(SingleSet.getId().equals(markerSetId))return true;
        }
        return false;
    }

    //These methods use the BlueMap API to generate their own MarkerAPI and save it. They are self contained and can be called safely without having to save the api.
    public static boolean createMarkerSetByName(BlueMapAPI api, String MarkerSetName){
        MarkerAPI markerAPI = getMarkerAPI(api);
        assert markerAPI != null;
        if(!doesMarkerSet(markerAPI, MarkerSetName)){
            markerAPI.createMarkerSet(getPrefixedMarkerIdByName(MarkerSetName)).setLabel(MarkerSetName);
            saveMarkerAPI(markerAPI);
            return true;
        }else return false;
    }

    public static boolean deleteMarkerSetByName(BlueMapAPI api, String MarkerSetName){
        MarkerAPI markerAPI = getMarkerAPI(api);
        assert markerAPI != null;
        if(doesMarkerSet(markerAPI, MarkerSetName)){
            markerAPI.removeMarkerSet(getPrefixedMarkerIdByName(MarkerSetName));
            saveMarkerAPI(markerAPI);
            return true;
        }else return false;
    }

    public static String listMarkerSets(BlueMapAPI api){

        MarkerAPI markerAPI = getMarkerAPI(api);
        StringBuilder markerSetList = new StringBuilder();
        if (markerAPI != null) {
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
        }

        return markerSetList.toString();
    }

    //These are non-API helper methods
    public static String getPrefixedMarkerIdByName(String MarkerSetName){
        return signMarkerSetIDPrefix + "_" + MarkerSetName;
    }
}
