package net.justminecraft.prisons.playerdata;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.HashMap;

public class PlayerScoreboard {

    private Scoreboard scoreboard = null;
    private Objective sidebar = null;
    private HashMap<Integer, String> lastMessages = new HashMap<>();
    private Player player;

    public void show(Player player) {
        this.player = player;
        scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();

        sidebar = scoreboard.registerNewObjective("sidebar", "dummy");
        sidebar.setDisplaySlot(DisplaySlot.SIDEBAR);

        updateAll();

        player.setScoreboard(scoreboard);

        for (Player otherPlayer : Bukkit.getOnlinePlayers()) {
            updateRank(otherPlayer);
            PlayerDataManager.get(otherPlayer).getScoreboard().updateRank(player);
        }
    }

    private void updateAll() {
        updateTitle();

        for (PlayerScoreboardEntry entry : PlayerScoreboardEntry.values()) {
            update(entry);
        }
    }

    public void updateTitle() {
        if (scoreboard == null || player == null) return;

        PlayerData playerData = PlayerDataManager.get(player);
        sidebar.setDisplayName(String.format("%s%s[%s] %s",
                playerData.getRankColor(),
                ChatColor.BOLD,
                playerData.getRankText(),
                player.getName()));
    }

    public void update(PlayerScoreboardEntry entry) {
        if (scoreboard == null || player == null) return;

        // Remove previous message for this entry
        if (lastMessages.containsKey(entry.getId())) {
            scoreboard.resetScores(lastMessages.remove(entry.getId()));
        }

        // Set this message if it's not null
        String msg = entry.apply(PlayerDataManager.get(player));
        if (msg != null) {
            sidebar.getScore(msg).setScore(entry.getId());
            lastMessages.put(entry.getId(), msg);
        }
    }

    public void updateRank(Player player) {
        if (scoreboard == null) {
            // Scoreboard hasn't been loaded yet
            return;
        }

        PlayerData data = PlayerDataManager.get(player);
        Team team = scoreboard.getTeam(data.getRankText());

        if (team == null) {
            team = scoreboard.registerNewTeam(data.getRankText());
            team.setPrefix(data.getRankPrefix());
        }

        team.addEntry(player.getName());
    }

}
