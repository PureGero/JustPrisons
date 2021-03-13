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
    
    /////////////////////////////////
    public BigInteger getSwordFortune() {
        if (!object.has("swordFortune")) return BigInteger.ZERO;
        return object.getBigInteger("swordFortune");
    }
    public void setSwordFortune(BigInteger fortune) {
        object.put("swordFortune", fortune);
    }
    
    public BigInteger getSwordLuck() {
        if (!object.has("swordLuck")) return BigInteger.ZERO;
        return object.getBigInteger("swordLuck");
    }
    public void setSwordLuck(BigInteger luck) {
        object.put("swordLuck", luck);
    }
    
    public BigInteger getSwordDamage() {
        if (!object.has("swordDamage")) return BigInteger.ZERO;
        return object.getBigInteger("swordDamage");
    }
    public void setSwordDamage(BigInteger damage) {
        object.put("swordDamage", damage);
    }
    
    public BigInteger getSwordLooting() {
        if (!object.has("swordLooting")) return BigInteger.ZERO;
        return object.getBigInteger("swordLooting");
    }
    public void setSwordLooting(BigInteger looting) {
        object.put("swordLooting", looting);
    }
    
    public BigInteger getSwordSpeed() {
        if (!object.has("swordSpeed")) return BigInteger.ZERO;
        return object.getBigInteger("swordSpeed");
    }
    public void setSwordSpeed(BigInteger speed) {
        object.put("swordSpeed", speed);
    }
    
    public BigInteger getSwordCharity() {
        if (!object.has("swordCharity")) return BigInteger.ZERO;
        return object.getBigInteger("swordCharity");
    }
    public void setSwordCharity(BigInteger charity) {
        object.put("swordCharity", charity);
    }
    
    public BigInteger getSwordUnbreaking() {
        if (!object.has("swordUnbreaking")) return BigInteger.ZERO;
        return object.getBigInteger("swordUnbreaking");
    }
    public void setSwordUnbreaking(BigInteger unbreaking) {
        object.put("swordUnbreaking", unbreaking);
    }
    
    public BigInteger getSwordLure() {
        if (!object.has("swordLure")) return BigInteger.ZERO;
        return object.getBigInteger("swordLure");
    }
    public void setSwordLure(BigInteger lure) {
        object.put("swordLure", lure);
    }
    
    public BigInteger getHelmetProtection() {
        if (!object.has("helmetProtection")) return BigInteger.ZERO;
        return object.getBigInteger("helmetProtection");
    }
    public void setHelmetProtection(BigInteger protection) {
        object.put("helmetProtection", protection);
    }
    
    public BigInteger getHelmetBlastProt() {
        if (!object.has("helmetBlastProt")) return BigInteger.ZERO;
        return object.getBigInteger("helmetBlastProt");
    }
    public void setHelmetBlastProt(BigInteger BlastProt) {
        object.put("helmetBlastProt", BlastProt);
    }
    
    public BigInteger getHelmetFireProt() {
        if (!object.has("helmetFireProt")) return BigInteger.ZERO;
        return object.getBigInteger("helmetFireProt");
    }
    public void setHelmetFireProt(BigInteger FireProt) {
        object.put("helmetFireProt", FireProt);
    }
    
    public BigInteger getHelmetProjProt() {
        if (!object.has("helmetProjProt")) return BigInteger.ZERO;
        return object.getBigInteger("helmetProjProt");
    }
    public void setHelmetProjProt(BigInteger ProjProt) {
        object.put("helmetProjProt", ProjProt);
    }
    
    public BigInteger getHelmetUnbreaking() {
        if (!object.has("helmetUnbreaking")) return BigInteger.ZERO;
        return object.getBigInteger("helmetUnbreaking");
    }
    public void setHelmetUnbreaking(BigInteger unbreaking) {
        object.put("helmetUnbreaking", unbreaking);
    }
    
    public BigInteger getHelmetThorns() {
        if (!object.has("helmetThorns")) return BigInteger.ZERO;
        return object.getBigInteger("helmetThorns");
    }
    public void setHelmetThorns(BigInteger Thorns) {
        object.put("helmetThorns", Thorns);
    }
    
    public BigInteger getChestplateProtection() {
        if (!object.has("chestplateProtection")) return BigInteger.ZERO;
        return object.getBigInteger("chestplateProtection");
    }
    public void setChestplateProtection(BigInteger protection) {
        object.put("chestplateProtection", protection);
    }
    
    public BigInteger getChestplateBlastProt() {
        if (!object.has("chestplateBlastProt")) return BigInteger.ZERO;
        return object.getBigInteger("chestplateBlastProt");
    }
    public void setChestplateBlastProt(BigInteger BlastProt) {
        object.put("chestplateBlastProt", BlastProt);
    }
    
    public BigInteger getChestplateFireProt() {
        if (!object.has("chestplateFireProt")) return BigInteger.ZERO;
        return object.getBigInteger("chestplateFireProt");
    }
    public void setChestplateFireProt(BigInteger FireProt) {
        object.put("chestplateFireProt", FireProt);
    }
    
    public BigInteger getChestplateProjProt() {
        if (!object.has("chestplateProjProt")) return BigInteger.ZERO;
        return object.getBigInteger("chestplateProjProt");
    }
    public void setChestplateProjProt(BigInteger ProjProt) {
        object.put("chestplateProjProt", ProjProt);
    }
    
    public BigInteger getChestplateUnbreaking() {
        if (!object.has("chestplateUnbreaking")) return BigInteger.ZERO;
        return object.getBigInteger("chestplateUnbreaking");
    }
    public void setChestplateUnbreaking(BigInteger unbreaking) {
        object.put("chestplateUnbreaking", unbreaking);
    }
    
    public BigInteger getChestplateThorns() {
        if (!object.has("chestplateThorns")) return BigInteger.ZERO;
        return object.getBigInteger("chestplateThorns");
    }
    public void setChestplateThorns(BigInteger Thorns) {
        object.put("chestplateThorns", Thorns);
    }
    
    public BigInteger getLeggingsProtection() {
        if (!object.has("leggingsProtection")) return BigInteger.ZERO;
        return object.getBigInteger("leggingsProtection");
    }
    public void setLeggingsProtection(BigInteger protection) {
        object.put("leggingsProtection", protection);
    }
    
    public BigInteger getLeggingsBlastProt() {
        if (!object.has("leggingsBlastProt")) return BigInteger.ZERO;
        return object.getBigInteger("leggingsBlastProt");
    }
    public void setLeggingsBlastProt(BigInteger BlastProt) {
        object.put("leggingsBlastProt", BlastProt);
    }
    
    public BigInteger getLeggingsFireProt() {
        if (!object.has("leggingsFireProt")) return BigInteger.ZERO;
        return object.getBigInteger("leggingsFireProt");
    }
    public void setLeggingsFireProt(BigInteger FireProt) {
        object.put("leggingsFireProt", FireProt);
    }
    
    public BigInteger getLeggingsProjProt() {
        if (!object.has("leggingsProjProt")) return BigInteger.ZERO;
        return object.getBigInteger("leggingsProjProt");
    }
    public void setLeggingsProjProt(BigInteger ProjProt) {
        object.put("leggingsProjProt", ProjProt);
    }
    
    public BigInteger getLeggingsUnbreaking() {
        if (!object.has("leggingsUnbreaking")) return BigInteger.ZERO;
        return object.getBigInteger("leggingsUnbreaking");
    }
    public void setLeggingsUnbreaking(BigInteger unbreaking) {
        object.put("leggingsUnbreaking", unbreaking);
    }
    
    public BigInteger getLeggingsThorns() {
        if (!object.has("leggingsThorns")) return BigInteger.ZERO;
        return object.getBigInteger("leggingsThorns");
    }
    public void setLeggingsThorns(BigInteger Thorns) {
        object.put("leggingsThorns", Thorns);
    }
    
    public BigInteger getBootsProtection() {
        if (!object.has("bootsProtection")) return BigInteger.ZERO;
        return object.getBigInteger("bootsProtection");
    }
    public void setBootsProtection(BigInteger protection) {
        object.put("bootsProtection", protection);
    }
    
    public BigInteger getBootsBlastProt() {
        if (!object.has("bootsBlastProt")) return BigInteger.ZERO;
        return object.getBigInteger("bootsBlastProt");
    }
    public void setBootsBlastProt(BigInteger BlastProt) {
        object.put("bootsBlastProt", BlastProt);
    }
    
    public BigInteger getBootsFireProt() {
        if (!object.has("bootsFireProt")) return BigInteger.ZERO;
        return object.getBigInteger("bootsFireProt");
    }
    public void setBootsFireProt(BigInteger FireProt) {
        object.put("bootsFireProt", FireProt);
    }
    
    public BigInteger getBootsProjProt() {
        if (!object.has("bootsProjProt")) return BigInteger.ZERO;
        return object.getBigInteger("bootsProjProt");
    }
    public void setBootsProjProt(BigInteger ProjProt) {
        object.put("bootsProjProt", ProjProt);
    }
    
    public BigInteger getBootsUnbreaking() {
        if (!object.has("bootsUnbreaking")) return BigInteger.ZERO;
        return object.getBigInteger("bootsUnbreaking");
    }
    public void setBootsUnbreaking(BigInteger unbreaking) {
        object.put("bootsUnbreaking", unbreaking);
    }
    
    public BigInteger getBootsThorns() {
        if (!object.has("bootsThorns")) return BigInteger.ZERO;
        return object.getBigInteger("bootsThorns");
    }
    public void setBootsThorns(BigInteger Thorns) {
        object.put("bootsThorns", Thorns);
    }
    /////////////////////////////////
    
    
    public boolean getStopKeyMessages() {
        if (!object.has("stopKeyMessages")) return false;
        return object.getBoolean("stopKeyMessages");
    }

    public void setStopKeyMessages(boolean stopKeyMessages) {
        object.put("stopKeyMessages", stopKeyMessages);
    }

    public boolean getStopKeyUseMessages() {
        if (!object.has("stopKeyUseMessages")) return false;
        return object.getBoolean("stopKeyUseMessages");
    }

    public void setStopKeyUseMessages(boolean stopKeyUseMessages) {
        object.put("stopKeyUseMessages", stopKeyUseMessages);
    }

    public boolean getStopTokenMessages() {
        if (!object.has("stopTokenMessages")) return false;
        return object.getBoolean("stopTokenMessages");
    }

    public void setStopTokenMessages(boolean stopTokenMessages) {
        object.put("stopTokenMessages", stopTokenMessages);
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
     * Get the amount of upgrades to buy at a time
     */
    public int getUpgradeAmount() {
        if(!object.has("upgradeAmount")) return 1;
        return object.getInt("upgradeAmount");
    }

    public void setUpgradeAmount(int amount) {
        object.put("upgradeAmount", amount);
    }


    public int getAmountUnlocked() {
        if(!object.has("amountUnlocked")) return 0;
        return object.getInt("amountUnlocked");
    }

    public void addAmountUnlocked() {
        int amount = getUpgradeAmount() == 5 ? 5 : getAmountUnlocked() + 1;
        object.put("upgradeAmount", amount);
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

    public void setRank(int rank) {
        object.put("rank", rank);

        updatePermissions();

        getScoreboard().updateTitle();

        for (Player otherPlayer : Bukkit.getOnlinePlayers()) {
            PlayerDataManager.get(otherPlayer).getScoreboard().updateRank(getPlayer());
        }
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
        String[] amountPerms = {"five", "ten", "one_hundred", "one_thousand", "max"};
        int unlockedAmounts = getAmountUnlocked();

        for (int i = 0; i <= rank && i < 26; i++) {
            String permission = "prisons." + (char) ('a' + i);
            if (!player.hasPermission(permission)) {
                player.addAttachment(PrisonsPlugin.getPlugin(), permission, true);
            }
        }

        for(int i=0; i < unlockedAmounts && i < amountPerms.length; i++) {
            String permission = "prison.amount." + amountPerms[i];
            if(!player.hasPermission(permission)) {
                player.addAttachment(PrisonsPlugin.getPlugin(), permission, true);
            }
        }
    }
}