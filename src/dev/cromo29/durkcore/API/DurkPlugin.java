package dev.cromo29.durkcore.API;

import com.google.common.collect.Maps;
import dev.cromo29.durkcore.Util.BreakLine;
import dev.cromo29.durkcore.Util.TXT;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Method;
import java.util.*;

import static dev.cromo29.durkcore.Util.RU.getBukkitClass;

public abstract class DurkPlugin extends JavaPlugin implements Listener {

    private String pluginName, version, credits, author;
    private long enableTime;

    private int listenerError, commandError;

    //  Static to get dates off all plugins.
    private Map<String, DurkCommand> registeredCommands = new HashMap<>();
    private static Map<String, List<DurkGetter>> commands = new HashMap<>();
    private static List<String> hookedPlugins = new ArrayList<>();

    public DurkPlugin() {
        this.pluginName = getDescription().getName();
        this.version = getDescription().getVersion();

        List<String> authors = getDescription().getAuthors();

        if (authors != null && !authors.isEmpty()) {
            this.author = TXT.createString(authors.toArray(new String[0]), 0, ", ");
        }

        this.enableTime = 0L;
        this.credits = " <b>" + pluginName + " <7>desenvolvido por <b>" + (author == null ? "?" : author) + "<7>!";
    }

    public DurkPlugin(String pluginName, String author) {
        this.author = author;
        this.pluginName = getDescription().getName();
        this.version = getDescription().getVersion();
        this.enableTime = 0L;
        this.credits = " <b>" + pluginName + " <7>desenvolvido por <b>" + author + "<7>!";
    }

    public String getPluginName() {
        return pluginName;
    }

    public String getAuthor() {
        return author;
    }

    public String getPluginVersion() {
        return version;
    }


    public void onPreStart() {
    }

    public void onStart() {

    }

    public void onStop() {
    }


    @Override
    public void onLoad() {
        this.onPreStart();
    }

    @Override
    public void onEnable() {
        if (!hookedPlugins.contains(pluginName))
            hookedPlugins.add(pluginName);

        enableTime = System.nanoTime();

        logs(" <8>----- <7>Habilitando plugin... <8>-----", "");
        this.onStart();

        this.onStartError();

        logs("", credits, "");
        this.onStartPost();

        if (!isEnabled()) return;
        this.getServer().getPluginManager().registerEvents(this, this);
    }


    @Override
    public void onDisable() {
        HandlerList.unregisterAll((Plugin) this);
        this.onStop();
    }

    private void onStartError() {
        if (commandError != 0) {
            String completer = commandError == 1 ? "" : "s";
            log(" <c>Tentei registrar " + commandError + " <c>comando%s, mas o plugin estava desativado!", completer);
        }

        if (listenerError != 0) {
            String completer = listenerError == 1 ? "" : "s";
            log(" <c>Tentei registrar " + listenerError + " <c>evento%s, mas o plugin estava desativado!", completer);
        }

        commandError = 0;
        listenerError = 0;
    }

    private void onStartPost() {
        long ms = System.nanoTime() - enableTime;
        long total = ms / 1000000L;

        log(" <8>----- <7>Plugin habilitado em <8>" + total + "ms<7>! <8>-----");
    }

    public void log(String text, Object... ss) {
        TXT.print("<8>[<b>" + pluginName + "<8>]" + text, ss);
    }

    public void logs(String... texts) {
        for (String text : texts)
            TXT.print("<8>[<b>" + pluginName + "<8>]" + text);
    }

    public void disablePlugin(Plugin plugin) {
        getServer().getPluginManager().disablePlugin(plugin);
    }

    public void callEvent(Event event) {
        getServer().getPluginManager().callEvent(event);
    }

    public static Plugin getPlugin(String pluginName) {
        return Bukkit.getServer().getPluginManager().getPlugin(pluginName);
    }

    public List<String> getRegisteredPlugins() {
        return hookedPlugins;
    }

    public Map<String, List<DurkGetter>> getAllRegistredCommands() {
        return commands;
    }

    public List<DurkGetter> getRegistredCommandsOf(String pluginName) {
        return commands.get(pluginName);
    }

    public Map<String, List<DurkGetter>> getRegistredCommands() {

        Map<String, List<DurkGetter>> toReturn = Maps.newHashMap();

        commands.forEach((plugin, durkGetters) -> {

            if (plugin.equalsIgnoreCase(pluginName)) {
                toReturn.put(pluginName, durkGetters);
            }
        });

        return toReturn;
    }

    public void setCredits(String pluginName, String author) {
        this.author = author;
        this.credits = " <b>" + pluginName + " <7>desenvolvido por <b>" + author + "<7>!";
    }

    public void setListeners(Listener... events) {
        for (Listener event : events)
            setListener(event);
    }

    public void setListener(Listener event) {
        if (!isEnabled()) {
            listenerError++;
            return;
        }

        Bukkit.getServer().getPluginManager().registerEvents(event, this);
    }

    public void registerCommands(DurkCommand... durkCommands) {
        for (DurkCommand durkCommand : durkCommands)
            registerCommand(durkCommand);
    }

