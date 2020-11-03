package net.justminecraft.prisons.playerdata;

import net.justminecraft.prisons.org.json.JSONObject;
import org.bukkit.ChatColor;

import java.math.BigInteger;
import java.util.UUID;

public class PlayerData {
    UUID uuid;
    JSONObject object = new JSONObject();

    private PlayerScoreboard scoreboard = new PlayerScoreboard();

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

    private int getRank() {
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
}