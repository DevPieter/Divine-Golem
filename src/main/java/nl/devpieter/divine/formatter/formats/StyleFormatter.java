package nl.devpieter.divine.formatter.formats;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import nl.devpieter.divine.formatter.ITextFormatter;

public class StyleFormatter implements ITextFormatter {

    private final String tag;
    private final Style style;

    public StyleFormatter(String tag, Style style) {
        this.tag = tag;
        this.style = style;
    }

    public StyleFormatter(String tag, TextColor color) {
        this(tag, Style.EMPTY.withColor(color));
    }

    public StyleFormatter(String tag, int rgbColor) {
        this(tag, Style.EMPTY.withColor(rgbColor));
    }

    @Override
    public String getTag() {
        return tag;
    }

    @Override
    public MutableComponent format(String content, Style baseStyle) {
        return Component.literal(content).setStyle(style);
    }
}
