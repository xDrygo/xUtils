package org.eldrygo.XUtils.Managers;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.eldrygo.XUtils.XUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class WarpManager {

    private final XUtils plugin;
    private final File warpsFile;
    private JSONObject data;

    public WarpManager(XUtils plugin) {
        this.plugin = plugin;
        this.warpsFile = new File(plugin.getDataFolder(), "data/warps.json");

        if (!warpsFile.exists()) {
            save(); // Crear archivo vacío
        }
        if (this.data == null) {
            this.data = new JSONObject();
        }

        load();
    }

    public void setWarp(String name, Location location) {
        JSONObject warpData = new JSONObject();
        warpData.put("world", location.getWorld().getName());
        warpData.put("x", location.getX());
        warpData.put("y", location.getY());
        warpData.put("z", location.getZ());

        if (location.getYaw() != 0) warpData.put("yaw", location.getYaw());
        if (location.getPitch() != 0) warpData.put("pitch", location.getPitch());

        data.put(name, warpData);
        save();
    }

    public Location getWarp(String name) {
        if (!data.containsKey(name)) return null;

        JSONObject warpData = (JSONObject) data.get(name);

        String worldName = (String) warpData.get("world");
        World world = Bukkit.getWorld(worldName);
        if (world == null) return null;

        double x = getDouble(warpData, "x");
        double y = getDouble(warpData, "y");
        double z = getDouble(warpData, "z");
        float yaw = (float) getDouble(warpData, "yaw", 0.0);
        float pitch = (float) getDouble(warpData, "pitch", 0.0);

        return new Location(world, x, y, z, yaw, pitch);
    }

    public void delWarp(String name) {
        data.remove(name);
        save();
    }

    public Set<String> getWarpNames() {
        return data.keySet();
    }

    private void load() {
        try (FileReader reader = new FileReader(warpsFile)) {
            JSONParser parser = new JSONParser();
            Object parsed = parser.parse(reader);
            this.data = (JSONObject) parsed;
        } catch (Exception e) {
            plugin.getLogger().severe("❌ Error al cargar warps.json:");
            e.printStackTrace();
            this.data = new JSONObject();
        }
    }

    public void save() {
        if (this.data == null) {
            this.data = new JSONObject();
        }
        try (FileWriter writer = new FileWriter(warpsFile)) {
            writer.write(data.toJSONString());
        } catch (IOException e) {
            plugin.getLogger().severe("❌ No se pudo guardar warps.json:");
            e.printStackTrace();
        }
    }

    private double getDouble(JSONObject obj, String key) {
        Object value = obj.get(key);
        return value instanceof Number ? ((Number) value).doubleValue() : 0.0;
    }

    private double getDouble(JSONObject obj, String key, double defaultValue) {
        Object value = obj.getOrDefault(key, defaultValue);
        return value instanceof Number ? ((Number) value).doubleValue() : defaultValue;
    }
}
