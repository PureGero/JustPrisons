package net.justminecraft.prisons;

import net.md_5.bungee.api.chat.TranslatableComponent;
import org.bukkit.command.CommandSender;
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

    private String format(String key, Object[] args) {
        if (messages.containsKey(key)) {
            // We have a translation for that!
            return String.format(messages.get(key), args);
        } else if (this != getDefault()) {
            // Fallback to en_us
            return getDefault().format(key, args);
        } else {
            // We don't have a translation for that in en_us
            return null;
        }
    }

    public static String formatMessage(CommandSender sender, String key, Object... args) {
        String msg = getTranslate(sender).format(key, args);
        return msg == null ? key : msg;
    }

    public static void sendMessage(CommandSender sender, String key, Object... args) {
        if (sender == null) return;

        String msg = getTranslate(sender).format(key, args);

        if (msg == null) {
            // Assume it's a mojang translation
            TranslatableComponent component = new TranslatableComponent(key);
            for (Object arg : args) {
                component.addWith(arg.toString());
            }
            if (sender instanceof Player) {
                ((Player) sender).spigot().sendMessage(component);
            } else {
                sender.sendMessage(key);
            }
            return;
        }

        sender.sendMessage(msg);
    }

    private static Translate getTranslate(CommandSender sender) {
        // TODO Translate chat messages based on locale
        return getDefault();
    }

    private static Translate getDefault() {
        if (en_us == null) {
            en_us = new Translate("en_us.properties");
        }

        return en_us;
    }
}
