package net.justminecraft.prisons.inventory;

import net.justminecraft.prisons.PrisonsPlugin;
import net.justminecraft.prisons.Translate;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import puregero.network.VoteEvent;

public class VoteListener implements Listener {

    public VoteListener(PrisonsPlugin plugin) {
        try {
            if (Class.forName("puregero.network.VoteEvent") != null) {
                plugin.getServer().getPluginManager().registerEvents(this, plugin);
            }
        } catch (ClassNotFoundException ignored) {}
    }

    @EventHandler
    public void onVote(VoteEvent event) {
        Key.giveVoteKey(event.getPlayer());
        Translate.sendMessage(event.getPlayer(), "prisons.vote.thanks", event.getWebsite());
    }

}
