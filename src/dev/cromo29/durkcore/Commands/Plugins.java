package dev.cromo29.durkcore.Commands;

import dev.cromo29.durkcore.API.DurkCommand;
import dev.cromo29.durkcore.DurkCore;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;

public class Plugins extends DurkCommand {

    @Override
    public void perform() {
        List<String> pluginList = new ArrayList<>();

        List<String> plugins = DurkCore.durkCore.getRegisteredPlugins();

        for (String plugin : plugins)
            if (Bukkit.getPluginManager().getPlugin(plugin).isEnabled())
                pluginList.add("<7>" + plugin);
            else
                pluginList.add("<4>" + plugin);

        String message = createString(pluginList.toArray(new String[0]), 0, "<e>, ") + "<e>.";
        int size = plugins.size();

        sendMessage("<e>DurkPlugins (<d>" + size + "<e>): " + message);
    }

    @Override
    public boolean canConsolePerform() {
        return true;
    }

    @Override
    public String getPermission() {
        return "29DurkCore.ADM";
    }

    @Override
    public String getCommand() {
        return "dplugins";
    }

    @Override
    public List<String> getAliases() {
        return getList("durkplugins", "durkplugin", "dplugin", "dplugins", "dplugins");
    }

    @Override
    public String getDescription() {
        return "Lista de plugins usando a DurkCore.";
    }
}