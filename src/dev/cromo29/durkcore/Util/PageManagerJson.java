package dev.cromo29.durkcore.Util;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;

public class PageManagerJson {

    private String text, hover, click, afterMessage;
    private HoverEvent.Action hoverEvent;
    private ClickEvent.Action clickEvent;

    public PageManagerJson() {
        this.text = null;
        this.hover = null;
        this.hoverEvent = null;
        this.click = null;
        this.clickEvent = null;
        this.afterMessage = null;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public String getHover() {
        return hover;
    }

    public void setHover(String hover) {
        this.hover = hover;
    }

    public String getClick() {
        return click;
    }

    public void setClick(String click) {
        this.click = click;
    }

    public String getAfterMessage() {
        return afterMessage;
    }

    public void setAfterMessage(String afterMessage) {
        this.afterMessage = afterMessage;
    }

    public HoverEvent.Action getHoverEvent() {
        return hoverEvent;
    }

    public void setHoverEvent(HoverEvent.Action hoverEvent) {
        this.hoverEvent = hoverEvent;
    }

    public ClickEvent.Action getClickEvent() {
        return clickEvent;
    }

    public void setClickEvent(ClickEvent.Action clickEvent) {
        this.clickEvent = clickEvent;
    }
}
