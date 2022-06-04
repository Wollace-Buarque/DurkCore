package dev.cromo29.durkcore.hologram;

import com.google.common.collect.ImmutableList;
import dev.cromo29.durkcore.util.TXT;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class Hologram implements Listener {

    private final JavaPlugin plugin;
    private Location location;
    private final List<HologramLine> hologramLines;
    private String metadata;

    private double yToDecrease;
    private boolean removeOnDisable;

    public Hologram(JavaPlugin plugin, Location location, String metadata) {
        this.plugin = plugin;
        this.location = location;
        this.metadata = metadata;
        this.hologramLines = new ArrayList<>();
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }

    public List<HologramLine> getHologramLines() {
        return ImmutableList.copyOf(hologramLines);
    }

    public String getMetadata() {
        return metadata;
    }

    public boolean isRemoveOnDisable() {
        return removeOnDisable;
    }

    public void setRemoveOnDisable(boolean removeOnDisable) {
        this.removeOnDisable = removeOnDisable;
    }

    public Hologram addLine(String text) {
        hologramLines.add(new HologramLine(text, metadata, location.clone().add(0, yToDecrease, 0)));
        yToDecrease -= 0.25;
        return this;
    }

    public Hologram removeLine(String text) {

        for (int index = 0; index < hologramLines.size(); index++) {
            HologramLine hologramLine = hologramLines.get(index);

            if (hologramLine.getText().equals(TXT.parse(text))) {
                hologramLine.getArmorStand().remove();

                hologramLines.remove(hologramLine);
            }
        }

        return this;
    }

    public Hologram clear() {

        for (ArmorStand entitiesByClass : location.getWorld().getEntitiesByClass(ArmorStand.class)) {

            if (entitiesByClass.hasMetadata(metadata)) entitiesByClass.remove();
        }

        hologramLines.clear();
        return this;
    }

    public Hologram changeMetadata(String metadata) {
        this.metadata = metadata;

        for (int index = 0; index < hologramLines.size(); index++) {
            HologramLine hologramLine = hologramLines.get(index);

            hologramLine.getArmorStand().setMetadata(metadata, new FixedMetadataValue(plugin, metadata));
        }

        return this;
    }

    public void setup() {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    private void onManiple(PlayerArmorStandManipulateEvent event) {

        if (event.getRightClicked().hasMetadata(metadata)) event.setCancelled(true);
    }

    @EventHandler
    private void onDisable(PluginDisableEvent event) {

        if (!event.isAsynchronous() && removeOnDisable) clear();
    }
}
