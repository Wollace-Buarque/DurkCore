package dev.cromo29.durkcore;

import com.google.common.collect.Sets;
import dev.cromo29.durkcore.API.DurkPlugin;
import dev.cromo29.durkcore.Commands.Plugins;
import dev.cromo29.durkcore.Event.PlayerJumpEvent;
import dev.cromo29.durkcore.Inventory.Inv;
import dev.cromo29.durkcore.Util.GetValueFromPlayerChat;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

public class DurkCore extends DurkPlugin {

    public DurkCore() {
        super("<a>DurkCore", "<b>Cromo29");
    }

    public static DurkCore durkCore;
    private Set<UUID> prevPlayersOnGround = Sets.newHashSet();

    @Override
    public void onPreStart() {
        durkCore = this;
    }

    @Override
    public void onStart() {
        log(" <e><m>>---------------------------------------------------------------<"
                , ""
                , " <a>Thanks to <b>Razec <a>for making <f>RCore <7>(This is a edited copy)<a>!"
                , ""
                , " <9>Discord: <f>Cromo29#9556"
                , ""
                , " <e><m>>---------------------------------------------------------------<");

        registerCommand(new Plugins());
        setListener(new GetValueFromPlayerChat());
    }


    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        if (player.getVelocity().getY() > 0.0D) {
            double jumpVelocity = 0.41999998688697815D;
            if (player.hasPotionEffect(PotionEffectType.JUMP)) {
                int level = 0;
                Iterator var6 = player.getActivePotionEffects().iterator();

                while (var6.hasNext()) {
                    PotionEffect potion = (PotionEffect) var6.next();
                    if (potion.getType() == PotionEffectType.JUMP) {
                        level = potion.getAmplifier();
                        break;
                    }
                }

                jumpVelocity += (float) (level + 1) * 0.1F;
            }

            if (e.getPlayer().getLocation().getBlock().getType() != Material.LADDER && this.prevPlayersOnGround.contains(player.getUniqueId()) && !player.isOnGround() && Double.compare(player.getVelocity().getY(), jumpVelocity) == 0) {
                PlayerJumpEvent event = new PlayerJumpEvent(player);
                callEvent(event);
                if (event.isCancelled()) {
                    e.setCancelled(true);
                }
            }
        }

        if (player.isOnGround())
            prevPlayersOnGround.add(player.getUniqueId());
        else
            prevPlayersOnGround.remove(player.getUniqueId());
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onInventoryClick(InventoryClickEvent e) {
        if (e.getInventory().getHolder() instanceof Inv && e.getClickedInventory() != null) {
            Inv inv = (Inv) e.getInventory().getHolder();
            if (e.getClickedInventory().getType() == InventoryType.PLAYER) {

                if (inv.isCancellingPlayerInventoryClick())
                    e.setCancelled(true);

                if (inv.isIgnoringPlayerInventoryClick()) return;
            }

            boolean wasCancelled = e.isCancelled();

            e.setCancelled(inv.isCancellingClick());
            inv.handleClick(e);

            if (!wasCancelled && !e.isCancelled()) e.setCancelled(false);
        }

    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent e) {
        if (e.getInventory().getHolder() instanceof Inv) {
            Inv inv = (Inv) e.getInventory().getHolder();
            inv.handleOpen(e);
        }

    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        if (e.getInventory().getHolder() instanceof Inv) {
            Inv inv = (Inv) e.getInventory().getHolder();
            if (inv.handleClose(e)) {
                Bukkit.getScheduler().runTask(durkCore, () -> {
                    inv.open((Player) e.getPlayer());
                });
            }
        }
    }
}
