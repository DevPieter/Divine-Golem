package nl.devpieter.divine.rendering.text.texts;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import nl.devpieter.divine.formatter.TextFormatUtils;
import nl.devpieter.divine.rendering.text.IText;
import org.jetbrains.annotations.NotNull;

public class ItemStackText implements IText {

    public static ItemStackText of(ItemStack stack, Text text) {
        return new ItemStackText(stack, text);
    }

    public static ItemStackText of(ItemStack stack, String text) {
        return new ItemStackText(stack, Text.of(text));
    }

    public static ItemStackText off(ItemStack stack, String key, Style style, Object... args) {
        return new ItemStackText(stack, TextFormatUtils.format(key, style, args));
    }

    private final ItemStack stack;
    private final Text text;

    private int xPadding;
    private int yPadding;

    public ItemStackText(ItemStack stack, Text text) {
        this.stack = stack;
        this.text = text;

        this.xPadding = 0;
        this.yPadding = 4;
    }

    @Override
    public Text text() {
        return text;
    }

    public ItemStack stack() {
        return stack;
    }

    @Override
    public void render(@NotNull DrawContext context, @NotNull TextRenderer textRenderer, int x, int y) {
        context.drawItem(stack(), x - 4, y - 4);
        context.drawTextWithShadow(textRenderer, text(), x + 14, y + 2, 0xFFFFFFFF);
    }

    @Override
    public int width(@NotNull TextRenderer textRenderer) {
        return IText.super.width(textRenderer) + 14 + xPadding;
    }

    @Override
    public int height(@NotNull TextRenderer textRenderer) {
        return IText.super.height(textRenderer) + 2 + yPadding;
    }

    public ItemStackText padding(int xPadding, int yPadding) {
        this.xPadding = xPadding;
        this.yPadding = yPadding;
        return this;
    }

    public ItemStackText xPadding(int xPadding) {
        this.xPadding = xPadding;
        return this;
    }

    public ItemStackText yPadding(int yPadding) {
        this.yPadding = yPadding;
        return this;
    }
}
