package nl.devpieter.divine.rendering.screens;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
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
        super.extractRenderState(graphics, mouseX, mouseY, a);
    }
}
