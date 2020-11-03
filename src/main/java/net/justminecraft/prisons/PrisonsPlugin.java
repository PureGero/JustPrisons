package net.justminecraft.prisons;

import net.justminecraft.prisons.commands.BalanceCommand;
import net.justminecraft.prisons.commands.SetupPrisonsCommand;
import net.justminecraft.prisons.commands.SpawnCommand;
import net.justminecraft.prisons.commands.WarpCommand;
import net.justminecraft.prisons.inventory.UpgradeGuiListener;
import net.justminecraft.prisons.mines.Mine;
import net.justminecraft.prisons.mines.MineManager;
import net.justminecraft.prisons.playerdata.PlayerDataManager;
import net.justminecraft.prisons.spawn.SpawnManager;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
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
        new PlayerDataManager(this);
        new SpawnManager(this);
        new UpgradeGuiListener(this);

        enableCommands();

        Bukkit.getScheduler().runTaskLater(this, () -> mineManager.getMines().forEach(Mine::reset), 1);
    }

    @EventHandler
    public void onDisable() {
        PlayerDataManager.saveAll();
    }

    private void enableCommands() {
        new BalanceCommand(this);
        new SetupPrisonsCommand(this);
        new SpawnCommand(this);
        new WarpCommand(this);
    }

    public MineManager getMineManager() {
        return mineManager;
    }
}
