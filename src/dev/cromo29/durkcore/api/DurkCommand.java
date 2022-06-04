package dev.cromo29.durkcore.api;

import com.google.common.collect.Lists;
import dev.cromo29.durkcore.entity.DurkPlayer;
import dev.cromo29.durkcore.specificutils.ListUtil;
import dev.cromo29.durkcore.specificutils.PlayerUtil;
import dev.cromo29.durkcore.util.JAction;
import dev.cromo29.durkcore.util.TXT;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;

import java.text.Normalizer;
import java.util.*;

public abstract class DurkCommand implements Listener {

    private DurkPlugin plugin;
    private String performedCommand;
    private CommandSender sender;
    private String[] args;

    private Player player;

    public void setValues(String command, CommandSender sender, String[] args, DurkPlugin plugin) {
        this.sender = sender;
        this.performedCommand = command;
        this.args = args;
        this.plugin = plugin;

        try {
            player = (Player) sender;
        } catch (Exception ignored) {

        }
    }

    public void execute(String command, CommandSender sender, String[] args, DurkPlugin plugin) {
        setValues(command, sender, args, plugin);
        perform();
    }

    public abstract void perform();

    public abstract boolean canConsolePerform();

    public abstract String getPermission();

    public abstract String getCommand();

    public abstract List<String> getAliases();

    public String getDescription() {
        return "Nenhuma.";
    }

    public List<String> tabComplete() {

        try {
            return getPlayersTabComplete(args[args.length - 1], plugin.getServer().getOnlinePlayers());
        } catch (Exception exception) {
            List<String> toRet = new ArrayList<>();

            for (Player player : plugin.getServer().getOnlinePlayers()) toRet.add(player.getName());

            Collections.sort(toRet);

            return toRet;
        }
    }


    public String getUsedCommand() {
        return performedCommand;
    }

    public CommandSender getSender() {
        return sender;
    }

    public Player asPlayer() {
        return player;
    }

    public String[] getArgs() {
        return args;
    }

    public DurkPlugin getPlugin() {
        return plugin;
    }


    public Player getPlayer(UUID uuid) {
        return Bukkit.getPlayer(uuid);
    }

    public Player getPlayer(String name) {
        return Bukkit.getPlayer(name);
    }

    public Player getPlayerAt(int i) {
        return Bukkit.getPlayer(argAt(i));
    }

    public Player getPlayerExact(String name) {
        return Bukkit.getPlayerExact(name);
    }


    public OfflinePlayer getOfflinePlayer(UUID uuid) {
        return Bukkit.getOfflinePlayer(uuid);
    }

    public OfflinePlayer getOfflinePlayer(String name) {
        return Bukkit.getOfflinePlayer(name);
    }

    public OfflinePlayer getOfflinePlayerAt(int i) {
        return Bukkit.getOfflinePlayer(argAt(i));
    }


    public Location getCenter(Location location) {
        return location.add(0.5, 0, 0.5);
    }


    public int getInt(String asInt) {
        return Integer.parseInt(asInt);
    }

    public long getLong(String asLong) {
        return Long.parseLong(asLong);
    }

    public double getDouble(String asDouble) {
        return Double.parseDouble(asDouble);
    }

    public boolean getBoolean(String asBoolean) {
        return Boolean.parseBoolean(asBoolean);
    }

    public String getArgs(int start) {
        return TXT.createString(args, start);
    }


    @SafeVarargs
    public final <T> List<T> getList(T... values) {
        return ListUtil.getList(values);
    }

    public List<String> getStringList(String... strings) {
        return ListUtil.getStringList(strings);
    }

    public String[] getStringArray(String... strings) {
        return ListUtil.getStringArray(strings);
    }


    public void sendGMessage(String message) {
        Bukkit.broadcastMessage(parse(message));
    }

    public void sendGMessage(String message, String permission) {
        Bukkit.broadcast(parse(message), permission);
    }

    public void sendMessages(CommandSender sender, String... messages) {
        TXT.sendMessages(sender, messages);
    }

    public void sendMessages(String... messages) {
        for (String message : messages) sendMessage(message);
    }

    public void sendMessage(String message) {

        if (isConsole()) sender.sendMessage(parse(message));
        else player.sendMessage(parse(message));
    }

    public void sendMessage(String message, boolean parse) {

        if (parse) {
            sendMessage(message);
            return;
        }

        if (isConsole()) sender.sendMessage(message);
        else player.sendMessage(message);
    }

    public void sendMessage(CommandSender sender, String message) {
        sender.sendMessage(parse(message));
    }

