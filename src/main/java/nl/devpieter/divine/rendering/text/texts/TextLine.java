package nl.devpieter.divine.rendering.text.texts;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import nl.devpieter.divine.formatter.TextFormatUtils;
import nl.devpieter.divine.rendering.text.IText;
import org.jetbrains.annotations.NotNull;

public class TextLine implements IText {

    public static TextLine of(Component text) {
        return new TextLine(text);
    }

    public static TextLine of(String text) {
        return TextLine.of(Component.literal(text));
    }

    public static TextLine off(String key, Style style, Object... args) {
        return TextLine.of(TextFormatUtils.format(key, style, args));
    }

    public static TextLine empty() {
        return TextLine.of(Component.empty());
    }

    private final Component text;

    private int xPadding;
    private int yPadding;

    public TextLine(Component text) {
        this.text = text;

        this.xPadding = 0;
        this.yPadding = 4;
    }

    @Override
    public Component text() {
        return text;
    }

    @Override
    public void render(@NotNull GuiGraphicsExtractor graphics, @NotNull Font font, int x, int y) {
        graphics.textWithBackdrop(font, text(), x, y, font.width(text()), 0xFFFFFFFF);
    }

    @Override
    public int width(@NotNull Font font) {
        return IText.super.width(font) + xPadding;
    }

    @Override
    public int height(@NotNull Font font) {
        return IText.super.height(font) + yPadding;
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
