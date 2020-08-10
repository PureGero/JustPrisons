package net.justminecraft.prisons;

import org.bukkit.Location;

public class MineBoundary {
    private final Location from;
    private final Location to;
    private final double volume;

    public MineBoundary(Location from, Location to) {
        this.from = from;
        this.to = to;
        volume = (to.getX() - from.getX()) * (to.getY() - from.getY()) * (to.getZ() - from.getZ());
    }

    public Location getFrom() {
        return from;
    }

    public Location getTo() {
        return to;
    }

    public double getVolume() {
        return volume;
    }
}