    public void sendMessage(String message, Object... args) {
        sendMessage(String.format(message, args));
    }

    public void sendTitle(String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        if (isPlayer()) PlayerUtil.sendTitle(asPlayer(), title, subtitle, fadeIn, stay, fadeOut);
    }
    public void sendTitle(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        PlayerUtil.sendTitle(player, title, subtitle, fadeIn, stay, fadeOut);
    }

    public void sendActionBar(Player player, String actionBar) {
        PlayerUtil.sendActionBar(player, actionBar);
    }

    public void sendTimedActionBar(Player player, String actionBar, int stayInSeconds) {
        PlayerUtil.sendTimedActionBar(player, actionBar, stayInSeconds);
    }

    public void sendHoverableMessage(String message, String hover) {
        JAction jAction = new JAction();
        jAction.parseText(message);

        if (hover != null) jAction.setParseEvent(hover, HoverEvent.Action.SHOW_TEXT);

        if (isConsole()) jAction.endJson().send(getSender());
        else jAction.endJson().send(asPlayer());
    }

    public void sendClickableCommand(String message, String hover, String click) {
        JAction jAction = new JAction();
        jAction.parseText(message);

        if (hover != null) jAction.setParseEvent(hover, HoverEvent.Action.SHOW_TEXT);
        if (click != null) jAction.setParseEvent(click, ClickEvent.Action.SUGGEST_COMMAND);

        if (isConsole()) jAction.endJson().send(getSender());
        else jAction.endJson().send(asPlayer());
    }


    public boolean isPlayer() {
        return sender instanceof Player;
    }

    public boolean isPlayer(CommandSender sender) {
        return sender instanceof Player;
    }

    public boolean isConsole() {
        return !isPlayer();
    }

    public boolean isConsole(CommandSender sender) {
        return !isPlayer(sender);
    }


    public String lastArg() {
        return argAt(argsLength() - 1);
    }

    public String argAt(int i) {
        return args[i];
    }

    public int argsLength() {
        return args.length;
    }

    public boolean isArgsLength(int i) {
        return args.length == i;
    }

    public boolean isArgsAt(int i, String s) {
        return args[i].equals(s);
    }

    public boolean isArgAt(int i, String s, String... ss) {
        String arg = argAt(i);

        if (arg.equals(s)) return true;

        if (ss != null && ss.length > 0) {
            for (String ssArg : ss) {

                if (arg.equals(ssArg)) return true;
            }
        }

        return false;
    }

    public boolean isArgAtIgnoreCase(int i, String s) {
        return args[i].equalsIgnoreCase(s);
    }

    public boolean isArgAtIgnoreCase(int i, String s, String... ss) {
        String arg = argAt(i);

        if (arg.equalsIgnoreCase(s)) return true;

        if (ss != null && ss.length > 0) {
            for (String ssArg : ss) {
                if (arg.equalsIgnoreCase(ssArg)) return true;
            }
        }
        return false;
    }

    public boolean isInventoryFull(Inventory inventory) {
        return inventory.firstEmpty() == -1;
    }

    public boolean isInventoryFull(Player player) {
        return player.getInventory().firstEmpty() == -1;
    }


    public boolean hasPermission(CommandSender sender, String permission) {
        return sender.hasPermission(permission);
    }

    public boolean hasPermission(String permission) {
        return sender.hasPermission(permission);
    }


    public void warnMustBeHoldingItem() {
        sendMessage("<c>Você precisa estar segurando um item.");
    }

    public void warnConsoleCannotPerform() {
        sendMessage("<c>Somente jogadores podem usar esse comando.");
    }

    public void warnNoPermission() {
        sendMessage("<c>Você não tem permissão para usar esse comando.");
    }

    public void warnPlayerCannotPerform() {
        sendMessage("<c>Somente o console pode usar esse comando.");
    }

    public void warnNotValidNumber(String notValid) {
        sendMessage(notValid + " <c>não é um número válido.");
    }

    public void warnPlayerOffline(String name) {
        sendMessage(name + " <c>não está online.");
    }

    public void warnInventoryFull(Player player) {
        sendMessage(player.getName() + " <c>está com o inventário cheio.");
    }


    public void playSound(Player player, Sound sound, double volume, double distortion) {
        player.playSound(player.getLocation(), sound, (float) volume, (float) distortion);
    }

    public void playSound(Sound sound, double volume, double distortion) {
        if (isPlayer()) playSound(asPlayer(), sound, volume, distortion);
    }

    public void spawnEntity(Player player, EntityType entityType) {
        player.getWorld().spawnEntity(player.getLocation(), entityType);
    }

