package org.eldrygo.XUtils.Managers;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.eldrygo.XUtils.Utils.ChatUtils;
import org.eldrygo.XUtils.XUtils;

import java.util.List;

public class AnnouncementManager {
    private final XUtils plugin;
    public BukkitTask task;
    private final ConfigManager configManager;
    private boolean isRunning;

    public AnnouncementManager(XUtils plugin, ConfigManager configManager) {
        this.plugin = plugin;
        this.configManager = configManager;
    }

    public void start() {
        isRunning = true;
        int intervalSeconds = plugin.getConfig().getInt("settings.announcements.interval", 60);
        List<String> announcementMessage = configManager.getMessageConfig().getStringList("announcements.string");

        task = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            for (Player target : Bukkit.getOnlinePlayers()) {
                for (String line : announcementMessage) {
                    target.sendMessage(ChatUtils.formatColor(line));
                }
            }
        }, intervalSeconds * 20L, intervalSeconds * 20L);
    }

    public void stop() {
        if (task != null && isRunning) {
            task.cancel();
            isRunning = false;
        }
    }

    public void restart() {
        stop();
        start();
    }

    public boolean isRunning() {
        return isRunning;
    }
}
