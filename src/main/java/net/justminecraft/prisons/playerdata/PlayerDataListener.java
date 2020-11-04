package net.justminecraft.prisons.playerdata;

import net.justminecraft.prisons.inventory.LegacyInventoryConverter;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerDataListener implements Listener {

    @EventHandler
    public void onPlayerPreLogin(AsyncPlayerPreLoginEvent event) {
        // Pre-load the playerdata
        PlayerDataManager.get(event.getUniqueId());
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        // Show the scoreboard
        PlayerDataManager.get(event.getPlayer()).getScoreboard().show(event.getPlayer());

        // Check for a legacy inventory to convert
        LegacyInventoryConverter.doConversion(event.getPlayer());

        // Update permissions
        PlayerDataManager.get(event.getPlayer()).updatePermissions();
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerQuit(PlayerQuitEvent event) {
        // Unload the playerdata
        PlayerDataManager.dispose(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        event.setFormat(event.getFormat().replace("%1$s", PlayerDataManager.get(event.getPlayer()).getRankPrefix() + "%1$s" + ChatColor.RESET));
    }

}
