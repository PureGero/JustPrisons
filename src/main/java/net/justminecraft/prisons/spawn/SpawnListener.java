package net.justminecraft.prisons.spawn;

import net.justminecraft.prisons.inventory.Upgrade;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;

public class SpawnListener implements Listener {

    private final SpawnManager spawnManager;

    public SpawnListener(SpawnManager spawnManager) {
        this.spawnManager = spawnManager;
    }

    @EventHandler
    public void onSpawn(PlayerSpawnLocationEvent event) {
        event.setSpawnLocation(spawnManager.getWorld().getSpawnLocation().add(Math.random(), 0, Math.random()));

        event.getPlayer().setFoodLevel(20);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if (!event.getPlayer().hasPlayedBefore()) {
            Bukkit.broadcastMessage(ChatColor.LIGHT_PURPLE + "Welcome " + event.getPlayer().getName() + " to JustPrisons!");

            ItemStack starterPickaxe = new ItemStack(Material.DIAMOND_PICKAXE);

            ItemMeta meta = starterPickaxe.getItemMeta();
            meta.setDisplayName(ChatColor.AQUA + "" + ChatColor.BOLD + "Starter Pickaxe");
            meta.setLore(new ArrayList<>(Arrays.asList("", ChatColor.WHITE + "Right-click this pickaxe to upgrade it")));
            starterPickaxe.setItemMeta(meta);

            Upgrade.setUpgrade(starterPickaxe, Upgrade.EFFICIENCY, BigInteger.TEN);

            event.getPlayer().getInventory().addItem(starterPickaxe);
        }
    }

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        if (event.getFoodLevel() < ((Player) event.getEntity()).getFoodLevel()) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getClickedBlock() != null
                && event.getClickedBlock().getWorld() == spawnManager.getWorld()
                && (event.getItem() == null || !event.getItem().getType().isEdible() || event.getAction() != Action.RIGHT_CLICK_BLOCK)) {
            event.setCancelled(true);
        }
    }

}
