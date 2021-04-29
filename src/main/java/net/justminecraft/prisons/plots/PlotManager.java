package net.justminecraft.prisons.plots;

import net.justminecraft.prisons.PrisonsPlugin;
import net.justminecraft.prisons.WorldEditUtil;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

import java.io.File;

public class PlotManager {
    private final PrisonsPlugin plugin;

    public PlotManager(PrisonsPlugin plugin) {
        this.plugin = plugin;

        plugin.getServer().getPluginManager().registerEvents(new PlotListener(this), plugin);
    }


    public Location getPlotSpawnLocation(Player player) {
        World world = getPlotWorld(player);
        return world.getSpawnLocation().add(0.5, 0, 0.5);
    }

    private World getPlotWorld(Player player) {
        String name = "plot-" + player.getUniqueId();
        World world = Bukkit.getWorld(name);
        
        if (world == null) {
            world = generateWorld(name);
        }
        
        return world;
    }

    private World generateWorld(String name) {
        long t = System.currentTimeMillis();
        
        boolean exists = new File(name).exists();
        
        if (exists) {
            plugin.getLogger().info("Loading plot world " + name + "...");
        } else {
            plugin.getLogger().info("Generating plot world " + name + "...");
        }

        World world = Bukkit.createWorld(new WorldCreator(name).generator(new PlotChunkGenerator()));
        world.setSpawnLocation(0, 150, 0);
        world.setKeepSpawnInMemory(false);

        world.setGameRuleValue("doDaylightCycle", "false");
        world.setGameRuleValue("doFireTick", "false");
        world.setGameRuleValue("doMobSpawning", "false");
        world.setGameRuleValue("keepInventory", "true");
        world.setTime(6000);

        world.getSpawnLocation().getBlock().getRelative(BlockFace.DOWN).setType(Material.GLOWSTONE);

        if (exists) {
            plugin.getLogger().info("Loaded plot world " + name + " (" + (System.currentTimeMillis() - t) + "ms)");
        } else {
            plugin.getLogger().info("Generated plot world " + name + " (" + (System.currentTimeMillis() - t) + "ms)");
        }
        
        if (world.getSpawnLocation().getBlock().getRelative(0, -2, 0).getType() == Material.AIR) {
            // Paste the initial plot schematic
            WorldEditUtil.pasteSchematic("prison_plot.schematic", world.getSpawnLocation(), true);
        }
        
        return world;
    }

    public boolean isPlotWorld(World world) {
        return world.getName().startsWith("plot-");
    }

    public boolean isInsideBuildArea(Block block) {
        return block.getX() >= -50 && block.getX() <= 50 && block.getZ() >= 7 && block.getZ() < 107;
    }
}
