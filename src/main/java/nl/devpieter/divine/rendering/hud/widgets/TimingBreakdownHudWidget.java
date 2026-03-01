package nl.devpieter.divine.rendering.hud.widgets;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import nl.devpieter.divine.GolemManager;
import nl.devpieter.divine.models.fightBreakdown.TimingBreakdown;
import nl.devpieter.divine.rendering.hud.widget.HudWidget;
import nl.devpieter.divine.rendering.text.IText;
import nl.devpieter.divine.rendering.text.texts.TextLine;
import nl.devpieter.divine.utils.TimeUtils;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class TimingBreakdownHudWidget extends HudWidget {

    private final MinecraftClient client = MinecraftClient.getInstance();
    private final GolemManager golemManager = GolemManager.getInstance();

    @Override
    public @NotNull String name() {
        return "Timing Breakdown";
    }

    @Override
    public @NotNull String identifier() {
        return "timing_breakdown";
    }

    @Override
    public boolean shouldRender() {
        return golemManager.currentFightBreakdown() != null;
    }

    @Override
    protected void renderWidget(DrawContext context) {
        TimingBreakdown breakdown = golemManager.currentFightBreakdown().calculateTimingBreakdown();
        if (breakdown == null) return;

        List<IText> lines = new ArrayList<>();
//        lines.add(TextLine.off("text.divine.widget.timing_breakdown.fight_duration.real", labelStyle, formatTime(breakdown.fightDurationRealMillis())));
//        lines.add(TextLine.off("text.divine.widget.timing_breakdown.fight_duration.tps_adjusted", labelStyle, formatTime(breakdown.fightDurationInGameMillis())));
//        lines.add(TextSpacer.of(6));
//        lines.add(TextLine.off("text.divine.widget.timing_breakdown.time_before_spawn.real", labelStyle, formatTime(breakdown.timeBeforeSpawnRealMillis())));
//        lines.add(TextLine.off("text.divine.widget.timing_breakdown.time_before_spawn.tps_adjusted", labelStyle, formatTime(breakdown.timeBeforeSpawnInGameMillis())));

        lines.add(TextLine.off("text.divine.widget.timing_breakdown.fight_duration.real", labelStyle, TimeUtils.formatDurationSSmmm(breakdown.fightDurationInGameMillis())));
        lines.add(TextLine.off("text.divine.widget.timing_breakdown.time_before_spawn.real", labelStyle, TimeUtils.formatDurationSSmmm(breakdown.timeBeforeSpawnInGameMillis())));

        drawDynamicBox(context, client.textRenderer, 0, 0, backgroundColor, lines);
    }

    @Override
    protected void renderDummyWidget(DrawContext context) {
        drawDynamicBox(context, client.textRenderer, 0, 0, backgroundColor, getDummyLines());
    }

    @Override
    public int dummyWidth() {
        return calculateBoxWidth(client.textRenderer, getDummyLines());
    }

    @Override
    public int dummyHeight() {
        return calculateBoxHeight(client.textRenderer, getDummyLines());
    }

    private List<IText> getDummyLines() {
        return List.of(
                TextLine.off("text.divine.widget.timing_breakdown.fight_duration.real", labelStyle, TimeUtils.formatDurationSSmmm(16_345)),
                TextLine.off("text.divine.widget.timing_breakdown.time_before_spawn.real", labelStyle, TimeUtils.formatDurationSSmmm(20_000))
        );
    }
}
