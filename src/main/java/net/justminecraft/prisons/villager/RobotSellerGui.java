package net.justminecraft.prisons.villager;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class RobotSellerGui implements ClickableInventoryHolder {

    private final ArrayList<Integer> BUY_SLOTS = new ArrayList<>(Arrays.asList(10, 13, 16));

    private final Player player;
    private final Inventory inventory;

    public RobotSellerGui(Player player) {
        this.player = player;
        this.inventory = Bukkit.createInventory(this, 3 * 9, "Buy some fancy robots!");

        player.openInventory(inventory);


    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        if (BUY_SLOTS.contains(event.getSlot())) buyRobot(event.getSlot());
    }

    private void buyRobot(Integer slot) {

    }

    @Override
    public void onClose(InventoryCloseEvent event) {}
}
