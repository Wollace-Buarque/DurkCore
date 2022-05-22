package dev.cromo29.durkcore.commands;

import dev.cromo29.durkcore.api.DurkCommand;
import dev.cromo29.durkcore.inventory.Inv;
import dev.cromo29.durkcore.util.MakeItem;
import dev.cromo29.durkcore.util.TimeFormat;
import org.apache.commons.lang3.StringUtils;
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

            Iterator<Map.Entry<String, List<ErrorPlugin>>> iterator = errorPluginMap.entrySet().iterator();

            while (iterator.hasNext()) {
                Map.Entry<String, List<ErrorPlugin>> next = iterator.next();
                String pluginName = next.getKey();
                List<ErrorPlugin> errorPlugins = next.getValue();

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
                                    " <d>Plugin com erro<f>: <7>" + plugin.getName(),
                                    " <d>Comando<f>: <7>/" + StringUtils.capitalize(errorPlugin.getCommand()),
                                    " <d>Sujeito<f>: <7>" + errorPlugin.getUser(),
                                    " <d>Data<f>: <7>" + TimeFormat.getTime(errorPlugin.getDate(), true),
                                    " <d>Erro<f>: <7>" + errorPlugin.getError(),
                                    "",
                                    " <8>Clique com o botão esquerdo para ver o local do erro. ",
                                    " <8>Clique com o botão direito para remover. ",
                                    "")
                            .build(), event -> {

                        asPlayer().closeInventory();

                        playSound(Sound.CLICK, 1, 1);

                        if (event.isRightClick()) {
                            iterator.remove();
                            sendMessages(
                                    "",
                                    "  <d>Você removeu o aviso de erro do plugin <7>" + plugin.getName() + "<d>!",
                                    "");
                            return;
                        }

                        sendMessages("",
                                " <7><m>---------------------<r> <d>LOCAL <7><m>---------------------<r>",
                                "  <d>O erro se encontra em<f>:",
                                "  <8>> " + errorPlugin.getPath(),
                                " <7><m>---------------------<r> <d>LOCAL <7><m>---------------------<r>",
                                "");

                    });

                });
            }

            playSound(Sound.CHEST_OPEN, 1, 1);

            inv.open(asPlayer());

            sendMessage(" <a>Menu de erros aberto com sucesso.");
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

        private final String user, command, error, path;
        private final long date;

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
