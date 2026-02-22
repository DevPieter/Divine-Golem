package nl.devpieter.divine.config.widget;

import net.minecraft.client.gui.DrawContext;
import nl.devpieter.divine.config.HudManager;
import nl.devpieter.divine.models.ScreenPosition;
import org.joml.Matrix3x2fStack;
import org.jspecify.annotations.NonNull;

public abstract class HudWidget implements IHudWidget {

    private final HudManager hudManager = HudManager.getInstance();

    @Override
    public void render(@NonNull DrawContext context) {
        pushMatrix(context);
        renderWidget(context);
        popMatrix(context);
    }

    @Override
    public void renderDummy(@NonNull DrawContext context) {
        pushMatrix(context);
        renderDummyWidget(context);
        popMatrix(context);
    }

    protected abstract void renderWidget(DrawContext context);

    protected abstract void renderDummyWidget(DrawContext context);

    private void pushMatrix(@NonNull DrawContext context) {
        Matrix3x2fStack matrixStack = context.getMatrices();
        matrixStack.pushMatrix();

        ScreenPosition pos = hudManager.getWidgetPosition(identifier());
        if (pos == null) pos = new ScreenPosition(0, 0);

        matrixStack.translate(pos.x(), pos.y());
    }

    private void popMatrix(@NonNull DrawContext context) {
        context.getMatrices().popMatrix();
    }
}
