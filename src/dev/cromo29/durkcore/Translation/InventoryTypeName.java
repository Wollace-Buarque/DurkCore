package dev.cromo29.durkcore.Translation;

import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

public enum InventoryTypeName {

    ANVIL("Reparar e renomear"), BEACON("Sinalizador"),
    BREWING("Suporte de poções"), CHEST("Baú"),
    CRAFTING("Inventário"), CREATIVE("Criativo"),
    DISPENSER("Ejetor"), DROPPER("Liberador"),
    ENCHANTING("Encantar"), ENDER_CHEST("Baú de ender"),
    FURNACE("Fornalha"), HOPPER("Funil"),
    PLAYER("Inventário"), WORKBENCH("Fabricação");

    private String name;

    InventoryTypeName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public static InventoryTypeName valueOf(Inventory inventory) {
        return valueOf(inventory.getType());
    }

    public static InventoryTypeName valueOf(InventoryType inventoryType) {
        return valueOf(inventoryType.name());
    }

    @Override
    public String toString() {
        return this.name;
    }

}
