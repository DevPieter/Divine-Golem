package nl.devpieter.divine.config.widgets;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import nl.devpieter.divine.config.widget.HudWidget;
import org.jetbrains.annotations.NotNull;

public class Test2Widget extends HudWidget {

    @Override
    public @NotNull String identifier() {
        return "test_widget_2";
    }

    @Override
    public int width() {
        return 100;
    }

    @Override
    public int height() {
        return 100;
    }

    @Override
    protected void renderWidget(DrawContext context) {
        context.fill(0, 0, width(), height(), 0x88000000);

        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
        context.drawCenteredTextWithShadow(textRenderer, "Test Widget 2", width() / 2, height() / 2 - textRenderer.fontHeight / 2, 0xFFFFFFFF);
    }

    @Override
    protected void renderDummyWidget(DrawContext context) {
        context.fill(0, 0, width(), height(), 0x88000000);

        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
        context.drawCenteredTextWithShadow(textRenderer, "Test Widget 2 (D)", width() / 2, height() / 2 - textRenderer.fontHeight / 2, 0xFFFFFFFF);
    }
}
