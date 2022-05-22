package dev.cromo29.durkcore.updater.event;

import dev.cromo29.durkcore.updater.UpdateType;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class UpdaterEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private UpdateType type;

    public UpdaterEvent(UpdateType type) {
        this.type = type;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public UpdateType getType() {
        return type;
    }

    public HandlerList getHandlers() {
        return handlers;
    }
}
