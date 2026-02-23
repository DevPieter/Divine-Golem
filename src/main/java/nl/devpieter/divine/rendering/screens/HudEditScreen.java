package nl.devpieter.divine.rendering.screens;

import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.input.KeyInput;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import nl.devpieter.divine.config.Settings;
import nl.devpieter.divine.config.setting.ClampedIntSetting;
import nl.devpieter.divine.rendering.hud.HudManager;
import nl.devpieter.divine.rendering.hud.models.ScreenPosition;
import nl.devpieter.divine.rendering.hud.widget.IHudWidget;
import nl.devpieter.divine.utils.FormatUtils;
import org.lwjgl.glfw.GLFW;

import java.util.List;

public class HudEditScreen extends Screen {

    private final HudManager hudManager = HudManager.getInstance();
    private final Settings settings = Settings.getInstance();

    private final Style instructionsStyle = Style.EMPTY.withColor(0x8f8f8f).withItalic(true);
    private final Style highlightStyle = Style.EMPTY.withColor(0xff3be477).withBold(true);

    private final List<MutableText> instructionsText = List.of(
            FormatUtils.formatTranslatable("text.divine.hud_editor.instruction.reposition", instructionsStyle, highlightStyle),
            FormatUtils.formatTranslatable("text.divine.hud_editor.instruction.toggle", instructionsStyle, highlightStyle),
            FormatUtils.formatTranslatable("text.divine.hud_editor.instruction.reset", instructionsStyle, highlightStyle),
            FormatUtils.formatTranslatable("text.divine.hud_editor.instruction.grid_size", instructionsStyle, highlightStyle)
    );

    private final ClampedIntSetting gridSize = new ClampedIntSetting(
            "edit_hud_grid_size",
            10,
            2,
            30
    );

    private IHudWidget draggingWidget = null;
    private float dragOffsetX = 0;
    private float dragOffsetY = 0;

    public HudEditScreen() {
        super(Text.translatable("text.divine.hud_editor.title"));
        settings.load(gridSize);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        renderGrid(context);

        IHudWidget hoveredWidget = hudManager.getWidgetByPosition(mouseX, mouseY);

        for (IHudWidget widget : hudManager.widgets()) {
            boolean isEnabled = hudManager.isWidgetEnabled(widget.identifier());
            widget.renderDummy(context, !isEnabled);
        }

        if (draggingWidget != null) {
            boolean isEnabled = hudManager.isWidgetEnabled(draggingWidget.identifier());
            draggingWidget.renderDummyHighlighted(context, !isEnabled);
        } else if (hoveredWidget != null) {
            boolean isEnabled = hudManager.isWidgetEnabled(hoveredWidget.identifier());
            hoveredWidget.renderDummyHighlighted(context, !isEnabled);
        }

        if (isControlPressed()) {
            MutableText gridSizeText = FormatUtils.formatTranslatable("text.divine.hud_editor.current_grid_size", instructionsStyle, highlightStyle, String.valueOf(gridSize.getValue()));
            context.drawCenteredTextWithShadow(client.textRenderer, gridSizeText, width / 2, height / 2 - textRenderer.fontHeight / 2, 0xFFFFFFFF);
        }

        int yOffset = height - 20 - (instructionsText.size() - 1) * (textRenderer.fontHeight + 4);

        for (MutableText line : instructionsText) {
            context.drawCenteredTextWithShadow(client.textRenderer, line, width / 2, yOffset, 0xFFFFFFFF);
            yOffset += textRenderer.fontHeight + 4;
        }
    }

    @Override
    public void mouseMoved(double mouseX, double mouseY) {
        if (draggingWidget == null) return;

        float newX = (float) (mouseX - dragOffsetX);
        float newY = (float) (mouseY - dragOffsetY);

        int size = gridSize.getValue();
        newX = Math.round(newX / size) * size;
        newY = Math.round(newY / size) * size;

        newX = Math.max(0, Math.min(newX, width - draggingWidget.dummyWidth()));
        newY = Math.max(0, Math.min(newY, height - draggingWidget.dummyHeight()));

        hudManager.setWidgetPosition(draggingWidget.identifier(), newX, newY);
    }

    @Override
    public boolean mouseClicked(Click click, boolean doubled) {
        float x = (float) click.x();
        float y = (float) click.y();

        IHudWidget widget = hudManager.getWidgetByPosition(x, y);
        if (widget == null) return super.mouseClicked(click, doubled);

        if (click.button() == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
            ScreenPosition pos = hudManager.getWidgetPosition(widget.identifier());
            if (pos == null) return super.mouseClicked(click, doubled);

            draggingWidget = widget;
            dragOffsetX = x - pos.x();
            dragOffsetY = y - pos.y();
        } else if (click.button() == GLFW.GLFW_MOUSE_BUTTON_RIGHT && isShiftPressed()) {
            hudManager.resetWidgetPosition(widget.identifier());
        } else if (click.button() == GLFW.GLFW_MOUSE_BUTTON_RIGHT) {
            hudManager.toggleWidgetEnabled(widget.identifier());
        }

        return true;
    }

    @Override
    public boolean mouseReleased(Click click) {
        draggingWidget = null;
        return super.mouseReleased(click);
    }

    @Override
    public boolean keyReleased(KeyInput input) {
        int key = input.key();
        boolean controlPressed = isControlPressed();

        if (controlPressed && (key == GLFW.GLFW_KEY_KP_ADD || key == GLFW.GLFW_KEY_EQUAL)) {
            gridSize.increment();
            return true;
        } else if (controlPressed && (key == GLFW.GLFW_KEY_KP_SUBTRACT || key == GLFW.GLFW_KEY_MINUS)) {
            gridSize.decrement();
            return true;
        }

        return super.keyReleased(input);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        if (!isControlPressed()) return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);

        if (verticalAmount != 0) {
            if (verticalAmount > 0) gridSize.increment();
            else gridSize.decrement();
            return true;
        }

        return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }

    @Override
    public void close() {
        super.close();

        settings.save(gridSize);
        hudManager.save();
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    private void renderGrid(DrawContext context) {
        int size = gridSize.getValue();

        for (int x = 0; x < width; x += size) context.fill(x, 0, x + 1, height, 0x30DCDCDC);
        for (int y = 0; y < height; y += size) context.fill(0, y, width, y + 1, 0x30DCDCDC);
    }

    private boolean isControlPressed() {
        long handle = client.getWindow().getHandle();
        return GLFW.glfwGetKey(handle, GLFW.GLFW_KEY_LEFT_CONTROL) == GLFW.GLFW_PRESS || GLFW.glfwGetKey(handle, GLFW.GLFW_KEY_RIGHT_CONTROL) == GLFW.GLFW_PRESS;
    }

    private boolean isShiftPressed() {
        long handle = client.getWindow().getHandle();
        return GLFW.glfwGetKey(handle, GLFW.GLFW_KEY_LEFT_SHIFT) == GLFW.GLFW_PRESS || GLFW.glfwGetKey(handle, GLFW.GLFW_KEY_RIGHT_SHIFT) == GLFW.GLFW_PRESS;
    }
}
