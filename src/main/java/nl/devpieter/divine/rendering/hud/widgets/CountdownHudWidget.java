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

public class CountdownHudWidget extends HudWidget {

    private final MinecraftClient client = MinecraftClient.getInstance();

    private final HypixelManager hypixelManager = HypixelManager.getInstance();
    private final GolemManager golemManager = GolemManager.getInstance();

    @Override
    public @NotNull String identifier() {
        return "countdown";
    }

    @Override
    public boolean shouldRender() {
        return hypixelManager.isInTheEnd() && golemManager.isAboutToSpawn();
    }

    @Override
    protected void renderWidget(DrawContext context) {
        List<Text> lines = new ArrayList<>();
        lines.add(FormatUtils.formatTranslatable("text.divine.widget.countdown.spawns_in", labelStyle, valueStyle, getFormattedRealTime()));
        lines.add(FormatUtils.formatTranslatable("text.divine.widget.countdown.tps_adjusted", labelStyle, valueStyle, getFormattedInGameTime()));

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
                FormatUtils.formatTranslatable("text.divine.widget.countdown.tps_adjusted", labelStyle, valueStyle, "16.7 seconds")
        );
    }

    private String getFormattedRealTime() {
        long nowRealTime = golemManager.nowRealTime();
        if (nowRealTime == -1) return "N/A";

        long currentRealTime = System.currentTimeMillis();
        long realTimeElapsed = currentRealTime - nowRealTime;
        long realTimeLeft = Math.max(0, GolemManager.GOLEM_SPAWN_DELAY - realTimeElapsed);

        return getFormattedTime(realTimeLeft / 1000.0);
    }

    private String getFormattedInGameTime() {
        long nowInGameTime = golemManager.nowInGameTime();
        if (nowInGameTime == -1 || MinecraftClient.getInstance().world == null) return "N/A";

        long currentInGameTime = MinecraftClient.getInstance().world.getLevelProperties().getTime();
        long inGameTimeElapsed = currentInGameTime - nowInGameTime;
        long inGameTimeLeft = Math.max(0, (GolemManager.GOLEM_SPAWN_DELAY / 1000) * 20 - inGameTimeElapsed);

        return getFormattedTime(inGameTimeLeft / 20.0);
    }

    private String getFormattedTime(double time) {
        return String.format("%.2f seconds", time);
    }
}
