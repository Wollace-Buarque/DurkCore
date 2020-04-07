package dev.cromo29.durkcore.API;

import dev.cromo29.durkcore.SpecificUtils.ListUtil;
import dev.cromo29.durkcore.Util.TXT;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import org.bukkit.*;
import org.bukkit.command.*;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.StringUtil;

import java.lang.reflect.Field;
import java.util.*;

public abstract class DurkCommand implements CommandExecutor {

    private static CommandMap cmap;
    private CommandSender sender;
    private String[] args;
    private static String usedCommand;

    private void setValues(String cmd, CommandSender sender, String[] args) {
        this.sender = sender;
        this.args = args;
        usedCommand = cmd;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if (!cmd.getLabel().equalsIgnoreCase(getCommand())) return true;

        if (getPermission() != null && !sender.hasPermission(getPermission())) {
            warnNoPermission();
            return true;
        }

        if (!canConsolePerform() && !(sender instanceof Player)) {
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
        String lastWord = args[args.length - 1];
        Player senderPlayer = (sender instanceof Player) ? ((Player) sender) : null;
        ArrayList<String> matchedPlayers = new ArrayList<>();
        for (Player player : sender.getServer().getOnlinePlayers()) {
            String name = player.getName();
            if ((senderPlayer == null || senderPlayer.canSee(player)) && StringUtil.startsWithIgnoreCase(name, lastWord))
                matchedPlayers.add(name);

        }
        Collections.sort(matchedPlayers, String.CASE_INSENSITIVE_ORDER);
        return matchedPlayers;
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

    public Player getPlayer(String uuid) {
        return Bukkit.getPlayer(uuid);
    }

    public Player getPlayer(int i) {
        return Bukkit.getPlayer(argsAt(i));
    }

    public Player getPlayerAt(String args) {
        return Bukkit.getPlayer(args);
    }

    public Player getPlayerExact(String name) {
        return Bukkit.getPlayerExact(name);
    }

    public OfflinePlayer getOfflinePlayer(String uuid) {
        return Bukkit.getOfflinePlayer(uuid);
    }

    public OfflinePlayer getOfflinePlayer(int i) {
        return Bukkit.getOfflinePlayer(argsAt(i));
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

    public double getDouble(String asDouble) {
        return Double.parseDouble(asDouble);
    }

    public String getArgsAfter(int start) {
        return TXT.createString(getArgs(), start);
    }

    public List<String> getStringList(String... ss) {
        return ListUtil.getStringList(ss);
    }

    @SafeVarargs
    public final <T> List<T> getList(T... values) {
        return ListUtil.getList(values);
    }

    private static void sendGlobalMessage(String s) {
        Bukkit.broadcastMessage(TXT.parse(s));
    }

    private static void sendGlobalMessage(String s, String perm) {
        Bukkit.broadcast(TXT.parse(s), perm);
    }

    public void sendMessages(Player target, String... msgs) {
        for (String s : msgs) sendMessage(target, s);
    }

    public void sendMessages(String... msgs) {
        for (String s : msgs) sendMessage(s);
    }

    public void sendMessage(String s) {
        sender.sendMessage(TXT.parse(s));
    }

    public void sendMessage(Player target, String s) {
        target.sendMessage(TXT.parse(s));
    }

    public void sendMessage(String s, Object... args) {
        String txt = String.format(s, args);
        sendMessage(txt);
    }

    public static void sendActionBar(Player p, String s) {
        PacketPlayOutChat packet = new PacketPlayOutChat(IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + TXT.parse(s) + "\"}"), (byte) 2);
        ((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
    }

    public void log(String s, String... ss) {
        Bukkit.getConsoleSender().sendMessage(TXT.parse(s));
        for (String text : ss)
            Bukkit.getConsoleSender().sendMessage(TXT.parse(text));
    }

    public static void logMessages(String perm, String m, String... ms) {
        for (Player p : Bukkit.getServer().getOnlinePlayers())
            if (p.hasPermission(perm))
                p.sendMessage(TXT.parse(m));
        for (String s : ms)
            for (Player p : Bukkit.getServer().getOnlinePlayers())
                if (p.hasPermission(perm))
                    p.sendMessage(TXT.parse(s));
    }

    public static void logConsoleMessages(String m, String... ms) {
        Bukkit.getConsoleSender().sendMessage(TXT.parse(m));
        for (String s : ms)
            Bukkit.getConsoleSender().sendMessage(TXT.parse(s));
    }

    public String argsAt(int i) {
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

    public int isArgsLength() {
        return args.length;
    }

    public boolean isArgsLength(int i) {
        return args.length == i;
    }

    public boolean isArgAtIgnoreCase(int i, String s) {
        return args[i].equalsIgnoreCase(s);
    }

    public boolean isArgsAt(int i, String s) {
        return s.equals(argsAt(i));
    }

    public boolean isArgAtIgnoreCase(int i, String s, String... ss) {
        String arg = argsAt(i);

        if (arg.equalsIgnoreCase(s))
            return true;

        if (ss != null && ss.length > 0)
            for (String ssArg : ss)
                if (arg.equalsIgnoreCase(ssArg))
                    return true;
        return false;
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

    public void warnInventoryFull(Player p) {
        sendMessage(p.getName() + " <c>está com o inventário cheio.");
    }

    public void playSound(Player p, Sound s, double volume, double distortion) {
        p.playSound(p.getLocation(), s, (float) volume, (float) distortion);
    }

    public void spawnEntity(Player p, EntityType e) {
        p.getWorld().spawnEntity(p.getLocation(), e);
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

    public <T> T tryCast(Object value, T to) {
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
            String a = this.args[i];
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String parse(String s, Object... ss) {
        return TXT.parse(s, ss);
    }

    public String unParse(String s) {
        return TXT.unparse(s);
    }

    public void callEvent(Event e) {
        Bukkit.getPluginManager().callEvent(e);
    }

    public String createString(String[] args, int start) {
        return TXT.createString(args, start);
    }

    public String createString(String[] args, int start, String glue) {
        return TXT.createString(args, start, glue);
    }

    public static boolean isEmptyInventory(Player target) {
        for (ItemStack itens : target.getInventory().getContents())
            if (itens != null
                    || target.getInventory().getHelmet() != null
                    || target.getInventory().getChestplate() != null
                    || target.getInventory().getLeggings() != null
                    || target.getInventory().getBoots() != null)
                return false;
        return true;
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

        @Override
        public boolean execute(CommandSender sender, String s, String[] args) {
            if (durkCmd != null) {
                try {
                    durkCmd.setValues(s, sender, args);
                    return durkCmd.onCommand(sender, this, s, args);
                } catch (Exception e) {
                    e.printStackTrace();

                    StringBuilder errors = new StringBuilder();
                    for (StackTraceElement stack : e.getStackTrace()) {
                        String clazz = stack.getClassName();
                        if (!clazz.contains("DurkPlugin") && !clazz.contains("DurkCommand")
                                && !clazz.contains("org.bukkit") && !clazz.contains("java")
                                && !clazz.contains("net.minecraft") && !clazz.contains("minecraft")
                                && !clazz.contains("spigot")) {
                            clazz = clazz.substring(clazz.lastIndexOf(".") + 1);
                            errors.append("<7>").append(clazz).append("<f>: <e>").append(stack.getLineNumber()).append(" <7>(<e>").append(stack.getMethodName()).append("<7>)<f>, ");
                        }
                    }
                    String cmd2 = s;
                    if (args.length != 0)
                        cmd2 = s + " " + TXT.createString(args, 0);

                    if (errors.toString().equals(""))
                        errors.append("Indisponivel!");

                    String error2;
                    if (e.getCause() != null)
                        error2 = e.getCause().getLocalizedMessage();
                    else
                        error2 = e.getMessage();

                    int i = 0;
                    for (Player on : Bukkit.getServer().getOnlinePlayers())
                        if (on.hasPermission("29Erros.ADM")) {
                            i++;
                            break;
                        }

                    String warnPlayer;
                    if (i > 0) {
                        warnPlayer = "<a>Um staff foi avisado! tenha paciencia ate resolvermos este erro!";
                    } else warnPlayer = "<a>Contate um staff urgentemente e tenha paciencia ate resolvermos este erro!";

                    sender.sendMessage(TXT.parse("<c>Ocorreu um erro ao tentar executar este comando! " + warnPlayer));

                    logMessages("29Erros.ADM"
                            , ""
                            , "<e><m>}----------------------------------{"
                            , "  <e>| <c>Comando com erro: <7>" + cmd2
                            , "  <e>| <c>Quem usou: <7>" + sender.getName()
                            , "  <e>| <c>Erro: <7>" + error2
                            , "  <e>| <c>O erro se encontra em:"
                            , "  <e>| <c>» " + errors.toString().substring(0, errors.length() - 5) + "<f>."
                            , "<e><m>}----------------------------------{"
                            , "");

                    logConsoleMessages(
                            ""
                            , "<e><m>}----------------------------------{"
                            , "  <e>| <c>Comando com erro: <7>" + cmd2
                            , "  <e>| <c>Quem usou: <7>" + sender.getName()
                            , "  <e>| <c>Erro: <7>" + error2
                            , "  <e>| <c>O erro se encontra em:"
                            , "  <e>| <c>» " + errors.toString().substring(0, errors.length() - 5) + "<f>."
                            , "<e><m>}----------------------------------{"
                            , "");

                }
            }
            return false;
        }


        public List<String> tabComplete(CommandSender sender, String label, String[] args) {
            try {
                boolean setValue = false;
                if (durkCmd.getCommand().equalsIgnoreCase(label))
                    setValue = true;
                if (!setValue && durkCmd.getAliases() != null)
                    for (String aliases : durkCmd.getAliases())
                        if (aliases.equalsIgnoreCase(label)) {
                            setValue = true;
                            break;
                        }

                if (setValue)
                    durkCmd.setValues(label, sender, args);

                List<String> completions = durkCmd.tabComplete();
                if (completions == null) return new ArrayList<>();
                return completions;
            } catch (Exception e) {
                e.printStackTrace();

                StringBuilder errors = new StringBuilder();
                for (StackTraceElement stack : e.getStackTrace()) {
                    String clazz = stack.getClassName();
                    if (!clazz.contains("DurkPlugin") && !clazz.contains("DurkCommand")
                            && !clazz.contains("org.bukkit") && !clazz.contains("java")
                            && !clazz.contains("net.minecraft") && !clazz.contains("minecraft")
                            && !clazz.contains("spigot")) {
                        clazz = clazz.substring(clazz.lastIndexOf(".") + 1);
                        errors.append("<7>").append(clazz).append("<f>: <e>").append(stack.getLineNumber()).append(" <7>(<e>").append(stack.getMethodName()).append("<7>)<f>, ");
                    }
                }

                String cmd2 = label;
                if (args.length != 0)
                    cmd2 = label + " " + TXT.createString(args, 0);

                if (errors.toString().equals(""))
                    errors.append("Indisponivel!");

                String error2;
                if (e.getCause() != null)
                    error2 = e.getCause().getLocalizedMessage();
                else
                    error2 = e.getMessage();

                int i = 0;
                for (Player on : Bukkit.getServer().getOnlinePlayers())
                    if (on.hasPermission("29Erros.ADM")) {
                        i++;
                        break;
                    }

                String warnPlayer;
                if (i > 0) {
                    warnPlayer = "<a>Um staff foi avisado! tenha paciencia ate resolvermos este erro!";
                } else warnPlayer = "<a>Contate um staff urgentemente e tenha paciencia ate resolvermos este erro!";

                sender.sendMessage(TXT.parse("<c>Ocorreu um erro ao tentar executar o completor de comando! " + warnPlayer));

                logMessages("29Erros.ADM"
                        , ""
                        , "<e><m>}----------------------------------{"
                        , "  <e>| <c>Tab-Completer com erro: <7>" + cmd2
                        , "  <e>| <c>Quem usou: <7>" + sender.getName()
                        , "  <e>| <c>Erro: <7>" + error2
                        , "  <e>| <c>O erro se encontra em:"
                        , "  <e>| <c>» " + errors.toString().substring(0, errors.length() - 5) + "<f>."
                        , "<e><m>}----------------------------------{"
                        , "");

                logConsoleMessages(""
                        , "<e><m>}----------------------------------{"
                        , "  <e>| <c>Tab-Completer com erro: <7>" + cmd2
                        , "  <e>| <c>Quem usou: <7>" + sender.getName()
                        , "  <e>| <c>Erro: <7>" + error2
                        , "  <e>| <c>O erro se encontra em:"
                        , "  <e>| <c>» " + errors.toString().substring(0, errors.length() - 5) + "<f>."
                        , "<e><m>}----------------------------------{"
                        , "");
                return null;
            }
        }
    }
}