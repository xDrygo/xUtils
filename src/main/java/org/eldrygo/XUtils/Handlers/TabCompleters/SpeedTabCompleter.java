package org.eldrygo.XUtils.Handlers.TabCompleters;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class SpeedTabCompleter implements TabCompleter {

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        List<String> suggestions = new ArrayList<>();

        if (args.length == 1) {
            // Speed multipliers
            for (int i = 1; i <= 10; i++) {
                suggestions.add(String.valueOf(i));
            }
        } else if (args.length == 2) {
            // Walk or fly
            suggestions.add("walk");
            suggestions.add("fly");
        } else if (args.length == 3) {
            // Player names or '*' for all players
            suggestions.add("*");
            for (Player player : sender.getServer().getOnlinePlayers()) {
                suggestions.add(player.getName());
            }
        }

        return suggestions;
    }
}