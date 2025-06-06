package dev.drygo.XUtils.Managers;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import dev.drygo.XUtils.XUtils;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CmdBlockerManager {
    private final XUtils plugin;

    private Set<String> allowedCommands = new HashSet<>();

    public CmdBlockerManager(XUtils plugin) {
        this.plugin = plugin;
        loadConfig();
    }

    private void loadConfig() {
        File configFile = new File(plugin.getDataFolder(), "cmd_blocker.yml");
        try {
            if(!configFile.exists()) {
                plugin.saveResource("cmd_blocker.yml", false);
            }

            FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);

            List<String> commandList = config.getStringList("allowed_commands");
            allowedCommands = new HashSet<>();
            for (String cmd : commandList) {
                allowedCommands.add(cmd.toLowerCase());
            }
            plugin.getLogger().info("✅ The cmd_blocker.yml file has been loaded successfully.");
        } catch (Exception e) {
            plugin.getLogger().severe("❌ Failed to load allowed_commands.json due to an unexpected error at " + configFile.getPath() + ": " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public boolean isAllowedCommand(String command) { return allowedCommands.contains(command.toLowerCase()); }
    public void reload() { loadConfig(); }
    public Set<String> getAllowedCommands() { return allowedCommands; }
}
