package nl.devpieter.divine.config.widget;

import net.minecraft.client.gui.DrawContext;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

public interface IHudWidget {

    @NotNull String identifier();

    void render(@NonNull DrawContext context);

    void renderDummy(@NonNull DrawContext context);

    int width();

    int height();
}
