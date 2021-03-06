package net.justminecraft.prisons.mines;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import net.justminecraft.prisons.PrisonsPlugin;
import net.justminecraft.prisons.inventory.pickaxe.Upgrade2Pickaxe;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import io.github.miraclefoxx.math.BigDecimalMath;
import net.justminecraft.prisons.Translate;
import net.justminecraft.prisons.inventory.Key;
import net.justminecraft.prisons.inventory.pickaxe.UpgradePickaxe;
import net.justminecraft.prisons.playerdata.PlayerData;
import net.justminecraft.prisons.playerdata.PlayerDataManager;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;

public class MineListener implements Listener {
    public static final int TOKEN_MULTIPLIER = 2;
    private final MineManager mineManager;

    private HashMap<Player, Integer> multiMineProtector = new HashMap<>();
    private ArrayList<Player> multiMineCoolDown = new ArrayList<>();

    ArrayList<Material> bannedMultiMineMaterials = new ArrayList<>(Arrays.asList(Material.AIR, Material.BARRIER, Material.BEDROCK, Material.GLOWSTONE));

    public MineListener(MineManager mineManager) {
        this.mineManager = mineManager;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Mine mine = mineManager.getMine(player);

        if (mine == null || event.getClickedBlock() == null
                || (event.getItem() != null && event.getItem().getType().isEdible() && event.getAction() == Action.RIGHT_CLICK_BLOCK)) {
            return;
        }

        if (!mine.inMine(event.getClickedBlock())) {
            event.setCancelled(true);
        }
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
        ItemStack item = player.getItemInHand();

        if (mine == null) {
            return;
        }

        if (item != null && item.getType() == Material.DIAMOND_PICKAXE && item.getDurability() > 1561) {
            player.sendMessage(ChatColor.RED + "Your pickaxe is out of durability! Please repair it!!!");
            event.setCancelled(true);
            return;
        }

        PlayerData playerData = PlayerDataManager.get(player);
        playerData.setBlocksBroken(playerData.getBlocksBroken().add(BigInteger.ONE));

        runBalance(player, item, playerData);

        BigDecimal chance;

        // Coins
        BigInteger coinGain = BigInteger.valueOf(MineOres.getBlockValue(event.getBlock().getType())).multiply(
                item == null ? BigInteger.ONE : UpgradePickaxe.getLevel(item, UpgradePickaxe.FORTUNE).divide(BigInteger.valueOf(3)).add(BigInteger.ONE));
        playerData.setCoins(playerData.getCoins().add(coinGain.multiply(BigInteger.valueOf(playerData.getMulti()))));

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

        // Multi Mine
        for(int x = -1; x <= 1; x++)
            for(int y = -1; y <= 1; y++)
                for(int z = -1; z <= 1; z++)
                    runMultiMine(item, event.getBlock().getLocation().clone().add(x,y,z), player);


        // Explodi boiiiii
        chance = BigDecimalMath.pow(BigDecimal.valueOf(1.0005), new BigDecimal(Upgrade2Pickaxe.getLevel(item, Upgrade2Pickaxe.EXPLOSION)).multiply(BigDecimal.valueOf(-1)));
        if (chance.compareTo(BigDecimal.valueOf(Math.random())) < 0) {
            player.playSound(event.getBlock().getLocation(), Sound.EXPLODE, 1, 1);
            for(int x = -1; x <= 1; x++) {
                for(int y = -1; y <= 1; y++) {
                    for(int z = -1; z <= 1; z++) {
                        Location l = event.getBlock().getLocation().clone().add(x, y, z);
                        if(!bannedMultiMineMaterials.contains(l.getBlock().getType())) {
                            l.getBlock().setType(Material.AIR);
                            runBalance(player, item, playerData);
                        }
                    }
                }
            }
        }

        event.setExpToDrop(0);
        for (Player otherPlayer : event.getBlock().getWorld().getPlayers()) {
            if (otherPlayer != player) {
                otherPlayer.playEffect(event.getBlock().getLocation(), Effect.STEP_SOUND, event.getBlock().getType());
            }
        }
        event.getBlock().setType(Material.AIR);

        mine.onBlockBreak();

        if (new BigDecimal(UpgradePickaxe.getLevel(item, UpgradePickaxe.UNBREAKING)).add(BigDecimal.ONE).multiply(BigDecimal.valueOf(Math.random())).compareTo(BigDecimal.ONE) < 0) {
            item.setDurability((short) (item.getDurability() + 1));
            if (item.getType() == Material.GOLD_PICKAXE && item.getDurability() > 32) {
                player.getWorld().playSound(player.getLocation(), Sound.ITEM_BREAK, 1, 1);
                player.setItemInHand(null);
            } else {
                player.setItemInHand(item);
            }
        }
    }

