package dev.devous.vandium.scoreboard.registry;

import dev.devous.vandium.scoreboard.Scoreboard;
import dev.devous.vandium.scoreboard.ScoreboardEntry;
import dev.devous.vandium.scoreboard.adapter.ScoreboardAdapter;
import dev.devous.vandium.scoreboard.event.ScoreboardCreateEvent;
import dev.devous.vandium.scoreboard.listener.ScoreboardListener;
import dev.devous.vandium.threading.Thread;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Objective;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static dev.devous.vandium.color.Color.color;
import static dev.devous.vandium.color.Color.colorList;

public class ScoreboardRegistry {

    private final Map<UUID, Scoreboard> scoreboards;
    private final ScoreboardAdapter adapter;
    private final boolean hook;

    public ScoreboardRegistry(JavaPlugin plugin, ScoreboardAdapter adapter) {
        if (plugin == null) {
            throw new RuntimeException("Plugin instance required for scoreboards.");
        }

        this.scoreboards = new HashMap<>();

        this.adapter = adapter;

        plugin.getServer().getPluginManager().registerEvents(new ScoreboardListener(this, plugin), plugin);

        for (Player player : plugin.getServer().getOnlinePlayers()) {
            ScoreboardCreateEvent createEvent = new ScoreboardCreateEvent(player);
            Bukkit.getServer().getPluginManager().callEvent(createEvent);

            if (createEvent.isCancelled()) {
                continue;
            }

            register(player);
        }

        Thread thread = new Thread(UUID.randomUUID(), "scoreboard");
        thread.runTaskTimer(() -> tick(plugin), 50L, TimeUnit.MILLISECONDS);

        this.hook = false;
    }

    public void register(Player player) {
        scoreboards.putIfAbsent(player.getUniqueId(), new Scoreboard(player, this));
    }

    public void unregister(Player player) {
        scoreboards.remove(player.getUniqueId());
    }

    public ScoreboardAdapter getAdapter() {
        return adapter;
    }

    public boolean isHook() {
        return hook;
    }

    private void tick(JavaPlugin plugin) {
        for (Player player : plugin.getServer().getOnlinePlayers()) {
            Scoreboard board = scoreboards.getOrDefault(player.getUniqueId(), null);

            if (board == null) {
                continue;
            }

            org.bukkit.scoreboard.Scoreboard scoreboard = board.getScoreboard();
            Objective objective = board.getObjective();

            if (scoreboard == null || objective == null) {
                continue;
            }

            String title = color(adapter.getTitle(player));

            if (!objective.getDisplayName().equals(title)) {
                objective.setDisplayName(title);
            }

            List<String> newLines = colorList(adapter.getLines(player));

            if (newLines.isEmpty()) {
                board.getEntries().forEach(ScoreboardEntry::remove);
                board.getEntries().clear();

            } else {
                if (newLines.size() > 15) {
                    newLines = newLines.subList(0, 15);
                }

                if (board.getEntries().size() > newLines.size()) {
                    for (int i = newLines.size(); i < board.getEntries().size(); i++) {
                        ScoreboardEntry entry = board.getEntryAtPosition(i);

                        if (entry != null) {
                            entry.remove();
                        }
                    }
                }

                int cache = 15;

                for (int i = 0; i < newLines.size(); i++) {
                    ScoreboardEntry entry = board.getEntryAtPosition(i);

                    String line = newLines.get(i);

                    if (entry == null) {
                        entry = new ScoreboardEntry(board, line, i);
                    }

                    entry.setText(line);
                    entry.setup();
                    entry.send(cache--);
                }
            }

            if (player.getScoreboard() != scoreboard && !hook) {
                player.setScoreboard(scoreboard);
            }
        }
    }
}
