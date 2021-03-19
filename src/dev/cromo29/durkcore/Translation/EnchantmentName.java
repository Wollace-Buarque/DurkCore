package dev.cromo29.durkcore.Translation;

import org.bukkit.enchantments.Enchantment;

public enum EnchantmentName {

    ARROW_DAMAGE("Força"), ARROW_FIRE("Chama"),
    ARROW_INFINITE("Infinidade"), ARROW_KNOCKBACK("Impacto"),
    DAMAGE_ALL("Afiação"), DAMAGE_ARTHROPODS("Ruína dos artrópodes"),
    DAMAGE_UNDEAD("Julgamento"), DEPTH_STRIDER("Passos profundos"),
    DIG_SPEED("Eficiência"), DURABILITY("Durabilidade"),
    FIRE_ASPECT("Aspecto flamejante"), KNOCKBACK("Repulsão"),
    LOOT_BONUS_BLOCKS("Fortuna"), LOOT_BONUS_MOBS("Pilhagem"),
    LUCK("Sorte do mar"), LURE("Isca"),
    OXYGEN("Respiração"), PROTECTION_ENVIRONMENTAL("Proteção"),
    PROTECTION_EXPLOSIONS("Proteção contra explosões"), PROTECTION_FALL("Peso-pena"),
    PROTECTION_FIRE("Proteção contra o fogo"), PROTECTION_PROJECTILE("Proteção contra projéteis"),
    SILK_TOUCH("Toque suave"), THORNS("Espinhos"), WATER_WORKER("Afinidade aquática");

    private String name;

    EnchantmentName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public static EnchantmentName valueOf(Enchantment enchantment) {
        return EnchantmentName.valueOf(enchantment.getName());
    }

    public static Enchantment of(EnchantmentName enchantmentName) {
        return Enchantment.getByName(enchantmentName.name());
    }

    public static Enchantment of(String enchantmentName) {
        Enchantment enchantment = Enchantment.getByName(enchantmentName);

        if (enchantment != null) return enchantment;

        for (EnchantmentName enchantName : values()) {

            if (enchantmentName.equalsIgnoreCase(enchantName.getName())) {
                enchantment = Enchantment.getByName(enchantName.name());
                break;
            }

        }

        return enchantment;
    }

    @Override
    public String toString() {
        return this.name;
    }

}