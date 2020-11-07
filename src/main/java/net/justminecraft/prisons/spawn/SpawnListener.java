package net.justminecraft.prisons.spawn;

import net.justminecraft.prisons.PrisonsPlugin;
import net.justminecraft.prisons.inventory.Multi;
import net.justminecraft.prisons.inventory.Upgrade;
import net.justminecraft.prisons.playerdata.PlayerData;
import net.justminecraft.prisons.playerdata.PlayerDataManager;
import org.bukkit.*;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class SpawnListener implements Listener {

    private static Map<String, Location> portals = new HashMap<>();
    static {
        portals.put(ChatColor.GRAY + "Coming soon" + ChatColor.GREEN, new Location(Bukkit.getWorlds().get(0), 15, 61, 36));
        portals.put(ChatColor.GRAY + "Coming soon" + ChatColor.AQUA, new Location(Bukkit.getWorlds().get(0), 8, 61, 36));
        portals.put(ChatColor.AQUA + "Mines", new Location(Bukkit.getWorlds().get(0), 0, 61, 36));
        portals.put(ChatColor.GRAY + "Coming soon" + ChatColor.RED, new Location(Bukkit.getWorlds().get(0), -8, 61, 36));
        portals.put(ChatColor.GRAY + "Coming soon" + ChatColor.YELLOW, new Location(Bukkit.getWorlds().get(0), -15, 61, 36));
    }

    private final SpawnManager spawnManager;

    public SpawnListener(SpawnManager spawnManager) {
        this.spawnManager = spawnManager;

        for (Map.Entry<String, Location> entry : portals.entrySet()) {
            summonArmourStand(entry.getKey(), entry.getValue().clone().add(0.5, -1, 0.5));
        }
    }

    public static void summonArmourStand(String name, Location location) {
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
    public void onPortalEnter(PlayerMoveEvent event) {
        if (!event.getTo().getBlock().equals(event.getFrom().getBlock())
                && event.getTo().getBlock().getType() == Material.STATIONARY_WATER
                && event.getTo().getWorld() == spawnManager.getWorld()) {
            ArmorStand closest = null;
            double closestDistance = Double.MAX_VALUE;
            for (Entity entity : event.getPlayer().getNearbyEntities(5, 5, 5)) {
                if (entity instanceof ArmorStand && entity.getCustomName() != null) {
                    double d = entity.getLocation().distanceSquared(event.getTo());
                    if (d < closestDistance) {
                        closest = (ArmorStand) entity;
                        closestDistance = d;
                    }
                }
            }

            if (closest != null) {
                if (closest.getCustomName().contains("Mines")) {
                    teleportToMines(event.getPlayer());
                }
            }
        }
    }

    private void teleportToMines(Player player) {
        PlayerData data = PlayerDataManager.get(player);

        // Get rank from A-Z
        String rank = data.getRankText();
        if (rank.length() > 1) {
            rank = "Z";
        }

        player.teleport(PrisonsPlugin.getPlugin().getMineManager().getMine(rank).getRandomSpawnLocation());

        player.sendMessage(ChatColor.GREEN + "Teleported to mine " + rank + ".");
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
            Upgrade.setUpgrade(starterPickaxe, Upgrade.UNBREAKING, BigInteger.ONE);

            event.getPlayer().getInventory().addItem(starterPickaxe);

            Multi.giveMulti(5, event.getPlayer());
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
        if (event.getPlayer().getGameMode() != GameMode.CREATIVE
                && event.getClickedBlock() != null
                && event.getClickedBlock().getWorld() == spawnManager.getWorld()
                && (event.getItem() == null || !event.getItem().getType().isEdible() || event.getAction() != Action.RIGHT_CLICK_BLOCK)
                && event.getClickedBlock().getType() != Material.ENDER_CHEST) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onKick(PlayerKickEvent event) {
        if (event.getReason().equalsIgnoreCase("disconnect.spam")) {
            // Disable vanilla's disconnect for spamming tab completion (an issue for 1.13+ clients)
            event.setCancelled(true);
        }
    }

}
