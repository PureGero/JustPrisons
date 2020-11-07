package net.justminecraft.prisons.villager;

import net.justminecraft.prisons.PrisonsPlugin;
import net.justminecraft.prisons.inventory.Multi;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public class MultiCombineGui implements ClickableInventoryHolder {
    private static final int COMBINE_NUMBER = 5;

    private final Player player;
    private final Inventory inventory;
    private BukkitTask combineTask = null;

    public MultiCombineGui(Player player) {
        this.player = player;
        this.inventory = Bukkit.createInventory(this, 3 * 9, "Combine 5 multis into a larger multi");

        player.openInventory(inventory);
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        combineLater();
    }

    private void combineLater() {
        if (combineTask != null && Bukkit.getScheduler().isQueued(combineTask.getTaskId())) {
            Bukkit.getScheduler().cancelTask(combineTask.getTaskId());
        }

        combineTask = Bukkit.getScheduler().runTaskLater(PrisonsPlugin.getPlugin(), this::combine, 20L);
    }

    private void combine() {
        HashMap<Integer, Integer> counts = new HashMap<>();

        for (ItemStack item : inventory.getContents()) {
            if (item != null && item.getType() == Material.GOLDEN_APPLE) {
                int value = item.getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS);
                counts.put(value, counts.getOrDefault(value, 0) + item.getAmount());
            }
        }

        for (Map.Entry<Integer, Integer> entry : counts.entrySet()) {
            if (entry.getValue() >= COMBINE_NUMBER) {
                int taken = 0;
                for (int i = 0; i < inventory.getSize(); i++) {
                    ItemStack item = inventory.getItem(i);
                    if (item != null && item.getType() == Material.GOLDEN_APPLE) {
                        int value = item.getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS);
                        if (value == entry.getKey()) {
                            item.setAmount(item.getAmount() - (COMBINE_NUMBER - taken));
                            taken += COMBINE_NUMBER - taken;

                            if (item.getAmount() < 0) {
                                taken += item.getAmount();
                                item.setAmount(0);
                            }

                            inventory.setItem(i, item);
                            
                            if (taken == COMBINE_NUMBER) {
                                // Add the new multi
                                for (ItemStack multi : inventory.addItem(Multi.createMulti(value + 5)).values()) {
                                    for (ItemStack multi2 : player.getInventory().addItem(multi).values()) {
                                        dropItem(multi2);
                                    }
                                }

                                // Do another combine later
                                combineLater();
                                return;
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onClose(InventoryCloseEvent event) {
        ArrayList<ItemStack> items = new ArrayList<>(Arrays.asList(inventory.getContents()));

        items.removeIf(Objects::isNull);

        for (ItemStack itemStack : player.getInventory().addItem(items.toArray(new ItemStack[0])).values()) {
            dropItem(itemStack);
        }
    }

    private void dropItem(ItemStack itemStack) {
        Item item = player.getWorld().dropItem(player.getEyeLocation(), itemStack);
        item.setVelocity(player.getEyeLocation().getDirection().multiply(0.3));
    }
}
