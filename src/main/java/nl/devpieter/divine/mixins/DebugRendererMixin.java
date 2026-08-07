package nl.devpieter.divine.mixins;

import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.debug.DebugRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.gizmos.Gizmos;
import net.minecraft.gizmos.TextGizmo;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import nl.devpieter.divine.GolemManager;
import nl.devpieter.divine.enums.GolemLocation;
import nl.devpieter.utilize.client.utils.ClientUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;
import java.util.OptionalDouble;


@Mixin(DebugRenderer.class)
public abstract class DebugRendererMixin {

    @Unique
    private GolemManager golemManager;

    // TODO - Get from settings
    @Unique
    private final int textColor = new Color(0x3be477).getRGB();
    @Unique
    private final float minTextScale = 0.02f;
    @Unique
    private final float maxTextScale = 0.5f;

    @Inject(at = @At("TAIL"), method = "emitGizmos")
    private void onRender(Frustum frustum, double cameraX, double cameraY, double cameraZ, float tickProgress, CallbackInfo ci) {
        Player player = ClientUtils.getPlayer();
        if (player == null) return;

        if (golemManager == null) {
            deferredInitialization();
            return;
        }

        GolemLocation golemLocation = golemManager.currentLocation();
        if (golemLocation == GolemLocation.UNDEFINED) return;

        Vec3 eyePos = player.getEyePosition();
        BlockPos pos = golemLocation.headPosMax();
        double distance = eyePos.distanceTo(Vec3.atCenterOf(pos));

        Vec3 vecPos = Vec3.atCenterOf(pos);
        Gizmos.billboardText("Golem", vecPos, new TextGizmo.Style(textColor, getTextScale(distance) * 20, OptionalDouble.empty()));
    }

    @Unique
    private void deferredInitialization() {
        if (golemManager == null) golemManager = GolemManager.getInstance();
    }

    @Unique
    private float getTextScale(double distance) {
        float scale = (float) (distance * 0.005);
        return Math.max(minTextScale, Math.min(scale, maxTextScale));
    }
}
