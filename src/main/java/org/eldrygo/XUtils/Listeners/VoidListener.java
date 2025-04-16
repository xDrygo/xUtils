package org.eldrygo.XUtils.Listeners;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.eldrygo.XUtils.Managers.FirstSpawnManager;
import org.eldrygo.XUtils.Utils.ChatUtils;
import org.eldrygo.XUtils.XUtils;

import java.util.List;

public class VoidListener implements Listener {
    private final XUtils plugin;
    private final FirstSpawnManager firstSpawnManager;
    private final ChatUtils chatUtils;

    public VoidListener(XUtils plugin, FirstSpawnManager firstSpawnManager, ChatUtils chatUtils) {
        this.plugin = plugin;
        this.firstSpawnManager = firstSpawnManager;
        this.chatUtils = chatUtils;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        World playerWorld = player.getLocation().getWorld();
        String playerWorldName = playerWorld.getName();
        List<String> voidWorlds = plugin.getConfig().getStringList("settings.void_tp.worlds");
        float minY = plugin.getConfig().getInt("settings.void_tp.trigger_y");
        boolean sendMessage = plugin.getConfig().getBoolean("settings.void_tp.send_message");

        if (voidWorlds.contains(playerWorldName) && player.getLocation().getY() < minY) {
            Location spawnLocation = firstSpawnManager.getFirstSpawnLocation();

            if (spawnLocation != null) {
                player.teleport(spawnLocation);
                if (sendMessage) {
                    player.sendMessage(chatUtils.getMessage("void_tp.on_fall", null));
                }
            } else {
                plugin.getLogger().warning("❌ No se pudo teletransportar a " + player.getName() + " porque la ubicación de spawn no está definida.");
            }
        }
    }
}
