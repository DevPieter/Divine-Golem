package nl.devpieter.divine.mixins;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import nl.devpieter.divine.GolemManager;
import nl.devpieter.divine.HypixelManager;
import nl.devpieter.utilize.utils.minecraft.TextUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin {

    @Unique
    private static final Style TITLE_STYLE = Style.EMPTY.withColor(0x3be477).withBold(true);
    @Unique
    private static final Style LABEL_STYLE = Style.EMPTY.withColor(0x8f8f8f);
    @Unique
    private static final Style VALUE_STYLE = Style.EMPTY.withColor(0x3be477).withItalic(true);

    @Unique
    private static final int BOX_BACKGROUND_COLOR = new Color(18, 18, 18, 179).getRGB();

    @Shadow
    public abstract TextRenderer getTextRenderer();

    @Unique
    private HypixelManager hypixelManager /*= HypixelManager.getInstance()*/;
    @Unique
    private GolemManager golemManager /*= GolemManager.getInstance()*/;

    @Inject(at = @At("HEAD"), method = "renderScoreboardSidebar(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/client/render/RenderTickCounter;)V")
    private void renderScoreboardSidebar(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {

        if (hypixelManager == null || golemManager == null) {
            deferredInitialization();
            return;
        }

        if (!hypixelManager.isInTheEnd()) return;
        TextRenderer textRenderer = this.getTextRenderer();

        List<Text> lines = new ArrayList<>();
        lines.add(formatLine("Stage: ", golemManager.getFormattedStageText()));
        lines.add(formatLine("Location: ", golemManager.getFormattedLocationText()));

        Text titleText = TextUtils.withStyle("Divine Golem Tracker", TITLE_STYLE);
        drawDynamicBox(context, 20, 20, BOX_BACKGROUND_COLOR, titleText, lines, textRenderer);
    }

    @Unique
    private void deferredInitialization() {
        if (hypixelManager == null) hypixelManager = HypixelManager.getInstance();
        if (golemManager == null) golemManager = GolemManager.getInstance();
    }

    @Unique
    private Text formatLine(String label, String value) {
        return TextUtils.withStyle(label, LABEL_STYLE).append(TextUtils.withStyle(value, VALUE_STYLE));
    }

    @Unique
    private void drawDynamicBox(DrawContext context, int x, int y, int color, Text title, List<Text> lines, TextRenderer textRenderer) {
        int largestTextWidth = textRenderer.getWidth(title);

        for (Text line : lines) {
            int lineWidth = textRenderer.getWidth(line);
            if (lineWidth > largestTextWidth) largestTextWidth = lineWidth;
        }

        int lineHeight = textRenderer.fontHeight + 2;
        int titleHeight = textRenderer.fontHeight + 8;

        int boxPadding = 10;
        int boxWidth = largestTextWidth + boxPadding * 2;
        int boxHeight = titleHeight + (lineHeight * lines.size()) + (boxPadding * 2) - 4;

        // Draw background
        context.fill(x, y, x + boxWidth, y + boxHeight, color);

        int textX = x + boxPadding;
        int textY = y + boxPadding;

        // Draw title
        context.drawTextWithShadow(textRenderer, title, textX, textY, 0xFFFFFFFF);
        textY += titleHeight;

        // Draw lines
        for (Text line : lines) {
            context.drawTextWithShadow(textRenderer, line, textX, textY, 0xFFFFFFFF);
            textY += lineHeight;
        }
    }
}
