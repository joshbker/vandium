package dev.devous.vandium.assemble.events;

import dev.devous.vandium.assemble.AssembleBoard;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class AssembleBoardCreatedEvent extends Event {

    public static HandlerList handlerList = new HandlerList();

    private final AssembleBoard board;

    public AssembleBoardCreatedEvent(AssembleBoard board) {
        this.board = board;
    }

    public AssembleBoard getBoard() {
        return board;
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }

}
