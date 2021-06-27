package dev.devous.vandium.scoreboard.registry;

import dev.devous.vandium.scoreboard.Scoreboard;
import dev.devous.vandium.scoreboard.adapter.ScoreboardAdapter;
import dev.devous.vandium.scoreboard.event.ScoreboardCreateEvent;
import dev.devous.vandium.scoreboard.listener.ScoreboardListener;
import dev.devous.vandium.threading.Thread;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class ScoreboardRegistry {

    private final Map<UUID, Scoreboard> scoreboards;

    private final ScoreboardAdapter adapter;
    private final Thread thread;

    private boolean hook;

    public ScoreboardRegistry(JavaPlugin plugin, ScoreboardAdapter adapter) {
        if (plugin == null) {
            throw new RuntimeException("Plugin instance required for scoreboards.");
        }

        this.scoreboards = new HashMap<>();

        this.adapter = adapter;

        plugin.getServer().getPluginManager().registerEvents(new ScoreboardListener(this), plugin);

        for (Player player : plugin.getServer().getOnlinePlayers()) {
            ScoreboardCreateEvent createEvent = new ScoreboardCreateEvent(player);
            Bukkit.getServer().getPluginManager().callEvent(createEvent);

            if (createEvent.isCancelled()) {
                continue;
            }

            register(player);
        }

        this.thread = new Thread(UUID.randomUUID(), "Scoreboard");
        this.thread.runTaskTimer(() -> {

        }, 50L, TimeUnit.MILLISECONDS);

        this.hook = false;
    }

    public void register(Player player) {
        scoreboards.putIfAbsent(player.getUniqueId(), new Scoreboard(player, this));
    }

    public void unregister(Player player) {
        scoreboards.remove(player.getUniqueId());
    }

    public Map<UUID, Scoreboard> getScoreboards() {
        return scoreboards;
    }

    public ScoreboardAdapter getAdapter() {
        return adapter;
    }

    public Thread getThread() {
        return thread;
    }

    public boolean isHook() {
        return hook;
    }

    public void setHook(boolean hook) {
        this.hook = hook;
    }
}
