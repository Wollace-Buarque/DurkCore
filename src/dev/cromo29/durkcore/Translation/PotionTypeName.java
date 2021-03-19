package dev.cromo29.durkcore.Translation;

import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public enum PotionTypeName {

    ABSORPTION("Absorção"), BLINDNESS("Cegueira"),
    CONFUSION("Náusea"), DAMAGE_RESISTANCE("Resistência"),
    FAST_DIGGING("Pressa"), FIRE_RESISTANCE("Resistência ao fogo"),
    GLOWING("Brilho"), HARM("Dano instantâneo"),
    HEAL("Vida instantânea"), HEALTH_BOOST("Vida extra"),
    HUNGER("Fome"), INCREASE_DAMAGE("Força"),
    INVISIBILITY("Invisibilidade"), NIGHT_VISION("Visão noturna"),
    POISON("Veneno"), REGENERATION("Regeneração"),
    SATURATION("Saturação"), SLOW("Lentidão"),
    SLOW_DIGGING("Cansaço"), SPEED("Velocidade"),
    WATER_BREATHING("Respiração aquática"),
    WEAKNESS("Fraqueza"), WITHER("Decomposição");

    private String name;

    PotionTypeName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public static PotionTypeName valueOf(PotionEffect potionEffect) {
        return valueOf(potionEffect.getType());
    }

    public static PotionTypeName valueOf(PotionEffectType potionEffect) {
        return valueOf(potionEffect.getName());
    }

    public static PotionEffectType of(PotionTypeName potionTypeName) {
        return PotionEffectType.getByName(potionTypeName.name());
    }

    public static PotionEffectType of(String potionName) {
        PotionEffectType potionEffectType = PotionEffectType.getByName(potionName);

        if (potionEffectType != null) return potionEffectType;

        for (PotionTypeName potionTypeName : values()) {

            if (potionName.equalsIgnoreCase(potionTypeName.getName())) {
                potionEffectType = PotionEffectType.getByName(potionTypeName.name());
                break;
            }

        }

        return potionEffectType;
    }

    @Override
    public String toString() {
        return this.name;
    }

}
