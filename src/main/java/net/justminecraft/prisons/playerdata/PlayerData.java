package net.justminecraft.prisons.playerdata;

import net.justminecraft.prisons.PrisonsPlugin;
import net.justminecraft.prisons.Translate;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.json.JSONArray;
import org.json.JSONObject;

import java.math.BigInteger;
import java.util.UUID;

public class PlayerData {
    UUID uuid;
    JSONObject object = new JSONObject();

    private PlayerScoreboard scoreboard = new PlayerScoreboard();
    private long lastRankupReminder = 0;

    public void save() {
        PlayerDataSaver.save(this);
    }

    public BigInteger getCoins() {
        if (!object.has("coins")) return BigInteger.ZERO;
        return object.getBigInteger("coins");
    }

    public void setCoins(BigInteger coins) {
        object.put("coins", coins);
        scoreboard.update(PlayerScoreboardEntry.COINS);
    }

    public BigInteger getTokens() {
        if (!object.has("tokens")) return BigInteger.ZERO;
        return object.getBigInteger("tokens");
    }

    public void setTokens(BigInteger coins) {
        object.put("tokens", coins);
        scoreboard.update(PlayerScoreboardEntry.TOKENS);
    }

    public void giveTokens(BigInteger tokens, boolean important) {
        setTokens(getTokens().add(tokens));

        if (important || !object.has("stopTokenMessages") || !object.getBoolean("stopTokenMessages")) {
            Translate.sendMessage(getPlayer(), "prisons.tokens.receive", tokens);
        }
    }

    public boolean takeTokens(BigInteger tokens) {
        BigInteger newTokens = getTokens().subtract(tokens);

        if (newTokens.compareTo(BigInteger.ZERO) < 0) {
            Translate.sendMessage(getPlayer(), "prisons.tokens.notenough");
            return false;
        }

        setTokens(newTokens);
        Translate.sendMessage(getPlayer(), "prisons.tokens.take", tokens);
        return true;
    }

    public boolean getStopKeyUseMessages() {
        if (!object.has("stopKeyUseMessages")) return false;
        return object.getBoolean("stopKeyUseMessages");
    }

    private Player getPlayer() {
        return Bukkit.getPlayer(uuid);
    }

    public BigInteger getBlocksBroken() {
        if (!object.has("blocksBroken")) return BigInteger.ZERO;
        return object.getBigInteger("blocksBroken");
    }

    public void setBlocksBroken(BigInteger blocksBroken) {
        object.put("blocksBroken", blocksBroken);
    }

    public int getMulti() {
        if (!object.has("multi") || getMultiStart() < System.currentTimeMillis() - getMultiLength()) return 1;
        return object.getInt("multi");
    }

    /**
     * When the multi started in milliseconds since 1st Jan 1970
     */
    public long getMultiStart() {
        if (!object.has("multiStart")) return 0;
        return object.getLong("multiStart");
    }

    /**
     * Duration of the multi in milliseconds
     */
    public long getMultiLength() {
        if (!object.has("multiLength")) return 0;
        return object.getLong("multiLength");
    }

    public void startMulti(int multi, long multiLength) {
        object.put("multi", multi);
        object.put("multiStart", System.currentTimeMillis());
        object.put("multiLength", multiLength);
        scoreboard.update(PlayerScoreboardEntry.MULTI);
    }

    public PlayerScoreboard getScoreboard() {
        return scoreboard;
    }

    public int getRank() {
        if (!object.has("rank")) return 0;
        return object.getInt("rank");
    }

    public ChatColor getRankColor() {
        int prestige = getRank() - 25;
        if (prestige >= 500)
            return ChatColor.DARK_RED;
        if (prestige >= 200)
            return ChatColor.AQUA;
        if (prestige >= 100)
            return ChatColor.GREEN;
        if (prestige >= 50)
            return ChatColor.GOLD;
        if (prestige >= 1)
            return ChatColor.YELLOW;
        return ChatColor.DARK_PURPLE;
    }

    public String getRankText() {
        if(getRank() < 26)
            return Character.toString((char) ('A' + (getRank() % 26)));
        return "P" + (getRank() - 25);
    }

    public String getRankPrefix() {
        return String.format("%s[%s] ", getRankColor(), getRankText());
    }

    public long getLastRankupReminder() {
        return lastRankupReminder;
    }

    public void setLastRankupReminder(long lastRankupReminder) {
        this.lastRankupReminder = lastRankupReminder;
    }

    public BigInteger getLargestBlockCoinGain() {
        if (!object.has("largestBlockCoinGain")) return BigInteger.ZERO;
        return object.getBigInteger("largestBlockCoinGain");
    }

    public void setLargestBlockCoinGain(BigInteger largestBlockCoinGain) {
        object.put("largestBlockCoinGain", largestBlockCoinGain);
    }

    public JSONArray getItems() {
        if (!object.has("items")) return null;
        return object.getJSONArray("items");
    }

    public void removeItems() {
        object.remove("items");
    }

    public JSONArray getEnderChest() {
        if (!object.has("enderChest")) return null;
        return object.getJSONArray("enderChest");
    }

    public void removeEnderChest() {
        object.remove("enderChest");
    }

    public void updatePermissions() {
        Player player = getPlayer();
        int rank = getRank();

        for (int i = 0; i <= rank && i < 26; i++) {
            String permission = "prisons." + (char) ('a' + i);
            if (!player.hasPermission(permission)) {
                player.addAttachment(PrisonsPlugin.getPlugin(), permission, true);
            }
        }
    }
}