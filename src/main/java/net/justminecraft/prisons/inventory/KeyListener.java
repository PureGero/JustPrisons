package net.justminecraft.prisons.inventory;

import net.justminecraft.prisons.PrisonsPlugin;
import net.justminecraft.prisons.spawn.SpawnListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class KeyListener implements Listener {

    private static Map<String, Location> keysToChests = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
    private static Map<Location, String> chestsToKeys = new HashMap<>();
    static {
        addKeyChest(Key.VOTE_KEY, new Location(Bukkit.getWorlds().get(0), 4, 61, 19));
        addKeyChest(Key.COMMON_KEY, new Location(Bukkit.getWorlds().get(0), 2, 61, 20));
        addKeyChest(Key.RARE_KEY, new Location(Bukkit.getWorlds().get(0), 0, 61, 21));
        addKeyChest(Key.EPIC_KEY, new Location(Bukkit.getWorlds().get(0), -2, 61, 20));
        addKeyChest(Key.LEGENDARY_KEY, new Location(Bukkit.getWorlds().get(0), -4, 61, 19));
    }

    private static void addKeyChest(String key, Location location) {
        keysToChests.put(key, location);
        chestsToKeys.put(location, key);
    }

    public KeyListener(PrisonsPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);

        for (Map.Entry<String, Location> entry : keysToChests.entrySet()) {
            SpawnListener.summonArmourStand(entry.getKey(), entry.getValue().clone().add(0.5, -1, 0.5));
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getItem() != null && event.getClickedBlock() != null
                && event.getItem().getType() == Material.TRIPWIRE_HOOK
                && event.getItem().getItemMeta() != null
                && event.getItem().getItemMeta().getDisplayName() != null
                && keysToChests.containsKey(event.getItem().getItemMeta().getDisplayName())
                && keysToChests.get(event.getItem().getItemMeta().getDisplayName()).getBlock().equals(event.getClickedBlock())) {
            // Right-clicked a key chest with the right key
            ItemStack item = event.getItem();
            do {
                Key.useKey(event.getPlayer(), item);
                item.setAmount(item.getAmount() - 1);
            } while (event.getPlayer().isSneaking() && item.getAmount() > 0);
            event.getPlayer().setItemInHand(item);
            event.setCancelled(true);
        } else if (event.getClickedBlock() != null
                && chestsToKeys.containsKey(event.getClickedBlock().getLocation())) {
            // Right-clicked a key chest without a key
            event.getPlayer().sendMessage(ChatColor.RED + "This chest only accepts " + chestsToKeys.get(event.getClickedBlock().getLocation()) + "s");
            event.setCancelled(true);
        }
    }
}
