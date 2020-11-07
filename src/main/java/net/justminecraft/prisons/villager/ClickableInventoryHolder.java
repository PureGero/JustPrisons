package net.justminecraft.prisons.villager;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.InventoryHolder;

public interface ClickableInventoryHolder extends InventoryHolder {

    void onClick(InventoryClickEvent event);

    void onClose(InventoryCloseEvent event);

}
