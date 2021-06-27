package dev.devous.vandium.scoreboard.listener;

import dev.devous.vandium.scoreboard.event.ScoreboardCreateEvent;
import dev.devous.vandium.scoreboard.event.ScoreboardDestroyEvent;
import dev.devous.vandium.scoreboard.registry.ScoreboardRegistry;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ScoreboardListener implements Listener {

    private final ScoreboardRegistry registry;

    public ScoreboardListener(ScoreboardRegistry registry) {
        this.registry = registry;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        ScoreboardCreateEvent createEvent = new ScoreboardCreateEvent(e.getPlayer());
        Bukkit.getServer().getPluginManager().callEvent(createEvent);

        if (createEvent.isCancelled()) {
            return;
        }

        registry.register(e.getPlayer());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        ScoreboardDestroyEvent destroyEvent = new ScoreboardDestroyEvent(e.getPlayer());
        Bukkit.getServer().getPluginManager().callEvent(destroyEvent);

        if (destroyEvent.isCancelled()) {
            return;
        }

        registry.unregister(e.getPlayer());
    }

}
