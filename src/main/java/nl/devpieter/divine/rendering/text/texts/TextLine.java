package nl.devpieter.divine.rendering.text.texts;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import nl.devpieter.divine.formatter.TextFormatUtils;
import nl.devpieter.divine.rendering.text.IText;
import org.jetbrains.annotations.NotNull;

public class TextLine implements IText {

    public static TextLine of(Text text) {
        return new TextLine(text);
    }

    public static TextLine of(String text) {
        return TextLine.of(Text.of(text));
    }

    public static TextLine off(String key, Style style, Object... args) {
        return TextLine.of(TextFormatUtils.format(key, style, args));
    }

    public static TextLine empty() {
        return TextLine.of(Text.empty());
    }

    private final Text text;

    private int xPadding;
    private int yPadding;

    public TextLine(Text text) {
        this.text = text;

        this.xPadding = 0;
        this.yPadding = 4;
    }

    @Override
    public Text text() {
        return text;
    }

    @Override
    public void render(@NotNull DrawContext context, @NotNull TextRenderer textRenderer, int x, int y) {
        context.drawTextWithShadow(textRenderer, text(), x, y, 0xFFFFFFFF);
    }

    @Override
    public int width(@NotNull TextRenderer textRenderer) {
        return IText.super.width(textRenderer) + xPadding;
    }

    @Override
    public int height(@NotNull TextRenderer textRenderer) {
        return IText.super.height(textRenderer) + yPadding;
    }

    public TextLine padding(int xPadding, int yPadding) {
        this.xPadding = xPadding;
        this.yPadding = yPadding;
        return this;
    }

    public TextLine xPadding(int xPadding) {
        this.xPadding = xPadding;
        return this;
    }

    public TextLine yPadding(int yPadding) {
        this.yPadding = yPadding;
        return this;
    }
}
