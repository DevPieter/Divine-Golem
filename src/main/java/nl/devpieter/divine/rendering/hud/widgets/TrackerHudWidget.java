package nl.devpieter.divine.rendering.hud.widgets;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import nl.devpieter.divine.GolemManager;
import nl.devpieter.divine.HypixelManager;
import nl.devpieter.divine.rendering.hud.widget.HudWidget;
import nl.devpieter.divine.utils.FormatUtils;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class TrackerHudWidget extends HudWidget {

    private final MinecraftClient client = MinecraftClient.getInstance();

    private final GolemManager golemManager = GolemManager.getInstance();
    private final HypixelManager hypixelManager = HypixelManager.getInstance();

    @Override
    public @NotNull String name() {
        return "Tracker";
    }

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

        lines.add(FormatUtils.formatTranslatable("text.divine.widget.tracker.stage", labelStyle, valueStyle, golemManager.getFormattedStageText()));
        lines.add(FormatUtils.formatTranslatable("text.divine.widget.tracker.location", labelStyle, valueStyle, golemManager.getFormattedLocationText()));

        drawDynamicBox(context, 0, 0, backgroundColor, getTitle(), lines, client.textRenderer);
    }

    @Override
    protected void renderDummyWidget(DrawContext context) {
        drawDynamicBox(context, 0, 0, backgroundColor, getTitle(), getDummyLines(), client.textRenderer);
    }

    @Override
    public int dummyWidth() {
        return calculateBoxWidth(getTitle(), getDummyLines(), client.textRenderer);
    }

    @Override
    public int dummyHeight() {
        return calculateBoxHeight(getTitle(), getDummyLines(), client.textRenderer);
    }

    private Text getTitle() {
        return FormatUtils.formatTranslatable("text.divine.widget.tracker.title", titleStyle, valueStyle);
    }

    private List<Text> getDummyLines() {
        return List.of(
                FormatUtils.formatTranslatable("text.divine.widget.tracker.stage", labelStyle, valueStyle, "Stage Name"),
                FormatUtils.formatTranslatable("text.divine.widget.tracker.location", labelStyle, valueStyle, "Location Name")
        );
    }
}
