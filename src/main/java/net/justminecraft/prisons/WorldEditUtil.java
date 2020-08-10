package net.justminecraft.prisons;

import com.sk89q.worldedit.*;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.function.pattern.Pattern;
import com.sk89q.worldedit.function.pattern.Patterns;
import com.sk89q.worldedit.regions.selector.CuboidRegionSelector;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldedit.world.registry.WorldData;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public class WorldEditUtil {

    public static void clearArea(Chunk chunk, int radius) {
        System.out.println("Clearing radius of " + radius + " around " + chunk);

        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                clearChunk(chunk.getWorld().getChunkAt(chunk.getX() + x, chunk.getZ() + z));
            }
        }
    }

    private static void clearChunk(Chunk chunk) {
        World world = new BukkitWorld(chunk.getWorld());

        try {
            EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(world , -1);
            editSession.setFastMode(true);

            Pattern air = new Pattern() {
                private BaseBlock baseBlock = new BaseBlock(0);

                @Override
                public BaseBlock apply(Vector vector) {
                    return baseBlock;
                }
            };

            editSession.setBlocks(new CuboidRegionSelector(world, new Vector(chunk.getX() << 4, 0, chunk.getZ() << 4), new Vector(chunk.getX() << 4 | 15, 255, chunk.getZ() << 4 | 15)).getRegion(), Patterns.wrap(air));
        } catch (MaxChangedBlocksException | IncompleteRegionException e) {
            e.printStackTrace();
        }

        for (Entity entity : chunk.getEntities()) {
            if (!(entity instanceof Player)) {
                entity.remove();
            }
        }
    }

    public static void pasteSchematic(String file, Location location) {
        System.out.println("Pasting schematic " + file + " at " + location);

        World world = new BukkitWorld(location.getWorld());

        EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(world , -1);
        editSession.setFastMode(true);

        ClipboardFormat schematicFormat = ClipboardFormat.findByAlias("schematic");

        Objects.requireNonNull(schematicFormat);

        try (InputStream inputStream = WorldEditUtil.class.getResourceAsStream("/schematics/" + file)) {
            BufferedInputStream buffer = new BufferedInputStream(inputStream);
            ClipboardReader reader = schematicFormat.getReader(buffer);
            WorldData worldData = world.getWorldData();
            Clipboard clipboard = reader.read(worldData);
            ClipboardHolder holder = new ClipboardHolder(clipboard, worldData);

            Vector to = new Vector(location.getBlockX(), location.getBlockY(), location.getBlockZ());
            Operation operation = holder.createPaste(editSession, editSession.getWorld().getWorldData()).to(to).ignoreAirBlocks(false).build();
            Operations.completeLegacy(operation);

            Bukkit.getWorlds().get(0).setSpawnLocation(0, 64, 0);
        } catch (IOException | MaxChangedBlocksException e) {
            throw new RuntimeException(e);
        }
    }

}
