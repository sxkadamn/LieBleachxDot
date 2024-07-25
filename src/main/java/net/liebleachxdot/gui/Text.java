package net.liebleachxdot.gui;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Text implements Serializable {

    private static final Pattern pattern = Pattern.compile("(?<!\\\\)(#[a-fA-F0-9]{6})");

    private String text;
    private String hoverText;
    private String clickCommand;
    private String suggestCommand;
    private String clickURL;
    private String clickBuffer;
    private TextComponent component;
    private boolean colorize;

    public Text() {
        text = "";
    }

    public Text(String text) {
        if (text == null) text = "";
        this.text = ChatColor.translateAlternateColorCodes('&', text);
        this.colorize = true;
    }

    public Text(String text, boolean colorize) {
        if (text == null) text = "";
        if (colorize)
            text = ChatColor.translateAlternateColorCodes('&', text);
        this.text = text;
        this.colorize = colorize;
    }

    public String getText() {
        return text;
    }

    public String getRaw() {
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            String hex = text.substring(matcher.start(), matcher.end());
            text = text.replace(hex, "" + ChatColor.of(hex));
        }
        return text;
    }

    public Text setText(String text) {
        if (text == null) text = "";
        this.text = colorize ? ChatColor.translateAlternateColorCodes('&', text) : text;
        if (component != null) component.setText(this.text);
        return this;
    }

    public String getHoverText() {
        return hoverText;
    }

    public Text setHoverText(String hoverText) {
        this.hoverText = ChatColor.translateAlternateColorCodes('&', hoverText);
        createComponent();
        component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(this.hoverText).create()));
        return this;
    }

    public Text setHoverText(Text hoverText) {
        this.hoverText = ChatColor.translateAlternateColorCodes('&', hoverText.getRaw());
        createComponent();
        component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(this.hoverText).create()));
        return this;
    }

    public String getClickCommand() {
        return clickCommand;
    }

    public Text setSuggestCommand(String suggestCommand) {
        this.suggestCommand = suggestCommand;
        createComponent();
        component.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, suggestCommand));
        return this;
    }

    public Text setSuggestCommand(Text suggestCommand) {
        this.suggestCommand = suggestCommand.getRaw();
        createComponent();
        component.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, suggestCommand.getRaw()));
        return this;
    }

    public String getSuggestCommand() {
        return suggestCommand;
    }

    public Text setClickCommand(String clickCommand) {
        this.clickCommand = clickCommand;
        createComponent();
        component.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, clickCommand));
        return this;
    }

    public Text setClickCommand(Text clickCommand) {
        this.clickCommand = clickCommand.getRaw();
        createComponent();
        component.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, clickCommand.getRaw()));
        return this;
    }

    public String getClickURL() {
        return clickURL;
    }

    public Text setClickURL(String clickURL) {
        this.clickURL = clickURL;
        createComponent();
        if (clickCommand == null) component.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, clickURL));
        return this;
    }

    public Text setClickURL(Text clickURL) {
        this.clickURL = clickURL.getRaw();
        createComponent();
        if (clickCommand == null)
            component.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, clickURL.getRaw()));
        return this;
    }

    public String getClickBuffer() {
        return clickBuffer;
    }

    public Text setClickBuffer(String clickBuffer) {
        this.clickBuffer = clickBuffer;
        createComponent();
        if (clickCommand == null)
            component.setClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, clickBuffer));
        return this;
    }

    public Text setClickBuffer(Text clickBuffer) {
        this.clickBuffer = clickBuffer.getRaw();
        createComponent();
        if (clickCommand == null)
            component.setClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, clickBuffer.getRaw()));
        return this;
    }

    public boolean hasComponent() {
        return component != null;
    }

    public TextComponent getComponent() {
        createComponent();
        return component;
    }

    private void createComponent() {
        if (component == null) {
            if (colorize) {
                component = new TextComponent();
                int i = 0;
                String lastColor = "";
                if (text.startsWith(" ")) component.addExtra(" ");
                for (String part : text.split(" ")) {
                    String colorized = !Text.getColorCodes(part).equals("") ? part : lastColor + part;
                    TextComponent extra = new TextComponent((i == 0 ? "" : " ") + colorized);
                    Matcher matcher = pattern.matcher(part);
                    if (matcher.find()) {
                        String hex = part.substring(matcher.start(), matcher.end());
                        extra.setText(part.replace(hex, ""));
                        extra.setColor(ChatColor.of(hex));
                    }
                    lastColor = Text.getColorCodes(colorized);
                    component.addExtra(extra);
                    i++;
                }
                if (!text.equals(" ") && text.endsWith(" ")) component.addExtra(" ");
            } else component = new TextComponent(text);
        }
    }

//    public TextComponent getComponent() {
//        if (component == null)
//            component = new TextComponent(text);
//        return component;
//    }

    public Text setComponent(TextComponent component) {
        this.component = component;
        this.text = component.getText();
        return this;
    }

    public Text removeColor(TextPosition position) {
        if (position == TextPosition.TEXT) {
            text = text.replace("§", "&");
            if (component != null) component.setText(text);
        } else {
            hoverText = hoverText.replace("§", "&");
            if (component == null) component = new TextComponent(text);
            component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(hoverText).create()));
        }
        return this;
    }

    public enum TextPosition {
        TEXT,
        HOVER
    }

    @Override
    public String toString() {
        return text;
    }

    public static Text emptyText() {
        return new Text(" ");
    }

    private static String getColorCodes(String text) {
        char[] array = text.toCharArray();
        StringBuilder codes = new StringBuilder();
        for (int i = 0; i < array.length - 1; i++) {
            if ((array[i] == ChatColor.COLOR_CHAR || array[i] == '&') &&
                    "0123456789abcdefklmnorx".contains(String.valueOf(array[i + 1]).toLowerCase()))
                codes.append("§").append(array[i + 1]);
            else break;
        }
        return codes.toString();
    }
}
