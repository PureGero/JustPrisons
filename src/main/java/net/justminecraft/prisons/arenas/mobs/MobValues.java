package net.justminecraft.prisons.arenas.mobs;

import java.util.HashMap;

// Value of reward for killing each mob
public class MobValues {

    public static HashMap<String, Long> MobValue = new HashMap<String, Long>();
    
    public void createList() {
        MobValue.put("Zombie", (long) 100.0);
        MobValue.put("Skeleton", (long) 100.0);
        MobValue.put("Blaze", (long) 100.0);
        
        //System.out.println("Boop List");
    }
    
    public Long getValue(String name) {
        if (MobValue.isEmpty()) {
            createList();
        }
        //System.out.println("Boop function");
        Long value = MobValue.get(name);
        
        return value;
    }
    
}
