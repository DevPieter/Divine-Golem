package nl.devpieter.divine.config.widget;

import net.minecraft.client.gui.DrawContext;
import org.jetbrains.annotations.NotNull;

public interface IHudWidget {

    @NotNull String identifier();

    boolean shouldRender();

    void render(@NotNull DrawContext context);

    void renderDummy(@NotNull DrawContext context);

    int dummyWidth();

    int dummyHeight();
}
