package nl.devpieter.divine.rendering.hud.widgets;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import nl.devpieter.divine.GolemManager;
import nl.devpieter.divine.HypixelManager;
import nl.devpieter.divine.rendering.hud.widget.HudWidget;
import nl.devpieter.utilize.utils.minecraft.TextUtils;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class TrackerHudWidget extends HudWidget {

    private final MinecraftClient client = MinecraftClient.getInstance();

    private final GolemManager golemManager = GolemManager.getInstance();
    private final HypixelManager hypixelManager = HypixelManager.getInstance();

    private final Text titleText = TextUtils.withStyle("Divine Golem Tracker", titleStyle);

    private final List<Text> dummyLines = List.of(
            formatLine("Stage: ", "Stage Name"),
            formatLine("Location: ", "Location Name")
    );

    @Override
    public @NotNull String identifier() {
        return "tracker";
    }

    @Override
    public boolean shouldRender() {
        return hypixelManager.isInTheEnd();
    }

    @Override
    protected void renderWidget(DrawContext context) {
        List<Text> lines = new ArrayList<>();
        lines.add(formatLine("Stage: ", golemManager.getFormattedStageText()));
        lines.add(formatLine("Location: ", golemManager.getFormattedLocationText()));

        drawDynamicBox(context, 0, 0, backgroundColor, titleText, lines, client.textRenderer);
    }

    @Override
    protected void renderDummyWidget(DrawContext context) {
        drawDynamicBox(context, 0, 0, backgroundColor, titleText, dummyLines, client.textRenderer);
    }

    @Override
    public int dummyWidth() {
        return calculateBoxWidth(titleText, dummyLines, client.textRenderer);
    }

    @Override
    public int dummyHeight() {
        return calculateBoxHeight(titleText, dummyLines, client.textRenderer);
    }
}
