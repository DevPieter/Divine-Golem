package nl.devpieter.divine.mixins;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import nl.devpieter.divine.HypixelManager;
import nl.devpieter.divine.config.HudManager;
import nl.devpieter.divine.config.widget.IHudWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin {

    @Unique
    private HypixelManager hypixelManager;
    @Unique
    private HudManager hudManager;

    @Inject(at = @At("HEAD"), method = "renderScoreboardSidebar(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/client/render/RenderTickCounter;)V")
    private void renderScoreboardSidebar(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {

        if (hypixelManager == null || hudManager == null) {
            deferredInitialization();
            return;
        }

//        if (!hypixelManager.isOnHypixel() || !hudManager.shouldRender()) return;
        if (!hudManager.shouldRender()) return;

        for (IHudWidget widget : hudManager.widgets()) {
            if (widget.shouldRender()) widget.render(context);
        }
    }

    @Unique
    private void deferredInitialization() {
        if (hypixelManager == null) hypixelManager = HypixelManager.getInstance();
        if (hudManager == null) hudManager = HudManager.getInstance();
    }
}
