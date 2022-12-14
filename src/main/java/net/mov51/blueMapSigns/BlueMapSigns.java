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
import java.util.logging.Logger;

import static net.mov51.blueMapSigns.helpers.iconHelper.makeIconData;
import static net.mov51.blueMapSigns.markerHandlers.markerSetHandler.*;

public final class BlueMapSigns extends JavaPlugin {

    public static org.bukkit.plugin.Plugin plugin = null;
    public static AspenChatHelper aspenChatHelper;
    public static AspenLogHelper aspenLogHelper;
    public static Logger blueLogger;

    File dataFolder;
    @Override
    public void onEnable() {
        plugin = this;

        String ConfigChatPrefix = BlueMapSigns.plugin.getConfig().getString("chat-prefix");
        aspenChatHelper = new AspenChatHelper((ConfigChatPrefix != null) ? ConfigChatPrefix : "BlueMap-Signs");

        dataFolder = BlueMapSigns.plugin.getDataFolder();
        blueLogger = BlueMapSigns.plugin.getLogger();
        aspenLogHelper = new AspenLogHelper(blueLogger,"BlueMap-Signs");

        File data = plugin.getDataFolder();
        String iconDataP = data + "/icons";
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

        String markerDataP = data + "/marker-sets";
        File markerDataF = new File(markerDataP);

        if(!markerDataF.exists()){
            boolean wasCreate = markerDataF.mkdirs();
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
        for (AspenMarkers aspenMarker : aspenMarkers) {
            String markerSetID = aspenMarker.getMarkerSetID();
            MarkerSet markerSet = aspenMarker.getMap().getMarkerSets().get(markerSetID);
            if(markerSet != null){
                String markerSetPath = dataFolder + "/marker-sets/" + markerSetID + ".json";
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
        //Wait for the BlueMap API to enable
            api.getMaps().forEach(map -> {
                MarkerSet MarkerSetToLoad = null;
                String path = dataFolder + "/marker-sets/" + generateMarkerSetID(map)+ ".json";
                File file = new File(path);
                if(file.exists()){
                    try (FileReader reader = new FileReader(path)) {
                        MarkerSetToLoad = MarkerGson.INSTANCE.fromJson(reader, MarkerSet.class);
                    } catch (IOException ex) {
                        // handle io-exception
                        ex.printStackTrace();
                    }
                    aspenMarkers.add(new AspenMarkers(generateMarkerSetID(map),MarkerSetToLoad,map));
                }else {
                    aspenLogHelper.sendLogWarning("No marker file found for map " + map.getId());
                }
            });
            for (AspenMarkers aspenMarker : aspenMarkers) {
                aspenMarker.getMap().getMarkerSets().put(aspenMarker.getMarkerSetID(),aspenMarker.getMarkerSet());
            }
    }
}
