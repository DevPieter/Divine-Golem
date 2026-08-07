package nl.devpieter.divine.mixins;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.Hud;
import net.minecraft.world.scores.Objective;
import nl.devpieter.divine.HypixelManager;
import nl.devpieter.divine.rendering.hud.HudManager;
import nl.devpieter.divine.rendering.hud.widget.IHudWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Hud.class)
public abstract class InGameHudMixin {

    @Unique
    private HypixelManager hypixelManager;
    @Unique
    private HudManager hudManager;

//    @Inject(at = @At("HEAD"), method = "renderScoreboardSidebar(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/client/render/RenderTickCounter;)V")
//    private void renderScoreboardSidebar(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
//
//        if (hypixelManager == null || hudManager == null) {
//            deferredInitialization();
//            return;
//        }
//
//        if (!hudManager.shouldRender()) return;
//
//        for (IHudWidget widget : hudManager.widgets()) {
//            if (!hudManager.isWidgetEnabled(widget.identifier()) || !widget.shouldRender()) continue;
//            widget.render(context);
//        }
//    }

    @Inject(at = @At("HEAD"), method = "displayScoreboardSidebar")
    private void displayScoreboardSidebar(GuiGraphicsExtractor graphics, Objective objective, CallbackInfo ci) {

        if (hypixelManager == null || hudManager == null) {
            deferredInitialization();
            return;
        }

        if (!hudManager.shouldRender()) return;

        for (IHudWidget widget : hudManager.widgets()) {
            if (!hudManager.isWidgetEnabled(widget.identifier()) || !widget.shouldRender()) continue;
            widget.render(graphics);
        }
    }

    @Unique
    private void deferredInitialization() {
        if (hypixelManager == null) hypixelManager = HypixelManager.getInstance();
        if (hudManager == null) hudManager = HudManager.getInstance();
    }
}
