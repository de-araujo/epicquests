package dev.ahowe.quests.quests.Listeners;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.session.ClipboardHolder;
import dev.ahowe.quests.quests.Quests;
import org.bukkit.Chunk;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkPopulateEvent;
import java.io.IOException;
import java.io.InputStream;
import static dev.ahowe.quests.quests.Utils.EditSessionUtils.getSession;

public class ChunkGenerated implements Listener {
	private final Quests plugin;

	public ChunkGenerated(Quests quests) {
		this.plugin = quests;
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onChunkPopulate(ChunkPopulateEvent ev) {
		plugin.getLogger().info("Server called the Chuk Populate event!");

		Chunk chunk = ev.getChunk();
		InputStream schematic = plugin.getResource("schematics/TEST.schem");
		ClipboardFormat format = ClipboardFormats.findByAlias("schematic");

		if(format == null) {
			plugin.getLogger().info("Format null");
			return;
		}

		try {
			ClipboardReader reader = format.getReader(schematic);
			Clipboard clipboard = reader.read();
			EditSession es = getSession(chunk.getWorld());

			Operation paste = new ClipboardHolder(clipboard)
					.createPaste(es)
					.to(BlockVector3.at(chunk.getX(), 100, chunk.getZ()))
					.build();

			Operations.complete(paste);
		} catch (IOException | WorldEditException e) {
			e.printStackTrace();
		}
	}
}
