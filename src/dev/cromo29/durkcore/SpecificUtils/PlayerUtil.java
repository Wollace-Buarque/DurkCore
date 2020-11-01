package dev.cromo29.durkcore.SpecificUtils;

import dev.cromo29.durkcore.Util.RU;
import dev.cromo29.durkcore.Util.TXT;
import org.apache.commons.lang.reflect.FieldUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.stream.IntStream;

public class PlayerUtil {

    public static void setMetadata(Plugin plugin, Player player, String key, Object value) {
        removeMetadata(plugin, player, key);
        player.setMetadata(key, new FixedMetadataValue(plugin, value));
    }

    public static boolean hasMetadata(Player player, String key) {
        return player.hasMetadata(key);
    }

    public static void removeMetadata(Plugin plugin, Player player, String key) {
        player.removeMetadata(key, plugin);
    }

    public static <T> T getMetadata(Player player, String key) {
        try {
            return (T) player.getMetadata(key).get(0).value();
        } catch (Exception ignored) {
        }
        return null;
    }

    public static boolean isAbilityMayBuild(Player player) {
        try {
            Object handle = RU.getHandle(player);
            Object playerAbilities = handle.getClass().getField("abilities").get(handle);
            return (boolean) FieldUtils.readField(playerAbilities, "mayBuild");
        } catch (Exception ignored) {
        }
        return false;
    }

    public static void setAbilityMayBuild(Player player, boolean mayBuild) {
        try {
            Object handle = RU.getHandle(player);
            Object playerAbilities = handle.getClass().getField("abilities").get(handle);
            RU.setObject(playerAbilities, "mayBuild", mayBuild);
        } catch (Exception ignored) {
        }
    }

