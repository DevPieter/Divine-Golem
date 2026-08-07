package nl.devpieter.divine.rendering.text;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TextDrawerHelper {

    public void drawDynamicBox(@NotNull GuiGraphicsExtractor graphics, @NotNull Font font, int x, int y, int color, List<IText> lines) {
        drawDynamicBox(graphics, font, x, y, color, null, lines);
    }

    public void drawDynamicBox(@NotNull GuiGraphicsExtractor graphics, @NotNull Font font, int x, int y, int color, @Nullable Component title, List<IText> lines) {
//        int largestTextWidth = getLargestLineWidth(textRenderer, title, lines);
        int titleHeight = title != null ? font.lineHeight + 8 : 0;

        int boxPadding = 10;
//        int boxWidth = largestTextWidth + boxPadding * 2;
//
//        int boxHeight = 0;
//        for (IText line : lines) boxHeight += line.height(textRenderer);
//        boxHeight += titleHeight + (boxPadding * 2) - 4;

        int boxWidth = calculateBoxWidth(font, title, lines);
        int boxHeight = calculateBoxHeight(font, title, lines);

        // Draw background
        graphics.fill(x, y, x + boxWidth, y + boxHeight, color);

        int textX = x + boxPadding;
        int textY = y + boxPadding;

        // Draw title
        if (title != null) {
//            graphics.drawTextWithShadow(font, title, textX, textY, 0xFFFFFFFF);
            graphics.textWithBackdrop(font, title, textX, textY, font.width(title), 0xFFFFFFFF);
            textY += titleHeight;
        }

        // Draw lines
        for (IText line : lines) {
            line.render(graphics, font, textX, textY);
            textY += line.height(font);
        }
    }

    public int getLargestLineWidth(@NotNull Font font, List<IText> lines) {
        return getLargestLineWidth(font, null, lines);
    }

    public int getLargestLineWidth(@NotNull Font font, @Nullable Component title, List<IText> lines) {
        int largestTextWidth = 0;
        if (title != null) largestTextWidth = font.width(title);

        for (IText line : lines) {
            int lineWidth = line.width(font);
            if (lineWidth > largestTextWidth) largestTextWidth = lineWidth;
        }

        return largestTextWidth;
    }

    public int calculateBoxWidth(@NotNull Font font, List<IText> lines) {
        return calculateBoxWidth(font, null, lines);
    }

    public int calculateBoxWidth(@NotNull Font font, @Nullable Component title, List<IText> lines) {
        int largestTextWidth = getLargestLineWidth(font, title, lines);

        int boxPadding = 10;
        return largestTextWidth + (boxPadding * 2);
    }

    public int calculateBoxHeight(@NotNull Font font, List<IText> lines) {
        return calculateBoxHeight(font, null, lines);
    }

    public int calculateBoxHeight(@NotNull Font font, @Nullable Component title, List<IText> lines) {
        int boxHeight = 0;
        for (IText line : lines) boxHeight += line.height(font);

        int titleHeight = title != null ? font.lineHeight + 8 : 0;
        int boxPadding = 10;
        return boxHeight + titleHeight + (boxPadding * 2) - 4;
    }
}
