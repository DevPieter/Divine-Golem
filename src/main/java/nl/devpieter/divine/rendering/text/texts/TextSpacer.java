package nl.devpieter.divine.rendering.text.texts;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
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
    public Text text() {
        return Text.empty();
    }

    @Override
    public void render(@NotNull DrawContext context, @NotNull TextRenderer textRenderer, int x, int y) {
        context.drawTextWithShadow(textRenderer, text(), x, y, 0xFFFFFFFF);
    }

    @Override
    public int width(@NotNull TextRenderer textRenderer) {
        return width;
    }

    @Override
    public int height(@NotNull TextRenderer textRenderer) {
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
