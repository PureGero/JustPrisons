package net.justminecraft.prisons.playerdata;

import net.justminecraft.prisons.PrisonsPlugin;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.UUID;

public class PlayerDataLoader {
    public static PlayerData load(UUID uuid) {
        PlayerData data = new PlayerData();
        data.uuid = uuid;

        File f = new File(PrisonsPlugin.getPlugin().getDataFolder(), "playerdata/" + uuid.toString() + ".json");
        try {
            if (f.isFile()) {
                backup(uuid);
                data.object = new JSONObject(new String(Files.readAllBytes(f.toPath()), StandardCharsets.UTF_8));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return data;
    }

    private static void backup(UUID uuid) {
        File from = new File(PrisonsPlugin.getPlugin().getDataFolder(), "playerdata/" + uuid.toString() + ".json");
        File to;
        int i = 1;

        // Find first free backup file
        do {
            to = new File(PrisonsPlugin.getPlugin().getDataFolder(), "playerdata_backups/" + uuid.toString() + "." + (i++) + ".json");
        } while (to.isFile());

        // Write the backup
        try {
            Files.createDirectories(to.getParentFile().toPath());
            Files.copy(from.toPath(), to.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
