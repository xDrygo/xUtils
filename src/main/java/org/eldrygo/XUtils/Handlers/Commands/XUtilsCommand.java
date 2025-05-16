package org.eldrygo.XUtils.Handlers.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.eldrygo.XUtils.Managers.CmdBlockerManager;
import org.eldrygo.XUtils.Utils.ChatUtils;
import org.eldrygo.XUtils.Utils.LoadUtils;
import org.jetbrains.annotations.NotNull;

public class XUtilsCommand implements CommandExecutor {
    private final ChatUtils chatUtils;
    private final LoadUtils loadUtils;
    private final CmdBlockerManager cmdBlockerManager;

    public XUtilsCommand(ChatUtils chatUtils, LoadUtils loadUtils, CmdBlockerManager cmdBlockerManager) {
        this.chatUtils = chatUtils;
        this.loadUtils = loadUtils;
        this.cmdBlockerManager = cmdBlockerManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (args.length < 1) {
            sender.sendMessage(chatUtils.getMessage("xutils.command.usage", null));
            return true;
        }

        String action = args[0];

        if (action.equals("reload")) {
            if (!sender.hasPermission("xutils.reload") && !sender.isOp()) {
                sender.sendMessage(chatUtils.getMessage("error.no_permission", (Player) sender));
            } else {
                handleReload(sender);
            }
        } else {
            sender.sendMessage(chatUtils.getMessage("xutils.command.usage", null));
        }
        return false;
    }

    public void handleReload(CommandSender sender) {
        try {
            loadUtils.loadConfigFiles();
            cmdBlockerManager.reload();
        } catch (Exception e) {
            sender.sendMessage(chatUtils.getMessage("xutils.command.reload.error", (Player) sender));
            return;
        }
        sender.sendMessage(chatUtils.getMessage("xutils.command.reload.success", (Player) sender));
    }
}
