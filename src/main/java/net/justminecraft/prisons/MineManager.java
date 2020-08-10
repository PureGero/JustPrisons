package net.justminecraft.prisons;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

public class MineManager extends GoogleSheet {

    private HashMap<String, Mine> mines = new HashMap<>();
    private Location nextOffset;
    private World world;

    public MineManager(PrisonsPlugin plugin) {
        super(plugin, "1ppUyeRFeEXgazITugzv0349xjk9MNEPQ7Hq3Y1Tw9yg");

        world = plugin.getServer().createWorld(new WorldCreator("mines").generatorSettings("3;minecraft:air"));

        nextOffset = new Location(world, 0, 0, 0);
    }

    public Collection<Mine> getMines() {
        return mines.values();
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

            if (args[0].equalsIgnoreCase("name")) {
                continue;
            }

            if (!args[0].isEmpty()) {
                mine = mines.computeIfAbsent(args[0], key -> new Mine(key, nextOffset.add(1000, 0, 0).clone()));
                keys.add(args[0]);
            }

            if (mine == null) {
                continue;
            }

            if (!args[1].isEmpty()) {
                mine.getSpawnLocations().add(new Location(
                        world,
                        Double.parseDouble(args[1]),
                        Double.parseDouble(args[2]),
                        Double.parseDouble(args[3]),
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
