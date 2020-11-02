package net.justminecraft.prisons.playerdata;

import net.justminecraft.prisons.PrisonsPlugin;
import org.bukkit.Bukkit;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class PlayerDataSaver {
    public static void save(PlayerData data) {
        File f = new File(PrisonsPlugin.getPlugin().getDataFolder(), "playerdata/" + data.uuid.toString() + ".json");
        byte[] b = data.object.toString().getBytes(StandardCharsets.UTF_8);

        // Run async if not already
        if (PrisonsPlugin.getPlugin().isEnabled() && Bukkit.isPrimaryThread()) {
            Bukkit.getScheduler().runTaskAsynchronously(PrisonsPlugin.getPlugin(), () -> {
                try {
                    Files.createDirectories(f.getParentFile().toPath());
                    Files.write(f.toPath(), b);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        } else {
            try {
                Files.createDirectories(f.getParentFile().toPath());
                Files.write(f.toPath(), b);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
