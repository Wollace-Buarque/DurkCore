package dev.cromo29.durkcore.Commands;

import dev.cromo29.durkcore.API.DurkCommand;
import dev.cromo29.durkcore.Inventory.Inv;
import dev.cromo29.durkcore.Util.MakeItem;
import dev.cromo29.durkcore.Util.TimeFormat;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.plugin.Plugin;

import java.util.*;

public class Errors extends DurkCommand {

    public static final Map<String, List<ErrorPlugin>> errorPluginMap = new HashMap<>();

    @Override
    public void perform() {

        if (errorPluginMap.isEmpty()) {
            sendMessage("<c>Nenhum erro detectado.");
            return;
        }

        if (isArgsLength(0)) {

            Inv inv = new Inv(54, "<c>Erros de alguns plugins.");

            errorPluginMap.forEach((pluginName, errorPlugins) -> {

                Plugin plugin = getPlugin(pluginName);

                if (plugin == null) return;

                String author = "";

                errorPlugins.sort(Comparator.comparing(ErrorPlugin::getDate));

                if (!plugin.getDescription().getAuthors().isEmpty())
                    author = plugin.getDescription().getAuthors().get(0);

                if (author.isEmpty()) author = plugin.getName();

                String finalAuthor = author;
                errorPlugins.forEach(errorPlugin -> {

                    inv.setInMiddle(new MakeItem(finalAuthor)
                            .setName(" <r>")
                            .addLoreList(
                                    "",
                                    " <d>Comando com erro<f>: <7>" + errorPlugin.getCommand(),
                                    " <d>Quem usou<f>: <7>" + errorPlugin.getUser(),
                                    " <d>Data<f>: <7>" + TimeFormat.getTime(errorPlugin.getDate(), true),
                                    " <d>Erro<f>: <7>" + errorPlugin.getError(),
                                    "",
                                    " <8>Clique aqui para ver o local do erro. ",
                                    "")
                            .build(), event -> {

                        asPlayer().closeInventory();

                        playSound(Sound.CLICK, 1, 1);

                        sendMessages("",
                                " <7><m>---------------------<r> <d>LOCAL <7><m>---------------------<r>",
                                "  <d>O erro se encontra em<f>:",
                                "  <8>> " + errorPlugin.getPath(),
                                " <7><m>---------------------<r> <d>LOCAL <7><m>---------------------<r>",
                                "");

                    });

                });
            });

            playSound(Sound.CHEST_OPEN, 1, 1);

            inv.open(asPlayer());
            sendMessage(" <a>Menu de erros aberto com sucesso.");

        } else if (isArgsLength(1)) {

        }

    }

    @Override
    public boolean canConsolePerform() {
        return false;
    }

    @Override
    public String getPermission() {
        return "DurkCore.*";
    }

    @Override
    public String getCommand() {
        return "erros";
    }

    @Override
    public List<String> getAliases() {
        return null;
    }

    private Plugin getPlugin(String pluginName) {
        Plugin[] plugins = Bukkit.getServer().getPluginManager().getPlugins();

        for (Plugin plugin : plugins) {
            if (plugin.getName().equalsIgnoreCase(pluginName)) return plugin;
        }

        return null;
    }

    public static void addErrorPlugin(String pluginName, String user, String command, String error, String path) {
        List<ErrorPlugin> errorPlugins = errorPluginMap.getOrDefault(pluginName, new ArrayList<>());

        ErrorPlugin errorPlugin = new ErrorPlugin(user, command, error, path);

        if (!errorPlugins.isEmpty()) {

            boolean alreadyRegistered = errorPlugins.stream()
                    .anyMatch(errorPlugin1 -> errorPlugin1.getError().equals(error) && errorPlugin1.getPath().equals(path));

            if (alreadyRegistered) return;

        }
        errorPlugins.add(errorPlugin);
        errorPluginMap.put(pluginName.toLowerCase(), errorPlugins);
    }

    public static class ErrorPlugin {

        private String user, command, error, path;
        private long date;

        public ErrorPlugin(String user, String command, String error, String path) {
            this.user = user;
            this.command = command;
            this.error = error;
            this.path = path;

            this.date = System.currentTimeMillis();
        }

        public String getUser() {
            return user;
        }

        public String getCommand() {
            return command;
        }

        public String getError() {
            return error;
        }

        public String getPath() {
            return path;
        }

        public long getDate() {
            return date;
        }
    }
}
