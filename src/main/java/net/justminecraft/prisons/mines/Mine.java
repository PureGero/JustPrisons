package net.justminecraft.prisons.mines;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Mine {

    private final String name;
    private final Location offset;
    private final List<Location> spawnLocations = new ArrayList<>();
    private final List<MineBoundary> boundaries = new ArrayList<>();

    private int blocksBroken = 0;

    public Mine(String name, Location offset) {
        this.name = name;
        this.offset = offset;
    }

    public String getName() {
        return name;
    }

    public List<Location> getSpawnLocations() {
        return spawnLocations;
    }

    public List<MineBoundary> getBoundaries() {
        return boundaries;
    }

    public Location getOffset() {
        return offset;
    }

    public Location getRandomSpawnLocation() {
        return spawnLocations.get((int) (Math.random() * spawnLocations.size()));
    }

    public Material getRandomMaterial() {
        return MineOres.getRandomMaterial(this);
    }

    public List<Player> getPlayers() {
        List<Player> players = new ArrayList<>();

        for (Player player : getRandomSpawnLocation().getWorld().getPlayers()) {
            if (player.getLocation().distanceSquared(getRandomSpawnLocation()) < 256*256) {
                players.add(player);
            }
        }

        return players;
    }

    public void reset() {
        blocksBroken = 0;

        for (Player player : getPlayers()) {
            player.sendMessage(ChatColor.GREEN + "Mine is resetting...");
            player.teleport(getRandomSpawnLocation());
        }

        MineReset.reset(this);
    }

    public boolean inMine(Block block) {
        for (MineBoundary boundary : boundaries) {
            if (boundary.getFrom().getBlockX() <= block.getX() && block.getX() < boundary.getTo().getBlockX() &&
                    boundary.getFrom().getBlockY() <= block.getY() && block.getY() < boundary.getTo().getBlockY() &&
                    boundary.getFrom().getBlockZ() <= block.getZ() && block.getZ() < boundary.getTo().getBlockZ()) {
                return true;
            }
        }

        return false;
    }

    public void onBlockBreak() {
        if (++blocksBroken >= getVolume() / 3) {
            reset();
        }
    }

    public int getVolume() {
        int volume = 0;

        for (MineBoundary boundary : boundaries) {
            volume += boundary.getVolume();
        }

        return volume;
    }
}
