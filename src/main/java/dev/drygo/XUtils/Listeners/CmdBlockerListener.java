package dev.drygo.XUtils.Listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerCommandSendEvent;
import dev.drygo.XUtils.Managers.CmdBlockerManager;
import dev.drygo.XUtils.Utils.ChatUtils;

public class CmdBlockerListener implements Listener {

    private final CmdBlockerManager cmdBlockerManager;
    private final ChatUtils chatUtils;

    public CmdBlockerListener(CmdBlockerManager cmdBlockerManager, ChatUtils chatUtils) {
        this.cmdBlockerManager = cmdBlockerManager;
        this.chatUtils = chatUtils;
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        if (player.hasPermission("xutils.cmdblocker.bypass") || player.isOp()) return;

        String[] args = event.getMessage().split(" ");
        if (args.length == 0) return;

        String cmd = args[0].substring(1).toLowerCase(); // remove "/"

        if (!cmdBlockerManager.isAllowedCommand(cmd)) {
            event.setCancelled(true);
            player.sendMessage(chatUtils.getMessage("cmd_blocker.block_command", null));
        }
    }

    @EventHandler
    public void onTab(PlayerCommandSendEvent event) {
        Player player = event.getPlayer();
        if (player.hasPermission("xutils.cmdblocker.bypass") || player.isOp()) return;

        event.getCommands().retainAll(cmdBlockerManager.getAllowedCommands());
    }
}
