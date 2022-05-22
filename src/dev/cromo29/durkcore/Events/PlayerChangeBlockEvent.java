package dev.cromo29.durkcore.events;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerChangeBlockEvent extends Event implements Cancellable {

    private Player player;
    private Block from;
    private Block to;
    private Location playerFromLocation;
    private Location playerToLocation;

    public PlayerChangeBlockEvent(Player player, Block from, Block to, Location playerFromLocation, Location playerToLocation) {
        this.player = player;
        this.from = from;
        this.to = to;
        this.playerFromLocation = playerFromLocation;
        this.playerToLocation = playerToLocation;
    }

    private boolean cancelled = false;
    @Override public boolean isCancelled() { return cancelled; }
    @Override public void setCancelled(boolean cancelled) { this.cancelled = cancelled; }

    public Player getPlayer() {return player;}
    public Block getFrom() {return from;}
    public Block getTo() {return to;}
    public Location getFromLocation() { return playerFromLocation;}
    public Location getToLocation() { return playerToLocation; }

    private static HandlerList handlerList = new HandlerList();
    public HandlerList getHandlers() {return handlerList;}
    public static HandlerList getHandlerList()  {return handlerList;}
}
