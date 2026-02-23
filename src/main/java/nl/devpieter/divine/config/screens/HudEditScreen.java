package nl.devpieter.divine.config.screens;

import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.input.KeyInput;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import nl.devpieter.divine.config.HudManager;
import nl.devpieter.divine.config.widget.IHudWidget;
import nl.devpieter.divine.models.ScreenPosition;
import nl.devpieter.divine.statics.Settings;
import nl.devpieter.utilize.utils.minecraft.TextUtils;
import org.lwjgl.glfw.GLFW;

import java.util.List;

public class HudEditScreen extends Screen {

    private final HudManager hudManager = HudManager.getInstance();

    private final Style instructionsStyle = Style.EMPTY.withColor(0x8f8f8f).withItalic(true);
    private final Style highlightStyle = Style.EMPTY.withColor(0xff3be477).withBold(true);

    private final List<MutableText> instructionsText = List.of(
            TextUtils.withStyle("", instructionsStyle)
                    .append(TextUtils.withStyle("Hold", highlightStyle))
                    .append(" and ")
                    .append(TextUtils.withStyle("Drag", highlightStyle))
                    .append(" widgets to reposition."),
            TextUtils.withStyle("", instructionsStyle)
                    .append(TextUtils.withStyle("Right-click ", highlightStyle))
                    .append("widgets to reset their position."),
            TextUtils.withStyle("Hold ", instructionsStyle)
                    .append(TextUtils.withStyle("Ctrl + Scroll", highlightStyle))
                    .append(" to adjust grid size.")
    );

    private final int minGridSize = 5;
    private final int maxGridSize = 20;
//    private int gridSize = 10;

    private IHudWidget draggingWidget = null;
    private float dragOffsetX = 0;
    private float dragOffsetY = 0;

    public HudEditScreen() {
        super(Text.of("HUD Edit Screen"));
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        super.render(context, mouseX, mouseY, deltaTicks);
        renderGrid(context);

        for (IHudWidget widget : hudManager.widgets()) {
            widget.renderDummy(context);
        }

        if (isControlPressed()) {
            int gridSize = Settings.EDIT_HUD_GRID_SIZE.getValue();

            MutableText gridSizeText = TextUtils.withStyle("Grid Size: ", instructionsStyle)
                    .append(TextUtils.withStyle(String.valueOf(gridSize), highlightStyle));

            context.drawCenteredTextWithShadow(client.textRenderer, gridSizeText, mouseX, mouseY - 10, 0xFFFFFFFF);
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

        int gridSize = Settings.EDIT_HUD_GRID_SIZE.getValue();
        newX = Math.round(newX / gridSize) * gridSize;
        newY = Math.round(newY / gridSize) * gridSize;

        newX = Math.max(0, Math.min(newX, width - draggingWidget.dummyWidth()));
        newY = Math.max(0, Math.min(newY, height - draggingWidget.dummyHeight()));

        hudManager.setWidgetPosition(draggingWidget.identifier(), new ScreenPosition(newX, newY));
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
        }

        if (click.button() == GLFW.GLFW_MOUSE_BUTTON_RIGHT) {
            hudManager.resetWidgetPosition(widget.identifier());
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

        int gridSize = Settings.EDIT_HUD_GRID_SIZE.getValue();

        if (controlPressed && (key == GLFW.GLFW_KEY_KP_ADD || key == GLFW.GLFW_KEY_EQUAL)) {
            Settings.EDIT_HUD_GRID_SIZE.setValue(Math.min(maxGridSize, gridSize + 1));
            return true;
        } else if (controlPressed && (key == GLFW.GLFW_KEY_KP_SUBTRACT || key == GLFW.GLFW_KEY_MINUS)) {
            Settings.EDIT_HUD_GRID_SIZE.setValue(Math.max(minGridSize, gridSize - 1));
            return true;
        }

        return super.keyReleased(input);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        if (!isControlPressed()) return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);

        int gridSize = Settings.EDIT_HUD_GRID_SIZE.getValue();

        if (verticalAmount > 0) {
            Settings.EDIT_HUD_GRID_SIZE.setValue(Math.min(maxGridSize, gridSize + 1));
            return true;
        } else if (verticalAmount < 0) {
            Settings.EDIT_HUD_GRID_SIZE.setValue(Math.max(minGridSize, gridSize - 1));
            return true;
        }

        return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }

    @Override
    public void close() {
        super.close();

        Settings.save(Settings.EDIT_HUD_GRID_SIZE);
        Settings.save(Settings.HUD_WIDGET_POSITIONS);
    }

    private void renderGrid(DrawContext context) {
        int gridSize = Settings.EDIT_HUD_GRID_SIZE.getValue();

        // Draw main grid
        for (int x = 0; x < width; x += gridSize) context.fill(x, 0, x + 1, height, 0x10FFFFFF);
        for (int y = 0; y < height; y += gridSize) context.fill(0, y, width, y + 1, 0x10FFFFFF);
    }

    private boolean isControlPressed() {
        long handle = client.getWindow().getHandle();
        return GLFW.glfwGetKey(handle, GLFW.GLFW_KEY_LEFT_CONTROL) == GLFW.GLFW_PRESS || GLFW.glfwGetKey(handle, GLFW.GLFW_KEY_RIGHT_CONTROL) == GLFW.GLFW_PRESS;
    }
}
