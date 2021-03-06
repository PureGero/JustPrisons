package net.justminecraft.prisons.commands;

import net.justminecraft.prisons.PrisonsPlugin;
import net.justminecraft.prisons.Translate;
import net.justminecraft.prisons.inventory.Key;
import net.justminecraft.prisons.playerdata.PlayerData;
import net.justminecraft.prisons.playerdata.PlayerDataManager;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;

import java.math.BigInteger;

public class PrestigeCommand implements CommandExecutor {

    public PrestigeCommand(PrisonsPlugin plugin) {
        PluginCommand command = plugin.getCommand("prestige");

        if (command != null) {
            command.setExecutor(this);
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "You must be a player to execute this command.");
            return false;
        }

        Player player = (Player) sender;
        PlayerData data = PlayerDataManager.get(player);

        int nextRank = data.getRank() - 25;

        if (nextRank < 0) {
            return false;
        }

        BigInteger cost = BigInteger.valueOf(1000000000L);
        BigInteger scalar = BigInteger.valueOf(250000000L);
        while (nextRank - 10 > 0) {
            cost = cost.add(scalar.multiply(BigInteger.TEN));
            scalar = scalar.multiply(BigInteger.valueOf(2));
            nextRank -= 10;
        }

        cost = cost.add(scalar.multiply(BigInteger.valueOf(nextRank)));

        if (data.getCoins().compareTo(cost) < 0) {
            Translate.sendMessage(sender, "prisons.rankup.notenough", cost);
        } else {
            data.setCoins(data.getCoins().subtract(cost));
            data.setRank(data.getRank() + 1);
            Key.giveLegendaryKey(player);
            Translate.broadcastMessage("prisons.rankup.broadcast", player.getName(), data.getRankText());
            Translate.sendMessage(sender, "prisons.rankup.success", data.getRankText());
            player.playSound(player.getLocation(), Sound.LEVEL_UP, 1, 1);
        }

        return true;
    }
}