    public static boolean isAbilityInvulnerable(Player player) {
        try {
            Object handle = RU.getHandle(player);
            Object playerAbilities = handle.getClass().getField("abilities").get(handle);
            return (boolean) FieldUtils.readField(playerAbilities, "isInvulnerable");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void setAbilityIsInvulnerable(Player player, boolean isInvulnerable) {
        try {
            Object handle = RU.getHandle(player);
            Object playerAbilities = handle.getClass().getField("abilities").get(handle);
            RU.setObject(playerAbilities, "isInvulnerable", isInvulnerable);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static float getAbilityWalkSpeed(Player player) {
        try {
            Object handle = RU.getHandle(player);
            Object playerAbilities = handle.getClass().getField("abilities").get(handle);
            return (float) FieldUtils.readField(playerAbilities, "walkSpeed");
        } catch (Exception ignored) {
        }
        return player.getWalkSpeed();
    }

    public static void setAbilityWalkSpeed(Player player, float walkSpeed) {
        try {
            Object handle = RU.getHandle(player);
            Object playerAbilities = handle.getClass().getField("abilities").get(handle);
            RU.setObject(playerAbilities, "walkSpeed", walkSpeed);
        } catch (Exception ignored) {
        }
    }

    public static float getAbilityFlySpeed(Player player) {
        try {
            Object handle = RU.getHandle(player);
            Object playerAbilities = handle.getClass().getField("abilities").get(handle);
            return (float) FieldUtils.readField(playerAbilities, "flySpeed");
        } catch (Exception ignored) {
        }
        return player.getFlySpeed();
    }

    public static void setAbilityFlySpeed(Player player, float flySpeed) {
        try {
            Object handle = RU.getHandle(player);
            Object playerAbilities = handle.getClass().getField("abilities").get(handle);
            RU.setObject(playerAbilities, "flySpeed", flySpeed);
        } catch (Exception ignored) {
        }
    }

    public static boolean isAbilityFlying(Player player) {
        try {
            Object handle = RU.getHandle(player);
            Object playerAbilities = handle.getClass().getField("abilities").get(handle);
            return (boolean) FieldUtils.readField(playerAbilities, "isFlying");
        } catch (Exception ignored) {
        }
        return player.isFlying();
    }

    public static boolean isAbilityCanInstantlyBuild(Player player) {
        try {
            Object handle = RU.getHandle(player);
            Object playerAbilities = handle.getClass().getField("abilities").get(handle);
            return (boolean) FieldUtils.readField(playerAbilities, "canInstantlyBuild");
        } catch (Exception ignored) {
        }
        return false;
    }

    public static void setAbilityCanInstantlyBuild(Player player, boolean canInstantlyBuild) {
        try {
            Object handle = RU.getHandle(player);
            Object playerAbilities = handle.getClass().getField("abilities").get(handle);
            RU.setObject(playerAbilities, "canInstantlyBuild", canInstantlyBuild);
        } catch (Exception ignored) {
        }
    }

    public static boolean isAbilityCanFly(Player player) {
        try {
            Object handle = RU.getHandle(player);
            Object playerAbilities = handle.getClass().getField("abilities").get(handle);
            return (boolean) FieldUtils.readField(playerAbilities, "canFly");
        } catch (Exception ignored) {
        }
        return player.getAllowFlight();
    }

    public static void setAbilityCanFly(Player player, boolean canFly) {
        try {
            Object handle = RU.getHandle(player);
            Object playerAbilities = handle.getClass().getField("abilities").get(handle);
            RU.setObject(playerAbilities, "canFly", canFly);
        } catch (Exception ignored) {
        }
    }

    public static int emptySlots(Player player) {
        return emptySlots(player.getInventory());
    }

    public static int emptySlots(Inventory inventory) {
        int empty = 0;

        for (ItemStack content : inventory.getContents())
            if (content == null || content.getType() == Material.AIR) empty++;

        return empty;
    }

    public static void decreaseItem(Player player, int slot) {
        decreaseItem(player, slot, 1);
    }

    public static void decreaseItem(Player player, int slot, int amount) {
        decreaseItem(player.getInventory(), slot, amount);
    }

    public static void decreaseItem(Inventory inventory, int slot, int amount) {
        ItemStack item = inventory.getItem(slot);
        int finalAmount = item.getAmount() - amount;

        if (finalAmount <= 0) {
            inventory.clear(slot);
        } else {
            item.setAmount(finalAmount);
            inventory.setItem(slot, item);
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

    public static void giveItems(Player player, List<ItemStack> items, boolean dropExcess) {
        items.forEach((item) -> {
            giveItem(player, item, dropExcess);
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
	
	public enum CardinalDirection { NORTH, EAST, SOUTH, WEST }

    public static CardinalDirection getCardinalDirection(Player player) {
        double rotation = (player.getLocation().getYaw() - 90) % 360;
        if (rotation < 0) {
            rotation += 360.0;
        }

        if (0 <= rotation && rotation < 45) {

            return CardinalDirection.WEST;

        } else if (45 <= rotation && rotation < 135) {

            return CardinalDirection.NORTH;

        } else if (135 <= rotation && rotation < 225) {

            return CardinalDirection.EAST;

        } else if (225 <= rotation && rotation < 315) {

            return CardinalDirection.SOUTH;

        } else if (315 <= rotation && rotation < 360) {

            return CardinalDirection.WEST;

        } else return null;

    }

    private static Class<?> chatSerializer;
    private static Method chatSerializerAMethod;
    private static Constructor<?> packetPlayOutChatConstructor;
    private static Constructor<?> packetPlayOutTitleConstructor;
    private static Constructor<?> packetPlayOutTitleTimesConstructor;
    private static Enum<?> enumTimes;
    private static Enum<?> enumSubtitle;
    private static Enum<?> enumTitle;
    private static Class<?> packetPlayOutPlayerListHeaderFooter;
    private static Field tabHeaderField;
    private static Field tabFooterField;

    static {
        try {

            chatSerializer = RU.getNMSClass("IChatBaseComponent$ChatSerializer");
            chatSerializerAMethod = chatSerializer.getMethod("a", String.class);
            Class<?> packetPlayOutChat = RU.getNMSClass("PacketPlayOutChat");

            packetPlayOutChatConstructor = RU.getConstructor(packetPlayOutChat, RU.getNMSClass("IChatBaseComponent"), byte.class);

            Class<?> iChatBaseComponent = RU.getNMSClass("IChatBaseComponent");
            Class<?> enumTitleAction = RU.getNMSClass("PacketPlayOutTitle$EnumTitleAction");
            Class<?> packetPlayOutTitle = RU.getNMSClass("PacketPlayOutTitle");
            packetPlayOutTitleConstructor = RU.getConstructor(packetPlayOutTitle, enumTitleAction, iChatBaseComponent);
            packetPlayOutTitleTimesConstructor =
                    RU.getConstructor(packetPlayOutTitle, enumTitleAction, iChatBaseComponent, int.class, int.class, int.class);
            enumTimes = RU.getEnum(enumTitleAction, "TIMES");
            enumSubtitle = RU.getEnum(enumTitleAction, "SUBTITLE");
            enumTitle = RU.getEnum(enumTitleAction, "TITLE");
            packetPlayOutPlayerListHeaderFooter = RU.getNMSClass("PacketPlayOutPlayerListHeaderFooter");
            tabHeaderField = packetPlayOutPlayerListHeaderFooter.getDeclaredFields()[0];
            tabHeaderField.setAccessible(true);
            tabFooterField = packetPlayOutPlayerListHeaderFooter.getDeclaredFields()[1];
            tabFooterField.setAccessible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sendTablist(HumanEntity humanEntity, List<String> header, List<String> footer) {
        sendTablist((Player) humanEntity, header, footer);
    }

    public static void sendTablist(Player player, List<String> header, List<String> footer) {
        StringBuilder headerString = new StringBuilder();
        StringBuilder footerString = new StringBuilder();
        for (String s : header) {
            if (headerString.length() == 0) headerString.append(s.equals("") ? " " : s);
            else headerString.append("\n").append(s.equals("") ? " " : s);
        }
        for (String s : footer) {
            if (footerString.length() == 0) footerString.append(s.equals("") ? " " : s);
            else footerString.append("\n").append(s.equals("") ? " " : s);
        }
        sendTablist(player, headerString.toString(), footerString.toString());
    }

    public static void sendTablist(HumanEntity player, String header, String footer) {
        sendTablist((Player) player, header, footer);
    }

    public static void sendTablist(Player player, String header, String footer) {
        if (header == null || footer == null) return;
        try {
            Object tabHeader = chatSerializerAMethod.invoke(null, "{\"text\":\"" + TXT.parse(header) + "\"}");
            Object tabFooter = chatSerializerAMethod.invoke(null, "{\"text\":\"" + TXT.parse(footer) + "\"}");
            Object packet = packetPlayOutPlayerListHeaderFooter.newInstance();
            tabHeaderField.set(packet, tabHeader);
            tabFooterField.set(packet, tabFooter);
            RU.sendPacket(player, packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	
    public static boolean isValidNickname(String nick) {
        return nick.matches("^\\w{3,16}$");
    }

    public static int getPing(Player player) {
        try {
            return RU.getHandle(player).getClass().getDeclaredField("ping").getInt(player);
        } catch (Exception e) {
            return 0;
        }
    }

    public static Location getRandomLocationArroundPlayer(Player player, int XmaxDistance, int ZmaxDistance) {
        return LocationUtil.getRandomLocationArroundPlayer(player, XmaxDistance, ZmaxDistance);
    }

    public static void lookAt(Player player, Location location) {
        Location from = player.getLocation().clone();
        Location to = location.clone();
        double dx = to.getX() - from.getX();
        double dy = to.getY() - from.getY();
        double dz = to.getZ() - from.getZ();
        if (dx != 0.0D) {

            if (dx < 0.0D)
                from.setYaw(4.712389F);
            else
                from.setYaw(1.5707964F);

            from.setYaw(from.getYaw() - (float) Math.atan(dz / dx));
        } else if (dz < 0.0D)
            from.setYaw(3.1415927F);

        double dxz = Math.sqrt(Math.pow(dx, 2.0D) + Math.pow(dz, 2.0D));

        from.setPitch((float) (-Math.atan(dy / dxz)));
        from.setYaw(-from.getYaw() * 180.0F / 3.1415927F);
        from.setPitch(from.getPitch() * 180.0F / 3.1415927F);

        player.teleport(from);
    }

    public static Player getTargetPlayer(Player player, double searchRadius, double targetOffset) {
        return (Player) getTargetEntity(player, searchRadius, targetOffset, ListUtil.getList(EntityType.PLAYER));
    }

    public static Entity getTargetEntity(Player player, double searchRadius, double targetOffset, List<EntityType> targetTypes) {
        List<Entity> targets = player.getNearbyEntities(searchRadius / 2, searchRadius / 2, searchRadius / 2);

        Location playerLoc = player.getEyeLocation();

        org.bukkit.util.Vector linePoint = playerLoc.toVector();
        org.bukkit.util.Vector lineDirection = playerLoc.getDirection();

        for (Entity target : targets) {
            if (targetTypes != null) {
                if (!targetTypes.contains(target.getType())) {
                    continue;
                }
            }

            org.bukkit.util.Vector planePoint = target.getLocation().toVector();
            org.bukkit.util.Vector planeNormal = linePoint.clone().subtract(planePoint);

            double t = (planePoint.dot(planeNormal) - planeNormal.dot(linePoint)) / planeNormal.dot(lineDirection);
            if (t < 0) {
                continue;
            }

            double x = linePoint.getX() + lineDirection.getX() * t;
            double y = linePoint.getY() + lineDirection.getY() * t;
            double z = linePoint.getZ() + lineDirection.getZ() * t;

            org.bukkit.util.Vector intersection = new org.bukkit.util.Vector(x, y, z);
            if (intersection.distanceSquared(planePoint) < Math.pow(targetOffset, 2)) {
                return target;
            }
        }

        return null;
    }
}
