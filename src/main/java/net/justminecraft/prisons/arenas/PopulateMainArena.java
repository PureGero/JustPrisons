package net.justminecraft.prisons.arenas;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import net.justminecraft.prisons.PrisonsPlugin;
import net.justminecraft.prisons.arenas.mobs.Skeleton;
import net.justminecraft.prisons.arenas.mobs.Zombie;
import net.justminecraft.prisons.arenas.mobs.Blaze;
import net.justminecraft.prisons.arenas.mobs.Creeper;;

public class PopulateMainArena {
    
    private final ArenaManager arenaManager;
    
    public PopulateMainArena(ArenaManager arenaManager) {
        this.arenaManager = arenaManager;
    }
    
    public static MobWeights mobweights = new MobWeights();
    
    public static Zombie zombie = new Zombie();
    public static Skeleton skeleton = new Skeleton();
    public static Blaze blaze = new Blaze();
    public static Creeper creeper = new Creeper();
    
    public static HashMap<Entity, String> MobName= new HashMap<Entity, String>();
    public static HashMap<Entity, Double> MobList = new HashMap<Entity, Double>();
    public static HashMap<Entity, Long> MobHealth = new HashMap<Entity, Long>();
    
    public void Populate(Player playerOnline) {
    
        Location MainArena = (Location) PrisonsPlugin.getPlugin().getArenaManager().getArena("ArenaMain").getSpawnLocations().get(0).clone();
        MainArena.setX(1000.5);
        MainArena.setY(5);
        MainArena.setZ(0.5);
        
        Location MainCorner = (Location) PrisonsPlugin.getPlugin().getArenaManager().getArena("ArenaMain").getSpawnLocations().get(0).clone();
        MainCorner.setX(969);
        MainCorner.setY(5);
        MainCorner.setZ(-30);
        
        int numEnt = 20; // Number of entities
        
        if (MainArena.getWorld().getNearbyEntities(MainArena, 100, 20, 100).size() < numEnt) {
            Location MobSpawn = MainCorner.clone().add(Math.random()*60, 0, Math.random()*60);
            
            List<?> NearMob = new ArrayList<Object>(MainArena.getWorld().getNearbyEntities(MobSpawn, 100, 20, 100));
            
            for (int j = 0; j < NearMob.size(); j++) {

                if (NearMob.get(j) instanceof Player && MobSpawn.add(0, 1, 0).getBlock().getType() == Material.AIR) {
                    
                    Player player = (Player) NearMob.get(j);
                    
                    if (mobweights.getMob().contains("Zombie")) {
                        zombie.spawnZombie(player, MobSpawn, MobName, MobList, MobHealth);
                    }
                    if (mobweights.getMob().contains("Skeleton")) {
                        skeleton.spawnSkeleton(player, MobSpawn, MobName, MobList, MobHealth);
                    }
                    if (mobweights.getMob().contains("Blaze")) {
                        blaze.spawnBlaze(player, MobSpawn, MobName, MobList, MobHealth);
                    }
                    if (mobweights.getMob().contains("Creeper")) {
                        creeper.spawnCreeper(player, MobSpawn, MobName, MobList, MobHealth);
                    }
                    
                    //zombie.spawnZombie(player, MobSpawn, MobName, MobList, MobHealth);
                    //skeleton.spawnSkeleton(player, MobSpawn, MobName, MobList, MobHealth);

                }
            }
        }     
    }
}



















