package dev.cromo29.durkcore.Util;

import com.google.common.collect.Maps;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Map;
import java.util.function.Consumer;

public class GetValueFromPlayerChat implements Listener {
	
    private static Map<Player, GettingValueFromPlayer> gettingValueFromPlayerMap = Maps.newHashMap();

    public static void removePlayer(Player player) { gettingValueFromPlayerMap.remove(player); }
    public static boolean isGettingValueFrom(Player player) { return gettingValueFromPlayerMap.containsKey(player); }

    @EventHandler public void onQuit(PlayerQuitEvent e) { GetValueFromPlayerChat.gettingValueFromPlayerMap.remove(e.getPlayer()); }
    @EventHandler(priority = EventPriority.LOWEST)
    public void onChat(AsyncPlayerChatEvent e) {
        if (GetValueFromPlayerChat.gettingValueFromPlayerMap.containsKey(e.getPlayer())) {
            e.setCancelled(true);

            Player whoTyped = e.getPlayer();
            String message = e.getMessage();

            GettingValueFromPlayer gettingValueFromPlayer = GetValueFromPlayerChat.gettingValueFromPlayerMap.get(whoTyped);
            GetValueFromPlayerChat.gettingValueFromPlayerMap.remove(whoTyped);

            if (gettingValueFromPlayer.typeToCancel != null && !gettingValueFromPlayer.typeToCancel.equals("")) {
                if (gettingValueFromPlayer.ignoreCase) {
                    if (message.equalsIgnoreCase(gettingValueFromPlayer.typeToCancel) && gettingValueFromPlayer.onCancel != null) {

                        gettingValueFromPlayer.onCancel.accept(new GettingValue(true, null, whoTyped, e, gettingValueFromPlayer));
                        return;
                    }
                } else if (message.equals(gettingValueFromPlayer.typeToCancel) && gettingValueFromPlayer.onCancel != null) {

                    gettingValueFromPlayer.onCancel.accept(new GettingValue(true, null, whoTyped, e, gettingValueFromPlayer));
                    return;
                }
            }

            if (gettingValueFromPlayer.onGetValue != null)
                gettingValueFromPlayer.onGetValue.accept(new GettingValue(false, message, whoTyped, e, gettingValueFromPlayer));
        }
    }

    public static void getValueFrom(Player p, String typeToCancel, boolean ignoreCase, OnGetValue onGetValue, OnCancel onCancel) {

        GettingValueFromPlayer gettingValueFromPlayer = new GettingValueFromPlayer();

        gettingValueFromPlayer.typeToCancel = typeToCancel;
        gettingValueFromPlayer.ignoreCase = ignoreCase;
        gettingValueFromPlayer.onGetValue = onGetValue;
        gettingValueFromPlayer.onCancel = onCancel;

        gettingValueFromPlayerMap.put(p, gettingValueFromPlayer);

    }

    private static class GettingValueFromPlayer { String typeToCancel; boolean ignoreCase; OnGetValue onGetValue; OnCancel onCancel; }

    public static class GettingValue {

        private boolean isCancelled;
        private String valueString;
        private Player whoTyped;
        private AsyncPlayerChatEvent asyncPlayerChatEvent;
        private GettingValueFromPlayer gettingValueFromPlayer;

        GettingValue(boolean isCancelled, String valueString, Player whoTyped, AsyncPlayerChatEvent asyncPlayerChatEvent, GettingValueFromPlayer gettingValueFromPlayer) {
            this.isCancelled = isCancelled; this.valueString = valueString; this.whoTyped = whoTyped; this.asyncPlayerChatEvent = asyncPlayerChatEvent;
            this.gettingValueFromPlayer = gettingValueFromPlayer;
        }

        public boolean isCancelled() { return isCancelled; }
        public String getValueString() { return valueString; }
        public Player getWhoTyped() { return whoTyped; }
        public AsyncPlayerChatEvent getAsyncPlayerChatEvent() { return asyncPlayerChatEvent; }

        public void repeatGetValueFrom() {
            getValueFrom(getWhoTyped(), gettingValueFromPlayer.typeToCancel, gettingValueFromPlayer.ignoreCase, gettingValueFromPlayer.onGetValue, gettingValueFromPlayer.onCancel);
        }

    }

    public interface OnGetValue extends Consumer<GettingValue> {}
    public interface OnCancel extends Consumer<GettingValue> {}
}
