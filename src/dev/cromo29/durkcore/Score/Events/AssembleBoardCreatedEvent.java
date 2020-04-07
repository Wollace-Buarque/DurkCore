package dev.cromo29.durkcore.Score.Events;

import dev.cromo29.durkcore.Score.AssembleBoard;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class AssembleBoardCreatedEvent extends Event {

    public static HandlerList handlerList = new HandlerList();
    private boolean cancelled = false;
    private AssembleBoard board;

    public AssembleBoardCreatedEvent(AssembleBoard board) {
        this.board = board;
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }
}
