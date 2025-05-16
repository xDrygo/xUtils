package org.eldrygo.XUtils.Handlers.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.eldrygo.XUtils.Utils.ChatUtils;
import org.jetbrains.annotations.NotNull;

public class PingCommand implements CommandExecutor {
    private final ChatUtils chatUtils;

    public PingCommand(ChatUtils chatUtils) {
        this.chatUtils = chatUtils;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String @NotNull [] args) {
        if (!sender.hasPermission("xutils.ping") && !sender.isOp()) {
            sender.sendMessage(chatUtils.getMessage("error.no_permission", null));
            return false;
        }
        Player senderPlayer = (sender instanceof Player) ? (Player) sender : null;
        Player target;
        boolean targetIsSelf;
        if (args.length == 0) {
            target = senderPlayer;
            targetIsSelf = true;
        } else {
            target = Bukkit.getPlayerExact(args[0]);
            targetIsSelf = false;
        }
        if (target == null) {
            if (targetIsSelf) {
                sender.sendMessage(chatUtils.getMessage("ping.error.only_player", null));
            } else {
                sender.sendMessage(chatUtils.getMessage("ping.error.player_not_found", null).replace("%arg%", args[0]));
            }
        }

        String ping = String.valueOf(target.getPing());

        if (targetIsSelf) {
            sender.sendMessage(chatUtils.getMessage("ping.success.self", null).replace("%ping%", ping));
        } else {
            sender.sendMessage(chatUtils.getMessage("ping.success.other", null).replace("%ping%", ping).replace("%target%", target.getName()));
        }

        return false;
    }
}
