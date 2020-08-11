package net.justminecraft.prisons;

import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

public class Mine {

    private final String name;
    private final Location offset;
    private final List<Location> spawnLocations = new ArrayList<>();
    private final List<MineBoundary> boundaries = new ArrayList<>();

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
}
