package dev.cromo29.durkcore.API;

import dev.cromo29.durkcore.Util.TXT;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public abstract class DurkPlugin extends JavaPlugin implements Listener {

    private String name, version, credits, author;
    private long enableTime;

    private static HashMap<String, String> commands = new HashMap<>();
    private static List<String> hookedPlugins = new ArrayList<>();

    public DurkPlugin() {
        this.name = getDescription().getName();
        this.version = getDescription().getVersion();
        this.enableTime = 0L;
        this.credits = " <b>" +  name + " <e>desenvolvido por <b>?<e>.";
    }

    public DurkPlugin(String pluginName, String author) {
        pluginName = TXT.parse(pluginName);
        this.author = TXT.parse(author);

        this.name = getDescription().getName();
        this.version = getDescription().getVersion();
        this.enableTime = 0L;
        this.credits = TXT.parse(" <b>" + pluginName + " <e>desenvolvido por <b>" + author + "<e>.");
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

        log(" <e>----- <a>Habilitando plugin... <e>-----", "");
        this.onStart();

        log("", credits, "");
        this.onStartPost();
        this.getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll((Plugin) this);
        this.onStop();
    }

    public void log(String... text) {
        for (String b : text)
            TXT.print("<f>[<b>" + name + "<f>]" + b);
    }

    public void log(String text, Object... ss) {
        TXT.print("<f>[<b>" + name + "<f>]" + text, ss);
    }

    public void setListener(Listener events) {
        Bukkit.getServer().getPluginManager().registerEvents(events, this);
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

    public static List<String> getPlugins() {
        return hookedPlugins;
    }

    public static HashMap<String, String> getRegistredCommands() {
        return commands;
    }

    private void onStartPost() {
        long ms = System.nanoTime() - enableTime;
        long total = ms / 1000000L;

        log(" <e>----- <a>Plugin habilitado em <b>" + total + "ms<a>! <e>-----");
    }

    protected void setCredits(String name, String author) {
        name = TXT.parse(name);
        this.author = TXT.parse(author);

        this.credits = TXT.parse(" <b>" + name + " <e>desenvolvido por <b>" + author + "<e>.");
    }

    protected void unregisterCommand(DurkCommand durk) {
        Command cmd = DurkCommand.getCommandMap().getCommand(durk.getCommand());

        commands.remove(getDescription().getName() + "-" + durk.getCommand());

        this.log(" <a>Comando <f>'" + durk.getCommand() + "' <a>desregistrado com <f>" + (durk.getAliases() != null ? durk.getAliases().size() : 0) + " apelidos<a>.");

        DurkCommand.getCommandMap().getCommand(cmd.getName()).unregister(DurkCommand.getCommandMap());
    }

    protected void registerCommand(DurkCommand durk) {
        DurkCommand.ReflectCommand cmd = new DurkCommand.ReflectCommand(durk.getCommand());

        if (durk.getAliases() != null)
            cmd.setAliases(durk.getAliases());

        if (durk.getPermission() != null)
            cmd.setPermissionMessage(TXT.parse("<c>Você não tem permissão para usar esse comando."));

        DurkCommand.getCommandMap().register(cmd.getName(), cmd);
        cmd.setExecutor(durk);

        commands.put(getDescription().getName() + "-" + durk.getCommand(),
                getDescription().getName() + "-" + (durk.getDescription() == null ? "Nenhuma." : durk.getDescription()));

        this.log(" <a>Comando <f>'" + durk.getCommand() + "' <a>registrado com <f>" + (durk.getAliases() != null ? durk.getAliases().size() : 0) + " apelidos<a>.");
    }
}