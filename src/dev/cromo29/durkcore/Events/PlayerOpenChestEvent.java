package dev.cromo29.durkcore.Events;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.Inventory;

public class PlayerOpenChestEvent extends Event implements Cancellable {

    private Player player;
    private Chest chest;
    private Block block;

    public PlayerOpenChestEvent(Player player, Chest chest, Block block) {
        this.player = player;
        this.chest = chest;
        this.block = block;
    }

    private boolean cancelled = false;
    @Override public boolean isCancelled() { return cancelled; }
    @Override public void setCancelled(boolean cancelled) { this.cancelled = cancelled; }

    public Player getPlayer() { return player; }
    public Chest getChest() { return chest; }
    public Block getBlock() { return block; }
    public Inventory getChestInventory() { return chest.getBlockInventory(); }
    public Location getChestLocation() { return chest.getLocation(); }

    private void updateChest() { if (chest != null) chest.update(); }

    private static HandlerList handlerList = new HandlerList();
    public HandlerList getHandlers() {return handlerList;}
    public static HandlerList getHandlerList()  {return handlerList;}
}
