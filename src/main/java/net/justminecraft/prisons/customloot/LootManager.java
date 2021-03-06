package net.justminecraft.prisons.customloot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.NavigableMap;
import java.util.Random;
import java.util.TreeMap;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.justminecraft.prisons.GoogleSheet;
import net.justminecraft.prisons.PrisonsPlugin;
import net.justminecraft.prisons.inventory.boots.UpgradeBoots;
import net.justminecraft.prisons.inventory.chestplate.UpgradeChestplate;
import net.justminecraft.prisons.inventory.helmet.UpgradeHelmet;
import net.justminecraft.prisons.inventory.leggings.UpgradeLeggings;
import net.justminecraft.prisons.inventory.sword.UpgradeSword;
import net.justminecraft.prisons.playerdata.PlayerData;
import net.justminecraft.prisons.playerdata.PlayerDataManager;

public class LootManager extends GoogleSheet {
    
    public List<String> loot_name = new ArrayList<>();
    public HashMap<String, String> loot_item = new HashMap<>();
    public HashMap<String, String> loot_name_color = new HashMap<>();
    public RandomCollection<Object> loot_weight = new RandomCollection<>();

    public LootManager(PrisonsPlugin plugin) {
        super(plugin, "1k1PEdYPit6SlINWgYDYWUbcm6ibgANlDrWZ_B_WcNic");
        
        plugin.getServer().getPluginManager().registerEvents(new LootListener(this), plugin);
    }
    
    
    @Override
    public void onContentsChange() {

        loot_name.clear();
        loot_item.clear();
        loot_name_color.clear();
        
        for (String line : getLines()) {
            String[] args = line.split(",");
            
            if (args[0].equalsIgnoreCase("name")) {
                continue;
            }
    
            //loot_name.add(args[0]);
            loot_item.put(args[0], args[3]);
            loot_name_color.put(args[0], args[1]); 
            loot_weight.add(Double.valueOf(args[5]), args[0]);
   
        }
    }
    
    public class RandomCollection<E> { // Random Weighted Selector
        private final NavigableMap<Double, E> map = new TreeMap<Double, E>();
        private final Random random;
        private double total = 0;

        public RandomCollection() {
            this(new Random());
        }

        public RandomCollection(Random random) {
            this.random = random;
        }

        public RandomCollection<E> add(double weight, E result) {
            if (weight <= 0) return this;
            total += weight;
            map.put(total, result);
            return this;
        }

        public E next() {
            double value = random.nextDouble() * total;
            return map.higherEntry(value).getValue();
        }
    }
    
    public String getLoot() {        
        String loot = null;
        loot = loot_weight.next().toString();
        return loot;
    }

    
    public void giveLoot(Player player) {

        PlayerData data = PlayerDataManager.get(player);
        String item_name = getLoot();
        
        
        ItemStack item = new ItemStack(Material.valueOf(loot_item.get(item_name)));
        ItemMeta itemmeta = item.getItemMeta();
        itemmeta.setDisplayName(ChatColor.valueOf(loot_name_color.get(item_name)) + item_name);
        
        item.setItemMeta(itemmeta);
        
        if (item.getType().name().contains("SWORD")) {
          UpgradeSword.setUpgrade(item, UpgradeSword.FORTUNE, data.getSwordFortune());
          UpgradeSword.setUpgrade(item, UpgradeSword.LUCK, data.getSwordLuck());
          UpgradeSword.setUpgrade(item, UpgradeSword.DAMAGE, data.getSwordDamage());
          UpgradeSword.setUpgrade(item, UpgradeSword.LOOTING, data.getSwordLooting());
          UpgradeSword.setUpgrade(item, UpgradeSword.SPEED_BOOST, data.getSwordSpeed());
          UpgradeSword.setUpgrade(item, UpgradeSword.CHARITY, data.getSwordCharity());
          UpgradeSword.setUpgrade(item, UpgradeSword.UNBREAKING, data.getSwordUnbreaking());
          UpgradeSword.setUpgrade(item, UpgradeSword.LURE, data.getSwordLure()); 
        }
        
        if (item.getType().name().contains("HELMET")) {
            UpgradeHelmet.setUpgrade(item, UpgradeHelmet.PROTECTION, data.getHelmetProtection());
            UpgradeHelmet.setUpgrade(item, UpgradeHelmet.BLAST_PROTECTION, data.getHelmetBlastProt());
            UpgradeHelmet.setUpgrade(item, UpgradeHelmet.FIRE_PROTECTION, data.getHelmetFireProt());
            UpgradeHelmet.setUpgrade(item, UpgradeHelmet.PROJECTILE_PROTECTION, data.getHelmetProjProt());
            UpgradeHelmet.setUpgrade(item, UpgradeHelmet.UNBREAKING, data.getHelmetUnbreaking());
            UpgradeHelmet.setUpgrade(item, UpgradeHelmet.THORNS, data.getHelmetThorns());
        }
        
        if (item.getType().name().contains("CHESTPLATE")) {
            UpgradeChestplate.setUpgrade(item, UpgradeChestplate.PROTECTION, data.getChestplateProtection());
            UpgradeChestplate.setUpgrade(item, UpgradeChestplate.BLAST_PROTECTION, data.getChestplateBlastProt());
            UpgradeChestplate.setUpgrade(item, UpgradeChestplate.FIRE_PROTECTION, data.getChestplateFireProt());
            UpgradeChestplate.setUpgrade(item, UpgradeChestplate.PROJECTILE_PROTECTION, data.getChestplateProjProt());
            UpgradeChestplate.setUpgrade(item, UpgradeChestplate.UNBREAKING, data.getChestplateUnbreaking());
            UpgradeChestplate.setUpgrade(item, UpgradeChestplate.THORNS, data.getChestplateThorns());
        }
        
        if (item.getType().name().contains("LEGGINGS")) {
            UpgradeLeggings.setUpgrade(item, UpgradeLeggings.PROTECTION, data.getLeggingsProtection());
            UpgradeLeggings.setUpgrade(item, UpgradeLeggings.BLAST_PROTECTION, data.getLeggingsBlastProt());
            UpgradeLeggings.setUpgrade(item, UpgradeLeggings.FIRE_PROTECTION, data.getLeggingsFireProt());
            UpgradeLeggings.setUpgrade(item, UpgradeLeggings.PROJECTILE_PROTECTION, data.getLeggingsProjProt());
            UpgradeLeggings.setUpgrade(item, UpgradeLeggings.UNBREAKING, data.getLeggingsUnbreaking());
            UpgradeLeggings.setUpgrade(item, UpgradeLeggings.THORNS, data.getLeggingsThorns());
        }
        
        if (item.getType().name().contains("BOOTS")) {
            UpgradeBoots.setUpgrade(item, UpgradeBoots.PROTECTION, data.getBootsProtection());
            UpgradeBoots.setUpgrade(item, UpgradeBoots.BLAST_PROTECTION, data.getBootsBlastProt());
            UpgradeBoots.setUpgrade(item, UpgradeBoots.FIRE_PROTECTION, data.getBootsFireProt());
            UpgradeBoots.setUpgrade(item, UpgradeBoots.PROJECTILE_PROTECTION, data.getBootsProjProt());
            UpgradeBoots.setUpgrade(item, UpgradeBoots.UNBREAKING, data.getBootsUnbreaking());
            UpgradeBoots.setUpgrade(item, UpgradeBoots.THORNS, data.getBootsThorns());
        }
        
        

        
        player.getInventory().setItem(8, item);
    }
}


