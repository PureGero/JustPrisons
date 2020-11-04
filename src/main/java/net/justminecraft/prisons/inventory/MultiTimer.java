package net.justminecraft.prisons.inventory;

import net.justminecraft.prisons.PrisonsPlugin;
import net.justminecraft.prisons.playerdata.PlayerData;
import net.justminecraft.prisons.playerdata.PlayerDataManager;
import net.justminecraft.prisons.playerdata.PlayerScoreboardEntry;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashSet;

public class MultiTimer implements Runnable {

    private HashSet<Player> playersWithMulti = new HashSet<>();

    public MultiTimer(PrisonsPlugin plugin) {
        plugin.getServer().getScheduler().runTaskTimer(plugin, this, 20, 20);
    }

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            PlayerData data = PlayerDataManager.get(player);
            int multi = data.getMulti();
            boolean hasMulti = playersWithMulti.contains(player);

            if (multi > 0 || hasMulti) {
                data.getScoreboard().update(PlayerScoreboardEntry.MULTI);
            }

            if (multi == 0 && hasMulti) {
                playersWithMulti.remove(player);
            }
        }
    }
}
