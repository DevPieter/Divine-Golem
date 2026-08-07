package nl.devpieter.divine.rendering.text.texts;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.ItemStack;
import nl.devpieter.divine.formatter.TextFormatUtils;
import nl.devpieter.divine.rendering.text.IText;
import org.jetbrains.annotations.NotNull;

public class ItemStackText implements IText {

    public static ItemStackText of(ItemStack stack, Component text) {
        return new ItemStackText(stack, text);
    }

    public static ItemStackText of(ItemStack stack, String text) {
        return new ItemStackText(stack, Component.literal(text));
    }

    public static ItemStackText off(ItemStack stack, String key, Style style, Object... args) {
        return new ItemStackText(stack, TextFormatUtils.format(key, style, args));
    }

    private final ItemStack stack;
    private final Component text;

    private int xPadding;
    private int yPadding;

    public ItemStackText(ItemStack stack, Component text) {
        this.stack = stack;
        this.text = text;

        this.xPadding = 0;
        this.yPadding = 4;
    }

    @Override
    public Component text() {
        return text;
    }

    public ItemStack stack() {
        return stack;
    }

    @Override
    public void render(@NotNull GuiGraphicsExtractor graphics, @NotNull Font font, int x, int y) {
        graphics.item(stack(), x - 4, y - 4);
        graphics.textWithBackdrop(font, text(), x + 14, y + 2, font.width(text()), 0xFFFFFFFF);
    }

    @Override
    public int width(@NotNull Font font) {
        return IText.super.width(font) + 14 + xPadding;
    }

    @Override
    public int height(@NotNull Font font) {
        return IText.super.height(font) + 2 + yPadding;
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
