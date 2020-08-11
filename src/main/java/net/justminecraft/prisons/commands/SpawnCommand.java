package net.justminecraft.prisons.commands;

import net.justminecraft.prisons.PrisonsPlugin;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;

public class SpawnCommand implements CommandExecutor {

    public SpawnCommand(PrisonsPlugin plugin) {
        PluginCommand command = plugin.getCommand("spawn");

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

        Player player = (Player) sender;

        player.teleport(Bukkit.getWorlds().get(0).getSpawnLocation().add(Math.random(), 0.5, Math.random()));

        sender.sendMessage(ChatColor.GREEN + "Teleported to spawn.");

        return true;
    }
}
