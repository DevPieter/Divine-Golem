package nl.devpieter.divine.rendering.hud.widgets;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import nl.devpieter.divine.GolemManager;
import nl.devpieter.divine.models.fightBreakdown.DamageBreakdown;
import nl.devpieter.divine.rendering.hud.widget.HudWidget;
import nl.devpieter.divine.rendering.text.IText;
import nl.devpieter.divine.rendering.text.texts.TextLine;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class DamageBreakdownHudWidget extends HudWidget {

    private final MinecraftClient client = MinecraftClient.getInstance();
    private final GolemManager golemManager = GolemManager.getInstance();

    @Override
    public @NotNull String name() {
        return "Damage Breakdown";
    }

    @Override
    public @NotNull String identifier() {
        return "damage_breakdown";
    }

    @Override
    public boolean shouldRender() {
        return golemManager.currentFightBreakdown() != null
                && golemManager.currentFightBreakdown().isComplete();
    }

    @Override
    protected void renderWidget(DrawContext context) {
        DamageBreakdown breakdown = golemManager.currentFightBreakdown().calculateDamageBreakdown();
        if (breakdown == null) return;

        List<IText> lines = new ArrayList<>();
//        lines.add(TextLine.off("text.divine.widget.damage_per_second.real_dps", labelStyle, formatDecimal(breakdown.realDps())));
//        lines.add(TextLine.off("text.divine.widget.damage_per_second.tps_adjusted", labelStyle, formatDecimal(breakdown.inGameDps())));

        lines.add(TextLine.off("text.divine.widget.damage_per_second.real_dps", labelStyle, formatDecimal(breakdown.realDps())));

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
                TextLine.off("text.divine.widget.damage_per_second.real_dps", labelStyle, "32.139,84")
//                TextLine.off("text.divine.widget.damage_per_second.tps_adjusted", labelStyle, "28.456,12")
        );
    }

    private String formatDecimal(double value) {
        return String.format("%,.2f", value);
    }
}
