package nl.devpieter.divine.rendering.text.texts;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import nl.devpieter.divine.rendering.text.IText;
import org.jetbrains.annotations.NotNull;

public class TextSpacer implements IText {

    public static TextSpacer of(int width, int height) {
        return new TextSpacer(width, height);
    }

    public static TextSpacer of(int height) {
        return new TextSpacer(0, height);
    }

    public static TextSpacer ofWidth(int width) {
        return new TextSpacer(width, 0);
    }

    public static TextSpacer ofHeight(int height) {
        return new TextSpacer(0, height);
    }

    private int width;
    private int height;

    public TextSpacer(int width, int height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public Component text() {
        return Component.empty();
    }

    @Override
    public void render(@NotNull GuiGraphicsExtractor graphics, @NotNull Font font, int x, int y) {
//        graphics.drawTextWithShadow(font, text(), x, y, 0xFFFFFFFF);
        graphics.textWithBackdrop(font, text(), x, y, font.width(text()), 0xFFFFFFFF);
    }

    @Override
    public int width(@NotNull Font font) {
        return width;
    }

    @Override
    public int height(@NotNull Font font) {
        return height;
    }

    public TextSpacer dimensions(int width, int height) {
        this.width = width;
        this.height = height;
        return this;
    }

    public TextSpacer width(int width) {
        this.width = width;
        return this;
    }

    public TextSpacer height(int height) {
        this.height = height;
        return this;
    }
}
