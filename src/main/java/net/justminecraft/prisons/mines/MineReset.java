package net.justminecraft.prisons.mines;

import net.justminecraft.prisons.PrisonsPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Material;

public class MineReset implements Runnable {

    private final MineBoundary boundary;
    private final Mine mine;
    private final int y;

    private MineReset(MineBoundary boundary, Mine mine, int y) {
        this.boundary = boundary;
        this.mine = mine;
        this.y = y;
    }

    public static void reset(Mine mine) {
        PrisonsPlugin.info("Resetting mine %s", mine.getName());

        mine.getBoundaries().forEach(boundary -> {
            int i = 0;
            for (int y = boundary.getTo().getBlockY() - 1; y >= boundary.getFrom().getY(); y--) {
                Bukkit.getScheduler().runTaskLater(PrisonsPlugin.getPlugin(), new MineReset(boundary, mine, y), ++i);
            }

            Bukkit.getScheduler().runTaskLater(PrisonsPlugin.getPlugin(), () -> walls(boundary), ++i);
        });
    }

    private static void walls(MineBoundary boundary) {
        // Walls
        for (int y = boundary.getFrom().getBlockY(); y < boundary.getTo().getY() - 1; y++) {
            for (int x = boundary.getFrom().getBlockX() - 1; x <= boundary.getTo().getBlockX(); x++) {
                boundary.getFrom().getWorld().getBlockAt(x, y, boundary.getFrom().getBlockZ() - 1).setType(randomWallMaterial());
                boundary.getFrom().getWorld().getBlockAt(x, y, boundary.getTo().getBlockZ()).setType(randomWallMaterial());
            }
            for (int z = boundary.getFrom().getBlockZ() - 1; z <= boundary.getTo().getBlockZ(); z++) {
                boundary.getFrom().getWorld().getBlockAt(boundary.getFrom().getBlockX() - 1, y, z).setType(randomWallMaterial());
                boundary.getFrom().getWorld().getBlockAt(boundary.getTo().getBlockX(), y, z).setType(randomWallMaterial());
            }
        }

        // Glowstone walls
        for (int x = boundary.getFrom().getBlockX() - 1; x <= boundary.getTo().getBlockX(); x++) {
            boundary.getFrom().getWorld().getBlockAt(x, boundary.getTo().getBlockY() - 1, boundary.getFrom().getBlockZ() - 1).setType(Material.GLOWSTONE);
            boundary.getFrom().getWorld().getBlockAt(x, boundary.getTo().getBlockY() - 1, boundary.getTo().getBlockZ()).setType(Material.GLOWSTONE);
        }
        for (int z = boundary.getFrom().getBlockZ() - 1; z <= boundary.getTo().getBlockZ(); z++) {
            boundary.getFrom().getWorld().getBlockAt(boundary.getFrom().getBlockX() - 1, boundary.getTo().getBlockY() - 1, z).setType(Material.GLOWSTONE);
            boundary.getFrom().getWorld().getBlockAt(boundary.getTo().getBlockX(), boundary.getTo().getBlockY() - 1, z).setType(Material.GLOWSTONE);
        }

        // Floor
        for (int x = boundary.getFrom().getBlockX(); x < boundary.getTo().getBlockX(); x++) {
            for (int z = boundary.getFrom().getBlockZ(); z < boundary.getTo().getBlockZ(); z++) {
                boundary.getFrom().getWorld().getBlockAt(x, boundary.getFrom().getBlockY() - 1, z).setType(randomWallMaterial());
            }
        }
    }

    private static Material randomWallMaterial() {
        if (Math.random() < 0.2) {
            return Material.GLOWSTONE;
        } else {
            return Material.BEDROCK;
        }
    }


    @Override
    public void run() {
        for (int x = boundary.getFrom().getBlockX(); x < boundary.getTo().getBlockX(); x++) {
            for (int z = boundary.getFrom().getBlockZ(); z < boundary.getTo().getBlockZ(); z++) {
                boundary.getFrom().getWorld().getBlockAt(x, y, z).setType(mine.getRandomMaterial());
            }
        }
    }

}
