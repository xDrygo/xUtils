package dev.drygo.XUtils.Handlers.Commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.util.Vector;
import dev.drygo.XUtils.Managers.WarpManager;
import dev.drygo.XUtils.Utils.ChatUtils;
import dev.drygo.XUtils.Utils.PlayerUtils;
import org.jetbrains.annotations.NotNull;

public class WarpCommand implements CommandExecutor {

    private final WarpManager warpManager;
    private final ChatUtils chatUtils;
    private final PlayerUtils playerUtils;

    public WarpCommand(WarpManager warpManager, ChatUtils chatUtils, PlayerUtils playerUtils) {
        this.warpManager = warpManager;
        this.chatUtils = chatUtils;
        this.playerUtils = playerUtils;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String @NotNull [] args) {
        if (hasPermission(sender, "xutils.warp")) return noPermission(sender);

        if (args.length == 0) {
            sender.sendMessage(chatUtils.getMessage("warp.error.usage", null));
            return true;
        }

        String subcommand = args[0].toLowerCase();

        return switch (subcommand) {
            case "set" -> {
                if (hasPermission(sender, "xutils.warp.set")) yield noPermission(sender);
                yield handleSet(sender, args);
            }
            case "del" -> {
                if (hasPermission(sender, "xutils.warp.del")) yield noPermission(sender);
                yield handleDelete(sender, args);
            }
            default -> handleTeleport(sender, args);
        };
    }

    private boolean handleSet(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(chatUtils.getMessage("warp.error.missing_warp_name", null));
            return true;
        }

        String warpName = args[1];

        if (sender instanceof Player player) {
            Location loc = parseLocationFromArgs(player.getWorld(), player.getLocation(), args, 2);
            if (loc == null) {
                sender.sendMessage(chatUtils.getMessage("warp.error.invalid_coordinates", null));
                return true;
            }
            warpManager.setWarp(warpName, loc);
            sender.sendMessage(chatUtils.getMessage("warp.set.success", null).replace("%warp%", warpName));
        } else {
            if (args.length < 6) {
                sender.sendMessage(chatUtils.getMessage("warp.error.console_missing_coordinates", null));
                return true;
            }
            World world = Bukkit.getWorld(args[2]);
            if (world == null) {
                sender.sendMessage(chatUtils.getMessage("warp.error.invalid_world", null).replace("%world%", args[2]));
                return true;
            }
            Location loc = parseLocationFromArgs(world, new Location(world, 0, 0, 0), args, 3);
            if (loc == null) {
                sender.sendMessage(chatUtils.getMessage("warp.error.invalid_coordinates", null));
                return true;
            }
            warpManager.setWarp(warpName, loc);
            sender.sendMessage(chatUtils.getMessage("warp.set.success", null).replace("%warp%", warpName));
        }

        return true;
    }

    private boolean handleDelete(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(chatUtils.getMessage("warp.error.missing_warp_name", null));
            return true;
        }

        String warpName = args[1];
        if (!warpManager.getWarpNames().contains(warpName)) {
            sender.sendMessage(chatUtils.getMessage("warp.error.warp_not_found", null).replace("%warp%", warpName));
            return true;
        }

        warpManager.delWarp(warpName);
        sender.sendMessage(chatUtils.getMessage("warp.del.success", null).replace("%warp%", warpName));
        return true;
    }

    private boolean handleTeleport(CommandSender sender, String[] args) {
        String warpName = args[0];
        Location loc = warpManager.getWarp(warpName);

        if (loc == null) {
            sender.sendMessage(chatUtils.getMessage("warp.error.warp_not_found", null).replace("%warp%", warpName));
            return true;
        }

        if (args.length == 1 && sender instanceof Player player) {
            if (hasPermission(sender, "xutils.warp.tp.self")) return noPermission(sender);
            teleport(player, loc);
            player.sendMessage(chatUtils.getMessage("warp.teleport.success.self", null).replace("%warp%", warpName));
            return true;
        }

        if (args.length >= 2) {
            String targetName = args[1];

            if (targetName.equals("*")) {
                if (hasPermission(sender, "xutils.warp.tp.all")) return noPermission(sender);
                for (Player online : playerUtils.getAllPlayers()) {
                    teleport(online, loc);
                    online.sendMessage(chatUtils.getMessage("warp.teleport.success.all.target", null).replace("%warp%", warpName));
                }
                sender.sendMessage(chatUtils.getMessage("warp.teleport.success.all.sender", null));
                return true;
            }

            Player target = Bukkit.getPlayer(targetName);
            if (target == null) {
                sender.sendMessage(chatUtils.getMessage("warp.error.player_not_found", null).replace("%arg%", targetName));
                return true;
            }

            if (hasPermission(sender, "xutils.warp.tp.other")) return noPermission(sender);

            teleport(target, loc);
            target.sendMessage(chatUtils.getMessage("warp.teleport.success.other.target", null)
                    .replace("%warp%", warpName).replace("%sender%", sender.getName()));
            sender.sendMessage(chatUtils.getMessage("warp.teleport.success.other.sender", null)
                    .replace("%warp%", warpName).replace("%target%", target.getName()));
            return true;
        }

        sender.sendMessage(chatUtils.getMessage("warp.error.only_players", null));

        return true;
    }

    private Location parseLocationFromArgs(World world, Location defaultLoc, String[] args, int start) {
        try {
            double x = args.length > start && !args[start].equalsIgnoreCase("null") ? Double.parseDouble(args[start]) : defaultLoc.getX();
            double y = args.length > start + 1 && !args[start + 1].equalsIgnoreCase("null") ? Double.parseDouble(args[start + 1]) : defaultLoc.getY();
            double z = args.length > start + 2 && !args[start + 2].equalsIgnoreCase("null") ? Double.parseDouble(args[start + 2]) : defaultLoc.getZ();
            float yaw = args.length > start + 3 && !args[start + 3].equalsIgnoreCase("null") ? Float.parseFloat(args[start + 3]) : defaultLoc.getYaw();
            float pitch = args.length > start + 4 && !args[start + 4].equalsIgnoreCase("null") ? Float.parseFloat(args[start + 4]) : defaultLoc.getPitch();
            return new Location(world, x, y, z, yaw, pitch);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private boolean hasPermission(CommandSender sender, String node) {
        return !sender.hasPermission(node) && !sender.hasPermission("xutils.admin") && !sender.isOp();
    }

    private boolean noPermission(CommandSender sender) {
        sender.sendMessage(chatUtils.getMessage("error.no_permission", null));
        return true;
    }
    public static void teleport(Player player, Location target) {
        player.setVelocity(new Vector(0, 0, 0));
        player.setFallDistance(0);
        player.setNoDamageTicks(20);
        player.teleport(target, PlayerTeleportEvent.TeleportCause.PLUGIN);
    }
}
