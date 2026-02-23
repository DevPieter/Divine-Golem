package nl.devpieter.divine.rendering.hud.widget;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import nl.devpieter.divine.rendering.hud.HudManager;
import nl.devpieter.divine.rendering.hud.models.ScreenPosition;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix3x2fStack;

import java.awt.*;
import java.util.List;

public abstract class HudWidget implements IHudWidget {

    private final HudManager hudManager = HudManager.getInstance();

    protected static final int backgroundColor = new Color(18, 18, 18, 179).getRGB();
    protected static final int highlightColor = new Color(59, 228, 119, 179).getRGB();
    protected static final int disabledColor = new Color(228, 59, 104, 179).getRGB();

    protected final Style titleStyle = Style.EMPTY.withColor(0x3be477).withBold(true);
    protected final Style labelStyle = Style.EMPTY.withColor(0x8f8f8f);
    protected final Style valueStyle = Style.EMPTY.withColor(0x3be477).withItalic(true);

    @Override
    public boolean shouldRender() {
        return true;
    }

    @Override
    public void render(@NotNull DrawContext context) {
        pushMatrix(context);
        renderWidget(context);
        popMatrix(context);
    }

    @Override
    public void renderDummy(@NotNull DrawContext context, boolean highlighted, boolean disabled) {
        pushMatrix(context);
        renderDummyWidget(context);

        if (highlighted) drawOutline(context, dummyWidth(), dummyHeight(), highlightColor);
        else if (disabled) drawOutline(context, dummyWidth(), dummyHeight(), disabledColor);

        popMatrix(context);
    }

    private void drawOutline(DrawContext context, int width, int height, int color) {
        context.fill(0, 0, width, 1, color);
        context.fill(0, height - 1, width, height, color);
        context.fill(0, 0, 1, height, color);
        context.fill(width - 1, 0, width, height, color);
    }

    protected abstract void renderWidget(DrawContext context);

    protected abstract void renderDummyWidget(DrawContext context);

    private void pushMatrix(@NotNull DrawContext context) {
        Matrix3x2fStack matrixStack = context.getMatrices();
        matrixStack.pushMatrix();

        ScreenPosition pos = hudManager.getWidgetPosition(identifier());
        if (pos == null) pos = new ScreenPosition(0, 0);

        matrixStack.translate(pos.x(), pos.y());
    }

    private void popMatrix(@NotNull DrawContext context) {
        context.getMatrices().popMatrix();
    }

    protected void drawDynamicBox(DrawContext context, int x, int y, int color, List<Text> lines, TextRenderer textRenderer) {
        drawDynamicBox(context, x, y, color, null, lines, textRenderer);
    }

    protected void drawDynamicBox(DrawContext context, int x, int y, int color, @Nullable Text title, List<Text> lines, TextRenderer textRenderer) {
        int largestTextWidth = getLargestLineWidth(title, lines, textRenderer);

        int lineHeight = textRenderer.fontHeight + 2;
        int titleHeight = title != null ? textRenderer.fontHeight + 8 : 0;

        int boxPadding = 10;
        int boxWidth = largestTextWidth + boxPadding * 2;
        int boxHeight = titleHeight + (lineHeight * lines.size()) + (boxPadding * 2) - 4;

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
        for (Text line : lines) {
            context.drawTextWithShadow(textRenderer, line, textX, textY, 0xFFFFFFFF);
            textY += lineHeight;
        }
    }

    protected int getLargestLineWidth(List<Text> lines, TextRenderer textRenderer) {
        return getLargestLineWidth(null, lines, textRenderer);
    }

    protected int getLargestLineWidth(@Nullable Text title, List<Text> lines, TextRenderer textRenderer) {
        int largestTextWidth = 0;
        if (title != null) largestTextWidth = textRenderer.getWidth(title);

        for (Text line : lines) {
            int lineWidth = textRenderer.getWidth(line);
            if (lineWidth > largestTextWidth) largestTextWidth = lineWidth;
        }

        return largestTextWidth;
    }

    protected int calculateBoxWidth(List<Text> lines, TextRenderer textRenderer) {
        return calculateBoxWidth(null, lines, textRenderer);
    }

    protected int calculateBoxWidth(@Nullable Text title, List<Text> lines, TextRenderer textRenderer) {
        int largestTextWidth = getLargestLineWidth(title, lines, textRenderer);
        int boxPadding = 10;
        return largestTextWidth + boxPadding * 2;
    }

    protected int calculateBoxHeight(List<Text> lines, TextRenderer textRenderer) {
        return calculateBoxHeight(null, lines, textRenderer);
    }

    protected int calculateBoxHeight(@Nullable Text title, List<Text> lines, TextRenderer textRenderer) {
        int lineHeight = textRenderer.fontHeight + 2;
        int titleHeight = title != null ? textRenderer.fontHeight + 8 : 0;
        int boxPadding = 10;
        return titleHeight + (lineHeight * lines.size()) + (boxPadding * 2) - 4;
    }
}
