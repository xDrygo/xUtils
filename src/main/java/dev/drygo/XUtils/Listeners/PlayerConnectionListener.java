package dev.drygo.XUtils.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import dev.drygo.XUtils.Utils.ChatUtils;
import dev.drygo.XUtils.Utils.PlayerUtils;

public class PlayerConnectionListener implements Listener {
    private final ChatUtils chatUtils;
    private final PlayerUtils playerUtils;

    public PlayerConnectionListener(ChatUtils chatUtils, PlayerUtils playerUtils) {
        this.chatUtils = chatUtils;
        this.playerUtils = playerUtils;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        event.setJoinMessage(null);
        Location playerLocation = player.getLocation();
        player.playSound(playerLocation, Sound.ENTITY_PLAYER_LEVELUP, 10f, 2f);
        for (Player players : Bukkit.getServer().getOnlinePlayers()) {
            players.sendMessage(chatUtils.getMessage("connection.join_message", players)
                    .replace("%target%", player.getName())
                    .replace("%target_prefix%", playerUtils.getPrefix(player)));
        }
    }
    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        event.setQuitMessage(null);
        for (Player players : Bukkit.getServer().getOnlinePlayers()) {
            players.sendMessage(chatUtils.getMessage("connection.leave_message", players)
                    .replace("%target%", player.getName())
                    .replace("%target_prefix%", playerUtils.getPrefix(player)));
        }
    }
}
