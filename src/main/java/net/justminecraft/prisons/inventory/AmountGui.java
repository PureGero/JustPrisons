package net.justminecraft.prisons.inventory;

import net.justminecraft.prisons.playerdata.PlayerDataManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;

public class AmountGui implements InventoryHolder {

    private static final int[] AMOUNT_SLOTS = {12, 13, 14, 15, 16, 17};

    private final Player player;
    private final int slot;
    private int currentAmount;
    private final Inventory inventory;
    private final Inventory returnInventory;

    private HashMap<Integer, Runnable> callbacks = new HashMap<>();

    public AmountGui(Player player, int slot, Inventory returnInventory) {
        this.player = player;
        this.slot = slot;
        this.inventory = Bukkit.createInventory(this, 3 * 9, "Amount to Buy");
        this.returnInventory = returnInventory;
        this.currentAmount = PlayerDataManager.get(player).getUpgradeAmount();
        initialize();
    }

    public AmountGui(Player player, int slot, Inventory returnInventory, int currentAmount) {
        this.player = player;
        this.slot = slot;
        this.inventory = Bukkit.createInventory(this, 3 * 9, "Amount to Buy");
        this.returnInventory = returnInventory;
        this.currentAmount = currentAmount;
        initialize();
    }

    private void initialize() {
        for(int i = 0; i < AMOUNT_SLOTS.length && i < Amount.values().length; i++) {
            setAmount(i, Amount.values()[i]);
        }

        setAccept();
        setCurrentAmount();

        player.openInventory(inventory);
    }

    public void onClick(int slot) {
        if (callbacks.containsKey(slot)) {
            callbacks.get(slot).run();
        }
    }

    private void setAccept() {
        ItemStack item = new ItemStack(Material.STAINED_GLASS_PANE, 1, DyeColor.LIME.getWoolData());
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.GREEN + "Accept Changes");
        ArrayList<String> lore = new ArrayList<>();
        wordWrap(lore, "This will save your changes and close the menu.", 26);
        meta.setLore(lore);
        item.setItemMeta(meta);
        inventory.setItem(19, item);
        callbacks.put(19, () -> {PlayerDataManager.get(player).setUpgradeAmount(currentAmount);player.openInventory(returnInventory);});
    }

    private void setCurrentAmount() {
        ItemStack item = new ItemStack(Material.GHAST_TEAR);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.AQUA + "Amount: " + currentAmount);
        ArrayList<String> lore = new ArrayList<>();
        wordWrap(lore, "Amount that will be bought when an upgrade is purchased.", 26);
        meta.setLore(lore);
        item.setItemMeta(meta);
        inventory.setItem(10, item);
    }

    private void setAmount(int i, Amount amount) {
        ItemStack item = new ItemStack(amount.getIcon());
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(amount.getColor() + amount.getName());
        boolean available = amount.getPermission() != null && player.hasPermission(amount.getPermission());
        ArrayList<String> lore = new ArrayList<>();
        if(available) {
            lore.add(ChatColor.RED + "You do not have this unlocked yet!");
        } else {
            wordWrap(lore, amount.getDescription(), 26);
        }

        meta.setLore(lore);
        item.setItemMeta(meta);

        inventory.setItem(AMOUNT_SLOTS[i], item);
        if(!available)
            callbacks.put(i, () -> amount(amount));
    }

    private void amount(Amount amount) {
        int am = currentAmount;
        switch (amount) {
            case ONE:
                am = 1;
                break;
            case FIVE:
                am = 5;
                break;
            case TEN:
                am = 10;
                break;
            case ONE_HUNDRED:
                am = 100;
                break;
            case ONE_THOUSAND:
                am = 1000;
                break;
            case MAX:
                am = 2;
                break;
        }
        currentAmount = am;
        setCurrentAmount();
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
