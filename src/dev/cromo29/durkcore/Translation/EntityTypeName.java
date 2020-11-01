package dev.cromo29.durkcore.Translation;


import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

public enum EntityTypeName {

    AREA_EFFECT_CLOUD("Área de Efeito de Poção"), ARMOR_STAND("Suporte para Armaduras"),
    ARROW("Flecha"), BAT("Morcego"), BLAZE("Blaze"),
    BOAT("Barco"), CAT("Gato"),
    CAVE_SPIDER("Aranha da Caverna"), CHICKEN("Galinha"),
    COMPLEX_PART("Desconhecido"), COW("Vaca"),
    CREEPER("Creeper"), DONKEY("Burro"),
    DRAGON_FIREBALL("Bola de Fogo"), DROPPED_ITEM("Item dropado"),
    EGG("Ovo"), ENDER_CRYSTAL("Cristal do End"),
    ENDER_DRAGON("Dragão do End"), ENDER_PEARL("Pérola de ender"),
    ENDER_SIGNAL("Olho de ender"), ENDERMAN("Enderman"),
    ENDERMITE("Endermite"), FALLING_BLOCK("Bloco Caindo"),
    FIREBALL("Bola de Fogo"), FIREWORK("Fogos de Artifício"),
    FISHING_HOOK("Isca da Vara de Pesca"), GHAST("Ghast"),
    GIANT("Zumbi Gigante"), GUARDIAN("Guardião"),
    HORSE("Cavalo"), IRON_GOLEM("Golem de Ferro"),
    ITEM_FRAME("Moldura"), LEASH_HITCH("Desconhecido"),
    LIGHTNING("Raio"), LINGERING_POTION("Poção"),
    MAGMA_CUBE("Cubo de Magma"), MINECART("Carrinho"),
    MINECART_CHEST("Carrinho com Baú"), MINECART_COMMAND("Carrinho com Bloco de Comando"),
    MINECART_FURNACE("Carrinho com Fornalha"), MINECART_HOPPER("Carrinho com Funil"),
    MINECART_MOB_SPAWNER("Carrinho com Gerador de Monstros"), MINECART_TNT("Carrinho com Dinamite"),
    MUSHROOM_COW("Vaca de Cogumelo"), OCELOT("Jaguatirica"),
    PAINTING("Pintura"), PIG("Porco"),
    PIG_ZOMBIE("Homem-porco Zumbi"), PLAYER("Jogador"),
    PRIMED_TNT("Dinamite"), PUFFERFISH("Baiacu"),
    SALMON("Salmão"), SHEEP("Ovelha"),
    SILVERFISH("Silverfish"), SKELETON("Esqueleto"),
    SKELETON_HORSE("Cavalo Esqueleto"), SLIME("Slime"),
    SMALL_FIREBALL("Bola de Fogo Pequena"), SNOWBALL("Bola de Neve"),
    SNOWMAN("Golem de Neve"), SPIDER("Aranha"),
    SPLASH_POTION("Poção Arremessável"), SQUID("Lula"),
    THROWN_EXP_BOTTLE("Frasco de Experiência"), UNKNOWN("Desconhecido"),
    VILLAGER("Aldeão"), WEATHER("Chuva"),
    WITCH("Bruxa"), WITHER("Wither"),
    WITHER_SKELETON("Esqueleto Wither"), WITHER_SKULL("Cabeça do Wither"),
    WOLF("Lobo"), ZOMBIE("Zumbi"),
    ZOMBIE_HORSE("Cavalo Zumbi"), ZOMBIE_VILLAGER("Aldeão Zumbi");

    private String name;

    EntityTypeName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public static EntityTypeName valueOf(Entity entity) {
        return valueOf(entity.getType());
    }

    public static EntityTypeName valueOf(EntityType entityType) {
        return valueOf(entityType.name());
    }

    @Override
    public String toString() {
        return this.name;
    }

}
