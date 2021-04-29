package net.justminecraft.prisons.mines;

import net.justminecraft.prisons.GoogleSheet;
import net.justminecraft.prisons.PrisonsPlugin;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

public class MineManager extends GoogleSheet {

    private final PrisonsPlugin plugin;
    private final HashMap<String, Mine> mines = new HashMap<>();
    private final Location nextOffset;
    private final World world;

    public MineManager(PrisonsPlugin plugin) {
        super(plugin, "1ppUyeRFeEXgazITugzv0349xjk9MNEPQ7Hq3Y1Tw9yg");
        this.plugin = plugin;

        plugin.getServer().getPluginManager().registerEvents(new MineListener(this), plugin);

        world = plugin.getServer().createWorld(new WorldCreator("mines").type(WorldType.FLAT).generatorSettings("3;minecraft:air").generateStructures(false));

        nextOffset = new Location(world, 0, 0, 0);
    }

    public Collection<Mine> getMines() {
        return mines.values();
    }

    public Mine getMine(String name) {
        return mines.get(name.toLowerCase());
    }

    public Mine getMine(Player player) {
        if (plugin.getPlotManager().isPlotWorld(player.getWorld()) && plugin.getPlotManager().isInsideMine(player.getLocation())) {
            return plugin.getPlotManager().getMine(player.getWorld());
        }
        
        if (player.getWorld() != world) {
            return null;
        }

        double distance = Double.MAX_VALUE;
        Mine closest = null;

        for (Mine mine : mines.values()) {
            double d = player.getLocation().distanceSquared(mine.getRandomSpawnLocation());
            if (d < distance) {
                distance = d;
                closest = mine;
            }
        }

        return closest;
    }

    public World getWorld() {
        return world;
    }

    @Override
    public void onContentsChange() {
        HashSet<String> keys = new HashSet<>();

        for (Mine mine : mines.values()) {
            mine.getSpawnLocations().clear();
            mine.getBoundaries().clear();
        }

        Mine mine = null;

        for (String line : getLines()) {
            String[] args = line.split(",");

            String key = args[0].toLowerCase();

            if (key.equalsIgnoreCase("name")) {
                continue;
            }

            if (!key.isEmpty()) {
                mine = mines.computeIfAbsent(key, key2 -> new Mine(args[0], nextOffset.add(1000, 0, 0).clone()));
                keys.add(key);
            }

            if (mine == null) {
                continue;
            }

            if (!args[1].isEmpty()) {
                mine.getSpawnLocations().add(new Location(
                        world,
                        Double.parseDouble(args[1]) + 0.5,
                        Double.parseDouble(args[2]),
                        Double.parseDouble(args[3]) + 0.5,
                        Float.parseFloat(args[4]),
                        Float.parseFloat(args[5])
                ).add(mine.getOffset()));
            }

            if (!args[6].isEmpty()) {
                mine.getBoundaries().add(new MineBoundary(new Location(
                        world,
                        Double.parseDouble(args[6]),
                        Double.parseDouble(args[8]) - Double.parseDouble(args[11]),
                        Double.parseDouble(args[7])
                ).add(mine.getOffset()), new Location(
                        world,
                        Double.parseDouble(args[6]) + Double.parseDouble(args[9]),
                        Double.parseDouble(args[8]),
                        Double.parseDouble(args[7]) + Double.parseDouble(args[10])
                ).add(mine.getOffset())));
            }
        }

        for (String key : mines.keySet()) {
            if (!keys.contains(key)) {
                mines.remove(key);
            }
        }
    }
}
