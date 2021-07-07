package dev.devous.vandium.assemble;

import dev.devous.vandium.assemble.events.AssembleBoardCreatedEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AssembleBoard {

	private final List<AssembleBoardEntry> entries = new ArrayList<>();
	private final List<String> identifiers = new ArrayList<>();
	private final UUID uuid;

	private final Assemble assemble;

	public AssembleBoard(Player player, Assemble assemble) {
		this.uuid = player.getUniqueId();
		this.assemble = assemble;
		this.setup(player);
	}

	public Scoreboard getScoreboard() {
		Player player = Bukkit.getServer().getPlayer(getUuid());

		if (this.getAssemble().isHook() || player.getScoreboard() != Bukkit.getServer().getScoreboardManager().getMainScoreboard())
			return player.getScoreboard();

		return Bukkit.getServer().getScoreboardManager().getNewScoreboard();
	}

	public Objective getObjective() {
		Scoreboard scoreboard = this.getScoreboard();

		if (scoreboard.getObjective("Assemble") == null) {
			Objective objective = scoreboard.registerNewObjective("Assemble", "dummy");
			objective.setDisplaySlot(DisplaySlot.SIDEBAR);
			objective.setDisplayName(this.getAssemble().getAdapter().getTitle(Bukkit.getServer().getPlayer(getUuid())));

			return objective;
		}

		return scoreboard.getObjective("Assemble");
	}


	private void setup(Player player) {
		Scoreboard scoreboard = getScoreboard();
		player.setScoreboard(scoreboard);
		this.getObjective();

		AssembleBoardCreatedEvent createdEvent = new AssembleBoardCreatedEvent(this);
		Bukkit.getServer().getPluginManager().callEvent(createdEvent);
	}

	public AssembleBoardEntry getEntryAtPosition(int pos) {
		if (pos >= this.entries.size()) {
			return null;
		} else {
			return this.entries.get(pos);
		}
	}

	public String getUniqueIdentifier(int position) {
		String identifier = getRandomChatColor(position) + ChatColor.WHITE;

		while (this.identifiers.contains(identifier))
			identifier = identifier + getRandomChatColor(position) + ChatColor.WHITE;

		if (identifier.length() > 16)
			return this.getUniqueIdentifier(position);

		this.identifiers.add(identifier);

		return identifier;
	}

	private static String getRandomChatColor(int position) {
		return ChatColor.values()[position].toString();
	}

	public List<AssembleBoardEntry> getEntries() {
		return entries;
	}

	public List<String> getIdentifiers() {
		return identifiers;
	}

	public UUID getUuid() {
		return uuid;
	}

	public Assemble getAssemble() {
		return assemble;
	}

}
