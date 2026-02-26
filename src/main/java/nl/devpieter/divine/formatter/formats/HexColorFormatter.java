package nl.devpieter.divine.formatter.formats;

import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
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
    public MutableText format(String content, Style baseStyle) {
        return Text.literal(content).setStyle(baseStyle.withColor(color));
    }
}
