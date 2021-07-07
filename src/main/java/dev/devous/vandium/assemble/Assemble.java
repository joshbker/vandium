package dev.devous.vandium.assemble;


import dev.devous.vandium.assemble.events.AssembleBoardCreateEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class Assemble {

	private final JavaPlugin plugin;
	private final AssembleAdapter adapter;
	private final Map<UUID, AssembleBoard> boards;
	private AssembleThread thread;
	private AssembleListener listeners;
	private final long ticks = 2;
	private final boolean hook = false;
	private final AssembleStyle assembleStyle = AssembleStyle.MODERN;
	private final boolean debugMode = true;

	public Assemble(JavaPlugin plugin, final AssembleAdapter adapter) {
		if (plugin == null) {
			throw new RuntimeException("Assemble can not be instantiated without a plugin instance!");
		}

		this.plugin = plugin;
		this.adapter = adapter;
		this.boards = new ConcurrentHashMap<>();

		this.setup();
	}

	public void setup() {
		this.listeners = new AssembleListener(this);
		this.plugin.getServer().getPluginManager().registerEvents(listeners, this.plugin);

		if (this.thread != null) {
			this.thread.stop();
			this.thread = null;
		}

		for (Player player : Bukkit.getServer().getOnlinePlayers()) {
			AssembleBoardCreateEvent createEvent = new AssembleBoardCreateEvent(player);

			Bukkit.getServer().getPluginManager().callEvent(createEvent);
			if (createEvent.isCancelled())
				return;

			this.getBoards().putIfAbsent(player.getUniqueId(), new AssembleBoard(player, this));
		}

		this.thread = new AssembleThread(this);
	}

	public void cleanup() {
		if (this.thread != null) {
			this.thread.stop();
			this.thread = null;
		}

		if (this.listeners != null) {
			HandlerList.unregisterAll(this.listeners);
			this.listeners = null;
		}

		for (UUID uuid : this.getBoards().keySet()) {
			Player player = Bukkit.getServer().getPlayer(uuid);

			if (player == null || !player.isOnline()) {
				continue;
			}

			this.getBoards().remove(uuid);
			player.setScoreboard(Bukkit.getServer().getScoreboardManager().getNewScoreboard());
		}
	}

	public JavaPlugin getPlugin() {
		return plugin;
	}

	public AssembleAdapter getAdapter() {
		return adapter;
	}

	public Map<UUID, AssembleBoard> getBoards() {
		return boards;
	}

	public long getTicks() {
		return ticks;
	}

	public boolean isHook() {
		return hook;
	}

	public AssembleStyle getAssembleStyle() {
		return assembleStyle;
	}

}
