package org.eldrygo.XUtils.Utils;

import org.bukkit.Bukkit;
import org.eldrygo.XUtils.Handlers.Commands.*;
import org.eldrygo.XUtils.Handlers.TabCompleters.*;
import org.eldrygo.XUtils.Listeners.*;
import org.eldrygo.XUtils.Managers.*;
import org.eldrygo.XUtils.XUtils;

public class LoadUtils {
    private final XUtils plugin;
    private final ConfigManager configManager;
    private final WarpManager warpManager;
    private final ChatUtils chatUtils;
    private final FirstSpawnManager firstSpawnManager;
    private final PlayerUtils playerUtils;
    private final CmdBlockerManager cmdBlockerManager;
    private final VanishManager vanishManager;

    public LoadUtils(XUtils plugin, ConfigManager configManager, WarpManager warpManager, ChatUtils chatUtils, FirstSpawnManager firstSpawnManager, PlayerUtils playerUtils, CmdBlockerManager cmdBlockerManager, VanishManager vanishManager) {
        this.plugin = plugin;
        this.configManager = configManager;
        this.warpManager = warpManager;
        this.chatUtils = chatUtils;
        this.firstSpawnManager = firstSpawnManager;
        this.playerUtils = playerUtils;
        this.cmdBlockerManager = cmdBlockerManager;
        this.vanishManager = vanishManager;
    }

    public void loadFeatures() {
        loadConfigFiles();
        loadPlaceholderAPI();
        loadCommands();
        loadListeners();
    }

    private void loadListeners() {
        plugin.getServer().getPluginManager().registerEvents(new PlayerConnectionListener(chatUtils, playerUtils), plugin);
        plugin.getServer().getPluginManager().registerEvents(new FirstSpawnListener(firstSpawnManager), plugin);
        plugin.getServer().getPluginManager().registerEvents(new VoidListener(plugin, firstSpawnManager, chatUtils), plugin);
        plugin.getServer().getPluginManager().registerEvents(new CmdBlockerListener(cmdBlockerManager, chatUtils), plugin);
        plugin.getServer().getPluginManager().registerEvents(new VanishListener(vanishManager), plugin);
    }

    private void loadCommands() {
        loadXUtilsCommand();
        loadHealCommand();
        loadSpeedCommand();
        loadWarpCommand();
        loadFirstSpawnCommand();
        loadVanishCommand();
    }

    private void loadXUtilsCommand() {
        if (plugin.getCommand("xutils") == null) {
            plugin.getLogger().severe("❌ Error: /xutils command is not registered in plugin.yml");
        } else {
            plugin.getCommand("xutils").setExecutor(new XUtilsCommand(plugin, chatUtils, configManager, cmdBlockerManager));
            plugin.getCommand("xutils").setTabCompleter(new XUtilsTabCompleter());
            plugin.getLogger().info("✅ /xutils command was successfully loaded.");
        }
    }
    private void loadHealCommand() {
        if (plugin.getCommand("heal") == null) {
            plugin.getLogger().severe("❌ Error: /heal command is not registered in plugin.yml");
        } else {
            plugin.getCommand("heal").setExecutor(new HealCommand(chatUtils, playerUtils));
            plugin.getCommand("heal").setTabCompleter(new HealTabCompleter());
            plugin.getLogger().info("✅ /heal command was successfully loaded.");
        }
    }

    private void loadSpeedCommand() {
        if (plugin.getCommand("speed") == null) {
            plugin.getLogger().severe("❌ Error: /speed command is not registered in plugin.yml");
        } else {
            plugin.getCommand("speed").setExecutor(new SpeedCommand(chatUtils, playerUtils));
            plugin.getCommand("speed").setTabCompleter(new SpeedTabCompleter());
            plugin.getLogger().info("✅ /speed command was successfully loaded.");
        }
    }

    private void loadWarpCommand() {
        if (plugin.getCommand("warp") == null) {
            plugin.getLogger().severe("❌ Error: /warp command is not registered in plugin.yml");
        } else {
            plugin.getCommand("warp").setExecutor(new WarpCommand(warpManager, chatUtils, playerUtils));
            plugin.getCommand("warp").setTabCompleter(new WarpTabCompleter(warpManager));
            plugin.getLogger().info("✅ /warp command was successfully loaded.");
        }
    }
    private void loadFirstSpawnCommand() {
        if (plugin.getCommand("firstspawn") == null) {
            plugin.getLogger().severe("❌ Error: /firstspawn command is not registered in plugin.yml");
        } else {
            plugin.getCommand("firstspawn").setExecutor(new FirstSpawnCommand(firstSpawnManager, chatUtils));
            plugin.getCommand("firstspawn").setTabCompleter(new FirstSpawnTabCompleter());
            plugin.getLogger().info("✅ /firstspawn command was successfully loaded.");
        }
    }
    private void loadVanishCommand() {
        if (plugin.getCommand("vanish") == null) {
            plugin.getLogger().severe("❌ Error: /vanish command is not registered in plugin.yml");
        } else {
            plugin.getCommand("vanish").setExecutor(new VanishCommand(vanishManager, chatUtils));
            plugin.getCommand("vanish").setTabCompleter(new VanishTabCompleter());
            plugin.getLogger().info("✅ /vanish command was successfully loaded.");
        }
    }
    private void loadPlaceholderAPI() {
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            plugin.getLogger().info("✅ PlaceholderAPI detected. Placeholders will work.");
        } else {
            plugin.getLogger().warning("⚠ PlaceholderAPI not detected. Placeholders will not work.");
        }
    }

    public void loadConfigFiles() {
        try {
            plugin.reloadConfig();
            plugin.config = plugin.getConfig();
            plugin.getLogger().info("✅ The config.yml file has been loaded successfully.");
        } catch (Exception e) {
            plugin.getLogger().severe("❌ Failed to reload plugin configuration due to an unexpected error: " + e.getMessage());
            e.printStackTrace();
        }
        configManager.reloadWarpsConfig();
        configManager.reloadMessages();
        firstSpawnManager.loadFirstSpawnData();
        configManager.setPrefix(ChatUtils.formatColor(configManager.getMessageConfig().getString("prefix", "#9ae5ff&lx&r&lUtils &cDefault Prefix &8»&r")));
    }
}
