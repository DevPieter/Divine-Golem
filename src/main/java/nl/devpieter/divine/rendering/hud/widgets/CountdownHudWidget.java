package nl.devpieter.divine.rendering.hud.widgets;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import nl.devpieter.divine.GolemManager;
import nl.devpieter.divine.HypixelManager;
import nl.devpieter.divine.rendering.hud.widget.HudWidget;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CountdownHudWidget extends HudWidget {

    private final MinecraftClient client = MinecraftClient.getInstance();

    private final HypixelManager hypixelManager = HypixelManager.getInstance();
    private final GolemManager golemManager = GolemManager.getInstance();

    private final List<Text> dummyLines = List.of(
            formatLine("Spawns In: ", getFormattedTime(16.1)),
            formatLine("TPS Adjusted: ", getFormattedTime(16.7))
    );

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
        lines.add(formatLine("Spawns In: ", getFormattedRealTime()));
        lines.add(formatLine("TPS Adjusted: ", getFormattedInGameTime()));

        drawDynamicBox(context, 0, 0, backgroundColor, lines, client.textRenderer);
    }

    @Override
    protected void renderDummyWidget(DrawContext context) {
        drawDynamicBox(context, 0, 0, backgroundColor, dummyLines, client.textRenderer);
    }

    @Override
    public int dummyWidth() {
        return calculateBoxWidth(dummyLines, client.textRenderer);
    }

    @Override
    public int dummyHeight() {
        return calculateBoxHeight(dummyLines, client.textRenderer);
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
