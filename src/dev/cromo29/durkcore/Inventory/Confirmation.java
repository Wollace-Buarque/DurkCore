package dev.cromo29.durkcore.Inventory;

import dev.cromo29.durkcore.Util.MakeItem;
import dev.cromo29.durkcore.Util.TXT;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

public class Confirmation {

    public static Inv confirm(String title, ItemStack middleItem, Player p, OnConfirm onConfirm, OnReject onReject) {
        Inv confirm = new Inv(4 * 9, TXT.parse(title));

        confirm.setItem(13, middleItem);

        confirm.setItem(20, new MakeItem(Material.WOOL, (byte) 5).setName("<a>Confirmar").build(), e -> {
            if (e.getClickedInventory().getType() == InventoryType.PLAYER) return;

            e.getWhoClicked().closeInventory();
            if (onConfirm != null) onConfirm.accept(e);
        });

        confirm.setItem(24, new MakeItem(Material.WOOL, (byte) 14).setName("<c>Cancelar").build(), e -> {
            if (e.getClickedInventory().getType() == InventoryType.PLAYER) return;

            e.getWhoClicked().closeInventory();
            if (onReject != null) onReject.accept(e);
        });

        confirm.open(p);
        return confirm;
    }

    public interface OnConfirm extends Consumer<InventoryClickEvent> {}
    public interface OnReject extends Consumer<InventoryClickEvent> {}
}