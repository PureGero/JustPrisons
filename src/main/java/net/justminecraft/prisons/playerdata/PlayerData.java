package net.justminecraft.prisons.playerdata;

import net.justminecraft.prisons.org.json.JSONObject;

import java.math.BigInteger;
import java.util.UUID;

public class PlayerData {
    UUID uuid;
    JSONObject object = new JSONObject();

    public void save() {
        PlayerDataSaver.save(this);
    }

    public BigInteger getCoins() {
        if (!object.has("coins")) return BigInteger.ZERO;
        return object.getBigInteger("coins");
    }

    public void setCoins(BigInteger coins) {
        object.put("coins", coins);
    }

    public BigInteger getTokens() {
        if (!object.has("tokens")) return BigInteger.ZERO;
        return object.getBigInteger("tokens");
    }

    public void setTokens(BigInteger coins) {
        object.put("tokens", coins);
    }
}