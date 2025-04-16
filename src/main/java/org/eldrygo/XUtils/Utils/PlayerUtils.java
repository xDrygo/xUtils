package org.eldrygo.XUtils.Utils;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.cacheddata.CachedMetaData;
import net.luckperms.api.model.user.User;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.eldrygo.XUtils.XUtils;

import java.util.ArrayList;
import java.util.List;

public class PlayerUtils {
    private final XUtils plugin;

    public PlayerUtils(XUtils plugin) {
        this.plugin = plugin;
    }

    public List<Player> getAllPlayers() {
        List<Player> filteredPlayers = new ArrayList<>();

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (plugin.getConfig().getBoolean("settings.all_targeted_commands_bypass.enabled", true)) {
                if (!plugin.getConfig().getStringList("settings.all_targeted_commands_bypass.player_list").contains(String.valueOf(player))) {
                    filteredPlayers.add(player);
                }
            } else {
                filteredPlayers.add(player);
            }
        }
        return filteredPlayers;
    }
    public String getPrefix(Player player) {
        if (Bukkit.getPluginManager().getPlugin("LuckPerms") != null) {
            String prefix = "";
            LuckPerms luckPerms = LuckPermsProvider.get();
            User user = luckPerms.getUserManager().getUser(player.getUniqueId());
            if (user != null) {
                CachedMetaData metaData = user.getCachedData().getMetaData();
                if (metaData.getPrefix() != null) {
                    prefix = metaData.getPrefix();
                }
            }
            return ChatUtils.formatColor(prefix);
        }
        return "Luckperms not installed.";
    }
}
