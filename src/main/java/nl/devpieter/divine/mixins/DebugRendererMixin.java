package nl.devpieter.divine.mixins;

import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.debug.DebugRenderer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import nl.devpieter.divine.GolemManager;
import nl.devpieter.divine.enums.GolemLocation;
import nl.devpieter.utilize.utils.minecraft.ClientUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;

//#if MC>=12111
import net.minecraft.world.debug.gizmo.GizmoDrawing;
//#else
//$$ import net.minecraft.client.render.VertexConsumerProvider;
//$$ import net.minecraft.client.util.math.MatrixStack;
//#endif

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

    @Inject(at = @At("TAIL"), method = "render")
    //#if MC>=12111
    private void onRender(Frustum frustum, double cameraX, double cameraY, double cameraZ, float tickProgress, CallbackInfo ci) {
    //#else
    //$$ private void onRender(MatrixStack matrices, Frustum frustum, VertexConsumerProvider.Immediate vertexConsumers, double cameraX, double cameraY, double cameraZ, boolean lateDebug, CallbackInfo ci) {
    //#endif
        PlayerEntity player = ClientUtils.getPlayer();
        if (player == null) return;

        if (golemManager == null) {
            deferredInitialization();
            return;
        }

        GolemLocation golemLocation = golemManager.currentLocation();
        if (golemLocation == GolemLocation.UNDEFINED) return;

        Vec3d eyePos = player.getEyePos();
        BlockPos pos = golemLocation.headPosMax();
        double distance = eyePos.distanceTo(Vec3d.ofCenter(pos));

        //#if MC>=12111
        GizmoDrawing.blockLabel("Golem", pos, 0, textColor, getTextScale(distance) * 20);
        //#else
        //$$ DebugRenderer.drawFloatingText(matrices, vertexConsumers, "Golem", pos, 0, textColor, getTextScale(distance));
        //#endif
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
