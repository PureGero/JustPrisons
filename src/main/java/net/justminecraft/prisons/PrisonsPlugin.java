package net.justminecraft.prisons;

import net.justminecraft.prisons.commands.SpawnCommand;
import org.bukkit.plugin.java.JavaPlugin;

public class PrisonsPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        enableCommands();
    }

    private void enableCommands() {
        new SpawnCommand(this);
    }

}
