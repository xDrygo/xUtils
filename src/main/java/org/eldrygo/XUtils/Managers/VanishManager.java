package org.eldrygo.XUtils.Managers;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.eldrygo.XUtils.Utils.ChatUtils;
import org.eldrygo.XUtils.XUtils;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.HashSet;
import java.util.Set;

public class VanishManager {

    private final XUtils plugin;
    private final Set<Player> vanishedPlayers = new HashSet<>();
    private final ConfigManager configManager;

    public VanishManager(XUtils plugin, ConfigManager configManager) {
        this.plugin = plugin;
        this.configManager = configManager;
        startInvisibilityTask();
    }

    public void setVanished(Player player, boolean vanish) {
        if (vanish) {
            if (vanishedPlayers.add(player)) {
                applyInvisibility(player);
                player.setCollidable(false);
                plugin.getLogger().warning("👻 Player " + player.getName() + " is now vanished.");
            }
        } else {
            if (vanishedPlayers.remove(player)) {
                player.removePotionEffect(PotionEffectType.INVISIBILITY);
                player.setCollidable(true);
                plugin.getLogger().warning("👻 Player " + player.getName() + " is no longer vanished.");
            }
        }
    }

    public boolean isVanished(Player player) {
        return vanishedPlayers.contains(player);
    }

    private void applyInvisibility(Player player) {
        // Agregar el efecto de invisibilidad y sobrescribir el anterior si ya lo tiene.
        player.addPotionEffect(new PotionEffect(
                PotionEffectType.INVISIBILITY, 160, 0, false, false // 8 segundos de invisibilidad
        ));
    }

    private void startInvisibilityTask() {
        new BukkitRunnable() {

            @Override
            public void run() {
                for (Player vanished : new HashSet<>(vanishedPlayers)) {
                    if (vanished.isOnline()) {
                        applyInvisibility(vanished);
                        String actionMessage = configManager.getMessageConfig().getString("vanish.actionbar");
                        // Mostrar mensaje en actionbar
                        vanished.spigot().sendMessage(
                                ChatMessageType.ACTION_BAR,
                                new TextComponent(ChatUtils.formatColor(actionMessage))
                        );
                    }
                }
            }
        }.runTaskTimer(plugin, 20L, 40L); // cada 2 segundos
    }
}
