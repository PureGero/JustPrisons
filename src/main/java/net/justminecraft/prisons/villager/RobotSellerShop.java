package net.justminecraft.prisons.villager;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;

public class RobotSellerShop extends VillagerShop {
    public RobotSellerShop(VillagerShopManager manager) {
        super(manager, new Location(Bukkit.getWorlds().get(0), 4.5, 57, -26.5), getConfig());
    }

    private static VillagerShopConfig getConfig() {
            VillagerShopConfig config = new VillagerShopConfig();
            config.setName("Garry The Engineer");
            config.setProfession(Villager.Profession.BLACKSMITH);
            return config;
    }

    @Override
    public void openShop(Player player) {
        new RobotSellerGui(player);
    }
}
