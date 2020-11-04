package net.justminecraft.prisons.inventory;

import org.bukkit.entity.Player;

public class Multi {
    public static void giveMulti(int multi, Player player) {
        player.sendMessage("You got a " + multi + "x multi!");
    }
}
