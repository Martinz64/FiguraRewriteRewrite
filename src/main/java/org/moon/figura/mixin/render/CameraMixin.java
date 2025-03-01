package org.moon.figura.mixin.render;

import net.minecraft.client.Camera;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import org.moon.figura.avatars.Avatar;
import org.moon.figura.avatars.AvatarManager;
import org.moon.figura.math.vector.FiguraVec3;
import org.moon.figura.trust.TrustContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(Camera.class)
public abstract class CameraMixin {

    @Shadow private float xRot;
    @Shadow private float yRot;

    @Unique private Avatar avatar;

    @Shadow protected abstract void setRotation(float yaw, float pitch);
    @Shadow protected abstract void move(double x, double y, double z);

    @Inject(method = "setup", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Camera;setPosition(DDD)V", shift = At.Shift.BEFORE))
    private void setupRot(BlockGetter area, Entity focusedEntity, boolean thirdPerson, boolean inverseView, float tickDelta, CallbackInfo ci) {
        avatar = AvatarManager.getAvatar(focusedEntity);
        if (avatar == null || avatar.luaRuntime == null || avatar.trust.get(TrustContainer.Trust.VANILLA_MODEL_EDIT) == 0) {
            avatar = null;
            return;
        }

        float x = xRot;
        float y = yRot;

        FiguraVec3 rot = avatar.luaRuntime.renderer.cameraRot;
        if (rot != null) {
            x = (float) rot.x;
            y = (float) rot.y;
        }

        FiguraVec3 bonus = avatar.luaRuntime.renderer.cameraBonusRot;
        if (bonus != null) {
            x += (float) bonus.x;
            y += (float) bonus.y;
        }

        setRotation(y, x);
    }

    @ModifyArgs(method = "setup", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Camera;setPosition(DDD)V"))
    private void setupPivot(Args args) {
        if (avatar != null) {
            double x = args.get(0);
            double y = args.get(1);
            double z = args.get(2);

            FiguraVec3 piv = avatar.luaRuntime.renderer.cameraPivot;
            if (piv != null) {
               x = piv.x;
               y = piv.y;
               z = piv.z;
            }

            FiguraVec3 bonus = avatar.luaRuntime.renderer.cameraBonusPivot;
            if (bonus != null) {
                x += bonus.x;
                y += bonus.y;
                z += bonus.z;
            }

            args.set(0, x);
            args.set(1, y);
            args.set(2, z);
        }
    }

    @Inject(method = "setup", at = @At(value = "RETURN"))
    private void setupPos(BlockGetter area, Entity focusedEntity, boolean thirdPerson, boolean inverseView, float tickDelta, CallbackInfo ci) {
        if (avatar != null) {
            FiguraVec3 pos = avatar.luaRuntime.renderer.cameraPos;
            if (pos != null)
                move(-pos.z, pos.y, -pos.x);

            avatar = null;
        }
    }
}
