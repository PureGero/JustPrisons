package net.justminecraft.prisons.arenas;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.entity.Player;

import net.justminecraft.prisons.GoogleSheet;
import net.justminecraft.prisons.PrisonsPlugin;
import net.justminecraft.prisons.arenas.mobs.MobListener;
import net.justminecraft.prisons.customloot.LootManager;

public class ArenaManager extends GoogleSheet {

    private HashMap<String, Arena> arenas = new HashMap<>();
    private Location nextOffset;
    private World world;

    public ArenaManager(PrisonsPlugin plugin) {
        super(plugin, "1HOLlfL1X2_DF7BuC7V-Qxr-KptTSrVU7ijztUVyCQnw");

        plugin.getServer().getPluginManager().registerEvents(new ArenaListener(this), plugin);
        plugin.getServer().getPluginManager().registerEvents(new MobListener(this, new LootManager(plugin)), plugin);

        world = plugin.getServer().createWorld(new WorldCreator("arenas").type(WorldType.FLAT).generatorSettings("3;minecraft:air").generateStructures(false));

        nextOffset = new Location(world, 0, 0, 0);
        
    }

    public Collection<Arena> getArenas() {
        return arenas.values();
    }

    public Arena getArena(String name) {
        return arenas.get(name.toLowerCase());
    }

    public Arena getArena(Player player) {
        if (player.getWorld() != world) {
            return null;
        }

        double distance = Double.MAX_VALUE;
        Arena closest = null;

        for (Arena arena : arenas.values()) {
            double d = player.getLocation().distanceSquared(arena.getRandomSpawnLocation());
            if (d < distance) {
                distance = d;
                closest = arena;
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

        for (Arena arena : arenas.values()) {
            arena.getSpawnLocations().clear();
        }

        Arena arena = null;

        for (String line : getLines()) {
            String[] args = line.split(",");

            String key = args[0].toLowerCase();

            if (key.equalsIgnoreCase("name")) {
                continue;
            }

            if (!key.isEmpty()) {
                arena = arenas.computeIfAbsent(key, key2 -> new Arena(args[0], nextOffset.clone()));
                keys.add(key);
            }

            if (arena == null) {
                continue;
            }

            if (!args[1].isEmpty()) {
                arena.getSpawnLocations().add(new Location(
                        world,
                        Double.parseDouble(args[1]) + 0.5,
                        Double.parseDouble(args[2]),
                        Double.parseDouble(args[3]) + 0.5,
                        Float.parseFloat(args[4]),
                        Float.parseFloat(args[5])
                ).add(arena.getOffset()));
            }

        }

        for (String key : arenas.keySet()) {
            if (!keys.contains(key)) {
                arenas.remove(key);
            }
        }
    }
}
