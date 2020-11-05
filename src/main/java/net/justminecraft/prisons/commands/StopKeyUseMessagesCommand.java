package net.justminecraft.prisons.commands;

import net.justminecraft.prisons.PrisonsPlugin;
import net.justminecraft.prisons.playerdata.PlayerData;
import net.justminecraft.prisons.playerdata.PlayerDataManager;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;

public class StopKeyUseMessagesCommand implements CommandExecutor {

    public StopKeyUseMessagesCommand(PrisonsPlugin plugin) {
        PluginCommand command = plugin.getCommand("stopkeyusemessages");

        if (command != null) {
            command.setExecutor(this);
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "You must be a player to execute this command.");
            return false;
        }

        PlayerData data = PlayerDataManager.get((Player) sender);

        data.setStopKeyUseMessages(!data.getStopKeyUseMessages());

        if (data.getStopKeyUseMessages()) {
            sender.sendMessage(ChatColor.GREEN + "You will no longer receive key use messages");
        } else {
            sender.sendMessage(ChatColor.GREEN + "You will now receive key use messages");
        }

        return true;
    }
}
