package dev.drygo.XUtils;

import org.bukkit.plugin.java.JavaPlugin;
import dev.drygo.XUtils.Managers.*;
import dev.drygo.XUtils.Utils.*;

public class XUtils extends JavaPlugin {
    public String version;
    private LogsUtils logsUtils;
    private WarpManager warpManager;

    @Override
    public void onEnable() {
        this.version = getDescription().getVersion();
        this.logsUtils = new LogsUtils(this);
        PlayerUtils playerUtils = new PlayerUtils(this);
        FirstSpawnManager firstSpawnManager = new FirstSpawnManager(this);
        ConfigManager configManager = new ConfigManager(this);
        this.warpManager = new WarpManager(this);
        ChatUtils chatUtils = new ChatUtils(configManager, this);
        CmdBlockerManager cmdBlockerManager = new CmdBlockerManager(this);
        VanishManager vanishManager = new VanishManager(this, configManager);
        AnnouncementManager announcementManager = new AnnouncementManager(this, configManager);
        LoadUtils loadUtils = new LoadUtils(this, configManager, warpManager, chatUtils, playerUtils, cmdBlockerManager, vanishManager, announcementManager);
        loadUtils.loadFeatures();
        logsUtils.sendStartupMessage();
        announcementManager.start();
    }

    @Override
    public void onDisable() {
        if (warpManager != null) { warpManager.save(); }
        if (logsUtils != null) { logsUtils.sendShutdownMessage(); }
    }
}
