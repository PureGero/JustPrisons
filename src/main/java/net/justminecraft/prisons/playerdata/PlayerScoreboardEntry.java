package net.justminecraft.prisons.playerdata;

import org.bukkit.ChatColor;

import java.util.function.Function;

public enum PlayerScoreboardEntry implements Function<PlayerData, String> {
    SEP_1(new Seperator(1)),
    COINS(playerData -> {
        return String.format(ChatColor.GOLD + "Coins: " + ChatColor.WHITE + "%,d", playerData.getCoins());
    }),
    TOKENS(playerData -> {
        return String.format(ChatColor.GREEN + "Tokens: " + ChatColor.WHITE + "%,d", playerData.getTokens());
    }),
    MULTI(playerData -> {
        String multi = "None";
        if (playerData.getMulti() > 1) {
            long seconds = (playerData.getMultiStart() + playerData.getMultiLength() - System.currentTimeMillis()) / 1000;
            multi = String.format("%dx %02d:%02d mins", playerData.getMulti(), seconds / 60, seconds % 60);
        }
        return ChatColor.LIGHT_PURPLE + "Multi: " + ChatColor.WHITE + multi;
    }),
    SEP_2(new Seperator(2)),
    WEBSITE(playerData -> ChatColor.AQUA + "prisons.justminecraft.net"),
    ALPHA(playerData -> ChatColor.YELLOW.toString() + ChatColor.BOLD + "ALPHA" + ChatColor.YELLOW + " - Report bugs"),
    ALPHA_2(playerData -> ChatColor.YELLOW + "            on /discord");

    static {
        // Set each entries' id
        int i = values().length;
        for (PlayerScoreboardEntry entry : values()) {
            entry.id = i--;
        }
    }

    private final Function<PlayerData, String> generator;
    private int id;

    PlayerScoreboardEntry(Function<PlayerData, String> generator) {
        this.generator = generator;
    }

    @Override
    public String apply(PlayerData playerData) {
        return generator.apply(playerData);
    }

    public int getId() {
        return id;
    }

    /**
     * A seperator between sections on the scoreboard
     */
    private static class Seperator implements Function<PlayerData, String> {
        private int value;

        private Seperator(int value) {
            this.value = value;
        }

        @Override
        public String apply(PlayerData playerData) {
            return ChatColor.GRAY + "+-------------------+" + ChatColor.COLOR_CHAR + value;
        }
    }
}
