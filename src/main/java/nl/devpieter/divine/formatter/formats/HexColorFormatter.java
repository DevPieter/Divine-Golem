package nl.devpieter.divine.formatter.formats;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import nl.devpieter.divine.formatter.ITextFormatter;
import org.jetbrains.annotations.NotNull;

public class HexColorFormatter implements ITextFormatter {

    private final String tag;
    private final TextColor color;

    public HexColorFormatter(@NotNull String hex) {
        this.tag = hex;
        this.color = TextColor.fromRgb(Integer.parseInt(hex.substring(1), 16));
    }

    @Override
    public String getTag() {
        return tag;
    }

    @Override
    public MutableComponent format(String content, Style baseStyle) {
        return Component.literal(content).setStyle(baseStyle.withColor(color));
    }
}
