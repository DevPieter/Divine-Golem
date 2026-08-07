package nl.devpieter.divine.rendering.hud.widgets;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
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

    private final Minecraft client = Minecraft.getInstance();

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
    protected void renderWidget(GuiGraphicsExtractor graphics) {
        List<IText> lines = new ArrayList<>();
        lines.add(TextLine.off("text.divine.widget.tracker.stage", labelStyle, golemManager.getFormattedStageText()));
        lines.add(TextLine.off("text.divine.widget.tracker.location", labelStyle, golemManager.getFormattedLocationText()));

        drawDynamicBox(graphics, client.font, 0, 0, backgroundColor, getTitle(), lines);
    }

    @Override
    protected void renderDummyWidget(GuiGraphicsExtractor graphics) {
        drawDynamicBox(graphics, client.font, 0, 0, backgroundColor, getTitle(), getDummyLines());
    }

    @Override
    public int dummyWidth() {
        return calculateBoxWidth(client.font, getTitle(), getDummyLines());
    }

    @Override
    public int dummyHeight() {
        return calculateBoxHeight(client.font, getTitle(), getDummyLines());
    }

    private Component getTitle() {
        return TextFormatUtils.format("text.divine.widget.tracker.title", titleStyle);
    }

    private List<IText> getDummyLines() {
        return List.of(
                TextLine.off("text.divine.widget.tracker.stage", labelStyle, "Stage Name"),
                TextLine.off("text.divine.widget.tracker.location", labelStyle, "Location Name")
        );
    }
}
