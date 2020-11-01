package dev.cromo29.durkcore.API;

import com.google.common.collect.Lists;
import dev.cromo29.durkcore.Entity.DurkPlayer;
import dev.cromo29.durkcore.SpecificUtils.ListUtil;
import dev.cromo29.durkcore.Util.BreakLine;
import dev.cromo29.durkcore.Util.JAction;
import dev.cromo29.durkcore.Util.TXT;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.Inventory;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public abstract class DurkCommand implements CommandExecutor {

    private static CommandMap cmap;

    private String usedCommand;
    private CommandSender sender;
    private String[] args;

    private String command, permission;
    private List<String> aliases;
    private boolean canConsolePerform;

    public DurkCommand() {
        this.command = getCommand();
        this.permission = getPermission();
        this.aliases = getAliases();
        this.canConsolePerform = canConsolePerform();
    }

    private void setValues(String cmd, CommandSender sender, String[] args) {
        this.usedCommand = cmd;
        this.sender = sender;
        this.args = args;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public void setAliases(List<String> aliases) {
        this.aliases = aliases;
    }

    public void setCanConsolePerform(boolean canConsolePerform) {
        this.canConsolePerform = canConsolePerform;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {

        if (!cmd.getLabel().equalsIgnoreCase(command)) return true;

        if (permission != null && !hasPermission(sender, permission)) {
            warnNoPermission();
            return true;
        }

        if (!canConsolePerform && !(sender instanceof Player)) {
            warnConsoleCannotPerform();
            return true;
        }

        perform();
        return false;
    }


    public abstract void perform();

    public abstract boolean canConsolePerform();

    public abstract String getPermission();

    public abstract String getCommand();

    public abstract List<String> getAliases();

    public abstract String getDescription();

    public List<String> tabComplete() {

        try {
            List<String> toRet = Lists.newArrayList();
            String currentArg = args[args.length - 1];

            for (Player player : Bukkit.getServer().getOnlinePlayers()) {

                if (currentArg.equals("")) toRet.add(player.getName());
                else if (player.getName().toLowerCase().startsWith(currentArg.toLowerCase()))
                    toRet.add(player.getName());
            }

            return toRet;
        } catch (Exception exception) {
            List<String> toRet = Lists.newArrayList();

            for (Player player : Bukkit.getServer().getOnlinePlayers()) toRet.add(player.getName());

            return toRet;
        }
    }


    public String getUsedCommand() {
        return usedCommand;
    }

    public CommandSender getSender() {
        return sender;
    }

    public String[] getArgs() {
        return args;
    }


    public Player getPlayer(UUID uuid) {
        return Bukkit.getPlayer(uuid);
    }

    public Player getPlayer(String name) {
        return Bukkit.getPlayer(name);
    }

    public Player getPlayer(int i) {
        return Bukkit.getPlayer(argAt(i));
    }

    public Player getPlayerAt(String args) {
        return Bukkit.getPlayer(args);
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

    public OfflinePlayer getOfflinePlayer(int i) {
        return Bukkit.getOfflinePlayer(argAt(i));
    }

    public OfflinePlayer getOfflinePlayerAt(String args) {
        return Bukkit.getOfflinePlayer(args);
    }


    public Location getCenter(Location loc) {
        return loc.add(0.5, 0, 0.5);
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

    public String getArgsAfter(int start) {
        return TXT.createString(getArgs(), start);
    }

    @SafeVarargs
    public final <T> List<T> getList(T... values) {
        return ListUtil.getList(values);
    }

    public List<String> getStringList(String... strings) {
        return ListUtil.getStringList(strings);
    }


    public void sendGMessage(String message) {
        Bukkit.broadcastMessage(parse(message));
    }

    public void sendGMessage(String message, String permission) {
        Bukkit.broadcast(parse(message), permission);
    }

    public void sendMessages(CommandSender target, String... messages) {
        for (String s : messages) sendMessage(target, s);
    }

    public void sendMessages(String... messages) {
        for (String text : messages) sendMessage(text);
    }

    public void sendMessage(String message) {
        sender.sendMessage(parse(message));
    }

    public void sendMessage(CommandSender sender, String s) {
        sender.sendMessage(parse(s));
    }

    public void sendMessage(String message, Object... args) {
        sendMessage(String.format(message, args));
    }


    public void sendActionBar(Player player, String actionBar) {
        TXT.sendActionBar(player, actionBar);
    }

    public void sendActionBar(Player player, String actionBar, int stayInSeconds) {
        TXT.sendActionBar(player, actionBar, stayInSeconds);
    }

    public void sendHoverableMessage(String message, String hover) {
        JAction jAction = new JAction();
        jAction.parseText(message);

        if (hover != null) jAction.setParseEvent(hover, HoverEvent.Action.SHOW_TEXT);

        jAction.endJson().send(getSender());
    }

    public void sendClickableCommand(String message, String hover, String click) {
        JAction jAction = new JAction();
        jAction.parseText(message);

        if (hover != null) jAction.setParseEvent(hover, HoverEvent.Action.SHOW_TEXT);
        if (click != null) jAction.setParseEvent(click, ClickEvent.Action.SUGGEST_COMMAND);

        jAction.endJson().send(getSender());
    }


    @Deprecated
    public String argsAt(int i) {
        return args[i];
    }

    public String argAt(int i) {
        return args[i];
    }

    public Player asPlayer() {
        return (Player) sender;
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


    @Deprecated
    public int isArgsLength() {
        return args.length;
    }

    public boolean isArgsLength(int i) {
        return args.length == i;
    }


    public int argsLength() {
        return args.length;
    }

    public String lastArg() {
        return argAt(argsLength() - 1);
    }


    public boolean isArgsAt(int i, String s) {
        return s.equals(argAt(i));
    }

    public boolean isArgAt(int i, String s, String... ss) {
        String arg = argAt(i);

        if (arg.equals(s))
            return true;

        if (ss != null && ss.length > 0)
            for (String ssArg : ss)
                if (arg.equals(ssArg))
                    return true;
        return false;
    }


    public boolean isArgAtIgnoreCase(int i, String s) {
        return args[i].equalsIgnoreCase(s);
    }

    public boolean isArgAtIgnoreCase(int i, String s, String... ss) {
        String arg = argAt(i);

        if (arg.equalsIgnoreCase(s))
            return true;

        if (ss != null && ss.length > 0)
            for (String ssArg : ss)
                if (arg.equalsIgnoreCase(ssArg))
                    return true;
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
        return getSender().hasPermission(permission);
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

    public void spawnEntity(Player player, EntityType entityType) {
        player.getWorld().spawnEntity(player.getLocation(), entityType);
    }

    public void callEvent(Event event) {
        Bukkit.getPluginManager().callEvent(event);
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
            if (usedCommand.equalsIgnoreCase(cmd)) return true;

            if (orCmds != null && orCmds.length > 0)
                for (String orCmd : orCmds)
                    if (orCmd.equalsIgnoreCase(usedCommand))
                        return true;

        } else {
            if (usedCommand.equals(cmd))
                return true;

            if (orCmds != null && orCmds.length > 0)
                for (String orCmd : orCmds)
                    if (orCmd.equals(usedCommand))
                        return true;
        }
        return false;
    }

    public boolean isValidInt(String toCheck) {
        if (toCheck.equalsIgnoreCase("nan"))
            return false;
        else {
            try {
                Integer.parseInt(toCheck);
                return true;
            } catch (Exception var3) {
                return false;
            }
        }
    }

    public boolean isValidDouble(String toCheck) {
        if (toCheck.equalsIgnoreCase("nan"))
            return false;
        else {
            try {
                Double.parseDouble(toCheck);
                return true;
            } catch (Exception var3) {
                return false;
            }
        }
    }

    public boolean isValidBoolean(String toCheck) {
        try {
            Boolean.parseBoolean(toCheck);
            return true;
        } catch (Exception ignored) {
        }
        return false;
    }

    public <T extends Enum<T>> T tryEnumValueOf(Class<T> enm, String enumName) {
        try {
            return Enum.valueOf(enm, enumName);
        } catch (Exception e) {
            return null;
        }
    }

    public <T> T tryCast(Object value) {
        try {
            return (T) value;
        } catch (Exception e) {
            return null;
        }
    }

    public EntityType tryGetEntityType(String type) {
        try {
            return EntityType.valueOf(type.toUpperCase());
        } catch (Exception e) {
            return null;
        }
    }

    public boolean containsArg(int i) {
        try {
            String a = args[i];
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public int getEmptySlots(Player player) {
        DurkPlayer durkPlayer = DurkPlayer.get(player);

        return durkPlayer.getInventoryEmptySlots();
    }

    public boolean isEmptyInventory(Player player) {
        DurkPlayer durkPlayer = DurkPlayer.get(player);

        return durkPlayer.isEmptyInventory();
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

    public void log(String message, String... ss) {
        Bukkit.getConsoleSender().sendMessage(parse(message));

        for (String text : ss)
            Bukkit.getConsoleSender().sendMessage(parse(text));
    }

    protected static CommandMap getCommandMap() {
        if (cmap == null) {
            try {
                Field f = Bukkit.getServer().getClass().getDeclaredField("commandMap");
                f.setAccessible(true);
                cmap = (CommandMap) f.get(Bukkit.getServer());
                return getCommandMap();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            return cmap;
        }
        return getCommandMap();
    }

    protected static class ReflectCommand extends Command {

        private DurkCommand durkCmd = null;

        ReflectCommand(String command) {
            super(command);
        }

        void setExecutor(DurkCommand durkCommand) {
            this.durkCmd = durkCommand;
        }

        private void logMessages(String permission, String... messagges) {
            for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                if (player.hasPermission(permission)) TXT.sendMessages(player, messagges);
            }
        }

        private void logConsoleMessages(String... messages) {
            for (String text : messages)
                Bukkit.getConsoleSender().sendMessage(TXT.parse(text));
        }

        @Override
        public boolean execute(CommandSender sender, String s, String[] args) {
            if (durkCmd != null) {
                try {
                    durkCmd.setValues(s, sender, args);

                    return durkCmd.onCommand(sender, this, s, args);

                } catch (Exception exception) {
                    exception.printStackTrace();

                    StringBuilder errors = new StringBuilder();
                    for (StackTraceElement stack : exception.getStackTrace()) {
                        String clazz = stack.getClassName();

                        if (!clazz.contains("DurkPlugin")
                                && !clazz.contains("DurkCommand")
                                && !clazz.contains("org.bukkit") && !clazz.contains("java")
                                && !clazz.contains("net.minecraft") && !clazz.contains("minecraft")
                                && !clazz.contains("spigot")) {

                            clazz = clazz.substring(clazz.lastIndexOf(".") + 1);

                            errors.append("<7>").append(clazz).append("<f>: <e>").append(stack.getLineNumber()).append(" <7>(<d>").append(stack.getMethodName()).append("<7>)<f>, ");
                        }
                    }

                    String cmd = s;
                    if (args.length != 0)
                        cmd = s + " " + TXT.createString(args, 0);

                    if (errors.toString().equals(""))
                        errors.append("<c>Indisponivel");

                    String error;

                    if (exception.getCause() != null)
                        error = exception.getCause().getLocalizedMessage();
                    else error = exception.getMessage();

                    sender.sendMessage(TXT.parse("<c>Ocorreu um erro ao executar o comando <7>'" + cmd + "'<c>."));

                    BreakLine breakWord = new BreakLine();

                    String err = errors.substring(0, errors.length() - 5);
                    String formated = breakWord.addWordOnBreak("  <8>> ", err).get();

                    logMessages("29Erros.ADM"
                            , ""
                            , " <7><m>---------------------<r> <d>ERRO <7><m>---------------------<r>"
                            , "  <d>Comando com erro<f>: <7>" + cmd
                            , "  <d>Quem usou<f>: <7>" + sender.getName()
                            , "  <d>Erro<f>: <7>" + error
                            , "  <d>O erro se encontra em<f>:"
                            , "  <8>> " + formated + "<f>."
                            , " <7><m>---------------------<r> <d>ERRO <7><m>---------------------<r>"
                            , "");

                    logConsoleMessages(""
                            , " <7><m>---------------------<r> <d>ERRO <7><m>---------------------<r>"
                            , "  <d>Comando com erro<f>: <7>" + cmd
                            , "  <d>Quem usou<f>: <7>" + sender.getName()
                            , "  <d>Erro<f>: <7>" + error
                            , "  <d>O erro se encontra em<f>:"
                            , "  <8>> " + err + "<f>."
                            , " <7><m>---------------------<r> <d>ERRO <7><m>---------------------<r>"
                            , "");

                }
            }
            return false;
        }

        public List<String> tabComplete(CommandSender sender, String label, String[] args) {
            try {
                boolean setValues = false;

                if (durkCmd.command.equalsIgnoreCase(label))
                    setValues = true;

                if (!setValues && durkCmd.aliases != null) {
                    for (String aliases : durkCmd.aliases) {
                        if (aliases.equalsIgnoreCase(label)) {
                            setValues = true;
                            break;
                        }
                    }
                }

                if (setValues)
                    durkCmd.setValues(label, sender, args);

                List<String> completions = durkCmd.tabComplete();

                if (completions == null) return Lists.newArrayList();

                return completions;
            } catch (Exception exception) {
                exception.printStackTrace();

                StringBuilder errors = new StringBuilder();
                for (StackTraceElement stack : exception.getStackTrace()) {
                    String clazz = stack.getClassName();

                    if (!clazz.contains("DurkPlugin") && !clazz.contains("DurkCommand")
                            && !clazz.contains("org.bukkit") && !clazz.contains("java")
                            && !clazz.contains("net.minecraft") && !clazz.contains("minecraft")
                            && !clazz.contains("spigot")) {

                        clazz = clazz.substring(clazz.lastIndexOf(".") + 1);

                        errors.append("<7>").append(clazz).append("<f>: <e>").append(stack.getLineNumber()).append(" <7>(<d>").append(stack.getMethodName()).append("<7>)<f>, ");
                    }
                }

                String cmd = label;
                if (args.length != 0)
                    cmd = label + " " + TXT.createString(args, 0);

                if (errors.toString().equals(""))
                    errors.append("<c>Indisponivel");

                String error;
                if (exception.getCause() != null)
                    error = exception.getCause().getLocalizedMessage();
                else error = exception.getMessage();

                sender.sendMessage(TXT.parse("<c>Ocorreu um erro ao usar o completor de comando em <7>'" + label + "'<c>!"));

                BreakLine breakWord = new BreakLine();

                String err = errors.substring(0, errors.length() - 5);
                String formated = breakWord.addWordOnBreak("  <8>> ", err).get();

                logMessages("29Erros.ADM"
                        , ""
                        , " <7><m>---------------------<r> <d>ERRO <7><m>---------------------<r>"
                        , "  <d>Completor com erro<f>: <7>" + cmd
                        , "  <d>Quem usou<f>: <7>" + sender.getName()
                        , "  <d>Erro<f>: <7>" + error
                        , "  <d>O erro se encontra em<f>:"
                        , "  <8>> " + formated + "<f>."
                        , " <7><m>---------------------<r> <d>ERRO <7><m>---------------------<r>"
                        , "");

                logConsoleMessages(""
                        , " <7><m>---------------------<r> <d>ERRO <7><m>---------------------<r>"
                        , "  <d>Completor com erro<f>: <7>" + cmd
                        , "  <d>Quem usou<f>: <7>" + sender.getName()
                        , "  <d>Erro<f>: <7>" + error
                        , "  <d>O erro se encontra em<f>:"
                        , "  <8>> " + err + "<f>."
                        , " <7><m>---------------------<r> <d>ERRO <7><m>---------------------<r>"
                        , "");

                List<String> toReturn = Lists.newArrayList();

                for (Player player : Bukkit.getServer().getOnlinePlayers()) toReturn.add(player.getName());

                return toReturn;
            }
        }
    }
}