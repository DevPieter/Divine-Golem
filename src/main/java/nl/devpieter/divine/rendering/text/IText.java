package nl.devpieter.divine.rendering.text;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;

public interface IText {

    Text text();

    void render(@NotNull DrawContext context, @NotNull TextRenderer textRenderer, int x, int y);

    default int width(@NotNull TextRenderer textRenderer) {
        return textRenderer.getWidth(text());
    }

    default int height(@NotNull TextRenderer textRenderer) {
        return textRenderer.fontHeight;
    }
}
