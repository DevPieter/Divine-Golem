package nl.devpieter.divine.rendering.hud.widget;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import nl.devpieter.divine.rendering.hud.HudManager;
import nl.devpieter.divine.rendering.hud.models.ScreenPosition;
import nl.devpieter.divine.rendering.text.TextDrawerHelper;
import nl.devpieter.utilize.utils.minecraft.TextUtils;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix3x2fStack;

import java.awt.*;

public abstract class HudWidget extends TextDrawerHelper implements IHudWidget {

    private final HudManager hudManager = HudManager.getInstance();
    private final MinecraftClient client = MinecraftClient.getInstance();

    protected static final int backgroundColor = new Color(18, 18, 18, 179).getRGB();
    protected static final int highlightColor = new Color(59, 228, 119, 179).getRGB();
    protected static final int disabledColor = new Color(228, 59, 104, 179).getRGB();

    protected static Style nameStyle = Style.EMPTY.withColor(0x3be477).withBold(true);
    protected static Style nameStyleDisabled = Style.EMPTY.withColor(0x8f8f8f).withItalic(true).withBold(true);

    protected final Style titleStyle = Style.EMPTY.withColor(0x3be477).withBold(true);
    protected final Style labelStyle = Style.EMPTY.withColor(0x8f8f8f);

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
    public void renderDummy(@NotNull DrawContext context, boolean disabled) {
        pushMatrix(context);
        renderDummyWidget(context);

        if (disabled) drawOutline(context, dummyWidth(), dummyHeight(), disabledColor);
        popMatrix(context);
    }

    @Override
    public void renderDummyHighlighted(@NotNull DrawContext context, boolean disabled) {
        pushMatrix(context);
        if (!disabled) drawOutline(context, dummyWidth(), dummyHeight(), highlightColor);

        TextRenderer textRenderer = client.textRenderer;
        Text name = TextUtils.withStyle(name(), disabled ? nameStyleDisabled : nameStyle);

        context.drawTextWithShadow(textRenderer, name, 4, dummyHeight() + 3, 0xFFFFFFFF);
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
}
