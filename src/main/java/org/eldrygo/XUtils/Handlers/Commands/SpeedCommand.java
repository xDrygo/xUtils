package org.eldrygo.XUtils.Handlers;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.eldrygo.XUtils.Utils.ChatUtils;
import org.jetbrains.annotations.NotNull;

public class SpeedCommand implements CommandExecutor {

    private ChatUtils chatUtils;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {

        if (args.length < 1) {
            sender.sendMessage(chatUtils.getMessage("speed.error.usage", null));
            return true;
        }

        if (!(sender instanceof Player) && args.length < 3) {
            sender.sendMessage(chatUtils.getMessage("speed.error.self_as_console", null));
            return true;
        }

        // Obtener multiplicador
        float multiplier;
        try {
            multiplier = Float.parseFloat(args[0]);
        } catch (NumberFormatException e) {
            sender.sendMessage(chatUtils.getMessage("speed.error.multiplier_not_valid", null));
            return true;
        }

        if (multiplier <= 0 || multiplier > 10) {
            sender.sendMessage(chatUtils.getMessage("speed.error.multiplier_not_in_range", null));
            return true;
        }

        // Determinar tipo de velocidad
        String mode = null;
        if (args.length >= 2 && (args[1].equalsIgnoreCase("walk") || args[1].equalsIgnoreCase("fly"))) {
            mode = args[1].toLowerCase();
        }

        // Determinar el objetivo
        if (args.length >= 3 && args[2].equals("*")) {
            // Cambiar a todos los jugadores
            if (!sender.hasPermission("xutils.speed.all")) {
                sender.sendMessage(chatUtils.getMessage("error.no_permission", null));
                return true;
            }

            String actualMode = mode != null ? mode : (
                    sender instanceof Player && ((Player) sender).isFlying() ? "fly" : "walk"
            );
            int affected = 0;
            for (Player target : Bukkit.getOnlinePlayers()) {
                applySpeed(target, multiplier, actualMode);
                if (!target.equals(sender)) {
                    target.sendMessage(chatUtils.getMessage("speed.success.other." + actualMode, target)
                            .replace("%sender%", sender.getName()));
                }
                affected++;
            }

            sender.sendMessage(chatUtils.getMessage("speed.success.all", null)
                    .replace("%players%", String.valueOf(affected))
                    .replace("%mode%", actualMode)
                    .replace("%multiplier%", String.valueOf(multiplier)));

            return true;
        }

        // Target individual o self
        Player target;
        boolean isSelf;

        if (args.length >= 3) {
            target = Bukkit.getPlayerExact(args[2]);
            if (target == null) {
                sender.sendMessage(chatUtils.getMessage("speed.error.player_not_found", null).replace("%arg%", args[2]));
                return true;
            }
            isSelf = sender instanceof Player && sender.equals(target);
        } else {
            if (!(sender instanceof Player)) {
                sender.sendMessage(chatUtils.getMessage("speed.error.self_as_console", null));
                return true;
            }
            target = (Player) sender;
            isSelf = true;
        }

        if (isSelf && !sender.hasPermission("xutils.speed.self")) {
            sender.sendMessage(chatUtils.getMessage("error.no_permission", null));
            return true;
        }

        if (!isSelf && !sender.hasPermission("xutils.speed.others")) {
            sender.sendMessage(chatUtils.getMessage("error.no_permission", null));
            return true;
        }

        if (mode == null) {
            mode = target.isFlying() ? "fly" : "walk";
        }

        applySpeed(target, multiplier, mode);

        if (isSelf) {
            sender.sendMessage(chatUtils.getMessage("speed.success.self." + mode, target)
                    .replace("%multiplier%", String.valueOf(multiplier)));
        } else {
            sender.sendMessage(chatUtils.getMessage("speed.success.other." + mode, target)
                    .replace("%sender%", sender.getName()));
            target.sendMessage(chatUtils.getMessage("speed.success.other." + mode, target)
                    .replace("%sender%", sender.getName()));
        }

        return true;
    }

    private void applySpeed(Player target, float multiplier, String mode) {
        if (mode.equals("walk")) {
            float speed = Math.min(multiplier * 0.2f, 1f);
            target.setWalkSpeed(speed);
        } else {
            float speed = Math.min(multiplier * 0.1f, 1f);
            target.setFlySpeed(speed);
        }
    }
}
