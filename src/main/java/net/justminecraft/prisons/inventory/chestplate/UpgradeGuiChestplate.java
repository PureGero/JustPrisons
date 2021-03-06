package net.justminecraft.prisons.inventory.chestplate;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.justminecraft.prisons.Translate;
import net.justminecraft.prisons.mines.MineListener;
import net.justminecraft.prisons.playerdata.PlayerData;
import net.justminecraft.prisons.playerdata.PlayerDataManager;

public class UpgradeGuiChestplate implements InventoryHolder {

    private static final int[] UPGRADE_SLOTS = {10, 16, 28, 34, 39, 41};

    private final Player player;
    private final int slot;
    private final Inventory inventory;
    private final ItemStack upgrading;
    private final HashMap<Integer, Runnable> callbacks = new HashMap<>();

    public UpgradeGuiChestplate(Player player, int slot) {
        this.player = player;
        this.slot = slot;
        this.inventory = Bukkit.createInventory(this, 5 * 9, "Upgrade your Chestplate");
        this.upgrading = player.getInventory().getItem(slot);

        for (int i = 0; i < UPGRADE_SLOTS.length && i < UpgradeChestplate.values().length; i++) {
            setUpgrade(i, UpgradeChestplate.values()[i]);
        }

        setTokens();
        setItem();
        setRepair();

        player.openInventory(inventory);
    }

    public void onClick(int slot) {
        if (callbacks.containsKey(slot)) {
            callbacks.get(slot).run();
        }
    }

    private void setTokens() {
        PlayerData data = PlayerDataManager.get(player);
        ItemStack item = new ItemStack(Material.BOOK);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(Translate.formatMessage(player, "prisons.upgrade.tokens", data.getTokens()));

        ArrayList<String> lore = new ArrayList<>();
        lore.add(Translate.formatMessage(player, "prisons.upgrade.tokens.description", data.getTokens()));
        meta.setLore(lore);

        item.setItemMeta(meta);
        inventory.setItem(21, item);
    }

    private void setItem() {
        inventory.setItem(22, upgrading);
        player.getInventory().setItem(slot, upgrading);
    }

    private void setRepair() {
        int cost = 10 * MineListener.TOKEN_MULTIPLIER;

        ItemStack item = new ItemStack(Material.ANVIL);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(Translate.formatMessage(player, "prisons.upgrade.repair.chestplate"));

        ArrayList<String> lore = new ArrayList<>();
        wordWrap(lore, Translate.formatMessage(player, "prisons.upgrade.repair.description", cost), 26);
        meta.setLore(lore);

        item.setItemMeta(meta);
        inventory.setItem(23, item);

        callbacks.put(23, () -> repair(cost));
    }

    private void repair(int cost) {
        if (PlayerDataManager.get(player).takeTokens(BigInteger.valueOf(cost))) {
            upgrading.setDurability((short) 0);
            setItem();
        }
    }

    private void setUpgrade(int i, UpgradeChestplate upgrade) {
        PlayerData data = PlayerDataManager.get(player); ////
        
        ItemStack item = new ItemStack(upgrade.getIcon());
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(String.format("%s%s+%,d %s",
                upgrade.getColor(),
                ChatColor.BOLD,
                UpgradeChestplate.getLevel(upgrading, upgrade).add(BigInteger.ONE),
                upgrade.getName()));

        ArrayList<String> lore = new ArrayList<>();
        lore.add(Translate.formatMessage(player, "prisons.upgrade.cost", upgrade.getCost(UpgradeChestplate.getLevel(upgrading, upgrade))));
        wordWrap(lore, upgrade.getDescription(), 26);
        meta.setLore(lore);

        item.setItemMeta(meta);
        inventory.setItem(UPGRADE_SLOTS[i], item);

        callbacks.put(UPGRADE_SLOTS[i], () -> upgrade(i, upgrade));
        
        //////////////////////////////
        if(upgrade.equals(UpgradeChestplate.PROTECTION)) {
            data.setChestplateProtection(UpgradeChestplate.getLevel(upgrading, upgrade));      }
        
        if(upgrade.equals(UpgradeChestplate.FIRE_PROTECTION)) {
            data.setChestplateFireProt(UpgradeChestplate.getLevel(upgrading, upgrade));
        }
        
        if(upgrade.equals(UpgradeChestplate.BLAST_PROTECTION)) {
            data.setChestplateBlastProt(UpgradeChestplate.getLevel(upgrading, upgrade));
        }
        
        if(upgrade.equals(UpgradeChestplate.PROJECTILE_PROTECTION)) {
            data.setChestplateProjProt(UpgradeChestplate.getLevel(upgrading, upgrade));
        }
        
        if(upgrade.equals(UpgradeChestplate.UNBREAKING)) {
            data.setChestplateUnbreaking(UpgradeChestplate.getLevel(upgrading, upgrade));
        }
        
        if(upgrade.equals(UpgradeChestplate.THORNS)) {
            data.setChestplateThorns(UpgradeChestplate.getLevel(upgrading, upgrade));
        }
        /////////////////////////////
    }

    private void upgrade(int i, UpgradeChestplate upgrade) {
//        if (upgrade == UpgradeHelmet.SPEED_BOOST && UpgradeHelmet.getLevel(upgrading, upgrade).compareTo(BigInteger.valueOf(3)) >= 0) {
//            player.sendMessage(ChatColor.RED + "You have reached the maximum level for speed!");
//            return;
//        }

        if (PlayerDataManager.get(player).takeTokens(upgrade.getCost(UpgradeChestplate.getLevel(upgrading, upgrade)))) {
            UpgradeChestplate.setUpgrade(upgrading, upgrade, UpgradeChestplate.getLevel(upgrading, upgrade).add(BigInteger.ONE));
            setItem();
            setUpgrade(i, upgrade);
        }
    }

    private void wordWrap(ArrayList<String> lore, String description, int length) {
        String[] words = description.split(" ");
        StringBuilder builder = new StringBuilder();

        for (String word : words) {
            if (builder.length() + 1 + word.length() > length) {
                lore.add(ChatColor.WHITE + builder.toString());
                builder = new StringBuilder();
            }
            if (builder.length() > 0) {
                builder.append(" ");
            }
            builder.append(word);
        }

        lore.add(ChatColor.WHITE + builder.toString());
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }
}
