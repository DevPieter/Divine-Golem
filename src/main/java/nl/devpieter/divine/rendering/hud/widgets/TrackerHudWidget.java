package nl.devpieter.divine.rendering.hud.widgets;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import nl.devpieter.divine.GolemManager;
import nl.devpieter.divine.HypixelManager;
import nl.devpieter.divine.enums.GolemLocation;
import nl.devpieter.divine.enums.GolemStage;
import nl.devpieter.divine.formatter.TextFormatUtils;
import nl.devpieter.divine.rendering.hud.widget.HudWidget;
import nl.devpieter.divine.rendering.text.IText;
import nl.devpieter.divine.rendering.text.texts.TextLine;
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
        return hypixelManager.isInTheEnd()
                || golemManager.currentStage() != GolemStage.UNDEFINED
                || golemManager.currentLocation() != GolemLocation.UNDEFINED;
    }

    @Override
    protected void renderWidget(DrawContext context) {
        List<IText> lines = new ArrayList<>();
        lines.add(TextLine.off("text.divine.widget.tracker.stage", labelStyle, golemManager.getFormattedStageText()));
        lines.add(TextLine.off("text.divine.widget.tracker.location", labelStyle, golemManager.getFormattedLocationText()));

        drawDynamicBox(context, client.textRenderer, 0, 0, backgroundColor, getTitle(), lines);
    }

    @Override
    protected void renderDummyWidget(DrawContext context) {
        drawDynamicBox(context, client.textRenderer, 0, 0, backgroundColor, getTitle(), getDummyLines());
    }

    @Override
    public int dummyWidth() {
        return calculateBoxWidth(client.textRenderer, getTitle(), getDummyLines());
    }

    @Override
    public int dummyHeight() {
        return calculateBoxHeight(client.textRenderer, getTitle(), getDummyLines());
    }

    private Text getTitle() {
        return TextFormatUtils.format("text.divine.widget.tracker.title", titleStyle);
    }

    private List<IText> getDummyLines() {
        return List.of(
                TextLine.off("text.divine.widget.tracker.stage", labelStyle, "Stage Name"),
                TextLine.off("text.divine.widget.tracker.location", labelStyle, "Location Name")
        );
    }
}
