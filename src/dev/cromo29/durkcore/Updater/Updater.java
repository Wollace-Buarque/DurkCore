package dev.cromo29.durkcore.updater;

import dev.cromo29.durkcore.updater.event.UpdaterEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Updater implements Runnable {

    private JavaPlugin plugin;

    public Updater(JavaPlugin plugin) {
        this.plugin = plugin;
        this.plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, this, 0L, 1L);
    }

    @Override
    public void run() {

        for (UpdateType updateType : UpdateType.values()) {

            if (updateType.elapsed()) {
                plugin.getServer().getPluginManager().callEvent(new UpdaterEvent(updateType));
            }
        }
    }
}
