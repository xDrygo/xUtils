package org.eldrygo.XUtils.Managers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.bukkit.Location;
import org.bukkit.World;
import org.eldrygo.XUtils.XUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FirstSpawnManager {

    private final XUtils plugin;
    private final File firstSpawnFile;
    private JsonObject firstSpawnData;

    public FirstSpawnManager(XUtils plugin) {
        this.plugin = plugin;
        this.firstSpawnFile = new File(plugin.getDataFolder(), "firstspawn.json");
        loadFirstSpawnData();
    }

    // Carga los datos desde el archivo JSON
    public void loadFirstSpawnData() {
        // Ensure the 'data' folder exists
        File dataFolder = new File(plugin.getDataFolder(), "data");
        if (!dataFolder.exists()) {
            dataFolder.mkdirs();  // Create the folder if it doesn't exist
            plugin.getLogger().info("✅ The 'data' folder did not exist, it has been created.");
        }

        File firstSpawnFile = new File(dataFolder, "firstspawn.json");

        try {
            if (!firstSpawnFile.exists()) {
                firstSpawnFile.createNewFile();
                try (FileWriter writer = new FileWriter(firstSpawnFile)) {
                    writer.write("{}"); // initial empty content
                }
                plugin.getLogger().info("✅ The firstspawn.json file did not exist, it has been created at: " + firstSpawnFile.getPath());
            } else {
                plugin.getLogger().info("✅ The firstspawn.json file has been loaded successfully from: " + firstSpawnFile.getPath());
            }

            // Using try-with-resources for automatic closing of FileReader
            try (FileReader reader = new FileReader(firstSpawnFile)) {
                JsonParser parser = new JsonParser();
                firstSpawnData = parser.parse(reader).getAsJsonObject();
            }

        } catch (Exception e) {
            plugin.getLogger().severe("❌ Failed to load firstspawn.json due to an unexpected error at " + firstSpawnFile.getPath() + ": " + e.getMessage());
            e.printStackTrace();
            firstSpawnData = new JsonObject(); // fallback empty data
        }
    }

    // Crea un nuevo archivo JSON con los valores predeterminados
    private void createNewFirstSpawnFile() {
        firstSpawnData = new JsonObject();
        firstSpawnData.addProperty("world", "world");
        firstSpawnData.addProperty("x", 0);
        firstSpawnData.addProperty("y", 100);
        firstSpawnData.addProperty("z", 0);
        firstSpawnData.addProperty("yaw", 0);
        firstSpawnData.addProperty("pitch", 0);

        saveFirstSpawnData();
    }

    // Guarda los datos del primer spawn en el archivo JSON
    private void saveFirstSpawnData() {
        try (FileWriter writer = new FileWriter(firstSpawnFile)) {
            Gson gson = new Gson();
            gson.toJson(firstSpawnData, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Obtiene la ubicación del primer spawn desde el archivo JSON
    public Location getFirstSpawnLocation() {
        String worldName = firstSpawnData.get("world").getAsString();
        double x = firstSpawnData.get("x").getAsDouble();
        double y = firstSpawnData.get("y").getAsDouble();
        double z = firstSpawnData.get("z").getAsDouble();
        float yaw = firstSpawnData.get("yaw").getAsFloat();
        float pitch = firstSpawnData.get("pitch").getAsFloat();

        World world = plugin.getServer().getWorld(worldName);
        if (world == null) {
            return null; // El mundo no existe
        }
        return new Location(world, x, y, z, yaw, pitch);
    }

    // Establece la ubicación del primer spawn en el archivo JSON
    public void setFirstSpawnLocation(Location location) {
        firstSpawnData.addProperty("world", location.getWorld().getName());
        firstSpawnData.addProperty("x", location.getX());
        firstSpawnData.addProperty("y", location.getY());
        firstSpawnData.addProperty("z", location.getZ());
        firstSpawnData.addProperty("yaw", location.getYaw());
        firstSpawnData.addProperty("pitch", location.getPitch());

        saveFirstSpawnData();
    }
}
