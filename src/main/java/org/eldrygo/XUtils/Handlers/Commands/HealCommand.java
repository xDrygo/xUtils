package org.eldrygo.XUtils.Handlers.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.eldrygo.XUtils.Utils.ChatUtils;
import org.eldrygo.XUtils.Utils.PlayerUtils;
import org.jetbrains.annotations.NotNull;

public class HealCommand implements CommandExecutor {

    private final ChatUtils chatUtils;
    private final PlayerUtils playerUtils;

    public HealCommand(ChatUtils chatUtils, PlayerUtils playerUtils) {
        this.chatUtils = chatUtils;
        this.playerUtils = playerUtils;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {

        if (args.length == 0) {
            // Autocuración
            if (!(sender instanceof Player)) {
                sender.sendMessage(chatUtils.getMessage("heal.self.only_player", null));
                return true;
            }

            if (!sender.hasPermission("xutils.heal.self") && !sender.hasPermission("xutils.heal.*") && !sender.hasPermission("xutils.admin") && !sender.isOp()) {
                sender.sendMessage(chatUtils.getMessage("error.no_permission", null));
                return true;
            }

            Player player = (Player) sender;
            healPlayer(player);
            player.sendMessage(chatUtils.getMessage("heal.self.success", player));
            return true;

        } else {
            // Curar a todos con *
            if (args[0].equals("*")) {
                if (!sender.hasPermission("xutils.heal.all") && !sender.hasPermission("xutils.heal.*") && !sender.hasPermission("xutils.admin") && !sender.isOp()) {
                    sender.sendMessage(chatUtils.getMessage("error.no_permission", null));
                    return true;
                }

                int affected = 0;
                for (Player target : playerUtils.getAllPlayers()) {
                    healPlayer(target);
                    if (!sender.equals(target)) {
                        target.sendMessage(chatUtils.getMessage("heal.other.target", target).replace("%sender%", sender.getName()));
                    }
                    affected++;
                }

                sender.sendMessage(chatUtils.getMessage("heal.other.all", null).replace("%players%", String.valueOf(affected)));
                return true;
            }

            // Curar a otro jugador específico
            if (!sender.hasPermission("xutils.heal.others") && !sender.hasPermission("xutils.heal.*") && !sender.hasPermission("xutils.admin") && !sender.isOp()) {
                sender.sendMessage(chatUtils.getMessage("error.no_permission", null));
                return true;
            }

            Player target = Bukkit.getPlayerExact(args[0]);
            if (target == null) {
                sender.sendMessage(chatUtils.getMessage("heal.other.player_not_found", null).replace("%arg%", args[0]));
                return true;
            }

            healPlayer(target);
            sender.sendMessage(chatUtils.getMessage("heal.other.sender", target));
            if (!sender.equals(target)) {
                target.sendMessage(chatUtils.getMessage("heal.other.target", target).replace("%sender%", sender.getName()));
            }
            return true;
        }
    }

    private void healPlayer(Player player) {
        player.setHealth(player.getMaxHealth());
        player.setFoodLevel(20);
        player.setSaturation(20f);
    }
}
