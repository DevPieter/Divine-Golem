package nl.devpieter.divine.config.widget;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import nl.devpieter.divine.config.HudManager;
import nl.devpieter.divine.models.ScreenPosition;
import nl.devpieter.utilize.utils.minecraft.TextUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix3x2fStack;

import java.awt.*;
import java.util.List;

public abstract class HudWidget implements IHudWidget {

    private final HudManager hudManager = HudManager.getInstance();

    protected static final int backgroundColor = new Color(18, 18, 18, 179).getRGB();
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
    public void renderDummy(@NotNull DrawContext context) {
        pushMatrix(context);
        renderDummyWidget(context);
        popMatrix(context);
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

    protected Text formatLine(String label, String value) {
        return TextUtils.withStyle(label, labelStyle).append(TextUtils.withStyle(value, valueStyle));
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
}
