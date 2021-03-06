package net.justminecraft.prisons.arenas;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class Arena {

    private final String name;
    private final Location offset;
    private final List<Location> spawnLocations = new ArrayList<>();


    public Arena(String name, Location offset) {
        this.name = name;
        this.offset = offset;
    }

    public String getName() {
        return name;
    }

    public List<Location> getSpawnLocations() {
        return spawnLocations;
    }

    public Location getOffset() {
        return offset;
    }

    public Location getRandomSpawnLocation() {
        return spawnLocations.get((int) (Math.random() * spawnLocations.size()));
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

}
