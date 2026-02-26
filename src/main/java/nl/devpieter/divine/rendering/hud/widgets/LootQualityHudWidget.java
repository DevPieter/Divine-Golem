package nl.devpieter.divine.rendering.hud.widgets;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import nl.devpieter.divine.GolemManager;
import nl.devpieter.divine.models.fightBreakdown.LootQualityBreakdown;
import nl.devpieter.divine.rendering.hud.widget.HudWidget;
import nl.devpieter.divine.rendering.text.IText;
import nl.devpieter.divine.rendering.text.texts.ItemStackText;
import nl.devpieter.divine.rendering.text.texts.TextLine;
import nl.devpieter.divine.rendering.text.texts.TextSpacer;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class LootQualityHudWidget extends HudWidget {

    private final MinecraftClient client = MinecraftClient.getInstance();
    private final GolemManager golemManager = GolemManager.getInstance();

    private final ItemStack tierBoostCoreStack = new ItemStack(Items.RED_DYE);
    private final ItemStack golemPetStack = new ItemStack(Items.SKELETON_SKULL);

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

        LootQualityBreakdown breakdown = golemManager.currentFightBreakdown().calculateLootQualityBreakdown();
        if (breakdown == null) return;

        List<IText> lines = new ArrayList<>();
//        lines.add(TextFormatUtils.format("text.divine.widget.loot_quality.base_quality", labelStyle, valueStyle, breakdown.baseQuality()));
        lines.add(TextLine.off("text.divine.widget.loot_quality.final_quality", labelStyle, breakdown.finalQuality()));
        lines.add(TextSpacer.of(6));

        String yes = Text.translatable("text.divine.generic.yes").getString();
        String no = Text.translatable("text.divine.generic.no").getString();

        boolean couldDropTbc = breakdown.finalQuality() >= 250;
        lines.add(ItemStackText.off(tierBoostCoreStack, "text.divine.widget.loot_quality.tier_boost_core", labelStyle, couldDropTbc ? yes : no));

        boolean canDropLgp = breakdown.finalQuality() >= 235;
        lines.add(ItemStackText.off(golemPetStack, "text.divine.widget.loot_quality.golem_pet_legendary", labelStyle, canDropLgp ? yes : no));

        boolean canDropEgp = breakdown.finalQuality() >= 220;
        lines.add(ItemStackText.off(golemPetStack, "text.divine.widget.loot_quality.golem_pet_epic", labelStyle, canDropEgp ? yes : no));

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
        String yes = Text.translatable("text.divine.generic.yes").getString();
        String no = Text.translatable("text.divine.generic.no").getString();

        return List.of(
                TextLine.off("text.divine.widget.loot_quality.final_quality", labelStyle, "235"),
                TextSpacer.of(6),
                ItemStackText.off(tierBoostCoreStack, "text.divine.widget.loot_quality.tier_boost_core", labelStyle, no),
                ItemStackText.off(golemPetStack, "text.divine.widget.loot_quality.golem_pet_legendary", labelStyle, yes),
                ItemStackText.off(golemPetStack, "text.divine.widget.loot_quality.golem_pet_epic", labelStyle, yes)
        );
    }
}
