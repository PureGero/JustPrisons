package net.justminecraft.prisons.mines;

import io.github.miraclefoxx.math.BigDecimalMath;
import net.justminecraft.prisons.Translate;
import net.justminecraft.prisons.inventory.Key;
import net.justminecraft.prisons.inventory.Upgrade;
import net.justminecraft.prisons.playerdata.PlayerData;
import net.justminecraft.prisons.playerdata.PlayerDataManager;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import java.math.BigDecimal;
import java.math.BigInteger;

public class MineListener implements Listener {
    public static final int TOKEN_MULTIPLIER = 2;
    private final MineManager mineManager;

    public MineListener(MineManager mineManager) {
        this.mineManager = mineManager;
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (mineManager.getMine(event.getPlayer()) != null) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Mine mine = mineManager.getMine(player);

        if (mine == null) {
            return;
        }

        if (!mine.inMine(event.getBlock())) {
            event.setCancelled(true);
            return;
        }

        PlayerData playerData = PlayerDataManager.get(player);
        playerData.setBlocksBroken(playerData.getBlocksBroken().add(BigInteger.ONE));
        ItemStack item = player.getItemInHand();

        // Coins
        BigInteger coinGain = BigInteger.valueOf(MineOres.getBlockValue(event.getBlock().getType())).multiply(
                item == null ? BigInteger.ONE : Upgrade.getLevel(item, Upgrade.FORTUNE).divide(BigInteger.valueOf(3)).add(BigInteger.ONE));
        playerData.setCoins(playerData.getCoins().add(coinGain));

        // Remind new players to do /rankup
        if (playerData.getRank() == 0
                && playerData.getCoins().compareTo(BigInteger.valueOf(150000)) > 0
                && playerData.getLastRankupReminder() < System.currentTimeMillis() - 5 * 60 * 1000L
                && Math.random() < 0.01) {
            player.spigot().sendMessage(new ComponentBuilder("Rankup with: ").color(ChatColor.GREEN).append("/rankup")
                    .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("/rankup").create()))
                    .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/rankup")).create());
            playerData.setLastRankupReminder(System.currentTimeMillis());
        }

        if (item == null)
            return;

        // Update the largestBlockCoinGain if they're using a diamond pick
        if (item.getType() == Material.DIAMOND_PICKAXE && coinGain.compareTo(playerData.getLargestBlockCoinGain()) > 0)
            playerData.setLargestBlockCoinGain(coinGain);

        // Tokens
        BigDecimal chance = BigDecimalMath.pow(BigDecimal.valueOf(1.001), new BigDecimal(Upgrade.getLevel(item, Upgrade.LUCK).add(BigInteger.valueOf(5))).multiply(BigDecimal.valueOf(-1)));
        if (chance.compareTo(BigDecimal.valueOf(Math.random())) < 0) {
            BigDecimal tokens = new BigDecimal(Upgrade.getLevel(item, Upgrade.LOOTING))
                    .multiply(BigDecimal.valueOf(Math.random() * 5))
                    .add(BigDecimal.valueOf(5))
                    .multiply(BigDecimal.valueOf(TOKEN_MULTIPLIER));
            playerData.giveTokens(tokens.toBigInteger(), false);
        }

        // Charity
        //chance = (1-chance)*0.01;
        double charityChance = 3.0e-5;
        for (int i = 0; i < Upgrade.getLevel(item, Upgrade.CHARITY).intValue(); i++) {
            if (Math.random() < charityChance) {
                BigDecimal tokensSmall = new BigDecimal(Upgrade.getLevel(item, Upgrade.LOOTING))
                        .multiply(BigDecimal.valueOf(Math.random() * 5))
                        .add(BigDecimal.valueOf(5))
                        .multiply(BigDecimal.valueOf(TOKEN_MULTIPLIER));
                BigDecimal tokensLarge = tokensSmall.multiply(BigDecimal.valueOf(Math.random() * 900 + 100));
                tokensSmall = tokensSmall.multiply(new BigDecimal(Upgrade.getLevel(item, Upgrade.CHARITY))).multiply(BigDecimal.TEN);
                tokensLarge = tokensLarge.multiply(new BigDecimal(Upgrade.getLevel(item, Upgrade.CHARITY)).multiply(BigDecimal.valueOf(0.2)).add(BigDecimal.ONE));
                for (Player receiver : Bukkit.getOnlinePlayers()) {
                    if (receiver != player) {
                        Translate.sendMessage(receiver, "prisons.charity.broadcast", tokensSmall, player.getName(), tokensLarge);
                        PlayerDataManager.get(receiver).giveTokens(tokensSmall.toBigInteger(), false);
                    }
                }
                Translate.sendMessage(player, "prisons.charity.broadcast", tokensSmall, player.getName(), tokensLarge);
                PlayerDataManager.get(player).giveTokens(tokensLarge.toBigInteger(), true);
                player.playSound(player.getLocation(), Sound.LEVEL_UP, 1, 1);
            }
        }

        // Keys
        chance = BigDecimalMath.pow(BigDecimal.valueOf(1.0005), new BigDecimal(Upgrade.getLevel(item, Upgrade.LURE)).multiply(BigDecimal.valueOf(-1)));
        if (chance.compareTo(BigDecimal.valueOf(Math.random())) < 0) {
            if (Math.random() < 0.8) {
                Key.giveCommonKey(player);
            } else if (Math.random() < 0.8) {
                Key.giveRareKey(player);
            } else {
                Key.giveEpicKey(player);
            }

        }

        event.setExpToDrop(0);
        event.getBlock().getWorld().playEffect(event.getBlock().getLocation(), Effect.STEP_SOUND, event.getBlock().getType());
        event.getBlock().setType(Material.AIR);

        mine.onBlockBreak();
    }
}
