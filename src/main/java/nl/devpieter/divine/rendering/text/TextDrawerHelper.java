package nl.devpieter.divine.rendering.text;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TextDrawerHelper {

    public void drawDynamicBox(@NotNull DrawContext context, @NotNull TextRenderer textRenderer, int x, int y, int color, List<IText> lines) {
        drawDynamicBox(context, textRenderer, x, y, color, null, lines);
    }

    public void drawDynamicBox(@NotNull DrawContext context, @NotNull TextRenderer textRenderer, int x, int y, int color, @Nullable Text title, List<IText> lines) {
//        int largestTextWidth = getLargestLineWidth(textRenderer, title, lines);
        int titleHeight = title != null ? textRenderer.fontHeight + 8 : 0;

        int boxPadding = 10;
//        int boxWidth = largestTextWidth + boxPadding * 2;
//
//        int boxHeight = 0;
//        for (IText line : lines) boxHeight += line.height(textRenderer);
//        boxHeight += titleHeight + (boxPadding * 2) - 4;

        int boxWidth = calculateBoxWidth(textRenderer, title, lines);
        int boxHeight = calculateBoxHeight(textRenderer, title, lines);

        // Draw background
        context.fill(x, y, x + boxWidth, y + boxHeight, color);

        int textX = x + boxPadding;
        int textY = y + boxPadding;

        // Draw title
        if (title != null) {
            context.drawTextWithShadow(textRenderer, title, textX, textY, 0xFFFFFFFF);
            textY += titleHeight;
        }

        // Draw lines
        for (IText line : lines) {
            line.render(context, textRenderer, textX, textY);
            textY += line.height(textRenderer);
        }
    }

    public int getLargestLineWidth(@NotNull TextRenderer textRenderer, List<IText> lines) {
        return getLargestLineWidth(textRenderer, null, lines);
    }

    public int getLargestLineWidth(@NotNull TextRenderer textRenderer, @Nullable Text title, List<IText> lines) {
        int largestTextWidth = 0;
        if (title != null) largestTextWidth = textRenderer.getWidth(title);

        for (IText line : lines) {
            int lineWidth = line.width(textRenderer);
            if (lineWidth > largestTextWidth) largestTextWidth = lineWidth;
        }

        return largestTextWidth;
    }

    public int calculateBoxWidth(@NotNull TextRenderer textRenderer, List<IText> lines) {
        return calculateBoxWidth(textRenderer, null, lines);
    }

    public int calculateBoxWidth(@NotNull TextRenderer textRenderer, @Nullable Text title, List<IText> lines) {
        int largestTextWidth = getLargestLineWidth(textRenderer, title, lines);

        int boxPadding = 10;
        return largestTextWidth + (boxPadding * 2);
    }

    public int calculateBoxHeight(@NotNull TextRenderer textRenderer, List<IText> lines) {
        return calculateBoxHeight(textRenderer, null, lines);
    }

    public int calculateBoxHeight(@NotNull TextRenderer textRenderer, @Nullable Text title, List<IText> lines) {
        int boxHeight = 0;
        for (IText line : lines) boxHeight += line.height(textRenderer);

        int titleHeight = title != null ? textRenderer.fontHeight + 8 : 0;
        int boxPadding = 10;
        return boxHeight + titleHeight + (boxPadding * 2) - 4;
    }
}
