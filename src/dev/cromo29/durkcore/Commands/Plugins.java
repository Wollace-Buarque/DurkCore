package dev.cromo29.durkcore.Commands;

import dev.cromo29.durkcore.API.DurkCommand;
import dev.cromo29.durkcore.API.DurkPlugin;
import dev.cromo29.durkcore.Util.TXT;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;

public class Plugins extends DurkCommand {

    @Override
    public void perform() {
        List<String> pl = new ArrayList<>();

        for (String pls : DurkPlugin.getPlugins())
            if (Bukkit.getPluginManager().getPlugin(pls).isEnabled())
                pl.add("<a>" + pls);
            else
                pl.add("<c>" + pls);

        String plugins = TXT.createString(pl.toArray(new String[0]), 0, "<f>, ") + "<f>.";
        int size = DurkPlugin.getPlugins().size();

        sendMessage("DurkPlugins <e>(<b>" + size + "<e>): " + plugins);
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
        return getList("durkplugins", "durkplugin", "dplugin");
    }

    @Override
    public String getDescription() {
        return "Lista de plugins usando a DurkCore.";
    }
}