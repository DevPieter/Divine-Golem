package nl.devpieter.divine.rendering.text;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

public interface IText {

    Component text();

    void render(@NotNull GuiGraphicsExtractor graphics, @NotNull Font font, int x, int y);

    default int width(@NotNull Font font) {
        return font.width(text());
    }

    default int height(@NotNull Font font) {
        return font.lineHeight;
    }
}
