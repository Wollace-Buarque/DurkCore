package dev.cromo29.durkcore.Util;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import net.md_5.bungee.api.chat.*;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

public class JAction {

    private Map<Integer, ComponentBuilder> components = Maps.newHashMap();

    private Integer id = 0;
    private Integer modifyID = 0;
    private ComponentBuilder textBuilder;

    private static Class<?> nbtTagCompoundClass;
    private static Method asNMSCopyMethod;
    private static Method saveMethod;

    static {

        try {

            nbtTagCompoundClass = RU.getNMSClass("NBTTagCompound");
            asNMSCopyMethod = RU.getBukkitClass("inventory.CraftItemStack").getMethod("asNMSCopy", ItemStack.class);
            saveMethod = RU.getNMSClass("ItemStack").getMethod("save", nbtTagCompoundClass);

        } catch (Exception ignored) {
        }
    }

    public JAction setItem(String text, ItemStack item) {
        return setItem(text, false, item);
    }

    public JAction setParsedItem(String text, ItemStack item) {
        return setItem(text, true, item);
    }

    private JAction setItem(String text, boolean parse, ItemStack item) {

        if (textBuilder == null) textBuilder = new ComponentBuilder("");
        try {

            Object nmsItemStack = asNMSCopyMethod.invoke(null, item);
            Object nbtTagCompound = saveMethod.invoke(nmsItemStack, nbtTagCompoundClass.newInstance());

            if (parse) parseText(text);
            else text(text);

            textBuilder.event(new HoverEvent(HoverEvent.Action.SHOW_ITEM, new ComponentBuilder(nbtTagCompound.toString()).create()));

        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return this;

    }

    public JAction parseText(String message, boolean retention) {
        return text(TXT.parse(message), retention);
    }

    public JAction parseText(String message) {
        return text(TXT.parse(message));
    }

    public JAction setParseEvent(String hover, HoverEvent.Action hoverType) {
        return setEvent(TXT.parse(hover), hoverType);
    }

    public JAction setParseEvent(String click, ClickEvent.Action clickType) {
        return setEvent(TXT.parse(click), clickType);
    }

    public JAction text(String message, boolean retention) {    //TODO: Inserir texto (se não der 'end'/'newLine' não muda de linha)
        if (textBuilder == null) textBuilder = new ComponentBuilder("");

        textBuilder = new CustomComponentBuilder(textBuilder, TextComponent.fromLegacyText(message));

        //TODO: Para detectar se não é pra pegar o evento do texto atrás
        if (retention) textBuilder.append("", ComponentBuilder.FormatRetention.NONE);

        return this;
    }

    public JAction text(String message) {   //TODO: Inserir texto (se não der 'end'/'newLine' não muda de linha)
        if (textBuilder == null) textBuilder = new ComponentBuilder("");

        textBuilder = new CustomComponentBuilder(textBuilder, TextComponent.fromLegacyText(message));

        return this;
    }

    public JAction setEvent(String click, ClickEvent.Action clickType) {    //TODO: Definir evento ao clicar
        if (textBuilder == null) textBuilder = new ComponentBuilder("");

        textBuilder.event(new ClickEvent(clickType, click));

        return this;
    }

    public JAction setEvent(String hover, HoverEvent.Action hoverType) {    //TODO: Definir evento de passar o mouse
        if (textBuilder == null) textBuilder = new ComponentBuilder("");

        textBuilder.event(new HoverEvent(hoverType, new ComponentBuilder(hover).create()));

        return this;
    }

    public JAction edit(int index) {    //TODO: Editar linha
        if (components.containsKey(index)) {
            textBuilder = components.get(index);
            modifyID = index;
        }
        return this;
    }

    public JAction end() {  //TODO: Finalizar linha
        if (textBuilder == null) textBuilder = new ComponentBuilder("");

        textBuilder.append("", ComponentBuilder.FormatRetention.NONE);

        return this;
    }

    public JAction space(int space) { //TODO: Adicionar 'x' quantidade de espaços
        if (textBuilder == null) textBuilder = new ComponentBuilder("");

        textBuilder.append(Strings.repeat(" ", space), ComponentBuilder.FormatRetention.NONE);

        return this;
    }

    public JAction newLine() {  //TODO: Para começar uma nova linha
        if (textBuilder != null) {

            if (modifyID == 0)
                components.put(modifyID, textBuilder);
            else {
                components.put(id, textBuilder);
                id++;
            }

            textBuilder = null;
            modifyID = 0;
        }
        return this;
    }

    public JAction endJson() {   //TODO: Para finalizar o json
        return newLine();
    }

    public JAction delete() {   //TODO: Para deletar a linha atual
        if (modifyID == 0)
            return delete(id);
        else return delete(modifyID);

    }

    public JAction delete(int index) {  //TODO: Para deletar uma linha
        if (textBuilder == null) textBuilder = new ComponentBuilder("");

        if (index == id)
            textBuilder.reset();
        else if (components.containsKey(index))
            components.replace(index, new CustomComponentBuilder(""));

        return this;
    }

    public JAction reset() {    //TODO: Para resetar a linha que você está editando
        textBuilder.reset();
        return this;
    }

    public JAction resetAll() { //TODO: Para resetar todas as linhas
        components = new HashMap<>();
        textBuilder.reset();
        return this;
    }

    public void send(CommandSender sender) {    //TODO: Para enviar o json. ( Se não finalizar 'endJson' ele não vai pegar a ultima linha editada )
        send(sender, false);
    }

    public void send(CommandSender sender, boolean reset) {    //TODO: Para enviar o json. ( Se não finalizar 'endJson' ele não vai pegar a ultima linha editada )
        Player player = Bukkit.getPlayer(sender.getName());

        if (player != null) {
            Player.Spigot playerSpigot = player.spigot();

            for (ComponentBuilder componentBuilder : components.values())
                playerSpigot.sendMessage(componentBuilder.create());

        } else {
            StringBuilder stringBuilder = new StringBuilder();

            for (ComponentBuilder componentBuilder : components.values()) {
                TextComponent component = new TextComponent(componentBuilder.create());

                stringBuilder.append(component.toLegacyText());
            }
            sender.sendMessage(stringBuilder.toString());

        }

        if (reset)
            resetAll();
    }

    public static class CustomComponentBuilder extends ComponentBuilder {

        public CustomComponentBuilder(ComponentBuilder original) {
            super(original);
        }

        public CustomComponentBuilder(String text) {
            super(text);
        }

        public CustomComponentBuilder(ComponentBuilder original, BaseComponent[] components) {
            super(original);
            append(components);
        }

        public net.md_5.bungee.api.chat.ComponentBuilder append(BaseComponent[] components) {
            return this.append(components, net.md_5.bungee.api.chat.ComponentBuilder.FormatRetention.ALL);
        }

        public net.md_5.bungee.api.chat.ComponentBuilder append(BaseComponent[] components, net.md_5.bungee.api.chat.ComponentBuilder.FormatRetention retention) {
            Preconditions.checkArgument(components.length != 0, "No components to append");

            for (BaseComponent component : components) {
                try {
                    Field current = findFieldInSuperClasses(this.getClass(), "current");
                    current.setAccessible(true);

                    Field parts = findFieldInSuperClasses(this.getClass(), "parts");
                    parts.setAccessible(true);

                    List partsList = (List) parts.get(this);
                    partsList.add(current.get(this));

                    current.set(this, component.duplicate());

                } catch (IllegalAccessException exception) {
                    exception.printStackTrace();
                }

                this.retain(retention);
            }

            return this;
        }

        private Field findFieldInSuperClasses(Class<?> classy, String name) {
            Class<?> current = classy;
            do {
                try {
                    return current.getDeclaredField(name);
                } catch (Exception ignored) {
                }
            } while ((current = current.getSuperclass()) != null);
            return null;
        }
    }
}
