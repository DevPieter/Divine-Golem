package nl.devpieter.divine.rendering.hud.widgets;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import nl.devpieter.divine.GolemManager;
import nl.devpieter.divine.models.fightBreakdown.TimingBreakdown;
import nl.devpieter.divine.rendering.hud.widget.HudWidget;
import nl.devpieter.divine.rendering.text.IText;
import nl.devpieter.divine.rendering.text.texts.TextLine;
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
//        lines.add(TextLine.off("text.divine.widget.timing_breakdown.fight_duration_real", labelStyle, formatTime(breakdown.fightDurationRealMilliseconds())));
//        lines.add(TextLine.off("text.divine.widget.timing_breakdown.fight_duration_in_game", labelStyle, formatTime(breakdown.fightDurationInGameMilliseconds())));
//        lines.add(TextSpacer.of(6));
//        lines.add(TextLine.off("text.divine.widget.timing_breakdown.time_before_spawn_real", labelStyle, formatTime(breakdown.timeBeforeSpawnRealMilliseconds())));
//        lines.add(TextLine.off("text.divine.widget.timing_breakdown.time_before_spawn_in_game", labelStyle, formatTime(breakdown.timeBeforeSpawnInGameMilliseconds())));

        lines.add(TextLine.off("text.divine.widget.timing_breakdown.fight_duration_real", labelStyle, formatTime(breakdown.fightDurationInGameMilliseconds())));
        lines.add(TextLine.off("text.divine.widget.timing_breakdown.time_before_spawn_real", labelStyle, formatTime(breakdown.timeBeforeSpawnInGameMilliseconds())));

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
                TextLine.off("Fight Duration: <highlight>%s</highlight>", labelStyle, "15.345"),
//                TextLine.off("TPS Adjusted: <highlight>%s</highlight>", labelStyle, "14.750"),
//                TextSpacer.of(6),
                TextLine.off("Time Before Spawn: <highlight>%s</highlight>", labelStyle, "20.000")
//                TextLine.off("TPS Adjusted: <highlight>%s</highlight>", labelStyle, "19.500")
        );
    }

    private String formatTime(long timeInMillis) {
        long seconds = (timeInMillis % 60000) / 1000;
        long milliseconds = timeInMillis % 1000;
        return String.format("%02d.%03d", seconds, milliseconds);
    }
}
