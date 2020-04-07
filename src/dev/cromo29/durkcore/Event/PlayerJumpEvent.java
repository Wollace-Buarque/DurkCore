package dev.cromo29.durkcore.Event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerJumpEvent extends Event {
    private Player p;
    private boolean cancel;
    private static HandlerList handlerList = new HandlerList();

    public PlayerJumpEvent(Player p) {
        this.p = p;
    }

    public Player getPlayer() {
        return this.p;
    }

    public boolean isCancelled() {
        return this.cancel;
    }

    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

    public HandlerList getHandlers() {
        return handlerList;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }
}