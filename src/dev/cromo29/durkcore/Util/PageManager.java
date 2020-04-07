package dev.cromo29.durkcore.Util;

import com.google.common.collect.Lists;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;

public class PageManager {

    private int size;
    private List<Object> list;

    public PageManager(int size, Object... values) {
        this.size = size;
        this.list = Arrays.asList(values);
    }

    public PageManager(List list, int size) {
        this.size = size;
        this.list = list;
    }

    public int getTotalPages() {
        double v = ((double) list.size() / (double) size);
        return (list.size() / size) == 0 ? 1 : (v >= Double.parseDouble(((int) v) + ".1") ? ((int) v) + 1 : (int) v);
    }

    public String getPage(int page) {
        if (page < 1) {
            page = 0;
        }

        int size = page == 0 ? 1 : (this.size * page) - this.size;
        List<String> values = Lists.newArrayList();

        try {
            int loop = 0;
            while (loop < this.size) {
                values.add("  " + list.get(size + loop).toString());
                loop++;
            }
        } catch (Exception ignored) {
        }

        StringBuilder format = new StringBuilder();
        int loop = 0;
        int vSize = values.size();

        while (loop < vSize) {
            String get = values.get(loop);
            loop++;
            format.append(get).append("\n");
        }
        int length = format.length();

        if (length > 1)
            format.substring(length - 2, length);

        return format.toString();
    }

    public void sendPage(int page, String next_command, String back_command, CommandSender p) {

        if (page == 0) page = 1;
        if (page > getTotalPages()) {
            page = 1;
        }

        p.sendMessage(TXT.parse(" <6><m>---------------------<r> <b>Página: <f>" + page + " <6><m>---------------------<r>"));
        p.sendMessage(TXT.parse(getPage(page)));

        JAction json = new JAction();

        json.text(" <6><m>-----------------<r> ");
        if ((page - 1) < 1)
            json.text("<8>[<]");
        else
            json.text("<e>[<]").setEvent(TXT.parse("<e>Clique para ir à página anterior."), HoverEvent.Action.SHOW_TEXT).setEvent("/" + back_command + " " + (page - 1), ClickEvent.Action.RUN_COMMAND).end();

        json.text(" <b>Página: <f>" + page + " / " + getTotalPages() + " ");

        if (getTotalPages() - page == 0)
            json.text("<8>[>]");
        else
            json.text("<e>[>]").setEvent(TXT.parse("<e>Clique para ir à próxima página."), HoverEvent.Action.SHOW_TEXT).setEvent("/" + next_command + " " + (page + 1), ClickEvent.Action.RUN_COMMAND).end();

        json.text(" <6><m>----------------<r>").endJson();
        json.send(p);
    }


}
