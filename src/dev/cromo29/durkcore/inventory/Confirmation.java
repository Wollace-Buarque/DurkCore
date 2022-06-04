package dev.cromo29.durkcore.inventory;

import dev.cromo29.durkcore.util.MakeItem;
import dev.cromo29.durkcore.util.TXT;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

public class Confirmation {

    public static Inv confirm(String title, ItemStack middleItem, Player player, OnConfirm onConfirm, OnReject onReject) {
        Inv confirm = new Inv(36, TXT.parse(title));

        confirm.setItem(13, middleItem);

        confirm.setItem(20, new MakeItem(Material.WOOL, (byte) 5).setName("<a>Confirmar").build(), event -> {
            if (event.getClickedInventory().getType() == InventoryType.PLAYER) return;

            event.getWhoClicked().closeInventory();

            if (onConfirm != null) onConfirm.accept(event);
        });

        confirm.setItem(24, new MakeItem(Material.WOOL, (byte) 14).setName("<c>Cancelar").build(), event -> {
            if (event.getClickedInventory().getType() == InventoryType.PLAYER) return;

            event.getWhoClicked().closeInventory();

            if (onReject != null) onReject.accept(event);
        });

        confirm.open(player);

        return confirm;
    }

    public interface OnConfirm extends Consumer<InventoryClickEvent> {}
    public interface OnReject extends Consumer<InventoryClickEvent> {}
}