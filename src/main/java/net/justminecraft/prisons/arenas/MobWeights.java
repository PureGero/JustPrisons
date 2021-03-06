package net.justminecraft.prisons.arenas;

import java.util.NavigableMap;
import java.util.Random;
import java.util.TreeMap;

// Spawn waiting in main arena

public class MobWeights {
    public class RandomCollection<E> {
        private final NavigableMap<Double, E> map = new TreeMap<Double, E>();
        private final Random random;
        private double total = 0;

        public RandomCollection() {
            this(new Random());
        }

        public RandomCollection(Random random) {
            this.random = random;
        }

        public RandomCollection<E> add(double weight, E result) {
            if (weight <= 0) return this;
            total += weight;
            map.put(total, result);
            return this;
        }

        public E next() {
            double value = random.nextDouble() * total;
            return map.higherEntry(value).getValue();
        }
    }

    public String getMob() {
        RandomCollection<Object> rc = new RandomCollection<>()
                .add(3, "Zombie")
                .add(1, "Skeleton")
                .add(1, "Creeper");
                //.add(0.5, "Blaze");
        
        String mob = null;
        
        for (int i = 0; i < 10; i++) {
            mob = (String) rc.next();
        }

        return mob;
    }
}
