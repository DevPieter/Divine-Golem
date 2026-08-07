package nl.devpieter.divine.rendering.hud.widgets;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
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

    private final Minecraft client = Minecraft.getInstance();
    private final GolemManager golemManager = GolemManager.getInstance();

    private ItemStack tierBoostCoreStack = null;
    private ItemStack golemPetStack = null;

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
    protected void renderWidget(GuiGraphicsExtractor graphics) {
        if (tierBoostCoreStack == null) tierBoostCoreStack = new ItemStack(Items.DYE.red());
        if (golemPetStack == null) golemPetStack = new ItemStack(Items.SKELETON_SKULL);

        LootQualityBreakdown breakdown = golemManager.currentFightBreakdown().calculateLootQualityBreakdown();
        if (breakdown == null) return;

        List<IText> lines = new ArrayList<>();
//        lines.add(TextFormatUtils.format("text.divine.widget.loot_quality.base_quality", labelStyle, valueStyle, breakdown.baseQuality()));
        lines.add(TextLine.off("text.divine.widget.loot_quality.final_quality", labelStyle, breakdown.finalQuality()));
        lines.add(TextSpacer.of(6));

        String yes = Component.translatable("text.divine.generic.yes").getString();
        String no = Component.translatable("text.divine.generic.no").getString();

        boolean couldDropTbc = breakdown.finalQuality() >= 250;
        lines.add(ItemStackText.off(tierBoostCoreStack, "text.divine.widget.loot_quality.tier_boost_core", labelStyle, couldDropTbc ? yes : no));

        boolean canDropLgp = breakdown.finalQuality() >= 235;
        lines.add(ItemStackText.off(golemPetStack, "text.divine.widget.loot_quality.golem_pet_legendary", labelStyle, canDropLgp ? yes : no));

        boolean canDropEgp = breakdown.finalQuality() >= 220;
        lines.add(ItemStackText.off(golemPetStack, "text.divine.widget.loot_quality.golem_pet_epic", labelStyle, canDropEgp ? yes : no));

        drawDynamicBox(graphics, client.font, 0, 0, backgroundColor, lines);
    }

    @Override
    protected void renderDummyWidget(GuiGraphicsExtractor graphics) {
        if (tierBoostCoreStack == null) tierBoostCoreStack = new ItemStack(Items.DYE.red());
        if (golemPetStack == null) golemPetStack = new ItemStack(Items.SKELETON_SKULL);

        drawDynamicBox(graphics, client.font, 0, 0, backgroundColor, getDummyLines());
    }

    @Override
    public int dummyWidth() {
        return calculateBoxWidth(client.font, getDummyLines());
    }

    @Override
    public int dummyHeight() {
        return calculateBoxHeight(client.font, getDummyLines());
    }

    private List<IText> getDummyLines() {
        String yes = Component.translatable("text.divine.generic.yes").getString();

        return List.of(
                TextLine.off("text.divine.widget.loot_quality.final_quality", labelStyle, "250"),
                TextSpacer.of(6),
                ItemStackText.off(tierBoostCoreStack, "text.divine.widget.loot_quality.tier_boost_core", labelStyle, yes),
                ItemStackText.off(golemPetStack, "text.divine.widget.loot_quality.golem_pet_legendary", labelStyle, yes),
                ItemStackText.off(golemPetStack, "text.divine.widget.loot_quality.golem_pet_epic", labelStyle, yes)
        );
    }
}
