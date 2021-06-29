package dev.devous.vandium.scoreboard.listener;

import dev.devous.vandium.scoreboard.event.ScoreboardCreateEvent;
import dev.devous.vandium.scoreboard.event.ScoreboardDestroyEvent;
import dev.devous.vandium.scoreboard.registry.ScoreboardRegistry;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class ScoreboardListener implements Listener {

    private final ScoreboardRegistry registry;
    private final JavaPlugin plugin;

    public ScoreboardListener(ScoreboardRegistry registry, JavaPlugin plugin) {
        this.registry = registry;
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        ScoreboardCreateEvent createEvent = new ScoreboardCreateEvent(e.getPlayer());
        plugin.getServer().getPluginManager().callEvent(createEvent);
        System.out.println(plugin.getServer());
        System.out.println(createEvent);

        if (createEvent.isCancelled()) {
            return;
        }

        registry.register(e.getPlayer());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        ScoreboardDestroyEvent destroyEvent = new ScoreboardDestroyEvent(e.getPlayer());
        plugin.getServer().getPluginManager().callEvent(destroyEvent);

        if (destroyEvent.isCancelled()) {
            return;
        }

        registry.unregister(e.getPlayer());
    }

}
