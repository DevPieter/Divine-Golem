package nl.devpieter.divine.rendering.hud.widgets;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import nl.devpieter.divine.GolemManager;
import nl.devpieter.divine.HypixelManager;
import nl.devpieter.divine.rendering.hud.widget.HudWidget;
import nl.devpieter.divine.utils.FormatUtils;
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
        List<Text> lines = new ArrayList<>();
        lines.add(FormatUtils.formatTranslatable("text.divine.widget.countdown.spawns_in", labelStyle, valueStyle, getFormattedRealTime()));
        lines.add(FormatUtils.formatTranslatable("text.divine.widget.countdown.tps_adjusted", labelStyle, valueStyle, getFormattedInGameTime()));

        lines.addAll(getOptionalDifferenceLines());
        drawDynamicBox(context, 0, 0, backgroundColor, lines, client.textRenderer);
    }

    @Override
    protected void renderDummyWidget(DrawContext context) {
        drawDynamicBox(context, 0, 0, backgroundColor, getDummyLines(), client.textRenderer);
    }

    @Override
    public int dummyWidth() {
        return calculateBoxWidth(getDummyLines(), client.textRenderer);
    }

    @Override
    public int dummyHeight() {
        return calculateBoxHeight(getDummyLines(), client.textRenderer);
    }

    private List<Text> getDummyLines() {
        return List.of(
                FormatUtils.formatTranslatable("text.divine.widget.countdown.spawns_in", labelStyle, valueStyle, "16.1 seconds"),
                FormatUtils.formatTranslatable("text.divine.widget.countdown.tps_adjusted", labelStyle, valueStyle, "16.7 seconds"),
                Text.empty(),
                FormatUtils.formatTranslatable("text.divine.widget.countdown.difference", labelStyle, valueStyle, "+0.60 seconds")
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

    private List<Text> getOptionalDifferenceLines() {
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
                Text.empty(),
                FormatUtils.formatTranslatable("text.divine.widget.countdown.difference", labelStyle, valueStyle, getFormattedDifference())
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
