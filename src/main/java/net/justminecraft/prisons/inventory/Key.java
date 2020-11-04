package net.justminecraft.prisons.inventory;

import net.justminecraft.prisons.Translate;
import net.justminecraft.prisons.mines.MineListener;
import net.justminecraft.prisons.playerdata.PlayerData;
import net.justminecraft.prisons.playerdata.PlayerDataManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.math.BigInteger;

public class Key {
    public static final String VOTE_KEY = ChatColor.GREEN + "Vote Key";
    public static final String COMMON_KEY = ChatColor.GRAY + "Common Key";
    public static final String RARE_KEY = ChatColor.AQUA + "Rare Key";
    public static final String EPIC_KEY = ChatColor.LIGHT_PURPLE + "Epic Key";
    public static final String LEGENDARY_KEY = ChatColor.GOLD + "" + ChatColor.BOLD + "Legendary Key";

    public static void giveVoteKey(Player player) {
        giveKey(player, VOTE_KEY);
    }

    public static void giveCommonKey(Player player) {
        giveKey(player, COMMON_KEY);
    }

    public static void giveRareKey(Player player) {
        giveKey(player, RARE_KEY);
    }

    public static void giveEpicKey(Player player) {
        giveKey(player, EPIC_KEY);
    }

    public static void giveLegendaryKey(Player player) {
        giveKey(player, LEGENDARY_KEY);
    }

    private static void giveKey(Player player, String key) {
        ItemStack item = new ItemStack(Material.TRIPWIRE_HOOK);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(key);
        item.setItemMeta(meta);

        if (key.equalsIgnoreCase(LEGENDARY_KEY)) {
            Upgrade.setUpgrade(item, Upgrade.RANKUP_TOKENS, BigInteger.valueOf(PlayerDataManager.get(player).getRank()));
        }

        if (player.getInventory().addItem(item).size() == 0) {
            Translate.sendMessage(player, "prisons.keys.receive", key.substring(0, 2), key);
        } else {
            Translate.sendMessage(player, "prisons.keys.use", key.substring(0, 2), key);
            useKey(player, item);
        }
    }

    public static void useKey(Player player, ItemStack item) {
        PlayerData data = PlayerDataManager.get(player);
        BigInteger tokens = null;
        ItemMeta meta = item.getItemMeta();
        if (meta == null || meta.getDisplayName() == null) return;

        if (meta.getDisplayName().equalsIgnoreCase(VOTE_KEY)) {
            giveCommonKey(player);
            while (Math.random() < 0.85)
                giveCommonKey(player);
            while (Math.random() < 0.75)
                giveRareKey(player);
            while (Math.random() < 0.5)
                giveEpicKey(player);
        }
        if (meta.getDisplayName().equalsIgnoreCase(COMMON_KEY)) {
            if (Math.random() < 0.05)
                Multi.giveMulti(5, player);
            else if (Math.random() < 0.02)
                Multi.giveMulti(10, player);
            tokens = BigInteger.valueOf((long) ((Math.random() * 150 + 50) * MineListener.TOKEN_MULTIPLIER));
        }
        if (meta.getDisplayName().equalsIgnoreCase(RARE_KEY)) {
            while (Math.random() < 0.05)
                giveCommonKey(player);
            if (Math.random() < 0.02)
                Multi.giveMulti(15, player);
            else if (Math.random() < 0.01)
                Multi.giveMulti(20, player);
            tokens = BigInteger.valueOf((long) ((Math.random() * 1500 + 500) * MineListener.TOKEN_MULTIPLIER));
        }
        if (meta.getDisplayName().equalsIgnoreCase(EPIC_KEY)) {
            while (Math.random() < 0.05)
                giveRareKey(player);
            tokens = BigInteger.valueOf((long) ((Math.random() * 15000 + 5000) * MineListener.TOKEN_MULTIPLIER));
            if (Math.random() < 0.01)
                Multi.giveMulti(25, player);
            else if (Math.random() < 0.01)
                Multi.giveMulti(30, player);
            if (Math.random() < 0.05)
                Pickaxe.giveEpicPickaxe(player);
        }
        if (meta.getDisplayName().equalsIgnoreCase(LEGENDARY_KEY)) {
            while (Math.random() < 0.05)
                giveEpicKey(player);
            if (Math.random() < 0.01 || Upgrade.getLevel(item, Upgrade.RANKUP_TOKENS).compareTo(BigInteger.valueOf(5)) == 0)
                Multi.giveMulti(40, player);
            else if (Math.random() < 0.01 || Upgrade.getLevel(item, Upgrade.RANKUP_TOKENS).compareTo(BigInteger.valueOf(20)) == 0)
                Multi.giveMulti(50, player);
            if (Math.random() < 0.1)
                Pickaxe.giveLegendPickAxe(player);
            tokens = Upgrade.getLevel(item, Upgrade.RANKUP_TOKENS).multiply(BigInteger.valueOf(1000 * MineListener.TOKEN_MULTIPLIER));
        }

        if (tokens != null) {
            if (data.getStopKeyUseMessages()) {
                data.setTokens(data.getTokens().add(tokens));
            } else {
                data.giveTokens(tokens, true);
            }
        }
    }


}
