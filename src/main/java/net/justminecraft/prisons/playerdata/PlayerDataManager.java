package net.justminecraft.prisons.playerdata;

import net.justminecraft.prisons.PrisonsPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class PlayerDataManager {
    private static HashMap<UUID, PlayerData> cache = new HashMap<>();

    public PlayerDataManager(PrisonsPlugin plugin) {
        // Save every 5 minutes
        plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, PlayerDataManager::saveAll, 5 * 60 * 20, 5 * 60 * 2);

        // Register PlayerDataListener
        plugin.getServer().getPluginManager().registerEvents(new PlayerDataListener(), plugin);

        // Load all the online players
        Bukkit.getOnlinePlayers().forEach(PlayerDataManager::get);
    }

    public static PlayerData get(Player player) {
        return get(player.getUniqueId());
    }

    public static PlayerData get(UUID uuid) {
        if (cache.containsKey(uuid)) {
            return cache.get(uuid);
        }

        PlayerData data = PlayerDataLoader.load(uuid);
        cache.put(uuid, data);
        return data;
    }

    public static void saveAll() {
        for (PlayerData data : cache.values()) {
            data.save();
            if (canSafeDispose(data)) {
                cache.remove(data.uuid);
            }
        }
    }

    public static void dispose(UUID uuid) {
        PlayerData data = cache.remove(uuid);
        if (data != null) {
            data.save();
        }
    }

    private static boolean canSafeDispose(PlayerData i) {
        // Can safely dispose if player is offline
        return Bukkit.getPlayer(i.uuid) == null;
    }

}
