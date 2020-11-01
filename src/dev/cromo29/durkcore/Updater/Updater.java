package dev.cromo29.durkcore.Updater;

import dev.cromo29.durkcore.Updater.event.UpdaterEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Updater implements Runnable {

    private JavaPlugin plugin;

    public Updater(JavaPlugin plugin) {
        this.plugin = plugin;
        this.plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, this, 0L, 1L);
    }

    @Override
    public void run() {
        UpdateType[] types;
        int typesAmount = (types = UpdateType.class.getEnumConstants()).length;

        for (int loop = 0; loop < typesAmount; ++loop) {
            UpdateType type = types[loop];

            if (type.elapsed()) plugin.getServer().getPluginManager().callEvent(new UpdaterEvent(type));
        }
    }
}
