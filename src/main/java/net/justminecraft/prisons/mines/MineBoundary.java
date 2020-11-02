package net.justminecraft.prisons.mines;

import org.bukkit.Location;

public class MineBoundary {
    private final Location from;
    private final Location to;
    private final int volume;

    public MineBoundary(Location from, Location to) {
        this.from = from;
        this.to = to;
        volume = (to.getBlockX() - from.getBlockX()) * (to.getBlockY() - from.getBlockY()) * (to.getBlockZ() - from.getBlockZ());
    }

    public Location getFrom() {
        return from;
    }

    public Location getTo() {
        return to;
    }

    public int getVolume() {
        return volume;
    }
}