    public void spawnEntity(Location location, EntityType entityType) {
        location.getWorld().spawnEntity(location, entityType);
    }


    public void callEvent(Event event) {
        plugin.callEvent(event);
    }


    public String parse(String string, Object... ss) {
        return TXT.parse(string, ss);
    }

    public String unparse(String string) {
        return TXT.unparse(string);
    }

    public String createString(String[] args, int start) {
        return TXT.createString(args, start);
    }

    public String createString(String[] args, int start, String glue) {
        return TXT.createString(args, start, glue);
    }


    public boolean isCommandPerformed(boolean ignoreCase, String cmd, String... orCmds) {

        if (ignoreCase) {

            if (performedCommand.equalsIgnoreCase(cmd)) return true;

            if (orCmds != null && orCmds.length > 0) {
                for (String orCmd : orCmds) {

                    if (orCmd.equalsIgnoreCase(performedCommand)) return true;
                }
            }

        } else {

            if (performedCommand.equals(cmd)) return true;

            if (orCmds != null && orCmds.length > 0) {
                for (String orCmd : orCmds) {
                    if (orCmd.equals(performedCommand)) return true;
                }
            }

        }

        return false;
    }

    public boolean isValidLong(String toCheck) {
        if (toCheck.equalsIgnoreCase("nan")) return false;

        try {
            Long.parseLong(toCheck);
            return true;
        } catch (Exception ignored) {
            return false;
        }
    }

    public boolean isValidInt(String toCheck) {
        if (toCheck.equalsIgnoreCase("nan")) return false;

        try {
            Integer.parseInt(toCheck);
            return true;
        } catch (Exception ignored) {
            return false;
        }
    }

    public boolean isValidDouble(String toCheck) {
        if (toCheck.equalsIgnoreCase("nan")) return false;

        try {
            Double.parseDouble(toCheck);
            return true;
        } catch (Exception ignored) {
            return false;
        }
    }

    public boolean isValidBoolean(String toCheck) {
        return toCheck != null && toCheck.equalsIgnoreCase("true");
    }

    public <T extends Enum<T>> T tryEnumValueOf(Class<T> enm, String enumName) {
        try {
            return Enum.valueOf(enm, enumName);
        } catch (Exception ignored) {
            return null;
        }
    }

    public <T> T tryCast(Object value) {
        try {
            return (T) value;
        } catch (Exception ignored) {
            return null;
        }
    }

    public EntityType tryGetEntityType(String type) {
        try {
            return EntityType.valueOf(type.toUpperCase());
        } catch (Exception ignored) {
            return null;
        }
    }

    public boolean containsArg(int i) {
        try {
            String a = args[i];
            return true;
        } catch (Exception ignored) {
            return false;
        }
    }


    public int getEmptySlots(Player player) {
        return DurkPlayer.of(player).getInventoryEmptySlots();
    }

    public boolean isEmptyInventory(Player player) {
        return DurkPlayer.of(player).isEmptyInventory();
    }


    public String replace(String text, String toReplace, String value, Object... replace) {
        text = text.replace(toReplace + "", value + "");

        Iterator<Object> iter = Arrays.asList(replace).iterator();

        while (iter.hasNext()) {
            String key = iter.next() + "";
            String iterValue = iter.next() + "";

            text = text.replace(key, iterValue);
        }
        return text;
    }


    public void log(String text, Object... ss) {
        plugin.log(text, ss);
    }

    public String deAccent(String string) {
        return Normalizer.normalize(string, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
    }


    public List<String> getTabComplete(String currentArg, String... possibleTabsComplete) {
        return getTabComplete(currentArg, Lists.newArrayList(possibleTabsComplete));
    }

    public List<String> getTabComplete(String currentArg, List<String> possibleTabsComplete) {
        List<String> toRet = new ArrayList<>();

        for (String possibleTabComplete : possibleTabsComplete) {

            if (currentArg.equals("")) toRet.add(possibleTabComplete);
            else if (possibleTabComplete.toLowerCase().startsWith(currentArg.toLowerCase()))
                toRet.add(possibleTabComplete);

        }

        return toRet;
    }

    public List<String> getPlayersTabComplete(String currentArg, Collection<? extends Player> possiblePlayersTabComplete) {
        List<String> toRet = new ArrayList<>();

        for (Player player : possiblePlayersTabComplete) {

            if (currentArg.equals("")) toRet.add(player.getName());
            else if (player.getName().toLowerCase().startsWith(currentArg.toLowerCase())) toRet.add(player.getName());

        }

        return toRet;
    }
}