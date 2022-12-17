package net.mov51.blueMapSigns.markerHandlers;

import de.bluecolored.bluemap.api.BlueMapMap;
import de.bluecolored.bluemap.api.markers.MarkerSet;
import de.bluecolored.bluemap.api.markers.POIMarker;
import org.bukkit.Location;

import static net.mov51.blueMapSigns.helpers.BlueMapApiHelper.generateMarkerID;

public class AspenMarkers {
    String markerSetID;
    BlueMapMap map;
    MarkerSet markerSet;
    public AspenMarkers(String markerSetID, MarkerSet markerSet, BlueMapMap map){
        this.markerSetID = markerSetID;
        this.map = map;
        this.markerSet = markerSet;
    }
    public String getMarkerSetID(){
        return markerSetID;
    }
    public BlueMapMap getMap(){
        return map;
    }
    public void addMarker(String markerID, POIMarker marker){
        markerSet.put(markerID, marker);
    }
    public void removeMarker(Location l){
        markerSet.remove(generateMarkerID(l));
    }
    public MarkerSet getMarkerSet(){
        return markerSet;
    }
}
