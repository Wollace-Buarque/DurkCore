package dev.cromo29.durkcore.commands;

import dev.cromo29.durkcore.api.DurkCommand;
import dev.cromo29.durkcore.DurkCore;
import dev.cromo29.durkcore.specificutils.StringUtil;
import dev.cromo29.durkcore.util.JAction;
import dev.cromo29.durkcore.util.TXT;
import net.md_5.bungee.api.chat.HoverEvent;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class Plugins extends DurkCommand {

    @Override
    public void perform() {
        List<String> plugins = DurkCore.durkCore.getRegisteredPlugins();

        JAction jAction = new JAction();
        boolean comma = false;

        sendMessages("", "<8>DurkPlugins (<d>" + plugins.size() + "<8>):");
        for (String pluginName : plugins) {

            Plugin plugin = Bukkit.getPluginManager().getPlugin(pluginName);

            String author = "Desconhecido";

            if (plugin.getDescription().getAuthors() != null && !plugin.getDescription().getAuthors().isEmpty()) {
                author = TXT.createString(plugin.getDescription().getAuthors().toArray(new String[0]), 0, "<f>, <7>");
                author = StringUtil.replaceLast(author, ",", " e");
            }

            List<String> depends = new ArrayList<>();

            if (plugin.getDescription().getDepend() != null) depends = plugin.getDescription().getDepend();

            String dependencies = "Nenhuma";

            if (!depends.isEmpty()) {
                dependencies = TXT.createString(depends.toArray(new String[0]), 0, "<f>, <7>");
                dependencies = StringUtil.replaceLast(dependencies, ",", " e");
            }

            if (plugin.isEnabled()) {
                String text = (comma ? ", " : "") + "<a>" + pluginName;
                jAction.parseText(text)
                        .setParseEvent(
                                "<8>Plugin: <7>" + pluginName + "<f>."
                                        + "\n<8>Versão: <7>" + plugin.getDescription().getVersion() + "<f>."
                                        + "\n<8>Autor(es): <7>" + author + "<f>."
                                        + "\n<8>Dependência(s): <7>" + dependencies + "<f>.",
                                HoverEvent.Action.SHOW_TEXT);
            } else {
                String text = (comma ? ", " : "") + "<c>" + pluginName;
                jAction.parseText(text)
                        .setParseEvent(
                                "<8>Plugin: <7>" + pluginName + "<f>."
                                        + "\n<8>Versão: <7>" + plugin.getDescription().getVersion() + "<f>."
                                        + "\n<8>Autor(es): <7>" + author + "<f>."
                                        + "\n<8>Dependência(s): <7>" + dependencies + "<f>.",
                                HoverEvent.Action.SHOW_TEXT);
            }

            comma = true;
        }

        jAction.parseText("<f>.").endJson().send(getSender());
        sendMessage("");
    }

    @Override
    public boolean canConsolePerform() {
        return true;
    }

    @Override
    public String getPermission() {
        return "29DurkCore.*";
    }

    @Override
    public String getCommand() {
        return "dplugins";
    }

    @Override
    public List<String> getAliases() {
        return getList("durkplugins", "durkplugin", "dplugin", "dplugins");
    }

    @Override
    public String getDescription() {
        return "Lista de plugins usando a DurkCore.";
    }
}