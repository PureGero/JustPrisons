package net.justminecraft.prisons.arenas.mobs;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import io.github.miraclefoxx.math.BigDecimalMath;
import net.justminecraft.prisons.Translate;
import net.justminecraft.prisons.arenas.ArenaManager;
import net.justminecraft.prisons.arenas.PopulateMainArena;
import net.justminecraft.prisons.customloot.LootManager;
import net.justminecraft.prisons.inventory.Key;
import net.justminecraft.prisons.inventory.sword.UpgradeSword;
import net.justminecraft.prisons.playerdata.PlayerData;
import net.justminecraft.prisons.playerdata.PlayerDataManager;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;

public class MobListener implements Listener {

    private final ArenaManager arenaManager;
    private final LootManager lootManager;
    //public static LootManager lootManager = new LootManager();
    public static MobValues mobvalues = new MobValues();
    public static final int TOKEN_MULTIPLIER = 2;
    
    public MobListener(ArenaManager arenaManager, LootManager lootManager) {
        this.lootManager = lootManager;
        this.arenaManager = arenaManager;
    }
    
    public HashMap<Entity, String> MobName = PopulateMainArena.MobName;
    public HashMap<Entity, Double> MobList = PopulateMainArena.MobList;
    public HashMap<Entity, Long> MobHealth = PopulateMainArena.MobHealth;
    
    
    @EventHandler // Player damages mob
    public void onMobDamage(EntityDamageByEntityEvent event) {
        if (MobList != null && MobList.containsKey(event.getEntity()) && event.getDamager() instanceof Player) {
            LivingEntity mob = (LivingEntity) event.getEntity();
            Player player = (Player) event.getDamager();
            
            Long damage = new Long(0);
            
            if (event.getCause() == DamageCause.THORNS) {
                Double thorns = 0.0;
                if (player.getInventory().getHelmet() != null) {
                    thorns += player.getInventory().getHelmet().getEnchantmentLevel(Enchantment.THORNS);
                }
                if (player.getInventory().getChestplate() != null) {
                    thorns += player.getInventory().getChestplate().getEnchantmentLevel(Enchantment.THORNS);
                }
                if (player.getInventory().getLeggings() != null) {
                    thorns += player.getInventory().getLeggings().getEnchantmentLevel(Enchantment.THORNS);
                }
                if (player.getInventory().getBoots() != null) {
                    thorns += player.getInventory().getBoots().getEnchantmentLevel(Enchantment.THORNS);
                }
                
                damage += 5*thorns.longValue();
            }
            else {
                damage += player.getInventory().getItemInHand().getEnchantmentLevel(Enchantment.DAMAGE_ALL);
            }

            Long newHealth = MobHealth.get(mob) - damage;
            
            if (newHealth > 0) {
                event.setCancelled(true);
                mob.damage(1);
                mob.setHealth(20);
                MobHealth.replace(mob, newHealth);
                
                mob.setCustomName(ChatColor.RED + MobName.get(mob) + " " + ChatColor.GREEN + MobHealth.get(mob).intValue() 
                        + ChatColor.WHITE + "/" + ChatColor.GREEN + Math.round(MobList.get(mob)*20) + ChatColor.RED + "HP");
            }
            else {                
                // Give player stuff
                ItemStack item = player.getItemInHand();
                
                if (item != null && item.getType() == Material.DIAMOND_SWORD && item.getDurability() > 1561) {
                    player.sendMessage(ChatColor.RED + "Your Sword is out of durability! Please repair it!!!");
                    event.setCancelled(true);
                    return;
                }
                
                PlayerData playerData = PlayerDataManager.get(player);
                
                BigInteger coinGain = (BigInteger.valueOf(mobvalues.getValue(MobName.get(mob))).multiply(BigInteger.valueOf(MobList.get(mob).longValue()))).multiply(
                        item == null ? BigInteger.ONE : UpgradeSword.getLevel(item, UpgradeSword.FORTUNE).divide(BigInteger.valueOf(3)).add(BigInteger.ONE));
                playerData.setCoins(playerData.getCoins().add(coinGain.multiply(BigInteger.valueOf(playerData.getMulti()))));
                
                // Remind new players to do /rankup
                if (playerData.getRank() == 0
                        && playerData.getCoins().compareTo(BigInteger.valueOf(150000)) > 0
                        && playerData.getLastRankupReminder() < System.currentTimeMillis() - 5 * 60 * 1000L
                        && Math.random() < 0.01) {
                    player.spigot().sendMessage(new ComponentBuilder(ChatColor.GREEN + "Rankup with: ").append("/rankup")
                            .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("/rankup").create()))
                            .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/rankup")).create());
                    playerData.setLastRankupReminder(System.currentTimeMillis());
                }

                if (item == null)
                    return;
                
                // Update the largestBlockCoinGain if they're using a diamond sword
                if (item.getType() == Material.DIAMOND_SWORD && coinGain.compareTo(playerData.getLargestBlockCoinGain()) > 0)
                    playerData.setLargestBlockCoinGain(coinGain);
                
                // Tokens
                BigDecimal chance = BigDecimalMath.pow(BigDecimal.valueOf(1.09), 
                        new BigDecimal(UpgradeSword.getLevel(item, UpgradeSword.LUCK).add(BigInteger.valueOf(5))).multiply(BigDecimal.valueOf(-1)));
                while (chance.compareTo(BigDecimal.valueOf(Math.random())) < 0) {
                    BigDecimal tokens = new BigDecimal(UpgradeSword.getLevel(item, UpgradeSword.LOOTING))
                            .multiply(BigDecimal.valueOf(Math.random() * 5))
                            .add(BigDecimal.valueOf(5))
                            .multiply(BigDecimal.valueOf(TOKEN_MULTIPLIER));
                    playerData.giveTokens(tokens.toBigInteger(), false);

                    chance = chance.multiply(BigDecimal.TEN);
                }

                // Charity
                //chance = (1-chance)*0.01;
                double charityChance = 3.0e-3;
                for (int i = 0; i < UpgradeSword.getLevel(item, UpgradeSword.CHARITY).intValue(); i++) {
                    if (Math.random() < charityChance) {
                        BigDecimal tokensSmall = new BigDecimal(UpgradeSword.getLevel(item, UpgradeSword.LOOTING))
                                .multiply(BigDecimal.valueOf(Math.random() * 5))
                                .add(BigDecimal.valueOf(5))
                                .multiply(BigDecimal.valueOf(TOKEN_MULTIPLIER));
                        BigDecimal tokensLarge = tokensSmall.multiply(BigDecimal.valueOf(Math.random() * 900 + 100));
                        tokensSmall = tokensSmall.multiply(new BigDecimal(UpgradeSword.getLevel(item, UpgradeSword.CHARITY))).multiply(BigDecimal.TEN);
                        tokensLarge = tokensLarge.multiply(new BigDecimal(UpgradeSword.getLevel(item, UpgradeSword.CHARITY)).multiply(BigDecimal.valueOf(0.2)).add(BigDecimal.ONE));
                        for (Player receiver : Bukkit.getOnlinePlayers()) {
                            if (receiver != player) {
                                Translate.sendMessage(receiver, "prisons.charity.broadcast", tokensSmall.toBigInteger(), player.getName(), tokensLarge.toBigInteger());
                                PlayerDataManager.get(receiver).giveTokens(tokensSmall.toBigInteger(), false);
                            }
                        }
                        Translate.sendMessage(player, "prisons.charity.broadcast", tokensSmall.toBigInteger(), player.getName(), tokensLarge.toBigInteger());
                        PlayerDataManager.get(player).giveTokens(tokensLarge.toBigInteger(), true);
                        player.playSound(player.getLocation(), Sound.LEVEL_UP, 1, 1);
                    }
                }

                // Keys
                chance = BigDecimalMath.pow(BigDecimal.valueOf(1.005), new BigDecimal(UpgradeSword.getLevel(item, UpgradeSword.LURE)).multiply(BigDecimal.valueOf(-1)));
                if (chance.compareTo(BigDecimal.valueOf(Math.random())) < 0) {
                    if (Math.random() < 0.8) {
                        Key.giveCommonKey(player);
                    } else if (Math.random() < 0.8) {
                        Key.giveRareKey(player);
                    } else {
                        Key.giveEpicKey(player);
                    }

                }
            
                // Kill Mob
                mob.damage(100);
                
                
                //System.out.println(player);
                lootManager.giveLoot(player);
            }
        } 
    }
    
    @EventHandler // Mob damages player
    public void onPlayerDamage(EntityDamageByEntityEvent event) {

        if (event.getDamager().getType() == EntityType.ARROW) {
            Arrow arrow = (Arrow) event.getDamager();
            if(MobList != null && MobList.containsKey(arrow.getShooter()) && event.getEntity() instanceof Player) {
                LivingEntity mob = (LivingEntity) arrow.getShooter();
                Player player = (Player) event.getEntity();
                
                Double projectile_prot = 0.0;
                if (player.getInventory().getHelmet() != null) {
                    projectile_prot += player.getInventory().getHelmet().getEnchantmentLevel(Enchantment.PROTECTION_PROJECTILE);
                }
                if (player.getInventory().getChestplate() != null) {
                    projectile_prot += player.getInventory().getChestplate().getEnchantmentLevel(Enchantment.PROTECTION_PROJECTILE);
                }
                if (player.getInventory().getLeggings() != null) {
                    projectile_prot += player.getInventory().getLeggings().getEnchantmentLevel(Enchantment.PROTECTION_PROJECTILE);
                }
                if (player.getInventory().getBoots() != null) {
                    projectile_prot += player.getInventory().getBoots().getEnchantmentLevel(Enchantment.PROTECTION_PROJECTILE);
                }
                
                Double damage = 38*MobList.get(mob) - projectile_prot;
                
                if (damage < 0) {
                return;
                }
                else if (player.getHealth() - damage < 20) {
                    player.damage(damage);
                }
                else {
                    player.damage(0.5);
                    player.setHealth(0);
                } 
            }
        }

        if (event.getDamager().getType() == EntityType.SMALL_FIREBALL) {
            Fireball fireball = (Fireball) event.getDamager();
            if(MobList != null && MobList.containsKey(fireball.getShooter()) && event.getEntity() instanceof Player) {
                LivingEntity mob = (LivingEntity) fireball.getShooter();
                Player player = (Player) event.getEntity();
                
                Double projectile_prot = 0.0;
                if (player.getInventory().getHelmet() != null) {
                    projectile_prot += player.getInventory().getHelmet().getEnchantmentLevel(Enchantment.PROTECTION_PROJECTILE);
                }
                if (player.getInventory().getChestplate() != null) {
                    projectile_prot += player.getInventory().getChestplate().getEnchantmentLevel(Enchantment.PROTECTION_PROJECTILE);
                }
                if (player.getInventory().getLeggings() != null) {
                    projectile_prot += player.getInventory().getLeggings().getEnchantmentLevel(Enchantment.PROTECTION_PROJECTILE);
                }
                if (player.getInventory().getBoots() != null) {
                    projectile_prot += player.getInventory().getBoots().getEnchantmentLevel(Enchantment.PROTECTION_PROJECTILE);
                }
                
                Double damage = 42*MobList.get(mob) - projectile_prot;
                
                if (damage < 0) {
                return;
                }
                else if (player.getHealth() - damage < 20) {
                    player.damage(damage);
                }
                else {
                    player.damage(0.5);
                    player.setHealth(0);
                } 
            }
        }
        
        if (event.getDamager().getType() == EntityType.CREEPER) {
            if (MobList != null && MobList.containsKey(event.getDamager()) && event.getEntity() instanceof Player) {
                LivingEntity mob = (LivingEntity) event.getDamager();
                Player player = (Player) event.getEntity();
                
                Double blast_protection = 0.0;
                if (player.getInventory().getHelmet() != null) {
                    blast_protection += player.getInventory().getHelmet().getEnchantmentLevel(Enchantment.PROTECTION_EXPLOSIONS);
                }
                if (player.getInventory().getChestplate() != null) {
                    blast_protection += player.getInventory().getChestplate().getEnchantmentLevel(Enchantment.PROTECTION_EXPLOSIONS);
                }
                if (player.getInventory().getLeggings() != null) {
                    blast_protection += player.getInventory().getLeggings().getEnchantmentLevel(Enchantment.PROTECTION_EXPLOSIONS);
                }
                if (player.getInventory().getBoots() != null) {
                    blast_protection += player.getInventory().getBoots().getEnchantmentLevel(Enchantment.PROTECTION_EXPLOSIONS);
                }
                
                Double damage = 38*MobList.get(mob) - blast_protection;
                if (damage < 0) {
                    
                }
                else if (player.getHealth() - damage < 20) {
                    player.damage(damage);
                }
                else {
                    player.damage(0.5);
                    player.setHealth(0);
                }       
            }
        }
        
        if (MobList != null && MobList.containsKey(event.getDamager()) && event.getEntity() instanceof Player) {
            LivingEntity mob = (LivingEntity) event.getDamager();
            Player player = (Player) event.getEntity();
            
            Double protection = 0.0;
            if (player.getInventory().getHelmet() != null) {
                protection += player.getInventory().getHelmet().getEnchantmentLevel(Enchantment.PROTECTION_ENVIRONMENTAL);
            }
            if (player.getInventory().getChestplate() != null) {
                protection += player.getInventory().getChestplate().getEnchantmentLevel(Enchantment.PROTECTION_ENVIRONMENTAL);
            }
            if (player.getInventory().getLeggings() != null) {
                protection += player.getInventory().getLeggings().getEnchantmentLevel(Enchantment.PROTECTION_ENVIRONMENTAL);
            }
            if (player.getInventory().getBoots() != null) {
                protection += player.getInventory().getBoots().getEnchantmentLevel(Enchantment.PROTECTION_ENVIRONMENTAL);
            }
            
            
            Double damage = 38*MobList.get(mob) - protection;
            //System.out.println(mob);
            //System.out.println(event.getCause());
            
            if (damage < 0) {
                
            }
            else if (player.getHealth() - damage < 20) {
                player.damage(damage);
                //player.setHealth(player.getHealth() - damage);
            }
            else {
                player.damage(0.5);
                player.setHealth(0);
            }       
        }
    }
    
    @EventHandler
    public void createKnockback(EntityDamageByEntityEvent event) {
        if (MobList != null && MobList.containsKey(event.getEntity()) && event.getDamager() instanceof Player) {
            LivingEntity mob = (LivingEntity) event.getEntity();
            Player player = (Player) event.getDamager();
            
            Double x_dir = (player.getLocation().getX() - mob.getLocation().getX())*0.1;
            Double z_dir = (player.getLocation().getZ() - mob.getLocation().getZ())*0.1;
            
            Vector attack = new Vector(-2*x_dir, 0.3, -2*z_dir);
            mob.setVelocity(attack);
        }
    }
    
    @EventHandler // Fall damage
    public void onFallDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player && event.getCause() == DamageCause.FALL) {
            event.setCancelled(true);
        }
    }
    
    @EventHandler // Creeper Explode
    public void onCreeperExplode(EntityExplodeEvent event) {
        if (event.getEntity().getType().equals(EntityType.CREEPER)) {
            event.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onItemDrop(EntityDeathEvent event) {
        for (int i = 0; i < event.getDrops().size(); i++) {
            event.getDrops().get(i).setAmount(0);
        }
        
        if (event.getDroppedExp() != 0) {
            event.setDroppedExp(0);
        }
    }
    
    @EventHandler
    public void onMobDeath(EntityDeathEvent event) {
        if (MobList != null && MobList.containsKey(event.getEntity())) {
            MobList.remove(event.getEntity());
            MobHealth.remove(event.getEntity());
        }
    }
    
    @EventHandler
    public void onChunkUnload(ChunkUnloadEvent event) {
        if (event.getWorld().getName().contains("arenas")) {
            for (int i = 0; i < event.getChunk().getEntities().length; i++) {
                if (MobList.containsKey(event.getChunk().getEntities()[i])) {
                    event.getChunk().getEntities()[i].remove();
                    
                    MobList.remove(event.getChunk().getEntities()[i]);
                    MobHealth.remove(event.getChunk().getEntities()[i]);
                }
            }
        }
    }
    
}















