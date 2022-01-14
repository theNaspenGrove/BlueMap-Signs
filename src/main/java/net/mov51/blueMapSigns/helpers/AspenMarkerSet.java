package net.mov51.blueMapSigns.helpers;

import de.bluecolored.bluemap.api.BlueMapAPI;
import de.bluecolored.bluemap.api.marker.MarkerAPI;
import de.bluecolored.bluemap.api.marker.MarkerSet;
import net.mov51.blueMapSigns.BlueMapSigns;

import static net.mov51.blueMapSigns.BlueMapSigns.aspenLogHelper;
import static net.mov51.blueMapSigns.helpers.BlueMapApiHelper.getMarkerAPI;
import static net.mov51.blueMapSigns.helpers.BlueMapApiHelper.saveMarkerAPI;
import static net.mov51.blueMapSigns.markerHandlers.markerSetHandler.*;

public class AspenMarkerSet {
    public static final String SignMarkerSetIDPrefix = "bmSigns";
    public static String DefaultMarkerSetID = BlueMapSigns.plugin.getConfig().getString("default-sign-marker-set-ID");
    public static String DefaultMarkerSetName = BlueMapSigns.plugin.getConfig().getString("default-sign-marker-set-name");

    public String MarkerSetName;
    public String MarkerSetID;

    public MarkerSet set;


    public AspenMarkerSet(BlueMapAPI api, String markerSetID, String markerSetName){
        //set object variables
        this.MarkerSetID = markerSetID;
        this.MarkerSetName = markerSetName;
        //Start Initialize
        initialize(api);
    }

    public AspenMarkerSet(BlueMapAPI api, String markerSetName){
        //set object variables
        this.MarkerSetID = markerSetNameToID(markerSetName);
        this.MarkerSetName = markerSetName;
        //get MarkerAPI
        initialize(api);
    }

    public AspenMarkerSet(String markerSetID, String markerSetName){
        //set object variables
        MarkerSetID = markerSetID;
        MarkerSetName = markerSetName;
        addMe();
    }

    private void initialize(BlueMapAPI api){
        MarkerAPI markerAPI = getMarkerAPI(api);
        //create MarkerSet
        if(setExists(markerAPI,this)){
            aspenLogHelper.sendLogWarning("MarkerSet " + this.getSetID() + " already exists!");
        }else{
            set = markerAPI.createMarkerSet(this.getPrefixedSetID());
            aspenLogHelper.sendLogInfo("Creating MarkerSet " + this.getSetID() + "!");
            if(markerAPI.getMarkerSet(this.getPrefixedSetID()).isPresent()){
                set.setLabel(this.getSetName());
            }else{
                aspenLogHelper.sendLogSevere("MarkerSet " + this.getSetID() + " could not be created!");
            }
        }
        saveMarkerAPI(markerAPI);
        addMe();
    }

    private void addMe(){
        AspenMarkerSets.add(this);
    }

    public String getSetName(){
        return MarkerSetName;
    }

    public String getSetID(){
        return MarkerSetID;
    }

    public String getPrefixedSetID(){
        return prefixSetID(MarkerSetID);
    }

    public MarkerSet getSet(MarkerAPI markerAPI){
        return markerAPI.getMarkerSet(this.getPrefixedSetID()).orElse(null);
    }
}
