package net.justminecraft.prisons.setup;

import net.justminecraft.prisons.Mine;
import net.justminecraft.prisons.WorldEditUtil;
import org.bukkit.World;

public class MineSetup implements Runnable {

    private final PrisonsSetup prisonsSetup;
    private final Mine mine;

    public MineSetup(PrisonsSetup prisonsSetup, Mine mine) {
        this.prisonsSetup = prisonsSetup;
        this.mine = mine;
    }

    @Override
    public void run() {
        World world = prisonsSetup.getPlugin().getMineManager().getWorld();

        world.setGameRuleValue("doDaylightCycle", "false");
        world.setGameRuleValue("doFireTick", "false");
        world.setGameRuleValue("doMobSpawning", "false");
        world.setTime(6000);

        prisonsSetup.queue(this::clearMine);
        prisonsSetup.queue(this::pasteMine);
    }

    private void clearMine() {
        WorldEditUtil.clearArea(mine.getOffset().getChunk(), 6);
    }

    private void pasteMine() {
        WorldEditUtil.pasteSchematic(mine.getName() + ".schematic", mine.getOffset().clone().add(0, mine.getBoundaries().get(0).getTo().getY(), 32));
    }
}
