package net.justminecraft.prisons.villager;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;

import java.util.Random;

public abstract class VillagerShop {
    private Villager entity;
    private Location location;

    public VillagerShop(VillagerShopManager manager, Location location) {
        this.location = location;
        entity = location.getWorld().spawn(location, Villager.class);
        entity.setCustomName(getClass().getSimpleName());
        entity.setCustomNameVisible(true);
        entity.setProfession(getProfession());

        manager.registerVillagerShop(this);
    }

    /**
     * Get a profession for this villager based on the class name
     */
    private Villager.Profession getProfession() {
        Villager.Profession[] professions = Villager.Profession.values();
        Random random = new Random(getClass().getName().hashCode());

        // Shuffle the random a bit
        random.nextBytes(new byte[32]);

        return professions[random.nextInt(professions.length)];
    }

    public Villager getEntity() {
        return entity;
    }

    public Location getLocation() {
        return location;
    }

    public abstract void openShop(Player player);
}