    private void runBalance(Player player, ItemStack item, PlayerData playerData) {

        // Tokens
        BigInteger luckLevel = UpgradePickaxe.getLevel(item, UpgradePickaxe.LUCK).add(BigInteger.valueOf(5));
        BigDecimal chance = BigDecimal.valueOf(1);
        while (luckLevel.compareTo(BigInteger.valueOf(100000)) > 0) {
            chance = chance.multiply(BigDecimalMath.pow(BigDecimal.valueOf(1.001), BigDecimal.valueOf(100000).multiply(BigDecimal.valueOf(-1))));
            luckLevel = luckLevel.subtract(BigInteger.valueOf(100000));
        }
        chance = chance.multiply(BigDecimalMath.pow(BigDecimal.valueOf(1.001), new BigDecimal(luckLevel).multiply(BigDecimal.valueOf(-1))));

        while (chance.compareTo(BigDecimal.valueOf(Math.random())) < 0) {
            BigDecimal tokens = new BigDecimal(UpgradePickaxe.getLevel(item, UpgradePickaxe.LOOTING))
                    .multiply(BigDecimal.valueOf(Math.random() * 5))
                    .add(BigDecimal.valueOf(5))
                    .multiply(BigDecimal.valueOf(TOKEN_MULTIPLIER));
            playerData.giveTokens(tokens.toBigInteger(), false);

            chance = chance.multiply(BigDecimal.TEN);
        }

        // Charity
        //chance = (1-chance)*0.01;
        double charityChance = 3.0e-5;
        for (int i = 0; i < UpgradePickaxe.getLevel(item, UpgradePickaxe.CHARITY).intValue(); i++) {
            if (Math.random() < charityChance) {
                BigDecimal tokensSmall = new BigDecimal(UpgradePickaxe.getLevel(item, UpgradePickaxe.LOOTING))
                        .multiply(BigDecimal.valueOf(Math.random() * 5))
                        .add(BigDecimal.valueOf(5))
                        .multiply(BigDecimal.valueOf(TOKEN_MULTIPLIER));
                BigDecimal tokensLarge = tokensSmall.multiply(BigDecimal.valueOf(Math.random() * 900 + 100));
                tokensSmall = tokensSmall.multiply(new BigDecimal(UpgradePickaxe.getLevel(item, UpgradePickaxe.CHARITY))).multiply(BigDecimal.TEN);
                tokensLarge = tokensLarge.multiply(new BigDecimal(UpgradePickaxe.getLevel(item, UpgradePickaxe.CHARITY)).multiply(BigDecimal.valueOf(0.2)).add(BigDecimal.ONE));
                for (Player receiver : Bukkit.getOnlinePlayers()) {
                    if (receiver != player) {
                        Translate.sendMessage(receiver, "prisons.charity.broadcast", tokensSmall.toBigInteger(), player.getName(), tokensLarge.toBigInteger());
                        PlayerDataManager.get(receiver).giveTokens(tokensSmall.toBigInteger(), false);
                    }
                }
                Translate.sendMessage(player, "prisons.charity.broadcast", tokensSmall.toBigInteger(), player.getName(), tokensLarge.toBigInteger());
                PlayerDataManager.get(player).giveTokens(tokensLarge.toBigInteger(), true);
                player.playSound(player.getLocation(), Sound.LEVEL_UP, 1, 1);
            }
        }

        // Keys
        chance = BigDecimalMath.pow(BigDecimal.valueOf(1.0005), new BigDecimal(UpgradePickaxe.getLevel(item, UpgradePickaxe.LURE)).multiply(BigDecimal.valueOf(-1)));
        if (chance.compareTo(BigDecimal.valueOf(Math.random())) < 0) {
            if (Math.random() < 0.8) {
                Key.giveCommonKey(player);
            } else if (Math.random() < 0.8) {
                Key.giveRareKey(player);
            } else {
                Key.giveEpicKey(player);
            }

        }
    }

    private void runMultiMine(ItemStack item, Location l, Player player) {
        double multiMineChance = 1.0e-2;
        if(bannedMultiMineMaterials.contains(l.getBlock().getType())) return;
        if(multiMineProtector.containsKey(player) && multiMineProtector.get(player) > 5000) {
            if(multiMineCoolDown.contains(player)) return;
            multiMineCoolDown.add(player);
            PrisonsPlugin.getPlugin().getServer().getScheduler().runTaskLater(PrisonsPlugin.getPlugin(), () -> {multiMineCoolDown.remove(player); multiMineProtector.remove(player);}, 20);
        } else
            for(int i = 0; i < Upgrade2Pickaxe.getLevel(item, Upgrade2Pickaxe.MULTI_MINE).intValue(); i++) {
                if(Math.random() < multiMineChance) {
                    BlockBreakEvent b = new BlockBreakEvent(l.getBlock(), player);
                    try {
                        PrisonsPlugin.getPlugin().getServer().getScheduler().runTaskLater(PrisonsPlugin.getPlugin(), () -> Bukkit.getPluginManager().callEvent(b), 1);
                        if(multiMineProtector.containsKey(player))
                            multiMineProtector.replace(player, multiMineProtector.get(player) + 1);
                        else
                            multiMineProtector.put(player, 1);
                    } catch (IllegalStateException e) {
                        e.printStackTrace();
                        break;
                    }
                }
            }
    }
}
