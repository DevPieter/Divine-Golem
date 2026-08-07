package nl.devpieter.divine.rendering.hud.widget;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import org.jetbrains.annotations.NotNull;

public interface IHudWidget {

    @NotNull String name();

    @NotNull String identifier();

    boolean shouldRender();

    void render(@NotNull GuiGraphicsExtractor graphics);

    void renderDummy(@NotNull GuiGraphicsExtractor graphics, boolean disabled);

    void renderDummyHighlighted(@NotNull GuiGraphicsExtractor graphics, boolean disabled);

    int dummyWidth();

    int dummyHeight();
}
