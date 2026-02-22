package nl.devpieter.divine.config.widgets;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import nl.devpieter.divine.GolemManager;
import nl.devpieter.divine.HypixelManager;
import nl.devpieter.divine.config.widget.HudWidget;
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

    private int dummyWidth;
    private int dummyHeight;

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
        if (dummyWidth == 0) {
            dummyWidth = getLargestLineWidth(titleText, dummyLines, client.textRenderer) + 20;
        }

        return dummyWidth;
    }

    @Override
    public int dummyHeight() {
        if (dummyHeight == 0) {
            int lineHeight = client.textRenderer.fontHeight + 2;
            int titleHeight = client.textRenderer.fontHeight + 8;
            dummyHeight = titleHeight + (lineHeight * dummyLines.size()) + 20 - 4;
        }

        return dummyHeight;
    }
}
