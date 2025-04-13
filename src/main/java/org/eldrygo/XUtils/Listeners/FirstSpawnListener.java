package org.eldrygo.XUtils.Listeners;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.entity.Player;
import org.eldrygo.XUtils.Managers.FirstSpawnManager;

public class FirstSpawnListener implements Listener {

    private final FirstSpawnManager firstSpawnManager;

    public FirstSpawnListener(FirstSpawnManager firstSpawnManager) {
        this.firstSpawnManager = firstSpawnManager;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        // Si el jugador nunca ha jugado antes
        if (!player.hasPlayedBefore()) {
            // Obtener la ubicación del primer spawn
            Location firstSpawnLocation = firstSpawnManager.getFirstSpawnLocation();
            if (firstSpawnLocation != null) {
                player.teleport(firstSpawnLocation);
            }
        }
    }
}
