package dev.devous.vandium.scoreboard.event;

import dev.devous.vandium.scoreboard.Scoreboard;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ScoreboardCreatedEvent extends Event {

    private final Scoreboard scoreboard;

    public ScoreboardCreatedEvent(Scoreboard scoreboard) {
        this.scoreboard = scoreboard;
    }

    public Scoreboard getScoreboard() {
        return scoreboard;
    }

    @Override
    public HandlerList getHandlers() {
        return null;
    }

}
