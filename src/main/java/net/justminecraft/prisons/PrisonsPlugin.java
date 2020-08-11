package net.justminecraft.prisons;

import net.justminecraft.prisons.commands.SetupPrisonsCommand;
import net.justminecraft.prisons.commands.SpawnCommand;
import net.justminecraft.prisons.commands.WarpCommand;
import org.bukkit.plugin.java.JavaPlugin;

public class PrisonsPlugin extends JavaPlugin {

    private MineManager mineManager;

    @Override
    public void onEnable() {
        mineManager = new MineManager(this);

        enableCommands();
    }

    private void enableCommands() {
        new SetupPrisonsCommand(this);
        new SpawnCommand(this);
        new WarpCommand(this);
    }

    public MineManager getMineManager() {
        return mineManager;
    }
}
