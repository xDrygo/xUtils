package dev.drygo.XUtils.Handlers.TabCompleters;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import dev.drygo.XUtils.Managers.WarpManager;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class WarpTabCompleter implements TabCompleter {

    private final WarpManager warpManager;

    public WarpTabCompleter(WarpManager warpManager) {
        this.warpManager = warpManager;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        List<String> suggestions = new ArrayList<>();

        // Si el primer argumento es "set" o "del"
        if (args.length == 1) {
            if ("set".startsWith(args[0].toLowerCase())) {
                suggestions.add("set");
            }
            if ("del".startsWith(args[0].toLowerCase())) {
                suggestions.add("del");
            }

            // Autocompletar los nombres de los warps existentes
            for (String warpName : warpManager.getWarpNames()) {
                if (warpName.toLowerCase().startsWith(args[0].toLowerCase())) {
                    suggestions.add(warpName);
                }
            }
        }

        // Si el primer argumento es un warp, completamos los jugadores o "*" para todos
        else if (args.length == 2) {
            if (warpManager.getWarpNames().contains(args[0])) {
                // Autocompletar jugadores online
                for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                    if (onlinePlayer.getName().toLowerCase().startsWith(args[1].toLowerCase())) {
                        suggestions.add(onlinePlayer.getName());
                    }
                }

                // Agregar "*" para teletransportar a todos
                if ("*".startsWith(args[1].toLowerCase())) {
                    suggestions.add("*");
                }
            }
        }

        // Si el primer argumento es "set", completamos las coordenadas XYZ
        else if (args.length == 3 && args[0].equalsIgnoreCase("set")) {
            if (!(sender instanceof Player)) {
                // Si el sender es la consola, necesitamos un world
                for (World world : Bukkit.getWorlds()) {
                    if (world.getName().toLowerCase().startsWith(args[2].toLowerCase())) {
                        suggestions.add(world.getName());  // Agregar nombre del mundo
                    }
                }
            } else {
                suggestions.add("<x>");
            }
        } else if (args.length == 4 && args[0].equalsIgnoreCase("set")) {
            if (!(sender instanceof Player)) {
                suggestions.add("<x>");
            } else {
                suggestions.add("<y>");
            }
        } else if (args.length == 5 && args[0].equalsIgnoreCase("set")) {
            if (!(sender instanceof Player)) {
                suggestions.add("<y>");
            } else {
                suggestions.add("<z>");
            }
        }

        // Para yaw y pitch, mostramos <yaw>, <pitch> o null
        else if (args.length == 6 && args[0].equalsIgnoreCase("set")) {
            if (!(sender instanceof Player)) {
                suggestions.add("<z>");
            } else {
                suggestions.add("<yaw>");
            }
        } else if (args.length == 7 && args[0].equalsIgnoreCase("set")) {
            if (!(sender instanceof Player)) {
                suggestions.add("<yaw>");
            } else {
                suggestions.add("<pitch>");
            }
        } else if (args.length == 8 && args[0].equalsIgnoreCase("set")) {
            if (!(sender instanceof Player)) {
                suggestions.add("<pitch>");
            }
        }

        // Si el primer argumento es "del", completamos el nombre del warp para eliminar
        else if (args.length == 2 && args[0].equalsIgnoreCase("del")) {
            // Completamos con los nombres de los warps existentes para eliminar
            for (String warpName : warpManager.getWarpNames()) {
                if (warpName.toLowerCase().startsWith(args[1].toLowerCase())) {
                    suggestions.add(warpName);
                }
            }
        }

        return suggestions;
    }
}
