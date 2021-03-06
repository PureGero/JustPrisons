package net.justminecraft.prisons.commands;

import net.justminecraft.prisons.PrisonsPlugin;
import net.justminecraft.prisons.setup.PrisonsSetup;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;

public class SetupPrisonsCommand implements CommandExecutor {

    private final PrisonsPlugin plugin;

    public SetupPrisonsCommand(PrisonsPlugin plugin) {
        this.plugin = plugin;

        PluginCommand command = plugin.getCommand("setupprisons");

        if (command != null) {
            command.setExecutor(this);
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        sender.sendMessage(ChatColor.RED + "Disabled due to how long this takes to run (like 30mins)\nPlease re-enable it in net.justminecraft.prisons.commands.SetupPrisonsCommand");

        // Commented out to disable it
        new PrisonsSetup(plugin);

        return true;
    }
}
