package nl.devpieter.divine.rendering.screens;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import nl.devpieter.divine.config.Settings;
import nl.devpieter.divine.config.setting.ClampedIntSetting;
import nl.devpieter.divine.formatter.TextFormatUtils;
import nl.devpieter.divine.rendering.hud.HudManager;
import nl.devpieter.divine.rendering.hud.models.ScreenPosition;
import nl.devpieter.divine.rendering.hud.widget.IHudWidget;
import nl.devpieter.utilize.client.setting.settings.BooleanSetting;
import org.lwjgl.glfw.GLFW;

import java.util.List;

public class HudEditScreen extends Screen {

    private final HudManager hudManager = HudManager.getInstance();
    private final Settings settings = Settings.getInstance();

    private final Style instructionsStyle = Style.EMPTY.withColor(0x8f8f8f).withItalic(true);
    private final Style highlightStyle = Style.EMPTY.withColor(0xff3be477).withBold(true);

    private final Component hintText = TextFormatUtils.format("text.divine.hud_editor.hint_show_instructions", instructionsStyle);

    private final List<Component> instructionsText = List.of(
            TextFormatUtils.format("text.divine.hud_editor.instruction.reposition", instructionsStyle),
            TextFormatUtils.format("text.divine.hud_editor.instruction.toggle", instructionsStyle),
            TextFormatUtils.format("text.divine.hud_editor.instruction.reset", instructionsStyle),
            TextFormatUtils.format("text.divine.hud_editor.instruction.grid_size", instructionsStyle),
            TextFormatUtils.format("text.divine.hud_editor.instruction.disable_grid", instructionsStyle)
    );

    private final ClampedIntSetting gridSize = new ClampedIntSetting(
            "edit_hud_grid_size",
            10,
            2,
            30
    );

    private final BooleanSetting snapToGrid = new BooleanSetting(
            "edit_hud_snap_to_grid",
            true
    );

    private IHudWidget draggingWidget = null;
    private float dragOffsetX = 0;
    private float dragOffsetY = 0;

    public HudEditScreen() {
        super(Component.translatable("text.divine.hud_editor.title"));
        settings.load(gridSize);
        settings.load(snapToGrid);
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        if (snapToGrid.getValue()) renderGrid(graphics);

        IHudWidget hoveredWidget = hudManager.getWidgetByPosition(mouseX, mouseY);

        for (IHudWidget widget : hudManager.widgets()) {
            boolean isEnabled = hudManager.isWidgetEnabled(widget.identifier());
            widget.renderDummy(graphics, !isEnabled);
        }

        if (draggingWidget != null) {
            boolean isEnabled = hudManager.isWidgetEnabled(draggingWidget.identifier());
            draggingWidget.renderDummyHighlighted(graphics, !isEnabled);
        } else if (hoveredWidget != null) {
            boolean isEnabled = hudManager.isWidgetEnabled(hoveredWidget.identifier());
            hoveredWidget.renderDummyHighlighted(graphics, !isEnabled);
        }

        if (isControlPressed() && snapToGrid.getValue()) {
            MutableComponent gridSizeText = TextFormatUtils.format("text.divine.hud_editor.current_grid_size", instructionsStyle, String.valueOf(gridSize.getValue()));
            graphics.centeredText(minecraft.font, gridSizeText, width / 2, height / 2 - minecraft.font.lineHeight / 2, 0xFFFFFFFF);
        }

        if (isSpacePressed()) {
            int yOffset = height - 20 - (instructionsText.size() - 1) * (minecraft.font.lineHeight + 5);

            for (Component line : instructionsText) {
                graphics.centeredText(minecraft.font, line, width / 2, yOffset, 0xFFFFFFFF);
                yOffset += minecraft.font.lineHeight + 5;
            }
        } else {
            graphics.centeredText(minecraft.font, hintText, width / 2, height - 20, 0xFFFFFFFF);
        }
    }

