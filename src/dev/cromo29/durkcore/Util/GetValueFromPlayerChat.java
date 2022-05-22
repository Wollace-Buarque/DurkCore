package dev.cromo29.durkcore.util;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class GetValueFromPlayerChat implements Listener {

    private static final Map<Player, GettingValueFromPlayer> GETTING_VALUE_MAP = new HashMap<>();

    public static void removePlayer(Player player) {
        GETTING_VALUE_MAP.remove(player);
    }

    public static boolean isGettingValueFrom(Player player) {
        return GETTING_VALUE_MAP.containsKey(player);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        GETTING_VALUE_MAP.remove(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onChat(AsyncPlayerChatEvent event) {

        if (!GETTING_VALUE_MAP.containsKey(event.getPlayer())) return;

        event.setCancelled(true);

        Player whoTyped = event.getPlayer();
        String message = event.getMessage();

        GettingValueFromPlayer gettingValueFromPlayer = GETTING_VALUE_MAP.get(whoTyped);
        GETTING_VALUE_MAP.remove(whoTyped);

        if (gettingValueFromPlayer.typeToCancel != null && !gettingValueFromPlayer.typeToCancel.equals("")) {
            if (gettingValueFromPlayer.ignoreCase) {
                if (message.equalsIgnoreCase(gettingValueFromPlayer.typeToCancel) && gettingValueFromPlayer.onCancel != null) {

                    gettingValueFromPlayer.onCancel.accept(new GettingValue(true, null, whoTyped, event, gettingValueFromPlayer));
                    return;
                }
            } else if (message.equals(gettingValueFromPlayer.typeToCancel) && gettingValueFromPlayer.onCancel != null) {

                gettingValueFromPlayer.onCancel.accept(new GettingValue(true, null, whoTyped, event, gettingValueFromPlayer));
                return;
            }
        }

        if (gettingValueFromPlayer.onGetValue != null)
            gettingValueFromPlayer.onGetValue.accept(new GettingValue(false, message, whoTyped, event, gettingValueFromPlayer));
    }

    public static void getValueFrom(Player player, String typeToCancel, boolean ignoreCase, OnGetValue onGetValue, OnCancel onCancel) {

        GettingValueFromPlayer gettingValueFromPlayer = new GettingValueFromPlayer();

        gettingValueFromPlayer.typeToCancel = typeToCancel;
        gettingValueFromPlayer.ignoreCase = ignoreCase;
        gettingValueFromPlayer.onGetValue = onGetValue;
        gettingValueFromPlayer.onCancel = onCancel;

        GETTING_VALUE_MAP.put(player, gettingValueFromPlayer);
    }

    private static class GettingValueFromPlayer {

        String typeToCancel;
        boolean ignoreCase;
        OnGetValue onGetValue;
        OnCancel onCancel;

    }

    public static class GettingValue {

        private final boolean isCancelled;
        private final String valueString;
        private final Player whoTyped;
        private final AsyncPlayerChatEvent asyncPlayerChatEvent;
        private final GettingValueFromPlayer gettingValueFromPlayer;

        public GettingValue(boolean isCancelled, String valueString, Player whoTyped, AsyncPlayerChatEvent asyncPlayerChatEvent, GettingValueFromPlayer gettingValueFromPlayer) {
            this.isCancelled = isCancelled;
            this.valueString = valueString;
            this.whoTyped = whoTyped;
            this.asyncPlayerChatEvent = asyncPlayerChatEvent;
            this.gettingValueFromPlayer = gettingValueFromPlayer;
        }

        public boolean isCancelled() {
            return isCancelled;
        }

        public String getValueString() {
            return valueString;
        }

        public Player getWhoTyped() {
            return whoTyped;
        }

        public AsyncPlayerChatEvent getAsyncPlayerChatEvent() {
            return asyncPlayerChatEvent;
        }

        public void repeatGetValueFrom() {
            getValueFrom(getWhoTyped(), gettingValueFromPlayer.typeToCancel, gettingValueFromPlayer.ignoreCase, gettingValueFromPlayer.onGetValue, gettingValueFromPlayer.onCancel);
        }

    }

    public interface OnGetValue extends Consumer<GettingValue> {
    }

    public interface OnCancel extends Consumer<GettingValue> {
    }
}