    public void registerCommand(DurkCommand durkCommand) {
        if (!isEnabled()) {
            commandError++;
            return;
        }

        try {

            String command = durkCommand.getCommand().toLowerCase();
            List<String> aliases = durkCommand.getAliases();

            if (command.isEmpty()) throw new Exception();

            Class<?> craftServer = getBukkitClass("CraftServer");
            Method getCommandMap = craftServer.getMethod("getCommandMap");
            SimpleCommandMap simpleCommandMap = (SimpleCommandMap) getCommandMap.invoke(getServer());

            Command originalCommand = new Command(command.trim()) {

                @Override
                public boolean execute(CommandSender sender, String label, String[] args) {
                    onCommand(sender, null, label.toLowerCase(), args);
                    return true;
                }

                @Override
                public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
                    return onTabComplete(sender, alias.toLowerCase(), args);
                }
            };

            int aliasesSize = 0;
            String permission = durkCommand.getPermission();

            if (permission != null) originalCommand.setPermission(permission);

            if (aliases != null && !aliases.isEmpty()) {
                originalCommand.setAliases(aliases);

                for (String aliase : aliases) {
                    if (aliase != null && !aliase.isEmpty() && !" ".equals(aliase) && !aliase.contentEquals(" ")) {
                        registeredCommands.put(aliase, durkCommand);
                        aliasesSize++;
                    }
                }
            }

            simpleCommandMap.register(getPluginName(), originalCommand);
            registeredCommands.put(command, durkCommand);

            DurkGetter durkGetter = new DurkGetter(durkCommand.getCommand(), durkCommand.getDescription(), durkCommand.getPermission(), durkCommand.getAliases());

            if (!commands.containsKey(pluginName)) {
                List<DurkGetter> durkGetters = new ArrayList<>();
                durkGetters.add(durkGetter);

                commands.put(pluginName, durkGetters);
            } else commands.get(pluginName).add(durkGetter);

            log(" <7>Comando <8>\"%s\" <7>registrado com <8>%s %s <7>apelidos.",
                    command,
                    aliasesSize, aliasesSize == 1 ? "apelido" : "apelidos");

        } catch (Exception exception) {
            exception.printStackTrace();

            log(" <c>Ocorreu um erro ao registrar o comando <f>" + durkCommand.getCommand() + "<c>.");
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        try {

            if (!registeredCommands.containsKey(label.toLowerCase())) return true;

            DurkCommand durkCommand = registeredCommands.get(label.toLowerCase());
            durkCommand.setValues(label, sender, args, this);

            if (!durkCommand.canConsolePerform() && (!(sender instanceof Player))) {
                durkCommand.warnConsoleCannotPerform();
                return true;
            }

            String permission = durkCommand.getPermission();

            if (permission != null && !durkCommand.hasPermission(sender, permission)) {
                durkCommand.warnNoPermission();
                return true;
            }

            durkCommand.execute(label, sender, args, this);

        } catch (Exception exception) {
            exception.printStackTrace();

            StringBuilder errors = new StringBuilder();
            for (StackTraceElement stack : exception.getStackTrace()) {
                String clazz = stack.getClassName();

                if (!clazz.contains("org.bukkit")
                        && !clazz.contains("net.minecraft")
                        && !clazz.contains("spigot")) {

                    clazz = clazz.substring(clazz.lastIndexOf(".") + 1);

                    errors.append("<7>").append(clazz).append("<f>: <e>").append(stack.getLineNumber())
                            .append(" <7>(<d>").append(stack.getMethodName()).append("<7>)<f>, ");
                }
            }

            String cmd = label;
            if (args.length != 0) cmd = label + " " + TXT.createString(args, 0);

            if (errors.toString().equals("")) errors.append("<c>Indisponivel");

            String error;

            if (exception.getCause() != null) error = exception.getCause().getLocalizedMessage();
            else error = exception.getMessage();

            sender.sendMessage(TXT.parse("<c>Ocorreu um erro ao executar o comando <7>'" + cmd + "'<c>."));

            BreakLine breakWord = new BreakLine();

            String err = errors.substring(0, errors.length() - 5);
            String formatedErrors = breakWord.addWordOnBreak("  <8>> ", err).get();

            for (Player player : this.getServer().getOnlinePlayers()) {
                if (!player.hasPermission("DurkCore.ADM")) continue;

                TXT.sendMessages(player, ""
                        , " <7><m>---------------------<r> <d>ERRO <7><m>---------------------<r>"
                        , "  <d>Comando com erro<f>: <7>" + cmd
                        , "  <d>Quem usou<f>: <7>" + sender.getName()
                        , "  <d>Erro<f>: <7>" + error
                        , "  <d>O erro se encontra em<f>:"
                        , "  <8>> " + formatedErrors + "<f>."
                        , " <7><m>---------------------<r> <d>ERRO <7><m>---------------------<r>"
                        , "");
            }

            logs(""
                    , " <7><m>---------------------<r> <d>ERRO <7><m>---------------------<r>"
                    , "  <d>Comando com erro<f>: <7>" + cmd
                    , "  <d>Quem usou<f>: <7>" + sender.getName()
                    , "  <d>Erro<f>: <7>" + error
                    , "  <d>O erro se encontra em<f>:"
                    , "  <8>> " + err + "<f>."
                    , " <7><m>---------------------<r> <d>ERRO <7><m>---------------------<r>"
                    , "");

        }

        return false;
    }

    public List<String> onTabComplete(CommandSender sender, String alias, String[] args) {

        try {

            if (!registeredCommands.containsKey(alias.toLowerCase())) return null;

            DurkCommand durkCommand = registeredCommands.get(alias.toLowerCase());
            durkCommand.setValues(alias, sender, args, this);

            if (!durkCommand.canConsolePerform() && (!(sender instanceof Player))) {
                durkCommand.warnConsoleCannotPerform();
                return null;
            }

            String permission = durkCommand.getPermission();

            if (permission != null && !durkCommand.hasPermission(sender, permission)) {
                durkCommand.warnNoPermission();
                return null;
            }

            if (durkCommand.tabComplete() == null) return new ArrayList<>();
            else return durkCommand.tabComplete();

        } catch (Exception ignored) {

        }

        return null;
    }
}