    @Override
    public void mouseMoved(double x, double y) {
        if (draggingWidget == null) return;

        float newX = (float) (x - dragOffsetX);
        float newY = (float) (y - dragOffsetY);

        if (snapToGrid.getValue()) {
            int size = gridSize.getValue();
            newX = Math.round(newX / size) * size;
            newY = Math.round(newY / size) * size;
        }

        newX = Math.clamp(newX, 0, width - draggingWidget.dummyWidth());
        newY = Math.clamp(newY, 0, height - draggingWidget.dummyHeight());

        hudManager.setWidgetPosition(draggingWidget.identifier(), newX, newY);
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
        float x = (float) event.x();
        float y = (float) event.y();

        IHudWidget widget = hudManager.getWidgetByPosition(x, y);
        if (widget == null) return super.mouseClicked(event, doubleClick);

        if (event.button() == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
            ScreenPosition pos = hudManager.getWidgetPosition(widget.identifier());
            if (pos == null) return super.mouseClicked(event, doubleClick);

            draggingWidget = widget;
            dragOffsetX = x - pos.x();
            dragOffsetY = y - pos.y();
        } else if (event.button() == GLFW.GLFW_MOUSE_BUTTON_RIGHT && isShiftPressed()) {
            hudManager.resetWidgetPosition(widget.identifier());
        } else if (event.button() == GLFW.GLFW_MOUSE_BUTTON_RIGHT) {
            hudManager.toggleWidgetEnabled(widget.identifier());
        }

        return true;
    }

    @Override
    public boolean mouseReleased(MouseButtonEvent event) {
        draggingWidget = null;
        return super.mouseReleased(event);
    }

    @Override
    public boolean mouseScrolled(double x, double y, double scrollX, double scrollY) {
        if (!isControlPressed() || !snapToGrid.getValue()) return super.mouseScrolled(x, y, scrollX, scrollY);

        if (scrollY != 0) {
            if (scrollY > 0) gridSize.increment();
            else gridSize.decrement();
            return true;
        }

        return super.mouseScrolled(x, y, scrollX, scrollY);
    }

    @Override
    public boolean keyReleased(KeyEvent event) {
        int key = event.key();

        if (key == GLFW.GLFW_KEY_S) {
            snapToGrid.toggle();
            return true;
        }

        if (!isControlPressed() || !snapToGrid.getValue()) return super.keyReleased(event);

        if (key == GLFW.GLFW_KEY_KP_ADD || key == GLFW.GLFW_KEY_EQUAL) {
            gridSize.increment();
            return true;
        } else if (key == GLFW.GLFW_KEY_KP_SUBTRACT || key == GLFW.GLFW_KEY_MINUS) {
            gridSize.decrement();
            return true;
        }

        return super.keyReleased(event);
    }

    @Override
    public void onClose() {
        super.onClose();

        settings.save(gridSize);
        settings.save(snapToGrid);
        hudManager.save();
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    private void renderGrid(GuiGraphicsExtractor graphics) {
        int size = gridSize.getValue();

        for (int x = 0; x < width; x += size) graphics.fill(x, 0, x + 1, height, 0x30DCDCDC);
        for (int y = 0; y < height; y += size) graphics.fill(0, y, width, y + 1, 0x30DCDCDC);
    }

    private boolean isShiftPressed() {
        long handle = minecraft.getWindow().handle();
        return GLFW.glfwGetKey(handle, GLFW.GLFW_KEY_LEFT_SHIFT) == GLFW.GLFW_PRESS || GLFW.glfwGetKey(handle, GLFW.GLFW_KEY_RIGHT_SHIFT) == GLFW.GLFW_PRESS;
    }

    private boolean isControlPressed() {
        long handle = minecraft.getWindow().handle();
        return GLFW.glfwGetKey(handle, GLFW.GLFW_KEY_LEFT_CONTROL) == GLFW.GLFW_PRESS || GLFW.glfwGetKey(handle, GLFW.GLFW_KEY_RIGHT_CONTROL) == GLFW.GLFW_PRESS;
    }

    private boolean isSpacePressed() {
        long handle = minecraft.getWindow().handle();
        return GLFW.glfwGetKey(handle, GLFW.GLFW_KEY_SPACE) == GLFW.GLFW_PRESS;
    }
}
