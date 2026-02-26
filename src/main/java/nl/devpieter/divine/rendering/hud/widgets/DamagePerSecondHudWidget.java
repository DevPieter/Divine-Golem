package nl.devpieter.divine.rendering.hud.widgets;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import nl.devpieter.divine.GolemManager;
import nl.devpieter.divine.formatter.TextFormatUtils;
import nl.devpieter.divine.models.fightBreakdown.DamageBreakdown;
import nl.devpieter.divine.rendering.hud.widget.HudWidget;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class DamagePerSecondHudWidget extends HudWidget {

    private final MinecraftClient client = MinecraftClient.getInstance();
    private final GolemManager golemManager = GolemManager.getInstance();

    @Override
    public @NotNull String name() {
        return "Damage Per Second";
    }

    @Override
    public @NotNull String identifier() {
        return "damage_per_second";
    }

    @Override
    public boolean shouldRender() {
        return golemManager.currentFightBreakdown() != null
                && golemManager.currentFightBreakdown().isComplete();
    }

    @Override
    protected void renderWidget(DrawContext context) {
        List<Text> lines = new ArrayList<>();

        DamageBreakdown breakdown = golemManager.currentFightBreakdown().calculateDamageBreakdown();
        if (breakdown == null) return;

        lines.add(TextFormatUtils.format("text.divine.widget.damage_per_second.real_dps", labelStyle, formatDecimal(breakdown.realDps())));
        lines.add(TextFormatUtils.format("text.divine.widget.damage_per_second.tps_adjusted", labelStyle, formatDecimal(breakdown.inGameDps())));

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
                TextFormatUtils.format("text.divine.widget.damage_per_second.real_dps", labelStyle, "32.139,84"),
                TextFormatUtils.format("text.divine.widget.damage_per_second.tps_adjusted", labelStyle, "28.456,12")
        );
    }

    private String formatDecimal(double value) {
        return String.format("%,.2f", value);
    }
}
