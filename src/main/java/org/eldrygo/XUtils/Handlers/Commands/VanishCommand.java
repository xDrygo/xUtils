package org.eldrygo.XUtils.Handlers.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.eldrygo.XUtils.Managers.VanishManager;
import org.eldrygo.XUtils.Utils.ChatUtils;
import org.jetbrains.annotations.NotNull;

public class VanishCommand implements CommandExecutor {

    private final VanishManager vanishManager;
    private final ChatUtils chatUtils;

    public VanishCommand(VanishManager vanishManager, ChatUtils chatUtils) {
        this.vanishManager = vanishManager;
        this.chatUtils = chatUtils;
    }

    @Override
    public boolean onCommand(CommandSender sender, @NotNull Command command, @NotNull String label, String @NotNull [] args) {

        if (!sender.hasPermission("xutils.vanish.self") && !sender.hasPermission("xutils.vanish.others")) {
            sender.sendMessage(chatUtils.getMessage("error.no_permission", null));
            return true;
        }

        if (args.length == 0) {
            if (!(sender instanceof Player senderPlayer)) {
                sender.sendMessage(chatUtils.getMessage("vanish.only_players", null));
                return true;
            }
            toggleVanish(senderPlayer, senderPlayer);
        } else if (args.length == 1) {
            Player senderPlayer = Bukkit.getPlayerExact(sender.getName());
            if (args[0].equalsIgnoreCase("on")) {
                if (!sender.hasPermission("xutils.vanish.self")) {
                    sender.sendMessage(chatUtils.getMessage("error.no_permission", null));
                    return true;
                }
                vanishManager.setVanished(senderPlayer, true);
                assert senderPlayer != null;
                senderPlayer.sendMessage(chatUtils.getMessage("vanish.self.on", null)); // vanish.self.on
            } else if (args[0].equalsIgnoreCase("off")) {
                if (!sender.hasPermission("xutils.vanish.self")) {
                    sender.sendMessage(chatUtils.getMessage("error.no_permission", null));
                    return true;
                }
                vanishManager.setVanished(senderPlayer, false);
                assert senderPlayer != null;
                senderPlayer.sendMessage(chatUtils.getMessage("vanish.self.off", null)); // vanish.self.off
            } else {
                if (!sender.hasPermission("xplugin.vanish.others")) {
                    sender.sendMessage(chatUtils.getMessage("error.no_permission", null)); /// error.no_permission
                    return true;
                }

                Player target = Bukkit.getPlayerExact(args[0]);
                if (target == null) {
                    sender.sendMessage(chatUtils.getMessage("vanish.player_not_found", null).replace("%arg%", args[0])); // vanish.player_not_found
                    return true;
                }
                assert senderPlayer != null;
                toggleVanish(senderPlayer, target);
            }
        } else {
            sender.sendMessage(chatUtils.getMessage("vanish.usage", null)); // vanish.usage
        }

        return true;
    }

    private void toggleVanish(Player sender, Player target) {
        boolean vanished = vanishManager.isVanished(target);
        vanishManager.setVanished(target, !vanished);

        if (sender.equals(target)) {
            sender.sendMessage(!vanished ? chatUtils.getMessage("vanish.self.on", null) : chatUtils.getMessage("vanish.self.off", null)); // vanish.self.on - vanish.self.off
        } else {
            String targetName = target.getName();
            sender.sendMessage(!vanished
                    ? chatUtils.getMessage("vanish.other.sender.on", null).replace("%target%", targetName) // vanish.other.sender.on
                    : chatUtils.getMessage("vanish.other.sender.off", null).replace("%target%", targetName)); // vanish.other.sender.off
            target.sendMessage(!vanished
                    ? chatUtils.getMessage("vanish.other.target.on", null) // vanish.other.target.on
                    : chatUtils.getMessage("vanish.other.target.off", null)); // vanish.other.target.off
        }
    }
}