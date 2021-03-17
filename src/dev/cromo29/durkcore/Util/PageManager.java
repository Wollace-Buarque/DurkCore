package dev.cromo29.durkcore.Util;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import dev.cromo29.durkcore.SpecificUtils.ListUtil;
import dev.cromo29.durkcore.SpecificUtils.MapUtil;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import org.bukkit.command.CommandSender;

import java.util.*;

public class PageManager {

    private int size;
    private List<String> list;
    private Map<String, PageManagerJson> map;
    private boolean useMap, order;

    public PageManager(int size, String... values) {
        this(size, false, values);
    }

    public PageManager(int size, boolean order, String... values) {
        this.size = size;
        this.list = Arrays.asList(values);
        this.useMap = false;
        this.order = order;
    }

    public PageManager(List<String> list, int size) {
        this(list, size, false);
    }

    public PageManager(List<String> list, int size, boolean order) {
        this.size = size;
        this.list = list;
        this.useMap = false;
        this.order = order;
    }

    public PageManager(LinkedHashMap<String, PageManagerJson> map, int size) {
        this(map, size, false);
    }

    public PageManager(LinkedHashMap<String, PageManagerJson> map, int size, boolean order) {
        this.size = size;
        this.map = map;
        this.useMap = true;
        this.order = order;
    }

    public int getTotalPages() {
        if (useMap) {
            double v = ((double) map.size() / (double) size);

            return (map.size() / size) == 0 ? 1 : (v >= Double.parseDouble(((int) v) + ".1") ? ((int) v) + 1 : (int) v);
        }

        double v = ((double) list.size() / (double) size);

        return (list.size() / size) == 0 ? 1 : (v >= Double.parseDouble(((int) v) + ".1") ? ((int) v) + 1 : (int) v);
    }

    public List<JAction> getPageMapList(int page) {
        if (page < 1) page = 0;

        int size = page == 0 ? 1 : (this.size * page) - this.size;

        List<JAction> values = Lists.newArrayList();

        // To order

        Map<String, JAction> valuesMap = Maps.newHashMap();

        try {
            int loop = 0;
            while (loop < this.size) {
                String text = (new ArrayList<>(map.keySet())).get(size + loop);
                PageManagerJson pageManagerJson = map.get(text);

                JAction jAction = new JAction();

                if (text == null) continue;

                jAction.parseText(text);

                if (pageManagerJson != null) {
                    pageManagerJson.setText(text);

                    if (pageManagerJson.getClick() != null && pageManagerJson.getClickEvent() != null) {

                        if (pageManagerJson.getHoverEvent() == null)
                            jAction.setParseEvent(pageManagerJson.getClick(), pageManagerJson.getClickEvent()).end();
                        else jAction.setParseEvent(pageManagerJson.getClick(), pageManagerJson.getClickEvent());
                    }

                    if (pageManagerJson.getHover() != null && pageManagerJson.getHoverEvent() != null)
                        jAction.setParseEvent(pageManagerJson.getHover(), pageManagerJson.getHoverEvent()).end();

                    if (pageManagerJson.getAfterMessage() != null)
                        jAction.parseText(pageManagerJson.getAfterMessage());
                }

                jAction.endJson();

                if (order)
                    valuesMap.put(text, jAction);
                else
                    values.add(jAction);

                loop++;
            }
        } catch (Exception ignored) {
        }

        List<JAction> jActionList = Lists.newArrayList();
        Map<String, JAction> fakeReturn = Maps.newLinkedHashMap();

        int loop = 0;
        int vSize = order ? valuesMap.size() : values.size();

        while (loop < vSize) {
            if (order) {

                String text = (new ArrayList<>(valuesMap.keySet()).get(loop));
                JAction get = valuesMap.get(text);
                loop++;

                get = get.newLine();

                fakeReturn.put(text, get);
            } else {

                JAction get = values.get(loop);
                loop++;

                get = get.newLine();

                jActionList.add(get);
            }
        }

        if (order) return ListUtil.invertList(new ArrayList<>(MapUtil.orderByKeyString(fakeReturn).values()));
        else return jActionList;
    }

    public String getPage(int page) {
        if (page < 1) page = 0;

        int size = page == 0 ? 1 : (this.size * page) - this.size;
        List<String> values = Lists.newArrayList();

        if (order) Collections.sort(list);

        try {
            int loop = 0;
            while (loop < this.size) {
                values.add("  " + list.get(size + loop));
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

        if (length > 1) format.substring(length - 2, length);

        return format.toString();
    }

    public void sendPage(int page, String next_command, String back_command, CommandSender sender) {
        if (page == 0) page = 1;

        if (page > getTotalPages()) page = 1;

        sender.sendMessage(TXT.parse(" <6><m>---------------------<r> <b>Página: <f>" + page + " <6><m>---------------------<r>"));

        if (useMap)
            getPageMapList(page).forEach(jAction -> jAction.send(sender));
        else sender.sendMessage(TXT.parse(getPage(page)));

        JAction json = new JAction();

        json.parseText(" <6><m>-----------------<r> ");

        if ((page - 1) < 1)
            json.parseText("<8>[<]");
        else json.parseText("<e>[<]")
                    .setParseEvent("<e>Clique para ir à página anterior.", HoverEvent.Action.SHOW_TEXT)
                    .setEvent("/" + back_command + " " + (page - 1), ClickEvent.Action.RUN_COMMAND).end();


        json.parseText(" <b>Página: <f>" + page + " / " + getTotalPages() + " ");

        if (getTotalPages() - page == 0)
            json.parseText("<8>[>]");
        else json.parseText("<e>[>]")
                    .setParseEvent("<e>Clique para ir à próxima página.", HoverEvent.Action.SHOW_TEXT)
                    .setEvent("/" + next_command + " " + (page + 1), ClickEvent.Action.RUN_COMMAND).end();

        json.parseText(" <6><m>----------------<r>").endJson();
        json.send(sender);
    }
}
