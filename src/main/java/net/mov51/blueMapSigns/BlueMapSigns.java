package net.mov51.blueMapSigns;

import de.bluecolored.bluemap.api.BlueMapAPI;
import de.bluecolored.bluemap.api.gson.MarkerGson;
import de.bluecolored.bluemap.api.markers.MarkerSet;
import net.mov51.blueMapSigns.listeners.signBreakListener;
import net.mov51.blueMapSigns.listeners.signChangeListener;
import net.mov51.blueMapSigns.markerHandlers.AspenMarkers;
import net.mov51.periderm.chat.AspenChatHelper;
import net.mov51.periderm.logs.AspenLogHelper;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Logger;

import static net.mov51.blueMapSigns.helpers.iconHelper.makeIconData;
import static net.mov51.blueMapSigns.markerHandlers.markerSetHandler.*;

public final class BlueMapSigns extends JavaPlugin {

    public static org.bukkit.plugin.Plugin plugin = null;
    public static AspenChatHelper aspenChatHelper;
    public static AspenLogHelper aspenLogHelper;
    public static Logger blueLogger;
    private static File dataFolder;
    private static File markerDataFolder;
    @Override
    public void onEnable() {
        plugin = this;

        String ConfigChatPrefix = BlueMapSigns.plugin.getConfig().getString("chat-prefix");
        aspenChatHelper = new AspenChatHelper((ConfigChatPrefix != null) ? ConfigChatPrefix : "BlueMap-Signs");

        dataFolder = BlueMapSigns.plugin.getDataFolder();
        blueLogger = BlueMapSigns.plugin.getLogger();
        aspenLogHelper = new AspenLogHelper(blueLogger,"BlueMap-Signs");

        String iconDataP = dataFolder + "/icons";
        File iconDataF = new File(iconDataP);

        plugin.saveDefaultConfig();

        if(!iconDataF.exists()){
            boolean wasCreate = iconDataF.mkdirs();
            if(wasCreate){
                aspenLogHelper.sendLogWarning("Plugin icon data folder has been created!");
            }else{
                aspenLogHelper.sendLogWarning("Plugin icon data folder was unable to be created!");
            }
        }

        String markerDataPath = dataFolder + "/marker-sets/";
        markerDataFolder = new File(markerDataPath);

        if(!markerDataFolder.exists()){
            boolean wasCreate = markerDataFolder.mkdirs();
            if(wasCreate){
                aspenLogHelper.sendLogWarning("Plugin marker data folder has been created!");
            }else{
                aspenLogHelper.sendLogWarning("Plugin marker data folder was unable to be created!");
            }
        }

        BlueMapAPI.onEnable(api -> {
            makeIconData(api);
            loadMarkerSets(api);
            createDefaultMarkerSets(api);
        });

        getServer().getPluginManager().registerEvents(new signChangeListener(), this);
        getServer().getPluginManager().registerEvents(new signBreakListener(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        saveMarkerSets();
    }

    private void saveMarkerSets(){
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

    private void loadMarkerSets(BlueMapAPI api){
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
            api.getMap(aspenMarker.getValue().getMap().getId()).ifPresent(map -> {
                map.getMarkerSets().put(aspenMarker.getValue().getMarkerSetID(),aspenMarker.getValue().getMarkerSet());
            });
        }
    }
}
