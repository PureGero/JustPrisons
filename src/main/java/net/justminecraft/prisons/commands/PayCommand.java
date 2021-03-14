package net.justminecraft.prisons.commands;

import net.justminecraft.prisons.PrisonsPlugin;
import net.justminecraft.prisons.playerdata.PlayerData;
import net.justminecraft.prisons.playerdata.PlayerDataManager;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PayCommand implements CommandExecutor, TabCompleter {

    private final PrisonsPlugin plugin;

    public PayCommand(PrisonsPlugin plugin) {

        this.plugin = plugin;

        PluginCommand command = plugin.getCommand("pay");

        if (command != null) {
            command.setExecutor(this);
            command.setTabCompleter(this);
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage("This is a player only command!");
            return false;
        }
        PlayerData data = PlayerDataManager.get((Player) sender);

        if(args.length < 3) {
            sender.sendMessage(ChatColor.RED + "You did not provide the correct arguments!");
            return false;
        }

        if(!args[0].equalsIgnoreCase("coins") && !args[0].equalsIgnoreCase("tokens")) {
            sender.sendMessage(ChatColor.RED + "You need to specify the type you are paying in!");
            return false;
        }

        Player giveToPlayer = Bukkit.getPlayer(args[1]);

        if(giveToPlayer == null) {
            sender.sendMessage(ChatColor.RED + "That is not a player!");
            return false;
        }

        if(giveToPlayer == sender) {
            sender.sendMessage(ChatColor.RED + "You cannot pay yourself!");
            return false;
        }

        PlayerData giveData = PlayerDataManager.get(giveToPlayer);

        BigInteger amountToGive = null;

        try {
            amountToGive = new BigInteger(args[2]);
        } catch (NumberFormatException e) {
            sender.sendMessage(ChatColor.RED + "That is not a valid amount!");
            return false;
        }

        if(amountToGive.signum() < 1) {
            sender.sendMessage(ChatColor.RED + "You cannot give someone \"" + amountToGive + "\" " + args[0]);
            return false;
        }

        if(args[0].equalsIgnoreCase("coins")) {
            if(takeCoins(data, amountToGive)) {
                giveCoins(giveData, amountToGive);
                sender.sendMessage(ChatColor.GREEN + String.format("You gave " + giveToPlayer.getName() + " " + amountToGive + " coins! You now have %,d coins", data.getCoins()));
                giveToPlayer.sendMessage(ChatColor.GREEN + String.format(sender.getName() + " gave you " + amountToGive + " coins! You now have %,d coins", giveData.getCoins()));
            } else {
                sender.sendMessage(ChatColor.RED + "You do not have enough coins to do this.");
            }
        } else {
            if(takeTokens(data, amountToGive)) {
                giveTokens(giveData, amountToGive);
                sender.sendMessage(ChatColor.GREEN + String.format("You gave " + giveToPlayer.getName() + " " + amountToGive + " tokens! You now have %,d tokens", data.getTokens()));
                giveToPlayer.sendMessage(ChatColor.GREEN + String.format(sender.getName() + " gave you " + amountToGive + " tokens! You now have %,d tokens", giveData.getCoins()));
            } else {
                sender.sendMessage(ChatColor.RED + "You do not have enough tokens to do this.");
            }
        }

        return true;
    }

    private boolean takeCoins(PlayerData data, BigInteger bigInteger) {
        BigInteger currentCoins = data.getCoins();
        if(currentCoins.subtract(bigInteger).signum() < 0) return false;
        data.setCoins(currentCoins.subtract(bigInteger));
        return true;
    }

    private boolean takeTokens(PlayerData data, BigInteger bigInteger) {
        BigInteger currentTokens = data.getTokens();
        if(currentTokens.subtract(bigInteger).signum() < 0) return false;
        data.setTokens(currentTokens.subtract(bigInteger));
        return true;
    }

    private void giveCoins(PlayerData data, BigInteger bigInteger) {
        BigInteger currentCoins = data.getCoins();
        data.setCoins(currentCoins.add(bigInteger));
    }

    private void giveTokens(PlayerData data, BigInteger bigInteger) {
        BigInteger currentTokens = data.getTokens();
        data.setTokens(currentTokens.add(bigInteger));
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String label, String[] args) {
        List<String> tabs = new ArrayList<>();

        if (args.length == 1) {
            tabs.addAll(Arrays.asList("coins", "tokens"));
        }

        if (args.length == 2) {
            for(Player p : Bukkit.getOnlinePlayers())
                tabs.add(p.getName());
        }

        return tabs;
    }
}
