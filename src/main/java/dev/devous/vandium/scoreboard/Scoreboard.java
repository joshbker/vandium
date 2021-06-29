package dev.devous.vandium.scoreboard;

import dev.devous.vandium.scoreboard.event.ScoreboardCreatedEvent;
import dev.devous.vandium.scoreboard.registry.ScoreboardRegistry;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static dev.devous.vandium.color.Color.color;

public class Scoreboard {

    private final List<ScoreboardEntry> entries = new ArrayList<>();
    private final List<String> identifiers = new ArrayList<>();
    private final UUID uniqueId;

    private final ScoreboardRegistry registry;

    public Scoreboard(Player player, ScoreboardRegistry registry) {
        this.uniqueId = player.getUniqueId();
        this.registry = registry;
        this.setup(player);
    }

    public org.bukkit.scoreboard.Scoreboard getScoreboard() {
        Player player = Bukkit.getServer().getPlayer(uniqueId);

        if (registry.isHook() || player.getScoreboard() != Bukkit.getServer().getScoreboardManager().getMainScoreboard())
            return player.getScoreboard();

        return Bukkit.getServer().getScoreboardManager().getNewScoreboard();
    }

    public Objective getObjective() {
        org.bukkit.scoreboard.Scoreboard scoreboard = getScoreboard();

        if (scoreboard.getObjective("Vandium") == null) {
            Objective objective = scoreboard.registerNewObjective("Vandium", "dummy");
            objective.setDisplaySlot(DisplaySlot.SIDEBAR);
            objective.setDisplayName(color(registry.getAdapter().getTitle(Bukkit.getServer().getPlayer(uniqueId))));

            return objective;
        }

        return scoreboard.getObjective("Vandium");
    }

    private void setup(Player player) {
        org.bukkit.scoreboard.Scoreboard scoreboard = getScoreboard();
        player.setScoreboard(scoreboard);
        getObjective();

        ScoreboardCreatedEvent createdEvent = new ScoreboardCreatedEvent(this);
        Bukkit.getServer().getPluginManager().callEvent(createdEvent);
    }

    public ScoreboardEntry getEntryAtPosition(int pos) {
        if (pos >= entries.size()) {
            return null;
        } else {
            return entries.get(pos);
        }
    }

    public String getUniqueIdentifier(int position) {
        String identifier = getRandomChatColor(position) + ChatColor.WHITE;

        while (identifiers.contains(identifier))
            identifier = identifier + getRandomChatColor(position) + ChatColor.WHITE;

        if (identifier.length() > 16)
            return getUniqueIdentifier(position);

        identifiers.add(identifier);

        return identifier;
    }

    private static String getRandomChatColor(int position) {
        return ChatColor.values()[position].toString();
    }

    public List<ScoreboardEntry> getEntries() {
        return entries;
    }

    public void addEntry(ScoreboardEntry entry) {
        entries.add(entry);
    }

    public void removeEntry(ScoreboardEntry entry) {
        entries.remove(entry);
    }

    public void addIdentifier(String string) {
        identifiers.add(string);
    }

    public void removeIdentifier(String string) {
        identifiers.remove(string);
    }

}
