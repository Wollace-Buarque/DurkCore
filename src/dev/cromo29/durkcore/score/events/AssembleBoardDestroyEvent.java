package dev.cromo29.durkcore.score.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class AssembleBoardDestroyEvent extends Event implements Cancellable {

    public static HandlerList handlerList = new HandlerList();

    private Player player;
    private boolean cancelled = false;

    public AssembleBoardDestroyEvent(Player player) {
        this.player = player;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }
}
