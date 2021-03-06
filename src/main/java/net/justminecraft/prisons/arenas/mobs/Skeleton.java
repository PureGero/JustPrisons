package net.justminecraft.prisons.arenas.mobs;

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Skeleton {
    
    public void spawnSkeleton(Player player, Location MobSpawn, HashMap<Entity, String> MobName, HashMap<Entity, Double> MobList, HashMap<Entity, Long> MobHealth) {

        LivingEntity skeleton = (LivingEntity) player.getWorld().spawnEntity(MobSpawn, EntityType.SKELETON);
        
        ItemStack skeletonHelmet = new ItemStack(Material.LEAVES);
        skeleton.getEquipment().setHelmet(skeletonHelmet);
        
        for (int i = 0; i < player.getInventory().getSize(); i++) {
            if ( player.getInventory().getItem(i) != null) {
                if (player.getInventory().getItem(i).getType() == Material.DIAMOND_SWORD ||
                        player.getInventory().getItem(i).getType() == Material.GOLD_SWORD ||
                        player.getInventory().getItem(i).getType() == Material.IRON_SWORD ||
                        player.getInventory().getItem(i).getType() == Material.STONE_SWORD ||
                        player.getInventory().getItem(i).getType() == Material.WOOD_SWORD) {
                    
                    Double weight = new Double(player.getInventory().getItem(i).getEnchantmentLevel(Enchantment.DAMAGE_ALL));
                    if (player.getInventory().getHelmet() != null) {
                        weight += 0.8*player.getInventory().getHelmet().getEnchantmentLevel(Enchantment.PROTECTION_ENVIRONMENTAL);
                    }
                    if (player.getInventory().getChestplate() != null) {
                        weight += 0.8*player.getInventory().getChestplate().getEnchantmentLevel(Enchantment.PROTECTION_ENVIRONMENTAL);
                    }
                    if (player.getInventory().getLeggings() != null) {
                        weight += 0.8*player.getInventory().getLeggings().getEnchantmentLevel(Enchantment.PROTECTION_ENVIRONMENTAL);
                    }
                    if (player.getInventory().getBoots() != null) {
                        weight += 0.8*player.getInventory().getBoots().getEnchantmentLevel(Enchantment.PROTECTION_ENVIRONMENTAL);
                    }
                    
                    Double level = weight/50;
                    Double MaxHealth = level*20;
                    
                    skeleton.setCustomName(ChatColor.RED + "Skeleton " + ChatColor.GREEN + Math.round(MaxHealth) 
                    + ChatColor.WHITE + "/" + ChatColor.GREEN + Math.round(level*20) + ChatColor.RED + "HP");
                    
                    MobName.put(skeleton, "Skeleton");
                    MobHealth.put(skeleton, Math.round(MaxHealth));
                    MobList.put(skeleton, level);
                }
            }
        }
    }
    
}
