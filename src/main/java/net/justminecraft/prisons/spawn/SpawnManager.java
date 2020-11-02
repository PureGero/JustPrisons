package net.justminecraft.prisons.spawn;

import net.justminecraft.prisons.PrisonsPlugin;
import org.bukkit.World;

public class SpawnManager {

    private World spawn;

    public SpawnManager(PrisonsPlugin plugin) {
        spawn = plugin.getServer().getWorlds().get(0);

        plugin.getServer().getPluginManager().registerEvents(new SpawnListener(this), plugin);
    }

    public World getSpawn() {
        return spawn;
    }
}
