package org.eldrygo.XUtils.Handlers.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.eldrygo.XUtils.Managers.CmdBlockerManager;
import org.eldrygo.XUtils.Managers.ConfigManager;
import org.eldrygo.XUtils.Utils.ChatUtils;
import org.eldrygo.XUtils.XUtils;
import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class XUtilsCommand implements CommandExecutor {
    private final XUtils plugin;
    private final ChatUtils chatUtils;
    private final ConfigManager configManager;
    private final CmdBlockerManager cmdBlockerManager;

    public XUtilsCommand(XUtils plugin, ChatUtils chatUtils, ConfigManager configManager, CmdBlockerManager cmdBlockerManager) {
        this.plugin = plugin;
        this.chatUtils = chatUtils;
        this.configManager = configManager;
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
            try {
                plugin.reloadConfig();
                plugin.config = plugin.getConfig();
                plugin.getLogger().info("✅ The config.yml file has been loaded successfully.");
            } catch (Exception e) {
                plugin.getLogger().severe("❌ Failed to reload plugin configuration due to an unexpected error: " + e.getMessage());
                e.printStackTrace();
            }
            try {
                File warpsFile = new File(plugin.getDataFolder(), "data/warps.json");
                if (!warpsFile.exists()) {
                    warpsFile.createNewFile();
                    try (FileWriter writer = new FileWriter(warpsFile)) {
                        writer.write("{}"); // contenido vacío inicial
                    }
                    plugin.getLogger().info("✅ The warps.json file did not exist, it has been created.");
                } else {
                    plugin.getLogger().info("✅ The warps.json file has been loaded successfully.");
                }

                JSONParser parser = new JSONParser();
                FileReader reader = new FileReader(warpsFile);
                configManager.warpsConfig = (JSONObject) parser.parse(reader);
                reader.close();
            } catch (Exception e) {
                plugin.getLogger().severe("❌ Failed to load warps configuration due to an unexpected error: " + e.getMessage());
                e.printStackTrace();
            }
            try {
                File messagesFile = new File(plugin.getDataFolder(), "messages.yml");
                if (!messagesFile.exists()) {
                    plugin.saveResource("messages.yml", false);
                    plugin.getLogger().info("✅ The messages.yml file did not exist, it has been created.");
                } else {
                    plugin.getLogger().info("✅ The messages.yml file has been loaded successfully.");
                }
                configManager.messagesConfig = YamlConfiguration.loadConfiguration(messagesFile);
                chatUtils.prefix = ChatUtils.formatColor("#9ae5ff&lx&r&lUtils &8»&r &cDefault Prefix &8»&r");
            } catch (Exception e) {
                plugin.getLogger().severe("❌ Failed to load messages configuration due to an unexpected error: " + e.getMessage());
                e.printStackTrace();
            }
            cmdBlockerManager.reload();
        } catch (Exception e) {
            sender.sendMessage(chatUtils.getMessage("xutils.command.reload.error", (Player) sender));
            return;
        }
        sender.sendMessage(chatUtils.getMessage("xutils.command.reload.success", (Player) sender));
    }
}
