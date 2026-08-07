package nl.devpieter.divine.rendering.hud.widget;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import nl.devpieter.divine.rendering.hud.HudManager;
import nl.devpieter.divine.rendering.hud.models.ScreenPosition;
import nl.devpieter.divine.rendering.text.TextDrawerHelper;
import nl.devpieter.utilize.client.utils.TextUtils;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix3x2fStack;

import java.awt.*;

public abstract class HudWidget extends TextDrawerHelper implements IHudWidget {

    private final HudManager hudManager = HudManager.getInstance();
    private final Minecraft client = Minecraft.getInstance();

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
    public void render(@NotNull GuiGraphicsExtractor graphics) {
        pushMatrix(graphics);
        renderWidget(graphics);
        popMatrix(graphics);
    }

    @Override
    public void renderDummy(@NotNull GuiGraphicsExtractor graphics, boolean disabled) {
        pushMatrix(graphics);
        renderDummyWidget(graphics);

        if (disabled) drawOutline(graphics, dummyWidth(), dummyHeight(), disabledColor);
        popMatrix(graphics);
    }

    @Override
    public void renderDummyHighlighted(@NotNull GuiGraphicsExtractor graphics, boolean disabled) {
        pushMatrix(graphics);
        if (!disabled) drawOutline(graphics, dummyWidth(), dummyHeight(), highlightColor);

        Font textRenderer = client.font;
        Component name = TextUtils.withStyle(name(), disabled ? nameStyleDisabled : nameStyle);
        int textWidth = textRenderer.width(name);

//        graphics.drawTextWithShadow(textRenderer, name, 4, dummyHeight() + 3, 0xFFFFFFFF);
        graphics.textWithBackdrop(textRenderer, name, 4, dummyHeight() + 3, textWidth, 0xFFFFFFFF);
        popMatrix(graphics);
    }

    private void drawOutline(GuiGraphicsExtractor graphics, int width, int height, int color) {
        graphics.fill(0, 0, width, 1, color);
        graphics.fill(0, height - 1, width, height, color);
        graphics.fill(0, 0, 1, height, color);
        graphics.fill(width - 1, 0, width, height, color);
    }

    protected abstract void renderWidget(GuiGraphicsExtractor graphics);

    protected abstract void renderDummyWidget(GuiGraphicsExtractor graphics);

    private void pushMatrix(@NotNull GuiGraphicsExtractor graphics) {
        Matrix3x2fStack matrixStack = graphics.pose();
        matrixStack.pushMatrix();

        ScreenPosition pos = hudManager.getWidgetPosition(identifier());
        if (pos == null) pos = new ScreenPosition(0, 0);

        matrixStack.translate(pos.x(), pos.y());
    }

    private void popMatrix(@NotNull GuiGraphicsExtractor graphics) {
        graphics.pose().popMatrix();
    }
}
