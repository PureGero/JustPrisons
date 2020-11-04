package net.justminecraft.prisons.commands;

import net.justminecraft.prisons.PrisonsPlugin;
import net.justminecraft.prisons.Translate;
import net.justminecraft.prisons.inventory.Key;
import net.justminecraft.prisons.playerdata.PlayerData;
import net.justminecraft.prisons.playerdata.PlayerDataManager;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;

import java.math.BigInteger;

public class RankupCommand implements CommandExecutor {

    public RankupCommand(PrisonsPlugin plugin) {
        PluginCommand command = plugin.getCommand("rankup");

        if (command != null) {
            command.setExecutor(this);
        }
    }

    public static long significant2(long l) {
        String s = Long.toString(l);
        if (s.length() < 3) return Long.parseLong(s);
        int length = s.length();
        s = s.substring(0, 2);
        for (int i = 2; i < length; i++)
            s += "0";
        return Long.parseLong(s);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "You must be a player to execute this command.");
            return false;
        }

        Player player = (Player) sender;
        PlayerData data = PlayerDataManager.get(player);

        if (data.getRank() >= 25) {
            player.spigot().sendMessage(new ComponentBuilder("You are at the highest rank!\nPrestige with ").color(ChatColor.RED).append("/prestige").color(ChatColor.DARK_RED)
                    .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("/prestige").create()))
                    .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/prestige")).create());
        }

        long cost = significant2((long) (100000L * Math.pow(1.5, data.getRank())));
        if (data.getCoins().compareTo(BigInteger.valueOf(cost)) < 0) {
            Translate.sendMessage(sender, "prisons.rankup.notenough", cost);
        } else {
            data.setCoins(data.getCoins().subtract(BigInteger.valueOf(cost)));
            data.setRank(data.getRank() + 1);
            Key.giveLegendaryKey(player);
            Translate.broadcastMessage("prisons.rankup.broadcast", player.getName(), data.getRankText());
            Translate.sendMessage(sender, "prisons.rankup.success", data.getRankText());
            player.playSound(player.getLocation(), Sound.LEVEL_UP, 1, 1);

            String warp = "/warp " + data.getRankText();
            Bukkit.getScheduler().runTaskLater(PrisonsPlugin.getPlugin(), () -> {
                player.spigot().sendMessage(new ComponentBuilder("You have unlocked: ").color(ChatColor.GOLD).append(warp)
                        .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(warp).create()))
                        .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, warp)).create());
                player.playSound(player.getLocation(), Sound.SUCCESSFUL_HIT, 1, 1);
            }, 40L);
        }

        return true;
    }
}
