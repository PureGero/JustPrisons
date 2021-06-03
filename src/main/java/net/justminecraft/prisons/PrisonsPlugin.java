package net.justminecraft.prisons;

import net.justminecraft.prisons.commands.*;
import net.justminecraft.prisons.inventory.*;
import net.justminecraft.prisons.mines.Mine;
import net.justminecraft.prisons.mines.MineManager;
import net.justminecraft.prisons.modsharprank.NewColors;
import net.justminecraft.prisons.playerdata.PlayerDataManager;
import net.justminecraft.prisons.spawn.SpawnManager;
import net.justminecraft.prisons.villager.VillagerShopManager;
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
        new MultiListener(this);
        new MultiTimer(this);
        new PickaxeTimer(this);
        new PlayerDataManager(this);
        new SpawnManager(this);
        new NewColors();
        new UpgradeGuiListener(this);
        new VoteListener(this);
        new VillagerShopManager(this);

        enableCommands();

        Bukkit.getScheduler().runTaskLater(this, () -> mineManager.getMines().forEach(Mine::reset), 1);
    }

    @EventHandler
    public void onDisable() {
        PlayerDataManager.saveAll();
    }

    private void enableCommands() {
        new BalanceCommand(this);
        new KeyListener(this);
        new PrestigeCommand(this);
        new RankupCommand(this);
        new SetupPrisonsCommand(this);
        new SpawnCommand(this);
        new StopKeyMessagesCommand(this);
        new StopKeyUseMessagesCommand(this);
        new StopTokenMessagesCommand(this);
        new WarpCommand(this);
    }

    public MineManager getMineManager() {
        return mineManager;
    }
}
