package net.mov51.blueMapSigns.markerHandlers;

import de.bluecolored.bluemap.api.BlueMapMap;
import de.bluecolored.bluemap.api.markers.MarkerSet;
import de.bluecolored.bluemap.api.markers.POIMarker;
import net.mov51.blueMapSigns.helpers.iconHelper;
import net.mov51.blueMapSigns.helpers.pairHelper;
import org.bukkit.Location;

import java.awt.image.BufferedImage;

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
    public void addMarker(String markerName, Location l, String icon){
        POIMarker marker = POIMarker.builder()
                .label(markerName)
                .position(l.getBlockX(), l.getBlockY(), l.getBlockZ())
                .build();
        if (!icon.isEmpty() && iconHelper.icons.containsKey(icon)) {
            pairHelper<String, BufferedImage> pair = iconHelper.icons.get(icon);
            String iconPath = pair.getFirst();
            BufferedImage image = pair.getSecond();

            int x = image.getHeight() / 2;
            int y = image.getWidth() / 2;
            marker.setIcon(iconPath, x, y);
        }

        markerSet.put(generateMarkerID(l), marker);
    }
    public void removeMarker(Location l){
        markerSet.remove(generateMarkerID(l));
    }
    public MarkerSet getMarkerSet(){
        return markerSet;
    }
}
