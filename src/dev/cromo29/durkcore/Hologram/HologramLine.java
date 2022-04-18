package dev.cromo29.durkcore.Hologram;

import dev.cromo29.durkcore.DurkCore;
import dev.cromo29.durkcore.Util.TXT;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.metadata.FixedMetadataValue;

public class HologramLine {

    private String text;
    private final ArmorStand armorStand;

    public HologramLine(String text, String metadata, Location location) {
        this.text = TXT.parse(text);

        armorStand = location.getWorld().spawn(location, ArmorStand.class);

        armorStand.setCustomName(this.text);
        armorStand.setCustomNameVisible(true);

        armorStand.setGravity(false);
        armorStand.setArms(false);

        armorStand.setVisible(false);
        armorStand.setMetadata(metadata, new FixedMetadataValue(DurkCore.durkCore, metadata));
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = TXT.parse(text);
        armorStand.setCustomName(this.text);
    }

    public ArmorStand getArmorStand() {
        return armorStand;
    }
}
