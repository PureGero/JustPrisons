package net.justminecraft.prisons.inventory;

import net.justminecraft.prisons.PrisonsPlugin;
import net.justminecraft.prisons.Translate;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import puregero.network.VoteEvent;

import java.util.Map;
import java.util.TreeMap;

public class KeyListener implements Listener {

    private static Map<String, Location> keyChests = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
    static {
        keyChests.put(Key.VOTE_KEY, new Location(Bukkit.getWorlds().get(0), 4, 61, 19));
        keyChests.put(Key.COMMON_KEY, new Location(Bukkit.getWorlds().get(0), 2, 61, 20));
        keyChests.put(Key.RARE_KEY, new Location(Bukkit.getWorlds().get(0), 0, 61, 21));
        keyChests.put(Key.EPIC_KEY, new Location(Bukkit.getWorlds().get(0), -2, 61, 20));
        keyChests.put(Key.LEGENDARY_KEY, new Location(Bukkit.getWorlds().get(0), -4, 61, 19));
    }

    public KeyListener(PrisonsPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);

        for (Map.Entry<String, Location> entry : keyChests.entrySet()) {
            summonArmourStand(entry.getKey(), entry.getValue().clone().add(0.5, -1, 0.5));
        }
    }

    private void summonArmourStand(String name, Location location) {
        for (Entity entity : location.getWorld().getEntities()) {
            if (name.equals(entity.getCustomName())) {
                entity.remove();
            }
        }

        ArmorStand armorStand = location.getWorld().spawn(location, ArmorStand.class);
        armorStand.setCustomName(name);
        armorStand.setCustomNameVisible(true);
        armorStand.setVisible(false);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getItem() != null && event.getClickedBlock() != null
                && event.getItem().getType() == Material.TRIPWIRE_HOOK
                && event.getItem().getItemMeta() != null
                && event.getItem().getItemMeta().getDisplayName() != null
                && keyChests.containsKey(event.getItem().getItemMeta().getDisplayName())
                && keyChests.get(event.getItem().getItemMeta().getDisplayName()).getBlock().equals(event.getClickedBlock())) {
            ItemStack item = event.getItem();
            do {
                Key.useKey(event.getPlayer(), item);
                item.setAmount(item.getAmount() - 1);
            } while (event.getPlayer().isSneaking() && item.getAmount() > 0);
            event.getPlayer().setItemInHand(item);
            event.setCancelled(true);
        }
    }
}
