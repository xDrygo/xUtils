package org.eldrygo.XUtils.Handlers.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.eldrygo.XUtils.Managers.FirstSpawnManager;
import org.eldrygo.XUtils.Utils.ChatUtils;
import org.jetbrains.annotations.NotNull;

public class FirstSpawnCommand implements CommandExecutor {

    private final FirstSpawnManager firstSpawnManager;
    private final ChatUtils chatUtils;

    public FirstSpawnCommand(FirstSpawnManager firstSpawnManager, ChatUtils chatUtils) {
        this.firstSpawnManager = firstSpawnManager;
        this.chatUtils = chatUtils;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String @NotNull [] args) {
        if (!sender.hasPermission("xutils.firstspawn.set") && !sender.isOp()) {
            sender.sendMessage(chatUtils.getMessage("error.no_permission", null));
            return true;
        }

        if (!(sender instanceof Player player)) {
            sender.sendMessage(chatUtils.getMessage("firstspawn.error.only_players", null));
            return true;
        }

        firstSpawnManager.setFirstSpawnLocation(player.getLocation());

        sender.sendMessage(chatUtils.getMessage("firstspawn.set.success", null).replace("%location%", player.getLocation().toString()));

        return true;
    }
}
