package nl.devpieter.divine.rendering.hud.widgets;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import nl.devpieter.divine.GolemManager;
import nl.devpieter.divine.models.fightBreakdown.LootQualityBreakdown;
import nl.devpieter.divine.rendering.hud.widget.HudWidget;
import nl.devpieter.divine.utils.FormatUtils;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class LootQualityHudWidget extends HudWidget {

    private final MinecraftClient client = MinecraftClient.getInstance();
    private final GolemManager golemManager = GolemManager.getInstance();

    @Override
    public @NotNull String name() {
        return "Loot Quality";
    }

    @Override
    public @NotNull String identifier() {
        return "loot_quality";
    }

    @Override
    public boolean shouldRender() {
        return golemManager.currentFightBreakdown() != null
                && golemManager.currentFightBreakdown().isComplete();
    }

    @Override
    protected void renderWidget(DrawContext context) {
        List<Text> lines = new ArrayList<>();

        LootQualityBreakdown breakdown = golemManager.currentFightBreakdown().calculateLootQualityBreakdown();
        if (breakdown == null) return;

//        lines.add(FormatUtils.formatTranslatable("text.divine.widget.loot_quality.base_quality", labelStyle, valueStyle, breakdown.baseQuality()));
        lines.add(FormatUtils.formatTranslatable("text.divine.widget.loot_quality.final_quality", labelStyle, valueStyle, breakdown.finalQuality()));

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
//                FormatUtils.formatTranslatable("text.divine.widget.loot_quality.base_quality", labelStyle, valueStyle, "200"),
                FormatUtils.formatTranslatable("text.divine.widget.loot_quality.final_quality", labelStyle, valueStyle, "250")
        );
    }
}
