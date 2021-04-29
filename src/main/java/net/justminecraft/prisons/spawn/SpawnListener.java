package net.justminecraft.prisons.spawn;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

import net.justminecraft.prisons.PrisonsPlugin;
import net.justminecraft.prisons.inventory.Multi;
import net.justminecraft.prisons.inventory.boots.UpgradeBoots;
import net.justminecraft.prisons.inventory.chestplate.UpgradeChestplate;
import net.justminecraft.prisons.inventory.helmet.UpgradeHelmet;
import net.justminecraft.prisons.inventory.leggings.UpgradeLeggings;
import net.justminecraft.prisons.inventory.pickaxe.UpgradePickaxe;
import net.justminecraft.prisons.inventory.sword.UpgradeSword;
import net.justminecraft.prisons.playerdata.PlayerData;
import net.justminecraft.prisons.playerdata.PlayerDataManager;

public class SpawnListener implements Listener {

    private static Map<String, Location> portals = new HashMap<>();
    static {
        portals.put(ChatColor.GRAY + "Coming soon" + ChatColor.GREEN, new Location(Bukkit.getWorlds().get(0), 15, 62, 36));
        portals.put(ChatColor.AQUA + "Arenas", new Location(Bukkit.getWorlds().get(0), 8, 62, 36));
        portals.put(ChatColor.GREEN + "Mines", new Location(Bukkit.getWorlds().get(0), 0, 62, 36));
        portals.put(ChatColor.GRAY + "Coming soon" + ChatColor.RED, new Location(Bukkit.getWorlds().get(0), -8, 62, 36));
        portals.put(ChatColor.GRAY + "Coming soon" + ChatColor.YELLOW, new Location(Bukkit.getWorlds().get(0), -15, 62, 36));
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
            if (name.equals(entity.getCustomName()) || entity.getLocation().distanceSquared(location) < 1) {
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
                
                if (closest.getCustomName().contains("Arenas")) {
                    teleportToArenas(event.getPlayer());
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
    
    private void teleportToArenas(Player player) {

        player.teleport(PrisonsPlugin.getPlugin().getArenaManager().getArena("ArenaMain").getSpawnLocations().get(0));
        player.sendMessage(ChatColor.GREEN + "Teleported to The Pit");
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

            // Give Starter Sword
            ItemStack starterSword = new ItemStack(Material.DIAMOND_SWORD);

            ItemMeta sword_meta = starterSword.getItemMeta();
            sword_meta.setDisplayName(ChatColor.AQUA + "" + ChatColor.BOLD + "Starter Sword");
            sword_meta.setLore(new ArrayList<>(Arrays.asList("", ChatColor.WHITE + "Right-click this sword to upgrade it")));
            starterSword.setItemMeta(sword_meta);

            UpgradeSword.setUpgrade(starterSword, UpgradeSword.DAMAGE, BigInteger.TEN);
            UpgradeSword.setUpgrade(starterSword, UpgradeSword.UNBREAKING, BigInteger.ONE);

            event.getPlayer().getInventory().addItem(starterSword);
            
            // Give Starter Pickaxe
            ItemStack starterPickaxe = new ItemStack(Material.DIAMOND_PICKAXE);

            ItemMeta pickaxe_meta = starterPickaxe.getItemMeta();
            pickaxe_meta.setDisplayName(ChatColor.AQUA + "" + ChatColor.BOLD + "Starter Pickaxe");
            pickaxe_meta.setLore(new ArrayList<>(Arrays.asList("", ChatColor.WHITE + "Right-click this pickaxe to upgrade it")));
            starterPickaxe.setItemMeta(pickaxe_meta);

            UpgradePickaxe.setUpgrade(starterPickaxe, UpgradePickaxe.EFFICIENCY, BigInteger.TEN);
            UpgradePickaxe.setUpgrade(starterPickaxe, UpgradePickaxe.UNBREAKING, BigInteger.ONE);

            event.getPlayer().getInventory().addItem(starterPickaxe);
            
            // Give Starter Helmet
            ItemStack starterHelmet = new ItemStack(Material.DIAMOND_HELMET);

            ItemMeta helmet_meta = starterHelmet.getItemMeta();
            helmet_meta.setDisplayName(ChatColor.AQUA + "" + ChatColor.BOLD + "Starter Helmet");
            helmet_meta.setLore(new ArrayList<>(Arrays.asList("", ChatColor.WHITE + "Right-click this helmet to upgrade it")));
            starterHelmet.setItemMeta(helmet_meta);

            UpgradeHelmet.setUpgrade(starterHelmet, UpgradeHelmet.PROTECTION, BigInteger.TEN);
            UpgradeHelmet.setUpgrade(starterHelmet, UpgradeHelmet.UNBREAKING, BigInteger.ONE);

            event.getPlayer().getInventory().setHelmet(starterHelmet);
            
            // Give Starter Chestplate
            ItemStack starterChestplate = new ItemStack(Material.DIAMOND_CHESTPLATE);

            ItemMeta chestplate_meta = starterChestplate.getItemMeta();
            chestplate_meta.setDisplayName(ChatColor.AQUA + "" + ChatColor.BOLD + "Starter Chestplate");
            chestplate_meta.setLore(new ArrayList<>(Arrays.asList("", ChatColor.WHITE + "Right-click this chestplate to upgrade it")));
            starterChestplate.setItemMeta(chestplate_meta);

            UpgradeChestplate.setUpgrade(starterChestplate, UpgradeChestplate.PROTECTION, BigInteger.TEN);
            UpgradeChestplate.setUpgrade(starterChestplate, UpgradeChestplate.UNBREAKING, BigInteger.ONE);

            event.getPlayer().getInventory().setChestplate(starterChestplate);
            
            // Give Starter Leggings
            ItemStack starterLeggings = new ItemStack(Material.DIAMOND_LEGGINGS);

            ItemMeta leggings_meta = starterLeggings.getItemMeta();
            leggings_meta.setDisplayName(ChatColor.AQUA + "" + ChatColor.BOLD + "Starter Leggings");
            leggings_meta.setLore(new ArrayList<>(Arrays.asList("", ChatColor.WHITE + "Right-click this leggings to upgrade it")));
            starterLeggings.setItemMeta(leggings_meta);

            UpgradeLeggings.setUpgrade(starterLeggings, UpgradeLeggings.PROTECTION, BigInteger.TEN);
            UpgradeLeggings.setUpgrade(starterLeggings, UpgradeLeggings.UNBREAKING, BigInteger.ONE);

            event.getPlayer().getInventory().setLeggings(starterLeggings);
            
            // Give Starter Boots
            ItemStack starterBoots = new ItemStack(Material.DIAMOND_BOOTS);

            ItemMeta boots_meta = starterBoots.getItemMeta();
            boots_meta.setDisplayName(ChatColor.AQUA + "" + ChatColor.BOLD + "Starter Boots");
            boots_meta.setLore(new ArrayList<>(Arrays.asList("", ChatColor.WHITE + "Right-click this boots to upgrade it")));
            starterBoots.setItemMeta(boots_meta);

            UpgradeBoots.setUpgrade(starterBoots, UpgradeBoots.PROTECTION, BigInteger.TEN);
            UpgradeBoots.setUpgrade(starterBoots, UpgradeBoots.UNBREAKING, BigInteger.ONE);

            event.getPlayer().getInventory().setBoots(starterBoots);

            // Give Starter Apple
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
            Player player = (Player) event.getEntity();
            if (player.getWorld() != PrisonsPlugin.getPlugin().getArenaManager().getWorld()) {
                event.setCancelled(true);   
            }
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getPlayer().getGameMode() != GameMode.CREATIVE
                && event.getClickedBlock() != null
                && event.getClickedBlock().getWorld() == spawnManager.getWorld()
                && (event.getItem() == null || !event.getItem().getType().isEdible() || event.getAction() != Action.RIGHT_CLICK_BLOCK)
                && event.getClickedBlock().getType() != Material.ENDER_CHEST
                && event.getClickedBlock().getType() != Material.CHEST) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onThrowItem(PlayerDropItemEvent event) {
        if (event.getItemDrop().getItemStack() != null && event.getItemDrop().getItemStack().getType() == Material.DIAMOND_PICKAXE) {
            event.getPlayer().sendMessage(ChatColor.RED + "You cannot throw your Pickaxe!");
            event.setCancelled(true);
        }
        
        if (event.getItemDrop().getItemStack() != null && event.getItemDrop().getItemStack().getType() == Material.DIAMOND_SWORD) {
            event.getPlayer().sendMessage(ChatColor.RED + "You cannot throw your Sword!");
            event.setCancelled(true);
        }
        
        if (event.getItemDrop().getItemStack() != null && event.getItemDrop().getItemStack().getType() == Material.DIAMOND_HELMET) {
            event.getPlayer().sendMessage(ChatColor.RED + "You cannot throw your Helmet!");
            event.setCancelled(true);
        }
        
        if (event.getItemDrop().getItemStack() != null && event.getItemDrop().getItemStack().getType() == Material.DIAMOND_CHESTPLATE) {
            event.getPlayer().sendMessage(ChatColor.RED + "You cannot throw your Chestplate!");
            event.setCancelled(true);
        }
        
        if (event.getItemDrop().getItemStack() != null && event.getItemDrop().getItemStack().getType() == Material.DIAMOND_LEGGINGS) {
            event.getPlayer().sendMessage(ChatColor.RED + "You cannot throw your Leggings!");
            event.setCancelled(true);
        }
        
        if (event.getItemDrop().getItemStack() != null && event.getItemDrop().getItemStack().getType() == Material.DIAMOND_BOOTS) {
            event.getPlayer().sendMessage(ChatColor.RED + "You cannot throw your Bootss!");
            event.setCancelled(true);
        }
    }

}
