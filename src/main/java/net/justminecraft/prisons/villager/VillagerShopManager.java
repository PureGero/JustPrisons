package net.justminecraft.prisons.villager;

import net.justminecraft.prisons.PrisonsPlugin;
import org.bukkit.entity.Entity;

import java.util.HashMap;

public class VillagerShopManager implements Runnable{
    private HashMap<Entity, VillagerShop> villagerShops = new HashMap<>();
    private PrisonsPlugin plugin;

    public VillagerShopManager(PrisonsPlugin plugin) {
        this.plugin = plugin;

        plugin.getServer().getPluginManager().registerEvents(new VillagerShopListener(this), plugin);

        plugin.getServer().getScheduler().runTaskTimer(plugin, this, 20L, 20L);

        new MultiCombineShop(this);
    }

    public VillagerShop getVillagerShop(Entity entity) {
        return villagerShops.get(entity);
    }

    public void registerVillagerShop(VillagerShop villagerShop) {
        villagerShops.put(villagerShop.getEntity(), villagerShop);

        // Remove any villagers with the same name
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            for (Entity entity : villagerShop.getEntity().getWorld().getEntities()) {
                if (entity != villagerShop.getEntity()
                        && villagerShop.getEntity().getCustomName() != null
                        && villagerShop.getEntity().getCustomName().equalsIgnoreCase(entity.getCustomName())) {
                    entity.remove();
                }
            }
        }, 1);
    }

    @Override
    public void run() {
        for (VillagerShop shop : villagerShops.values()) {
            if (shop.getEntity().getLocation().distanceSquared(shop.getLocation()) > 6) {
                new MoveToLocationRunnable(plugin, shop.getEntity(), shop.getLocation());
            }
        }
    }
}
