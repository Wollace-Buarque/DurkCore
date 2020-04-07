package dev.cromo29.durkcore.Util;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import net.md_5.bungee.api.chat.*;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class JAction {

    HashMap<Integer, ComponentBuilder> components = new HashMap<>();

    Integer id = 0;
    Integer modifyID = 0;
    ComponentBuilder textBuilder;

    public JAction text(String message, boolean retention){ //TODO: Inserir texto (se não der 'end'/'newLine' não muda de linha)
        if (textBuilder == null)
            textBuilder = new ComponentBuilder("");

        textBuilder = new CustomComponentBuilder(textBuilder, TextComponent.fromLegacyText(TXT.parse(message)));

        if (retention)  //TODO: Para detectar se não é pra pegar o evento do texto atrás
            textBuilder.append("", ComponentBuilder.FormatRetention.NONE);

        return this;
    }
    public JAction text(String message){    //TODO: Inserir texto (se não der 'end'/'newLine' não muda de linha)
        if (textBuilder == null)
            textBuilder = new ComponentBuilder("");

        textBuilder = new CustomComponentBuilder(textBuilder, TextComponent.fromLegacyText(TXT.parse(message)));

        return this;
    }
    public JAction setEvent(String click, ClickEvent.Action clickType){ //TODO: Definir evento ao clicar

        if (textBuilder != null)
            textBuilder.event(new ClickEvent(clickType, TXT.parse(click)));

        return this;
    }
    public JAction setEvent(String hover, HoverEvent.Action hoverType){ //TODO: Definir evento de passar o mouse

        if (textBuilder != null)
            textBuilder.event(new HoverEvent(hoverType, new ComponentBuilder(hover).create()));

        return this;
    }
    public JAction edit(int index){ //TODO: Editar linha
        if (components.containsKey(index)){
            textBuilder = components.get(index);
            modifyID = index;
        }
        return this;
    }
    public JAction end(){ //TODO: Finalizar linha
        textBuilder.append("", ComponentBuilder.FormatRetention.NONE);

        return this;
    }
    public JAction space(int space){
        textBuilder.append(Strings.repeat(" ", space), ComponentBuilder.FormatRetention.NONE);

        return this;
    }
    public JAction newLine(){//TODO: Para começar uma nova linha
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
    public JAction endJson(){ return newLine(); } //TODO: Para finalizar o json

    public JAction delete(){    //TODO: Para deletar a linha atual
        if (modifyID == 0)
            return delete(id);
         else
            return delete(modifyID);
    }

    public JAction delete(int index){   //TODO: Para deletar uma linha
        if (index == id)
            textBuilder.reset();
        else {
            if (components.containsKey(index))
                components.replace(index, new CustomComponentBuilder(""));
        }
        return this;
    }
    public JAction reset(){ //TODO: Para resetar a linha que você está editando
        textBuilder.reset();

        return this;
    }
    public JAction resetAll(){  //TODO: Para resetar todas as linhas
        components = new HashMap<>();
        textBuilder.reset();

        return this;
    }
    public void send(CommandSender sender){ //TODO: Para enviar o json. ( Se não finalizar 'endJson' ele não vai pegar a ultima linha editada )
        Player player = Bukkit.getPlayer(sender.getName());
        if (player != null){
            Player.Spigot playerSpigot = player.spigot();
            Iterator<ComponentBuilder> i = components.values().iterator();

            while (i.hasNext())
                playerSpigot.sendMessage(i.next().create());

            //resetAll();
        }else{
            sender.sendMessage(components.toString());
        }
    }

    public class CustomComponentBuilder extends ComponentBuilder {

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
            BaseComponent[] var3 = components;
            int var4 = components.length;

            for (int var5 = 0; var5 < var4; ++var5) {
                BaseComponent component = var3[var5];

                try {
                    Field current = findFieldInSuperClasses(this.getClass(), "current");
                    current.setAccessible(true);

                    Field parts = findFieldInSuperClasses(this.getClass(), "parts");
                    parts.setAccessible(true);
                    List partsList = (List) parts.get(this);
                    partsList.add(current.get(this));

                    current.set(this, component.duplicate());

                } catch (IllegalAccessException e) {
                    e.printStackTrace();
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
