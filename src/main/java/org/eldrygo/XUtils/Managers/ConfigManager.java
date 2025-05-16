package org.eldrygo.XUtils.Managers;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.eldrygo.XUtils.Utils.ChatUtils;
import org.eldrygo.XUtils.XUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class ConfigManager {

    private final XUtils plugin;
    private final ChatUtils chatUtils;
    public FileConfiguration messagesConfig;
    public JSONObject warpsConfig;

    public ConfigManager(XUtils plugin) {
        this.plugin = plugin;
        this.chatUtils = new ChatUtils(this, plugin);
    }

    // Método para obtener el prefijo
    public String getPrefix() {
        return chatUtils.prefix;
    }

    // Método para obtener la configuración de mensajes
    public FileConfiguration getMessageConfig() {
        return messagesConfig;
    }

    // Devuelve la config de warps.json como JSONObject
    public JSONObject getWarpsConfig() {
        return warpsConfig;
    }

    public void loadConfig() {
        try {
            plugin.saveDefaultConfig();
            plugin.reloadConfig();
            plugin.getLogger().info("✅ The config.yml file successfully loaded.");
        } catch (Exception e) {
            plugin.getLogger().severe("❌ Failed on loading config.yml: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void reloadWarpsConfig() {
        // Ensure the 'data' folder exists
        File dataFolder = new File(plugin.getDataFolder(), "data");
        if (!dataFolder.exists()) {
            dataFolder.mkdirs();  // Create the folder if it doesn't exist
            plugin.getLogger().info("✅ The 'data' folder did not exist, it has been created.");
        }

        File warpsFile = new File(dataFolder, "warps.json");

        try {
            if (!warpsFile.exists()) {
                warpsFile.createNewFile();
                try (FileWriter writer = new FileWriter(warpsFile)) {
                    writer.write("{}"); // initial empty content
                }
                plugin.getLogger().info("✅ The warps.json file did not exist, it has been created at: " + warpsFile.getPath());
            } else {
                plugin.getLogger().info("✅ The warps.json file has been loaded successfully from: " + warpsFile.getPath());
            }

            // Using try-with-resources for automatic closing of FileReader
            try (FileReader reader = new FileReader(warpsFile)) {
                JSONParser parser = new JSONParser();
                warpsConfig = (JSONObject) parser.parse(reader);
            }

        } catch (Exception e) {
            plugin.getLogger().severe("❌ Failed to load warps.json due to an unexpected error at " + warpsFile.getPath() + ": " + e.getMessage());
            e.printStackTrace();
            warpsConfig = new JSONObject(); // fallback empty config
        }
    }

    public void reloadMessages() {
        try {
            File messagesFile = new File(plugin.getDataFolder(), "messages.yml");
            if (!messagesFile.exists()) {
                plugin.saveResource("messages.yml", false);
                plugin.getLogger().info("✅ The messages.yml file did not exist, it has been created.");
            } else {
                plugin.getLogger().info("✅ The messages.yml file has been loaded successfully.");
            }

            messagesConfig = YamlConfiguration.loadConfiguration(messagesFile);
            chatUtils.prefix = ChatUtils.formatColor("#9ae5ff&lx&r&lUtils &8»&r &cDefault Prefix &8»&r");
        } catch (Exception e) {
            plugin.getLogger().severe("❌ Failed to load messages configuration due to an unexpected error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void setPrefix(String prefix) {
        chatUtils.prefix = prefix;
    }
}
