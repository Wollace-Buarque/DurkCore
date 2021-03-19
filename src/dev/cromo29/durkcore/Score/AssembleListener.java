package dev.cromo29.durkcore.Score;

import dev.cromo29.durkcore.Score.Events.AssembleBoardCreateEvent;
import dev.cromo29.durkcore.Score.Events.AssembleBoardDestroyEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.PluginDisableEvent;

public class AssembleListener implements Listener {

    private Assemble assemble;

    public AssembleListener(Assemble assemble) {
        this.assemble = assemble;
        Bukkit.getScheduler().runTaskLater(assemble.plugin, () ->

                Bukkit.getOnlinePlayers().forEach(player -> {
                    AssembleBoardCreateEvent createEvent = new AssembleBoardCreateEvent(player);

                    Bukkit.getPluginManager().callEvent(createEvent);

                    if (createEvent.isCancelled()) return;

                    if (!assemble.boards.containsKey(player.getUniqueId()) && assemble.shouldShowForPlayerOnJoin)
                        assemble.boards.put(player.getUniqueId(), new AssembleBoard(player, assemble));

                }), 3 * 20);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (assemble.shouldShowForPlayerOnJoin) {
            AssembleBoardCreateEvent createEvent = new AssembleBoardCreateEvent(event.getPlayer());

            Bukkit.getPluginManager().callEvent(createEvent);
            if (createEvent.isCancelled())
                return;

            assemble.boards.put(event.getPlayer().getUniqueId(), new AssembleBoard(event.getPlayer(), assemble));
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        AssembleBoardDestroyEvent destroyEvent = new AssembleBoardDestroyEvent(event.getPlayer());

        Bukkit.getPluginManager().callEvent(destroyEvent);
        if (destroyEvent.isCancelled())
            return;

        assemble.boards.remove(event.getPlayer().getUniqueId());
        event.getPlayer().setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
    }

    //TODO see how we can make this better
    @EventHandler
    public void onPluginDisable(PluginDisableEvent event) {
        if (event.getPlugin() == assemble.plugin) assemble.cleanup();
    }

}
