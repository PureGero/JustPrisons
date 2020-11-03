package net.justminecraft.prisons;

import net.md_5.bungee.api.chat.TranslatableComponent;
import org.bukkit.entity.Player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

public class Translate {
    private static Translate en_us = null;

    private HashMap<String, String> messages = new HashMap<>();

    private Translate(String file) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(PrisonsPlugin.getPlugin().getResource(file)))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.indexOf('=') > 0) {
                    String key = line.substring(0, line.indexOf('='));
                    String value = line.substring(line.indexOf('=') + 1);
                    messages.put(key, value);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void send(Player player, String key, Object[] args) {
        if (messages.containsKey(key)) {
            // We have a translation for that!
            player.sendMessage(String.format(messages.get(key), args));
        } else if (this != getDefault()) {
            // Fallback to en_us
            getDefault().send(player, key, args);
        } else {
            // Assume it's a mojang translation
            TranslatableComponent component = new TranslatableComponent(key);
            for (Object arg : args) {
                component.addWith(arg.toString());
            }
            player.spigot().sendMessage(component);
        }
    }

    public static void sendMessage(Player player, String key, Object... args) {
        // TODO Translate chat messages based on locale
        getDefault().send(player, key, args);
    }

    private static Translate getDefault() {
        if (en_us == null) {
            en_us = new Translate("en_us.properties");
        }

        return en_us;
    }
}
