package dev.cromo29.durkcore.SpecificUtils;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.stream.IntStream;

public class PlayerUtil {

    public static int emptySlots(Player p) {
        int empty = 0;
        ItemStack[] var2 = p.getInventory().getContents();
        int var3 = var2.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            ItemStack content = var2[var4];

            if (content == null || content.getType() == Material.AIR)
                ++empty;
        }

        return empty;
    }

    public static void decreaseItem(Player p, int slot) {
        decreaseItem(p, slot, 1);
    }

    public static void decreaseItem(Player p, int slot, int amount) {
        ItemStack item = p.getInventory().getItem(slot);
        int finalAmount = item.getAmount() - amount;

        if (finalAmount <= 0)
            p.getInventory().clear(slot);
         else {
            item.setAmount(finalAmount);
            p.getInventory().setItem(slot, item);
        }

    }

    public static void giveItem(Player player, ItemStack item, boolean dropExcess) {
        ItemStack singleItem = item.clone();
        int amount = item.getAmount();
        singleItem.setAmount(1);
        int stackAmount = item.getAmount();
        int stackSize = singleItem.getMaxStackSize();
        int stacks = amount * stackAmount / stackSize;
        int left = amount % stackSize;

        if (stacks != 0) {
            singleItem.setAmount(stackSize);
            IntStream.range(0, stacks).forEach((i) -> {
                insert(player, singleItem.clone(), dropExcess);
            });
        }

        if (left != 0) {
            singleItem.setAmount(left);
            insert(player, singleItem.clone(), dropExcess);
        }

    }

    public static void giveItems(Player p, List<ItemStack> items, boolean dropExcess) {
        items.forEach((item) -> {
            giveItem(p, item, dropExcess);
        });
    }

    public static void insert(Player player, ItemStack stack) {
        insert(player, stack, true);
    }

    public static void insert(Player player, ItemStack stack, boolean playsound) {
        HashMap<Integer, ItemStack> items = player.getInventory().addItem(stack);

        if (items.size() == 0 && playsound)
            player.playSound(player.getLocation(), Sound.ITEM_PICKUP, 1.0f, 1.0f);

         else {
            items.values().forEach((itemLeft) -> {
                player.getWorld().dropItemNaturally(player.getLocation(), itemLeft);
            });

            if (playsound)
                player.playSound(player.getLocation(), Sound.CHICKEN_EGG_POP, 0.800000011920929f, 1.0f);

        }

    }

    public static Location getRandomLocationArroundPlayer(Player player, int XmaxDistance, int ZmaxDistance) {
        return LocationUtil.getRandomLocationArroundPlayer(player, XmaxDistance, ZmaxDistance);
    }

    public static void lookAt(Player p, Location location) {
        Location from = p.getLocation().clone();
        Location to = location.clone();
        double dx = to.getX() - from.getX();
        double dy = to.getY() - from.getY();
        double dz = to.getZ() - from.getZ();
        if (dx != 0.0D) {

            if (dx < 0.0D)
                from.setYaw(4.712389F);
             else
                from.setYaw(1.5707964F);

            from.setYaw(from.getYaw() - (float)Math.atan(dz / dx));
        } else if (dz < 0.0D)
            from.setYaw(3.1415927F);

        double dxz = Math.sqrt(Math.pow(dx, 2.0D) + Math.pow(dz, 2.0D));

        from.setPitch((float)(-Math.atan(dy / dxz)));
        from.setYaw(-from.getYaw() * 180.0F / 3.1415927F);
        from.setPitch(from.getPitch() * 180.0F / 3.1415927F);

        p.teleport(from);
    }

}
