package net.justminecraft.prisons.villager;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class MultiCombineShop extends VillagerShop {
    public MultiCombineShop(VillagerShopManager manager) {
        super(manager, new Location(Bukkit.getWorlds().get(0), -16.5, 57, -24.5));
    }

    @Override
    public void openShop(Player player) {
        new MultiCombineGui(player);
    }
}
