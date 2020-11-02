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

public class BalanceCommand implements CommandExecutor {

    public BalanceCommand(PrisonsPlugin plugin) {
        PluginCommand command = plugin.getCommand("balance");

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

        sender.sendMessage(ChatColor.GREEN + String.format("Tokens: %,d tokens", data.getTokens()));
        sender.sendMessage(ChatColor.GOLD + String.format("Coins: %,d coins", data.getCoins()));

        return true;
    }
}
