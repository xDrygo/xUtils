package dev.drygo.XUtils.Managers;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import dev.drygo.XUtils.Utils.ChatUtils;
import dev.drygo.XUtils.XUtils;

import java.util.List;

public class AnnouncementManager {
    private final XUtils plugin;
    public BukkitTask task;
    private final ConfigManager configManager;
    private boolean isRunning;

    public AnnouncementManager(XUtils plugin, ConfigManager configManager) {
        this.plugin = plugin;
        this.configManager = configManager;
        this.isRunning = plugin.getConfig().getBoolean("settings.announcements.enabled", false);
    }

    public void start() {
        if (!isRunning) return;

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
        // Vuelve a cargar el estado desde config en caso de que haya cambiado
        this.isRunning = plugin.getConfig().getBoolean("settings.announcements.enabled", false);
        start();
    }

    public boolean isRunning() {
        return isRunning;
    }
}
