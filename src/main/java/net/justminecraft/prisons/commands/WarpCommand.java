package net.justminecraft.prisons.commands;

import net.justminecraft.prisons.mines.Mine;
import net.justminecraft.prisons.PrisonsPlugin;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class WarpCommand implements CommandExecutor, TabCompleter {

    private final PrisonsPlugin plugin;

    public WarpCommand(PrisonsPlugin plugin) {
        this.plugin = plugin;

        PluginCommand command = plugin.getCommand("warp");

        if (command != null) {
            command.setExecutor(this);
            command.setTabCompleter(this);
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "You must be a player to execute this command.");
            return false;
        }

        if (args.length < 1) {
            sender.sendMessage(ChatColor.RED + "Usage: /" + label + " <warp>");
            return false;
        }

        Mine mine = plugin.getMineManager().getMine(args[0]);

        if (mine == null) {
            sender.sendMessage(ChatColor.RED + "Unknown warp '" + args[0] + "'.");
            return false;
        }

        Player player = (Player) sender;

        player.teleport(mine.getRandomSpawnLocation());

        sender.sendMessage(ChatColor.GREEN + "Teleported to mine " + mine.getName() + ".");

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String label, String[] args) {
        List<String> tabs = new ArrayList<>();

        if (args.length == 1) {
            String warp = args[0].toLowerCase();

            for (Mine mine : plugin.getMineManager().getMines()) {
                if (mine.getName().toLowerCase().startsWith(warp) && commandSender.hasPermission("prisons." + mine.getName().toLowerCase())) {
                    tabs.add(mine.getName());
                }
            }
        }

        return tabs;
    }
}
