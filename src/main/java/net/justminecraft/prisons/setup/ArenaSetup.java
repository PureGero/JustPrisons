package net.justminecraft.prisons.setup;

import org.bukkit.World;

import net.justminecraft.prisons.WorldEditUtil;
import net.justminecraft.prisons.arenas.Arena;

public class ArenaSetup implements Runnable {

    private final PrisonsSetup prisonsSetup;
    private final Arena arena;

    public ArenaSetup(PrisonsSetup prisonsSetup, Arena arena) {
        this.prisonsSetup = prisonsSetup;
        this.arena = arena;
    }

    @Override
    public void run() {
        World world = prisonsSetup.getPlugin().getArenaManager().getWorld();

        world.setGameRuleValue("doDaylightCycle", "false");
        world.setGameRuleValue("doFireTick", "false");
        world.setGameRuleValue("doMobSpawning", "false");
        world.setGameRuleValue("keepInventory", "true");
        world.setTime(6000);

        prisonsSetup.queue(this::clearArena);
        prisonsSetup.queue(this::pasteArena);
    }

    private void clearArena() {
        WorldEditUtil.clearArea(arena.getOffset().getChunk(), 6);
    }

    private void pasteArena() {
        WorldEditUtil.pasteSchematic(arena.getName() + ".schematic", arena.getOffset().clone());
    }
}
