package net.justminecraft.prisons.setup;

import net.justminecraft.prisons.WorldEditUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class SpawnSetup implements Runnable {

    private final PrisonsSetup prisonsSetup;
    private Location spawn;

    public SpawnSetup(PrisonsSetup prisonsSetup) {
        this.prisonsSetup = prisonsSetup;
    }

    @Override
    public void run() {
        World world = Bukkit.getWorlds().get(0);

        world.setSpawnLocation(0, 64, 0);
        world.setGameRuleValue("doDaylightCycle", "false");
        world.setGameRuleValue("doFireTick", "false");
        world.setGameRuleValue("doMobSpawning", "false");
        world.setGameRuleValue("keepInventory", "true");
        world.setTime(6000);

        spawn = world.getSpawnLocation();

        prisonsSetup.queue(this::clearSpawn);
        prisonsSetup.queue(this::pasteSpawn);
    }

    private void clearSpawn() {
        WorldEditUtil.clearArea(spawn.getChunk(), 6);
    }

    private void pasteSpawn() {
        WorldEditUtil.pasteSchematic("prison_spawn.schematic", spawn);
    }
}
