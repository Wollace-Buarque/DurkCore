package dev.cromo29.durkcore.util;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public enum PickaxeMineableBlock {

    STONE_BUTTON(true, true, true, true, true),
    SMOOTH_BRICK(true, true, true, true, true),
    NETHERRACK(true, true, true, true, true),
    BED(true, true, true, true, true),
    IRON_PLATE(true, true, true, true, true),
    GOLD_PLATE(true, true, true, true, true),
    STONE_PLATE(true, true, true, true, true),
    DETECTOR_RAIL(true, true, true, true, true),
    POWERED_RAIL(true, true, true, true, true),
    ACTIVATOR_RAIL(true, true, true, true, true),
    RAILS(true, true, true, true, true),
    RED_SANDSTONE_STAIRS(true, true, true, true, true),
    RED_SANDSTONE(true, true, true, true, true),
    SANDSTONE_STAIRS(true, true, true, true, true),
    SANDSTONE(true, true, true, true, true),
    QUARTZ_STAIRS(true, true, true, true, true),
    QUARTZ_BLOCK(true, true, true, true, true),
    ENDER_STONE(true, true, true, true, true),
    PRISMARINE(true, true, true, true, true),
    STONE(true, true, true, true, true),
    DOUBLE_STEP(true, true, true, true, true),
    STEP(true, true, true, true, true),
    DOUBLE_STONE_SLAB2(true, true, true, true, true),
    STONE_SLAB2(true, true, true, true, true),
    NETHER_BRICK_STAIRS(false, true, true, true, true),
    NETHER_FENCE(true, true, true, true, true),
    NETHER_BRICKS(true, true, true, true, true),
    MOSSY_COBBLESTONE(true, true, true, true, true),
    COBBLE_WALL(true, true, true, true, true),
    COBBLESTONE_STAIRS(true, true, true, true, true),
    COBBLESTONE(true, true, true, true, true),
    CAULDRON(true, true, true, true, true),
    BRICK(true, true, true, true, true),
    BRICK_STAIRS(true, true, true, true, true),
    REDSTONE_ORE(false, false, true, true, false),
    QUARTZ_ORE(true, true, true, true, true),
    LAPIS_ORE(false, true, true, true, false),
    LAPIS_BLOCK(false, true, true, true, false),
    IRON_ORE(false, true, true, true, false),
    HOPPER(true, true, true, true, true),
    GOLD_ORE(false, false, true, true, false),
    END_STONE(true, true, true, true, true),
    EMERALD_ORE(false, false, true, true, false),
    DIAMOND_ORE(false, false, true, true, false),
    DRAGON_EGG(true, true, true, true, true),
    COAL_ORE(true, true, true, true, true),
    GOLD_BLOCK(false, false, true, true, false),
    FURNACE(true, true, true, true, true),
    DROPPER(true, true, true, true, true),
    DISPENDER(true, true, true, true, true),
    MOB_SPAWNER(true, true, true, true, true),
    IRON_TRAPDOOR(true, true, true, true, true),
    IRON_DOOR(true, true, true, true, true),
    IRON_BARS(true, true, true, true, true),
    ENCHANTMENT_TABLE(true, true, true, true, true),
    REDSTONE_BLOCK(true, true, true, true, true),
    IRON_BLOCK(false, true, true, true, false),
    EMERALD_BLOCK(false, false, true, true, false),
    DIAMOND_BLOCK(false, false, true, true, false),
    COAL_BLOCK(true, true, true, true, true),
    ANVIL(true, true, true, true, true),
    ENDER_CHEST(true, true, true, true, true),
    OBSIDIAN(false, false, false, true, false),

    NONE(false, false, false, false, false);

    private boolean wooden, stone, iron, diamond, golden;
    PickaxeMineableBlock(Boolean wooden, Boolean stone, Boolean iron, Boolean diamond, Boolean golden) {
        this.wooden = wooden; this.stone = stone; this.iron = iron; this.diamond = diamond; this.golden = golden;
    }

    public boolean canBeMinedBy(ItemStack itemStack) {

        if (itemStack == null) return false;

        switch (itemStack.getType()) {
            case WOOD_PICKAXE: return wooden;
            case STONE_PICKAXE: return stone;
            case IRON_PICKAXE: return iron;
            case DIAMOND_PICKAXE: return diamond;
            case GOLD_PICKAXE: return golden;
        }

        return false;

    }

    public static PickaxeMineableBlock valueOf(Material material) {
        try {
            return valueOf(material.name());
        } catch (Exception ignored) {}
        try {
            return valueOf(Material.getMaterial(material.getId()).name());
        } catch (Exception ignored) {}
        return NONE;
    }

}
