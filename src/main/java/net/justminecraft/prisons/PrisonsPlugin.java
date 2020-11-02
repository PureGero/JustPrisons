package net.justminecraft.prisons;

import net.justminecraft.prisons.commands.SetupPrisonsCommand;
import net.justminecraft.prisons.commands.SpawnCommand;
import net.justminecraft.prisons.commands.WarpCommand;
import net.justminecraft.prisons.mines.Mine;
import net.justminecraft.prisons.mines.MineManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class PrisonsPlugin extends JavaPlugin {

    private static PrisonsPlugin plugin;

    private MineManager mineManager;

    public static void info(String format, Object... args) {
        getPlugin().getLogger().info(String.format(format, args));
    }

    public static PrisonsPlugin getPlugin() {
        return plugin;
    }

    @Override
    public void onEnable() {
        plugin = this;

        mineManager = new MineManager(this);

        enableCommands();

        Bukkit.getScheduler().runTaskLater(this, () -> mineManager.getMines().forEach(Mine::reset), 1);
    }

    private void enableCommands() {
        new SetupPrisonsCommand(this);
        new SpawnCommand(this);
        new WarpCommand(this);
    }

    public MineManager getMineManager() {
        return mineManager;
    }
}
