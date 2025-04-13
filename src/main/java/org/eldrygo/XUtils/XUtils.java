package org.eldrygo.XUtils;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.eldrygo.XUtils.Managers.*;
import org.eldrygo.XUtils.Utils.*;

public class XUtils extends JavaPlugin {
    public String version;
    public FileConfiguration config;
    private LogsUtils logsUtils;

    @Override
    public void onEnable() {
        this.version = getDescription().getVersion();
        PlayerUtils playerUtils = new PlayerUtils(this);
        FirstSpawnManager firstSpawnManager = new FirstSpawnManager(this);
        ConfigManager configManager = new ConfigManager(this);
        WarpManager warpManager = new WarpManager(this);
        ChatUtils chatUtils = new ChatUtils(configManager, this);
        LoadUtils loadUtils = new LoadUtils(this, configManager, warpManager, chatUtils, firstSpawnManager, playerUtils);
        this.logsUtils = new LogsUtils(this);
        loadUtils.loadFeatures();
        logsUtils.sendStartupMessage();
    }

    @Override
    public void onDisable() {
        logsUtils.sendShutdownMessage();
    }
}
