package dev.cromo29.durkcore.API;

import com.google.common.collect.Maps;
import dev.cromo29.durkcore.Util.TXT;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class DurkPlugin extends JavaPlugin implements Listener {

    private String name, version, credits, author;
    private long enableTime;

    //  Static to get dates off all plugins.
    private static HashMap<String, List<DurkGetter>> commands = new HashMap<>();
    private static List<String> hookedPlugins = new ArrayList<>();

    public DurkPlugin() {
        this.name = getDescription().getName();
        this.version = getDescription().getVersion();

        List<String> authors = getDescription().getAuthors();
        if (authors != null && !authors.isEmpty())
            this.author = authors.get(0);

        this.enableTime = 0L;
        this.credits = " <b>" + name + " <e>desenvolvido por <b>" + (author == null ? "?" : author) + "<e>.";
    }

    public DurkPlugin(String pluginName, String author) {
        this.author = author;
        this.name = getDescription().getName();
        this.version = getDescription().getVersion();
        this.enableTime = 0L;
        this.credits = " <b>" + pluginName + " <e>desenvolvido por <b>" + author + "<e>.";
    }

    public String getPluginName() {
        return name;
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
        if (!hookedPlugins.contains(name))
            hookedPlugins.add(name);

        enableTime = System.nanoTime();

        logs(" <e>----- <a>Habilitando plugin... <e>-----", "");
        this.onStart();

        logs("", credits, "");
        this.onStartPost();

        if (!this.isEnabled()) return;

        this.getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll((Plugin) this);
        this.onStop();
    }

    private void onStartPost() {
        long ms = System.nanoTime() - enableTime;
        long total = ms / 1000000L;

        log(" <e>----- <a>Plugin habilitado em <b>" + total + "ms<a>! <e>-----");
    }

    public void log(String text, Object... ss) {
        TXT.print("<f>[<b>" + name + "<f>]" + text, ss);
    }

    public void logs(String... text) {
        for (String s : text)
            TXT.print("<f>[<b>" + name + "<f>]" + s);
    }

    public void disablePlugin(Plugin pl) {
        getServer().getPluginManager().disablePlugin(pl);
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

    public HashMap<String, List<DurkGetter>> getAllRegistredCommands() {
        return commands;
    }

    public List<DurkGetter> getRegistredCommandsOf(String pluginName) {
        return commands.get(pluginName);
    }

    public HashMap<String, List<DurkGetter>> getRegistredCommands() {

        HashMap<String, List<DurkGetter>> toReturn = Maps.newHashMap();

        commands.forEach((plugin, durkGetters) -> {

            if (plugin.equalsIgnoreCase(name)) {
                toReturn.put(name, durkGetters);
            }
        });

        return toReturn;
    }

    public void setCredits(String pluginName, String author) {
        this.author = author;
        this.credits = " <b>" + pluginName + " <e>desenvolvido por <b>" + author + "<e>.";
    }

    public void setListeners(Listener... events) {
        for (Listener event : events)
            setListener(event);
    }

    public void setListener(Listener event) {
        Bukkit.getServer().getPluginManager().registerEvents(event, this);
    }

    public void registerCommands(DurkCommand... durkCommands) {
        for (DurkCommand durkCommand : durkCommands)
            registerCommand(durkCommand);
    }

    public void registerCommand(DurkCommand durkCommand) {
        DurkCommand.ReflectCommand cmd = new DurkCommand.ReflectCommand(durkCommand.getCommand());
        List<String> toReturn = new ArrayList<>();

        if (durkCommand.getAliases() != null) {

            for (String aliase : durkCommand.getAliases()) {
                if (!durkCommand.getCommand().equalsIgnoreCase(aliase))

                    toReturn.add(aliase);
            }
        }

        if (!toReturn.isEmpty()) {
            durkCommand.setAliases(toReturn);
            cmd.setAliases(toReturn);
        }

        if (durkCommand.getPermission() != null)
            cmd.setPermissionMessage(TXT.parse("<c>Você não tem permissão para usar esse comando."));

        DurkCommand.getCommandMap().register(name, cmd);
        cmd.setExecutor(durkCommand);

        DurkGetter durkGetter = new DurkGetter(durkCommand.getCommand(), durkCommand.getDescription(), durkCommand.getPermission(), toReturn);

        if (commands.containsKey(name))
            commands.get(name).add(durkGetter);
        else {
            List<DurkGetter> durkGetters = new ArrayList<>();
            durkGetters.add(durkGetter);

            commands.put(name, durkGetters);
        }

        int lenght = toReturn.size();

        this.log(" <a>Comando <7>\"" + durkCommand.getCommand() + "\" <a>registrado com <7>" + lenght + " apelidos<a>.");
    }
}