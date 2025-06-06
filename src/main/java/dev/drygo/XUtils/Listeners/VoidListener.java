package dev.drygo.XUtils.Listeners;

import dev.drygo.XSpawn.API.XSpawnAPI;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import dev.drygo.XUtils.Utils.ChatUtils;
import dev.drygo.XUtils.XUtils;

import java.util.List;

public class VoidListener implements Listener {
    private final XUtils plugin;
    private final ChatUtils chatUtils;

    public VoidListener(XUtils plugin, ChatUtils chatUtils) {
        this.plugin = plugin;
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

            Location spawnLocation;
            if (Bukkit.getPluginManager().getPlugin("xSpawn") != null) {
                spawnLocation = XSpawnAPI.getSpawnFor(player);
            } else {
                spawnLocation = player.getWorld().getSpawnLocation();
            }

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
