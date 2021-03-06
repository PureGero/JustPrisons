package net.justminecraft.prisons.inventory.pickaxe;

import net.justminecraft.prisons.Translate;
import net.justminecraft.prisons.inventory.AmountGui;
import net.justminecraft.prisons.mines.MineListener;
import net.justminecraft.prisons.playerdata.PlayerData;
import net.justminecraft.prisons.playerdata.PlayerDataManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;

public class UpgradeGui2Pickaxe implements InventoryHolder {

    private static final int[] UPGRADE_SLOTS = {3, 5, 10, 16, 28, 34, 39, 41};

    private final Player player;
    private final int slot;
    private final Inventory inventory;
    private final ItemStack upgrading;
    private boolean canAffordUpgrade = true;
    private final HashMap<Integer, Runnable> callbacks = new HashMap<>();

    public UpgradeGui2Pickaxe(Player player, int slot) {
        this.player = player;
        this.slot = slot;
        this.inventory = Bukkit.createInventory(this, 6 * 9, "Upgrade your Pickaxe");
        this.upgrading = player.getInventory().getItem(slot);

        for (int i = 0; i < UPGRADE_SLOTS.length && i < Upgrade2Pickaxe.values().length; i++) {
            setUpgrade(i, Upgrade2Pickaxe.values()[i]);
        }

        setTokens();
        setItem();
        setRepair();
        setAmount();

        setPreviousPage();

        player.openInventory(inventory);
    }

    public void onClick(int slot) {
        if (callbacks.containsKey(slot)) {
            callbacks.get(slot).run();
        }
    }

    public boolean canAfford() {
        return canAffordUpgrade;
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
        meta.setDisplayName(Translate.formatMessage(player, "prisons.upgrade.repair.pickaxe"));

        ArrayList<String> lore = new ArrayList<>();
        wordWrap(lore, Translate.formatMessage(player, "prisons.upgrade.repair.description", cost), 26);
        meta.setLore(lore);

        item.setItemMeta(meta);
        inventory.setItem(23, item);

        callbacks.put(23, () -> {canAffordUpgrade=false;repair(cost);});
    }

    private void setPreviousPage() {
        ItemStack item = new ItemStack(Material.ARROW);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.GOLD + "Previous Page");
        ArrayList<String> lore = new ArrayList<>();
        wordWrap(lore, "Go to the previous page of upgrades!", 26);
        meta.setLore(lore);
        item.setItemMeta(meta);
        inventory.setItem(45, item);
        callbacks.put(45, () -> {canAffordUpgrade=false;new UpgradeGuiPickaxe(player, slot);});
    }

    private void setAmount() {
        ItemStack item = new ItemStack(Material.GOLD_BLOCK);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.GOLD + "Buy Amount");
        ArrayList<String> lore = new ArrayList<>();
        lore.add(ChatColor.GREEN + "Current: " + (PlayerDataManager.get(player).getUpgradeAmount() == 2 ? "Max" : PlayerDataManager.get(player).getUpgradeAmount()));
        wordWrap(lore, "Set the amount of upgrades to buy per click!", 26);
        meta.setLore(lore);
        item.setItemMeta(meta);
        inventory.setItem(49, item);
        callbacks.put(49, () -> {canAffordUpgrade=false;new AmountGui(player, slot, this.inventory);});
    }

    private void repair(int cost) {
        if (PlayerDataManager.get(player).takeTokens(BigInteger.valueOf(cost))) {
            upgrading.setDurability((short) 0);
            setItem();
        }
    }

    private void setUpgrade(int i, Upgrade2Pickaxe upgrade) {
        ItemStack item = new ItemStack(upgrade.getIcon());
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(String.format("%s%s+%,d %s",
                upgrade.getColor(),
                ChatColor.BOLD,
                Upgrade2Pickaxe.getLevel(upgrading, upgrade).add(BigInteger.ONE),
                upgrade.getName()));

        ArrayList<String> lore = new ArrayList<>();
        lore.add(Translate.formatMessage(player, "prisons.upgrade.cost", upgrade.getCost(Upgrade2Pickaxe.getLevel(upgrading, upgrade))));
        wordWrap(lore, upgrade.getDescription(), 26);
        meta.setLore(lore);

        item.setItemMeta(meta);
        inventory.setItem(UPGRADE_SLOTS[i], item);
        callbacks.put(UPGRADE_SLOTS[i], () -> upgrade(i, upgrade));
    }

    private void upgrade(int i, Upgrade2Pickaxe upgrade) {
        if (PlayerDataManager.get(player).takeTokens(upgrade.getCost(Upgrade2Pickaxe.getLevel(upgrading, upgrade)))) {
            Upgrade2Pickaxe.setUpgrade(upgrading, upgrade, Upgrade2Pickaxe.getLevel(upgrading, upgrade).add(BigInteger.ONE));
            setItem();
            setUpgrade(i, upgrade);
        } else
            canAffordUpgrade = false;
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
