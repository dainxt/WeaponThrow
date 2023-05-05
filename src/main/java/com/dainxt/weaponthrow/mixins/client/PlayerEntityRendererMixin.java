package com.dainxt.weaponthrow.mixins.client;

import com.dainxt.weaponthrow.events.OnStartPlayerRender;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.PlayerEntityRenderer;

@Mixin(PlayerEntityRenderer.class)
public class PlayerEntityRendererMixin{
	
	@Inject(method = "setModelPose", at = @At("TAIL"))
	private void setModelPose(AbstractClientPlayerEntity player, CallbackInfo info) {
		OnStartPlayerRender.EVENT.invoker().interact(((PlayerEntityRenderer)(Object)this), player);
	}
}
