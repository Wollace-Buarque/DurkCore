package dev.cromo29.durkcore.util.json;

import dev.cromo29.durkcore.util.json.fanciful.FancyMessage;
import dev.cromo29.durkcore.util.json.util.TextUtil;

public class LegacyMessagePart implements MessagePart {

    private final String text;
    private final boolean colorize;

    public LegacyMessagePart(String text) {
        this.text = text;
        this.colorize = true;
    }

    public LegacyMessagePart(String text, boolean colorize) {
        this.text = text;
        this.colorize = colorize;
    }

    @Override
    public FancyMessage append(FancyMessage fancyMessage) {
        TextUtil textUtil = new TextUtil();

        LegacyConverter.getMessageParts(fancyMessage.getLastColors() + (colorize ? textUtil.stylish(text) : text)).forEach(fancyMessage::then);
        return fancyMessage;
    }

    public String getText() {
        return text;
    }
}
