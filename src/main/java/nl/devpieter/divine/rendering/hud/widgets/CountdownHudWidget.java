package nl.devpieter.divine.rendering.hud.widgets;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import nl.devpieter.divine.GolemManager;
import nl.devpieter.divine.rendering.hud.widget.HudWidget;
import nl.devpieter.divine.rendering.text.IText;
import nl.devpieter.divine.rendering.text.texts.TextLine;
import nl.devpieter.divine.rendering.text.texts.TextSpacer;
import nl.devpieter.divine.utils.WorldUtils;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CountdownHudWidget extends HudWidget {

    private static final long DIFFERENCE_DISPLAY_DURATION = 2_000;

    private final MinecraftClient client = MinecraftClient.getInstance();
    private final GolemManager golemManager = GolemManager.getInstance();

    private boolean isShowingDifference = false;
    private long differenceShowStartTime = -1;

    @Override
    public @NotNull String name() {
        return "Spawn Countdown";
    }

    @Override
    public @NotNull String identifier() {
        return "countdown";
    }

    @Override
    public boolean shouldRender() {
        return golemManager.isAboutToSpawn();
    }

    @Override
    protected void renderWidget(DrawContext context) {
        List<IText> lines = new ArrayList<>();
        lines.add(TextLine.off("text.divine.widget.countdown.spawns_in", labelStyle, getFormattedRealTime()));
        lines.add(TextLine.off("text.divine.widget.countdown.tps_adjusted", labelStyle, getFormattedInGameTime()));

        lines.addAll(getOptionalDifferenceLines());
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
                TextLine.off("text.divine.widget.countdown.spawns_in", labelStyle, "16.1 seconds"),
                TextLine.off("text.divine.widget.countdown.tps_adjusted", labelStyle, "16.7 seconds"),
                TextSpacer.of(16),
                TextLine.off("text.divine.widget.countdown.difference", labelStyle, "+0.60 seconds")
        );
    }

    private double getRealTimeSecondsLeft() {
        long nowRealTime = golemManager.fightAboutToStartRealTime();
        if (nowRealTime == -1) return -1;

        long currentRealTime = System.currentTimeMillis();
        long realTimeElapsed = currentRealTime - nowRealTime;
        return Math.max(0, (GolemManager.GOLEM_SPAWN_DELAY - realTimeElapsed) / 1000.0);
    }

    private double getInGameTimeSecondsLeft() {
        long nowInGameTime = golemManager.fightAboutToStartInGameTime();
        if (nowInGameTime == -1) return -1;

        long currentInGameTime = WorldUtils.getWorldTime();
        long inGameTimeElapsed = currentInGameTime - nowInGameTime;
        return Math.max(0, (((double) GolemManager.GOLEM_SPAWN_DELAY / 1000) * 20 - inGameTimeElapsed) / 20.0);
    }

    private double getDifference() {
        double realTimeSecondsLeft = getRealTimeSecondsLeft();
        double inGameTimeSecondsLeft = getInGameTimeSecondsLeft();

        if (realTimeSecondsLeft == -1 || inGameTimeSecondsLeft == -1) return 0.0;
        return realTimeSecondsLeft - inGameTimeSecondsLeft;
    }

    private String getFormattedRealTime() {
        double realTimeSecondsLeft = getRealTimeSecondsLeft();
        return realTimeSecondsLeft == -1 ? "N/A" : getFormattedTime(realTimeSecondsLeft);
    }

    private String getFormattedInGameTime() {
        double inGameTimeSecondsLeft = getInGameTimeSecondsLeft();
        return inGameTimeSecondsLeft == -1 ? "N/A" : getFormattedTime(inGameTimeSecondsLeft);
    }

    private List<IText> getOptionalDifferenceLines() {
        if (!hasNotableDifference() && !isShowingDifference) return List.of();

        if (hasNotableDifference()) {
            isShowingDifference = true;
            differenceShowStartTime = System.currentTimeMillis();
        }

        if (System.currentTimeMillis() - differenceShowStartTime >= DIFFERENCE_DISPLAY_DURATION) {
            isShowingDifference = false;
            return List.of();
        }

        return List.of(
                TextSpacer.of(16),
                TextLine.off("text.divine.widget.countdown.difference", labelStyle, getFormattedDifference())
        );
    }

    private boolean hasNotableDifference() {
        return Math.abs(getDifference()) >= 1.0;
    }

    private String getFormattedDifference() {
        double difference = getDifference();

        boolean isAhead = difference < 0;
        String sign = isAhead ? "-" : "+";

        return sign + getFormattedTime(Math.abs(difference));
    }

    private String getFormattedTime(double time) {
        return String.format("%.2f seconds", time);
    }
}
