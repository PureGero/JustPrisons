package net.justminecraft.prisons;

import net.justminecraft.prisons.commands.*;
import net.justminecraft.prisons.inventory.*;
import net.justminecraft.prisons.inventory.blocks.BlockLoot;
import net.justminecraft.prisons.modsharprank.NewColors;
import net.justminecraft.prisons.plots.PlotManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.plugin.java.JavaPlugin;

import net.justminecraft.prisons.arenas.ArenaManager;
import net.justminecraft.prisons.arenas.PopulateMainArena;
import net.justminecraft.prisons.customloot.LootManager;
import net.justminecraft.prisons.inventory.boots.UpgradeGuiBootsListener;
import net.justminecraft.prisons.inventory.chestplate.UpgradeGuiChestplateListener;
import net.justminecraft.prisons.inventory.helmet.UpgradeGuiHelmetListener;
import net.justminecraft.prisons.inventory.leggings.UpgradeGuiLeggingsListener;
import net.justminecraft.prisons.inventory.pickaxe.PickaxeTimer;
import net.justminecraft.prisons.inventory.pickaxe.UpgradeGuiPickaxeListener;
import net.justminecraft.prisons.inventory.sword.SwordTimer;
import net.justminecraft.prisons.inventory.sword.UpgradeGuiSwordListener;
import net.justminecraft.prisons.mines.Mine;
import net.justminecraft.prisons.mines.MineManager;
import net.justminecraft.prisons.playerdata.PlayerDataManager;
import net.justminecraft.prisons.spawn.SpawnManager;
import net.justminecraft.prisons.villager.VillagerShopManager;

public class PrisonsPlugin extends JavaPlugin {

    private static PrisonsPlugin plugin;

    private MineManager mineManager;
    private ArenaManager arenaManager;
    private LootManager lootManager;
    private PlotManager plotManager;
    
    private final PopulateMainArena populateMain = new PopulateMainArena(arenaManager);

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
        arenaManager = new ArenaManager(this);
        lootManager = new LootManager(this);
        plotManager = new PlotManager(this);
        new MultiListener(this);
        new MultiTimer(this);
        new PickaxeTimer(this);
        new SwordTimer(this);
        new PlayerDataManager(this);
        new SpawnManager(this);
        new BlockLoot(this);
        new NewColors();
        new UpgradeGuiPickaxeListener(this);
        new UpgradeGuiSwordListener(this);
        new UpgradeGuiHelmetListener(this);
        new UpgradeGuiChestplateListener(this);
        new UpgradeGuiLeggingsListener(this);
        new UpgradeGuiBootsListener(this);
        new AmountListener(this);
        new VoteListener(this);
        new VillagerShopManager(this);

        enableCommands();

        Bukkit.getScheduler().runTaskLater(this, () -> mineManager.getMines().forEach(Mine::reset), 1);
        
        this.getServer().getScheduler().runTaskTimer(this, () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                populateMain.Populate(player);
            }
        }, 1, 1);
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
        new PayCommand(this);
    }

    public MineManager getMineManager() {
        return mineManager;
    }
    
    public ArenaManager getArenaManager() {
        return arenaManager;
    }
    
    public LootManager getLootManager() {
        return lootManager;
    }

    public PlotManager getPlotManager() {
        return plotManager;
    }
}