package net.justminecraft.prisons.mines;

import org.bukkit.Material;

public class MineOres {
    private static final Material[] ores = new Material[]{
            Material.STONE,
            Material.COBBLESTONE,
            Material.COAL_ORE,
            Material.IRON_ORE,
            Material.GOLD_ORE,
            Material.REDSTONE_ORE,
            Material.LAPIS_ORE,
            Material.DIAMOND_ORE,
            Material.EMERALD_ORE,
            Material.COAL_BLOCK,
            Material.IRON_BLOCK,
            Material.GOLD_BLOCK,
            Material.REDSTONE_BLOCK,
            Material.DIAMOND_BLOCK,
            Material.EMERALD_BLOCK
    };

    private static final double BLOCK_VALUE_SCALAR_CONSTANT = 1.3;

    public static Material getRandomMaterial(Mine mine) {
        if (mine.getName().length() == 1) {
            int lvl = mine.getName().charAt(0) - 'A';
            Material[] blocks = getBlockRandoms(lvl);
            return blocks[(int) (Math.random() * blocks.length)];
        }

        return Material.GLASS;
    }

    public static int getBlockValue(Material material) {
        int i;
        for (i = 0; i < ores.length; i++) {
            if (ores[i] == material) {
                break;
            }
        }

        if (i == ores.length) {
            i = 0;
        }

        return (int) ((Math.random() * 50 + 50) * Math.pow(BLOCK_VALUE_SCALAR_CONSTANT, i));
    }

    /**
     * @param level 0-25
     */
    private static Material[] getBlockRandoms(int level) {
        double lvl = level / 1.7;
        Material[] blocks = new Material[10];
        int j = 0;

        for (double i = -2; i < 1; i += 0.3) {
            int index = (int) (lvl + i);
            if (index < 0) index = 0;
            if (index >= ores.length) index = ores.length - 1;
            blocks[j++] = ores[index];
        }

        return blocks;
    }
}
