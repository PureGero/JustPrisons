package net.justminecraft.prisons.inventory.leggings;

import net.justminecraft.prisons.playerdata.PlayerData;
import net.justminecraft.prisons.playerdata.PlayerDataManager;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Convert from the old prisons json inventory to the bukkit inventory
 */
public class LegacyInventoryConverter {

    private static HashMap<Integer, UpgradeLeggings> upgradeMap = new HashMap<>();

    static {
        upgradeMap.put(21, UpgradeLeggings.PROJECTILE_PROTECTION);
        upgradeMap.put(32, UpgradeLeggings.FIRE_PROTECTION);
        upgradeMap.put(34, UpgradeLeggings.UNBREAKING);
        upgradeMap.put(35, UpgradeLeggings.PROTECTION);
        upgradeMap.put(62, UpgradeLeggings.THORNS);
        upgradeMap.put(-3, UpgradeLeggings.BLAST_PROTECTION);
        upgradeMap.put(-4, UpgradeLeggings.RANKUP_TOKENS);
    }

    public static void doConversion(Player player) {
        PlayerData data = PlayerDataManager.get(player);

        JSONArray array = data.getItems();
        if (array != null) {
            copyInventory(array, player.getInventory());
            data.removeItems();
        }

        array = data.getEnderChest();
        if (array != null) {
            copyInventory(array, player.getEnderChest());
            data.removeEnderChest();
        }
    }

    private static void copyInventory(JSONArray array, Inventory inventory) {
        for (int i = 0; i < array.length() && i < inventory.getSize(); i++) {
            inventory.setItem(i, array.isNull(i) ? null : parseItem(array.getJSONObject(i)));
        }
    }

    private static ItemStack parseItem(JSONObject item) {
        if (item.getInt("id") == 222) item.put("id", 54);
        ItemStack itemStack = new ItemStack(item.getInt("id"));
        ItemMeta meta = itemStack.getItemMeta();

        if (item.has("count")) itemStack.setAmount(item.getInt("count"));
        if (item.has("damage")) itemStack.setDurability((short) item.getInt("damage"));
        if (item.has("name")) meta.setDisplayName(item.getString("name"));

        itemStack.setItemMeta(meta);

        if (item.has("ench")) {
            JSONArray enchanments = item.getJSONArray("ench");
            for (int j = 0; j < enchanments.length(); j++) {
                JSONObject enchanment = enchanments.getJSONObject(j);
                UpgradeLeggings upgrade = upgradeMap.get(enchanment.getInt("id"));
                if (upgrade != null) {
                    UpgradeLeggings.setUpgrade(itemStack, upgrade, enchanment.getBigInteger("lvl"));
                } else {
                    itemStack.addUnsafeEnchantment(Enchantment.getById(enchanment.getInt("id")), enchanment.getInt("lvl"));
                }
            }
        }

        return itemStack;
    }
}
