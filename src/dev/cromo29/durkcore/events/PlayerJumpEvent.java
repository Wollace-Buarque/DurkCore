package dev.cromo29.durkcore.events;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerJumpEvent extends Event {

    private Player player;
    private Location from;
    private Location to;
    private boolean cancel;

    public PlayerJumpEvent(Player player, Location from, Location to) { this.player = player; this.from = from; this.to = to; }

    public Player getPlayer() {return player;}
    public Location getFrom() { return from; }
    public Location getTo() { return to; }

    public boolean isCancelled() {
        return cancel;
    }
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

    private static HandlerList handlerList = new HandlerList();
    public HandlerList getHandlers() { return handlerList; }
    public static HandlerList getHandlerList()  { return handlerList; }

}