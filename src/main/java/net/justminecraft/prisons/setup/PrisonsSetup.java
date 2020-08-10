package net.justminecraft.prisons.setup;

import net.justminecraft.prisons.Mine;
import net.justminecraft.prisons.PrisonsPlugin;
import org.bukkit.Bukkit;

import java.util.ArrayList;

public class PrisonsSetup implements Runnable {

    private final PrisonsPlugin plugin;
    private final ArrayList<Runnable> runnables = new ArrayList<>();

    public PrisonsSetup(PrisonsPlugin plugin) {
        this.plugin = plugin;
        broadcastStart();

        queue(new SpawnSetup(this));

        for (Mine mine : plugin.getMineManager().getMines()) {
            queue(new MineSetup(this, mine));
        }

        run();
    }

    public PrisonsPlugin getPlugin() {
        return plugin;
    }

    public void queue(Runnable runnable) {
        runnables.add(runnable);
    }

    private void broadcastStart() {
        Bukkit.broadcastMessage("Begun prisons setup");
    }

    private void broadcastFinish() {
        Bukkit.broadcastMessage("Finished prisons setup");
    }

    @Override
    public void run() {
        if (runnables.isEmpty()) {
            broadcastFinish();
            return;
        }

        runnables.remove(0).run();

        plugin.getServer().getScheduler().runTaskLater(plugin, this, 1);
    }
}
