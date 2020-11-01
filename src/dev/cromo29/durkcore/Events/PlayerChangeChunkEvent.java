package dev.cromo29.durkcore.Events;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerChangeChunkEvent extends Event implements Cancellable {

    private Player player;
    private Chunk from;
    private Chunk to;
    private Location playerFromLocation;
    private Location playerToLocation;

    public PlayerChangeChunkEvent(Player player, Chunk from, Chunk to, Location playerFromLocation, Location playerToLocation) {
        this.player = player;
        this.from = from;
        this.to = to;
        this.playerFromLocation = playerFromLocation;
        this.playerToLocation = playerToLocation;
    }

    private boolean cancelled = false;

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public Player getPlayer() {
        return player;
    }

    public Chunk getFrom() {
        return from;
    }

    public Chunk getTo() {
        return to;
    }

    public Location getPlayerFromLocation() {
        return playerFromLocation;
    }

    public Location getToLocation() {
        return playerToLocation;
    }

    private static HandlerList handlerList = new HandlerList();

    public HandlerList getHandlers() {
        return handlerList;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }
}