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
    public static final String signMarkerSetIDPrefix = "bmSigns";
    public static String defaultMarkerSetName = BlueMapSigns.plugin.getConfig().getString("sign-marker-set-name");
    public static String defaultMarkerSetID = BlueMapSigns.plugin.getConfig().getString("sign-marker-set-ID");

    BlueMapAPI blueAPI;

    public String markerSetName;
    public String markerSetID;

    public MarkerSet set;


    public AspenMarkerSet(BlueMapAPI api, String markerSetID, String markerSetName){
        //set object variables
        blueAPI = api;
        this.markerSetID = markerSetID!=null ? signMarkerSetIDPrefix + "_" + markerSetID : signMarkerSetIDPrefix + "_" + markerSetNameToID(markerSetName);
        this.markerSetName = markerSetName!=null ? markerSetName : defaultMarkerSetName;
        //get MarkerAPI
        MarkerAPI markerAPI = getMarkerAPI(blueAPI);
        //create MarkerSet
        if(setExists(markerAPI,this)){
            aspenLogHelper.sendLogWarning("MarkerSet " + this.getSetID() + " already exists!");
        }else{
            set = markerAPI.createMarkerSet(signMarkerSetIDPrefix + "_" +  this.getSetID());
            set.setLabel(this.getSetName());
        }
        saveMarkerAPI(markerAPI);
        addMe();
    }

    public AspenMarkerSet(String markerSetID, String markerSetName){
        //get BlueMap api!
        BlueMapAPI.getInstance().ifPresent(api -> {
            //set object variables
            blueAPI = api;
            this.markerSetID = markerSetID!=null ? signMarkerSetIDPrefix + "_" +  markerSetID : signMarkerSetIDPrefix + "_" + defaultMarkerSetID;
            this.markerSetName = markerSetName!=null ? markerSetName : defaultMarkerSetName;
            //get MarkerAPI
            MarkerAPI markerAPI = getMarkerAPI(blueAPI);
            //create MarkerSet
            if(setExists(markerAPI,this)){
                markerAPI.createMarkerSet(signMarkerSetIDPrefix + "_" + this.getSetID()).setLabel(this.getSetName());
                aspenLogHelper.sendLogWarning("MarkerSet " + this.getSetID() + " already exists!");
            }
            saveMarkerAPI(markerAPI);
            addMe();
        });
    }

    private void addMe(){
        AspenMarkerSets.add(this);
    }

    public String getSetName(){
        if(!markerSetName.isEmpty()){
            return defaultMarkerSetName;
        }else{
            return markerSetName;
        }
    }

    public String getSetID(){
        if(!markerSetID.isEmpty()){
            return defaultMarkerSetID;
        }else{
            return markerSetID;
        }
    }

    public MarkerSet getSet(){
        MarkerAPI markerAPI = getMarkerAPI(blueAPI);
        saveMarkerAPI(markerAPI);
        return markerAPI.getMarkerSet(getSetID()).orElse(null);
    }
}
