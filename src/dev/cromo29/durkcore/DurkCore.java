package dev.cromo29.durkcore;

import dev.cromo29.durkcore.API.DurkPlugin;
import dev.cromo29.durkcore.Commands.Errors;
import dev.cromo29.durkcore.Commands.Plugins;
import dev.cromo29.durkcore.Entity.DurkPlayer;
import dev.cromo29.durkcore.Events.PlayerChangeBlockEvent;
import dev.cromo29.durkcore.Events.PlayerChangeChunkEvent;
import dev.cromo29.durkcore.Events.PlayerJumpEvent;
import dev.cromo29.durkcore.Events.PlayerOpenChestEvent;
import dev.cromo29.durkcore.Inventory.Inv;
import dev.cromo29.durkcore.Updater.Updater;
import dev.cromo29.durkcore.Util.GetValueFromPlayerChat;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

public class DurkCore extends DurkPlugin {

    public Map<UUID, DurkPlayer.DurkMoving> playersMoving = new HashMap<>();
    private final Set<UUID> prevPlayersOnGround = new HashSet<>();

    public static DurkCore durkCore;

    @Override
    public void onPreStart() {
        durkCore = this;
    }

    @Override
    public void onStart() {
        logs(""
                , " <7>Thanks to <8>Razec <7>for making <8>RCore (This is an edited copy)<7>!"
                , " <7>My discord: <8>Cromo29#9928"
                , "");

        registerCommands(new Plugins(), new Errors());
        setListener(new GetValueFromPlayerChat());

        new Updater(this);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        playersMoving.remove(player.getUniqueId());
        prevPlayersOnGround.remove(player.getUniqueId());
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onMoveEvent(PlayerMoveEvent event) {

        Player player = event.getPlayer();

        if (event.isCancelled()) {
            playersMoving.remove(player.getUniqueId());
            return;
        }

        Location movedFrom = event.getFrom();
        Location movedTo = event.getTo();

        if (movedFrom.getX() != movedTo.getX() || movedFrom.getZ() != movedTo.getZ()) {

            if (playersMoving.containsKey(player.getUniqueId())) {
                DurkPlayer.DurkMoving durkMoving = playersMoving.get(player.getUniqueId());

                durkMoving.setBody(true);

                if (!movedFrom.getDirection().equals(movedTo.getDirection()))
                    durkMoving.setEyes(true);

                durkMoving.setLocation(movedTo);

            } else {
                DurkPlayer.DurkMoving durkMoving = new DurkPlayer.DurkMoving(movedTo);

                durkMoving.setBody(true);
                durkMoving.setEyes(true);

                playersMoving.put(player.getUniqueId(), durkMoving);
            }

        } else if (movedFrom.getX() == movedTo.getX() || movedFrom.getZ() == movedTo.getZ()) {

            if (playersMoving.containsKey(player.getUniqueId())) {
                DurkPlayer.DurkMoving durkMoving = playersMoving.get(player.getUniqueId());

                durkMoving.setBody(false);
                durkMoving.setEyes(!movedFrom.getDirection().equals(movedTo.getDirection()));

                durkMoving.setLocation(movedTo);

            } else {

                if (movedFrom.getDirection().equals(movedTo.getDirection())) {
                    playersMoving.remove(player.getUniqueId());
                } else {

                    DurkPlayer.DurkMoving durkMoving = new DurkPlayer.DurkMoving(movedTo);

                    durkMoving.setBody(false);
                    durkMoving.setEyes(true);

                    playersMoving.put(player.getUniqueId(), durkMoving);
                }
            }
        }

    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onMove(PlayerMoveEvent event) {

        if (event.isCancelled()) return;
        if (!event.getFrom().getWorld().getName().equalsIgnoreCase(event.getTo().getWorld().getName())) return;

        boolean notZero = event.getFrom().distance(event.getTo()) > 0;

        // PlayerChangeBlockEvent
        if (notZero && !event.getFrom().getBlock().equals(event.getTo().getBlock())) {
            PlayerChangeBlockEvent playerChangeBlockEvent = new PlayerChangeBlockEvent(event.getPlayer(), event.getFrom().getBlock(), event.getTo().getBlock(), event.getFrom(), event.getTo());
            callEvent(playerChangeBlockEvent);

            if (playerChangeBlockEvent.isCancelled()) {
                event.setCancelled(true);
                return;
            }
        }
        //


        //PlayerChangeChunkEvent
        if (notZero && !isSameChunk(event)) {
            PlayerChangeChunkEvent playerChangeChunkEvent = new PlayerChangeChunkEvent(event.getPlayer(), event.getFrom().getChunk(), event.getTo().getChunk(), event.getFrom(), event.getTo());
            callEvent(playerChangeChunkEvent);

            if (playerChangeChunkEvent.isCancelled()) {
                event.setCancelled(true);
                return;
            }
        }
        //


        //PlayerJumpEvent
        Player player = event.getPlayer();
        if (notZero && player.getVelocity().getY() > 0) {
            double jumpVelocity = 0.42;

            if (player.hasPotionEffect(PotionEffectType.JUMP)) {
                int level = 0;

                for (PotionEffect potion : player.getActivePotionEffects()) {
                    if (potion.getType() == PotionEffectType.JUMP) {
                        level = potion.getAmplifier();
                        break;
                    }
                }

                jumpVelocity += (level + 1) * 0.1;
            }

            if (player.getLocation().getBlock().getType() != Material.LADDER && prevPlayersOnGround.contains(player.getUniqueId())) {
                if (!player.isOnGround() && Double.compare(player.getVelocity().getY(), jumpVelocity) == 0) {

                    PlayerJumpEvent playerJumpEvent = new PlayerJumpEvent(player, event.getFrom(), event.getTo());
                    callEvent(playerJumpEvent);

                    if (playerJumpEvent.isCancelled()) event.setCancelled(true);
                }
            }
        }

        if (notZero) {
            if (player.isOnGround()) prevPlayersOnGround.add(player.getUniqueId());
            else prevPlayersOnGround.remove(player.getUniqueId());
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onInteract(PlayerInteractEvent event) {

        if (event.isCancelled()) return;

        if (event.getClickedBlock() != null
                && event.getAction() == Action.RIGHT_CLICK_BLOCK
                && event.getClickedBlock().getType() == Material.CHEST
                && event.getClickedBlock().getType() == Material.TRAPPED_CHEST) {
            PlayerOpenChestEvent playerOpenChestEvent = new PlayerOpenChestEvent(event.getPlayer(), (Chest) event.getClickedBlock().getState(), event.getClickedBlock());
            callEvent(playerOpenChestEvent);

            if (playerOpenChestEvent.isCancelled()) event.setCancelled(true);
        }
    }

    private boolean isSameChunk(PlayerMoveEvent event) {
        Location one = event.getFrom();
        Location two = event.getTo();

        if (one.getBlockX() >> 4 != two.getBlockX() >> 4) return false;
        if (one.getBlockZ() >> 4 != two.getBlockZ() >> 4) return false;

        return one.getWorld() == two.getWorld();
    }


    @EventHandler(priority = EventPriority.LOW)
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getInventory().getHolder() instanceof Inv && event.getClickedInventory() != null) {
            Inv inv = (Inv) event.getInventory().getHolder();

            if (event.getClickedInventory().getType() == InventoryType.PLAYER) {

                if (inv.isCancellingPlayerInventoryClick()) event.setCancelled(true);

                if (inv.isIgnoringPlayerInventoryClick()) return;
            }

            boolean wasCancelled = event.isCancelled();

            event.setCancelled(inv.isCancellingClick());
            inv.handleClick(event);

            if (!wasCancelled && !event.isCancelled()) event.setCancelled(false);
        }
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent event) {
        if (event.getInventory().getHolder() instanceof Inv) {
            Inv inv = (Inv) event.getInventory().getHolder();

            inv.handleOpen(event);
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {

        if (event.getInventory().getHolder() instanceof Inv) {
            Inv inv = (Inv) event.getInventory().getHolder();

            if (inv.handleClose(event)) {
                getServer().getScheduler().runTask(durkCore, () -> inv.open((Player) event.getPlayer()));
            }
        }
    }
}
