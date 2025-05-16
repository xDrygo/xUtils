package org.eldrygo.XUtils.Handlers.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.eldrygo.XUtils.Managers.AnnouncementManager;
import org.eldrygo.XUtils.Utils.ChatUtils;
import org.jetbrains.annotations.NotNull;

public class AnnouncementCommand implements CommandExecutor {
    private final ChatUtils chatUtils;
    private final AnnouncementManager announcementManager;

    public AnnouncementCommand(ChatUtils chatUtils, AnnouncementManager announcementManager) {
        this.chatUtils = chatUtils;
        this.announcementManager = announcementManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String @NotNull [] args) {
        if (!sender.hasPermission("xutils.announcement") && !sender.isOp()) {
            sender.sendMessage(chatUtils.getMessage("error.no_permission", null));
            return false;
        }
        if (args.length == 0) {
            sender.sendMessage(chatUtils.getMessage("announcements.command.no_args", null));
            return false;
        }

        String action = args[0];

        switch (action) {
            case "on" -> {
                if (announcementManager.isRunning()) {
                    sender.sendMessage(chatUtils.getMessage("announcements.command.value_on.already", null));
                    return false;
                }
                announcementManager.start();
                sender.sendMessage(chatUtils.getMessage("announcements.command.value_on.start", null));
                return false;
            }
            case "off" -> {
                if (!announcementManager.isRunning()) {
                    sender.sendMessage(chatUtils.getMessage("announcements.command.value_off.already", null));
                    return false;
                }
                announcementManager.stop();
                sender.sendMessage(chatUtils.getMessage("announcements.command.value_off.start", null));
                return false;
            }
        }
        return false;
    }
}
