package net.justminecraft.prisons.villager;

import net.justminecraft.prisons.PrisonsPlugin;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

public class MoveToLocationRunnable implements Runnable {

    private static final double SPEED = 0.1;

    private final PrisonsPlugin plugin;
    private final Entity entity;
    private final Location to;
    private final Location from;

    public MoveToLocationRunnable(PrisonsPlugin plugin, Entity entity, Location to) {
        this.plugin = plugin;
        this.entity = entity;
        this.to = to;
        this.from = entity.getLocation();

        run();
    }

    @Override
    public void run() {
        double angle = Math.atan2(from.getX() - to.getX(), to.getZ() - from.getZ());

        from.setX(from.getX() - SPEED * Math.sin(angle));
        from.setY(entity.getLocation().getY());
        from.setZ(from.getZ() + SPEED * Math.cos(angle));

        from.setYaw((float) angle);
        from.setPitch(0);

        if (from.distanceSquared(to) >= SPEED * SPEED) {
            entity.teleport(from);
            plugin.getServer().getScheduler().runTaskLater(plugin, this, 1L);
        } else {
            // We're done
            entity.teleport(to);
        }
    }
}
