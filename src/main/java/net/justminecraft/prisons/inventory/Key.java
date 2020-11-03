package net.justminecraft.prisons.inventory;

import org.bukkit.entity.Player;

public class Key {
    public static void giveCommonKey(Player player) {
        player.sendMessage("You got a common key!");
    }

    public static void giveRareKey(Player player) {
        player.sendMessage("You got a rare key!");
    }

    public static void giveEpicKey(Player player) {
        player.sendMessage("You got an epic key!");
    }
}
