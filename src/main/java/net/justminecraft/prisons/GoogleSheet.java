package net.justminecraft.prisons;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

public abstract class GoogleSheet implements Runnable {

    private final PrisonsPlugin plugin;
    private final String url;
    private final File cacheFile;

    private List<String> lines = new ArrayList<>();

    public GoogleSheet(PrisonsPlugin plugin, String googleIdentifier) {
        this.plugin = plugin;
        cacheFile = new File(plugin.getDataFolder(), "sheets/" + googleIdentifier + ".csv");
        url = "https://docs.google.com/spreadsheets/d/" + googleIdentifier + "/export?format=csv&gid=0";

        loadFromCache();

        plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, this, 0, 15 * 60 * 20);
    }

    public abstract void onContentsChange();

    public List<String> getLines() {
        return lines;
    }

    @Override
    public void run() {
        if (download()) {
            loadFromCache();
        }
    }

    private void loadFromCache() {
        try {
            if (!cacheFile.isFile()) {
                return;
            }

            lines = Files.readAllLines(cacheFile.toPath());

            plugin.getServer().getScheduler().runTask(plugin, this::onContentsChange);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean download() {
        try{
            URL url = new URL(this.url);
            InputStream in = url.openStream();

            if (!cacheFile.getParentFile().isDirectory() && !cacheFile.getParentFile().mkdirs()) {
                System.err.println("Failed to create parent directory for " + cacheFile.getAbsolutePath());
            }

            Files.copy(in, cacheFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            in.close();
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }


}
