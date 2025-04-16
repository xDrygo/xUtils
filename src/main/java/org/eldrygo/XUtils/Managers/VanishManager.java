package org.eldrygo.XUtils.Managers;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.eldrygo.XUtils.XUtils;

import java.util.HashSet;
import java.util.Set;

public class VanishManager {

    private final XUtils plugin;
    private final Set<Player> vanishedPlayers = new HashSet<>();

    public VanishManager(XUtils plugin) {
        this.plugin = plugin;
    }

    public void setVanished(Player player, boolean vanish) {
        if (vanish) {
            if (vanishedPlayers.add(player)) {
                for (Player online : Bukkit.getOnlinePlayers()) {
                    if (!online.equals(player)) {
                        online.hidePlayer(plugin, player);
                        plugin.getLogger().warning("👻 Player " + player + " is now vanished.");
                    }
                }
            }
        } else {
            if (vanishedPlayers.remove(player)) {
                for (Player online : Bukkit.getOnlinePlayers()) {
                    if (!online.equals(player)) {
                        online.showPlayer(plugin, player);
                        plugin.getLogger().warning("👻 Player " + player + " is not vanished anymore.");
                    }
                }
            }
        }
    }

    public boolean isVanished(Player player) {
        return vanishedPlayers.contains(player);
    }

    public void handleJoin(Player joining) {
        // Ocultar los vanished para el nuevo jugador
        for (Player vanished : vanishedPlayers) {
            joining.hidePlayer(plugin, vanished);
        }
    }
}
