package org.eldrygo.XUtils.Listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.eldrygo.XUtils.Managers.VanishManager;

public class VanishListener implements Listener {

    private final VanishManager vanishManager;

    public VanishListener(VanishManager manager) {
        this.vanishManager = manager;
    }

    @EventHandler
    public void onPickup(PlayerPickupItemEvent e) {
        if (vanishManager.isVanished(e.getPlayer())) e.setCancelled(true);
    }

    @EventHandler
    public void onEntityTarget(EntityTargetEvent e) {
        if (e.getTarget() instanceof Player player && vanishManager.isVanished(player)) {
            e.setCancelled(true);
        }
    }
}