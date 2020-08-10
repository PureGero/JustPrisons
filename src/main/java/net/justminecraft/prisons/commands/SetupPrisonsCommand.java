package net.justminecraft.prisons.commands;

import net.justminecraft.prisons.PrisonsPlugin;
import net.justminecraft.prisons.setup.PrisonsSetup;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;

public class SetupPrisonsCommand implements CommandExecutor {

    private final PrisonsPlugin plugin;

    public SetupPrisonsCommand(PrisonsPlugin plugin) {
        this.plugin = plugin;

        PluginCommand spawnCommand = plugin.getCommand("setupprisons");

        if (spawnCommand != null) {
            spawnCommand.setExecutor(this);
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        new PrisonsSetup(plugin);

        return true;
    }
}
