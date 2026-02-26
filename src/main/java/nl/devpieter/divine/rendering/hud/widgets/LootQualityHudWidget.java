package nl.devpieter.divine.rendering.hud.widgets;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import nl.devpieter.divine.GolemManager;
import nl.devpieter.divine.formatter.TextFormatUtils;
import nl.devpieter.divine.models.fightBreakdown.LootQualityBreakdown;
import nl.devpieter.divine.rendering.hud.widget.HudWidget;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
        List<IText> lines = new ArrayList<>();

        LootQualityBreakdown breakdown = golemManager.currentFightBreakdown().calculateLootQualityBreakdown();
        if (breakdown == null) return;

//        lines.add(TextFormatUtils.format("text.divine.widget.loot_quality.base_quality", labelStyle, valueStyle, breakdown.baseQuality()));
        lines.add(new TextLine(TextFormatUtils.format("text.divine.widget.loot_quality.final_quality", labelStyle, breakdown.finalQuality())));

        // Tier Boost Core = 250
        // Golem Pet (legendary) = 235
        // Golem Pet (epic) = 220

        ItemStack tbcStack = new ItemStack(Items.RED_DYE);
        boolean couldDropTbc = breakdown.finalQuality() >= 250;
        lines.add(new ItemStackText(tbcStack, TextFormatUtils.format("Tier Boost Core: %s", labelStyle, couldDropTbc ? "Yes" : "No")));

        drawDynamicBoxT(context, 0, 0, backgroundColor, null, lines, client.textRenderer);
    }

    @Override
    protected void renderDummyWidget(DrawContext context) {
        drawDynamicBoxT(context, 0, 0, backgroundColor, null, getDummyLines(), client.textRenderer);
    }

    @Override
    public int dummyWidth() {
        return calculateBoxHeightT(null, getDummyLines(), client.textRenderer);
    }

    @Override
    public int dummyHeight() {
        return calculateBoxHeightT(null, getDummyLines(), client.textRenderer);
    }

    private List<IText> getDummyLines() {

        ItemStack tbcStack = new ItemStack(Items.RED_DYE);
        ItemStack golemPetStack = new ItemStack(Items.SKELETON_SKULL);

//        return List.of(
////                TextFormatUtils.format("text.divine.widget.loot_quality.base_quality", labelStyle, valueStyle, "200"),
//                TextFormatUtils.format("text.divine.widget.loot_quality.final_quality", labelStyle, valueStyle, "250")
//        );

        return List.of(
                new TextLine(TextFormatUtils.format("text.divine.widget.loot_quality.final_quality", labelStyle, "250")),
                new TextLine(Text.empty()),
//                new ItemStackText(tbcStack, TextFormatUtils.format("Tier Boost Core: <highlight>Yes</highlight>", labelStyle, valueStyle)),
                new ItemStackText(tbcStack, TextFormatUtils.format("<r:legendary>Tier Boost Core</r:legendary>: <highlight>%s</highlight>", labelStyle, "Yes")),
                new ItemStackText(golemPetStack, TextFormatUtils.format("<r:legendary>Golem Pet</r:legendary>: <highlight>Yes</highlight>", labelStyle)),
                new ItemStackText(golemPetStack, TextFormatUtils.format("<r:epic>Golem Pet</r:epic>: <highlight>Yes</highlight>", labelStyle))
        );
    }

    interface IText {
        Text text();

        void render(DrawContext context, int x, int y, TextRenderer textRenderer);

        default int width(TextRenderer textRenderer) {
            return textRenderer.getWidth(text());
        }

        default int height(TextRenderer textRenderer) {
            return textRenderer.fontHeight;
        }
    }

    class TextLine implements IText {
        private final Text text;

        public TextLine(Text text) {
            this.text = text;
        }

        @Override
        public Text text() {
            return text;
        }

        @Override
        public void render(DrawContext context, int x, int y, TextRenderer textRenderer) {
            context.drawTextWithShadow(textRenderer, text(), x, y, 0xFFFFFFFF);
        }
    }

    class ItemStackText implements IText {

        private final ItemStack stack;
        private final Text text;

        public ItemStackText(ItemStack stack, Text text) {
            this.stack = stack;
            this.text = text;
        }

        @Override
        public Text text() {
            return text;
        }

        public ItemStack stack() {
            return stack;
        }

        @Override
        public void render(DrawContext context, int x, int y, TextRenderer textRenderer) {
            context.drawItem(stack(), x - 4, y);
            context.drawTextWithShadow(textRenderer, text(), x + 14, y + 6, 0xFFFFFFFF);
        }

        @Override
        public int width(TextRenderer textRenderer) {
            return IText.super.width(textRenderer) + 14;
        }

        @Override
        public int height(TextRenderer textRenderer) {
            return IText.super.height(textRenderer) + 6;
        }
    }

    protected void drawDynamicBoxT(DrawContext context, int x, int y, int color, @Nullable Text title, List<IText> lines, TextRenderer textRenderer) {
        int largestTextWidth = getLargestLineWidthT(title, lines, textRenderer);

        int lineHeight = textRenderer.fontHeight + 2;
        int titleHeight = title != null ? textRenderer.fontHeight + 8 : 0;

        int boxPadding = 10;
        int boxWidth = largestTextWidth + boxPadding * 2;
//        int boxHeight = titleHeight + (lineHeight * lines.size()) + (boxPadding * 2) - 4;

        int boxHeight = 0;
        for (IText line : lines) boxHeight += line.height(textRenderer);
        boxHeight += titleHeight + (boxPadding * 2) - 4;

        // Draw background
        context.fill(x, y, x + boxWidth, y + boxHeight, color);

        int textX = x + boxPadding;
        int textY = y + boxPadding;

        // Draw title
        if (title != null) {
            context.drawTextWithShadow(textRenderer, title, textX, textY, 0xFFFFFFFF);
            textY += titleHeight;
        }

        // Draw lines
        for (IText line : lines) {
//            context.drawTextWithShadow(textRenderer, line, textX, textY, 0xFFFFFFFF);
//            textY += lineHeight;

            line.render(context, textX, textY, textRenderer);
            textY += line.height(textRenderer);
        }
    }

    protected int getLargestLineWidthT(@Nullable Text title, List<IText> lines, TextRenderer textRenderer) {
        int largestTextWidth = 0;
        if (title != null) largestTextWidth = textRenderer.getWidth(title);

        for (IText line : lines) {
//            int lineWidth = textRenderer.getWidth(line);
//            if (lineWidth > largestTextWidth) largestTextWidth = lineWidth;

            int lineWidth = line.width(textRenderer);
            if (lineWidth > largestTextWidth) largestTextWidth = lineWidth;
        }

        return largestTextWidth;
    }

    protected int calculateBoxHeightT(@Nullable Text title, List<IText> lines, TextRenderer textRenderer) {
//        int lineHeight = textRenderer.fontHeight + 2;
//        int titleHeight = title != null ? textRenderer.fontHeight + 8 : 0;
//        int boxPadding = 10;
//        return titleHeight + (lineHeight * lines.size()) + (boxPadding * 2) - 4;

        int boxHeight = 0;
        for (IText line : lines) boxHeight += line.height(textRenderer);

        int titleHeight = title != null ? textRenderer.fontHeight + 8 : 0;
        int boxPadding = 10;
        return boxHeight + titleHeight + (boxPadding * 2) - 4;
    }
}
