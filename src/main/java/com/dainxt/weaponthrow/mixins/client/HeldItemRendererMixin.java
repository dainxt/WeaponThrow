package com.dainxt.weaponthrow.mixins.client;

import com.dainxt.weaponthrow.events.OnHeldItemRender;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;

@Mixin(HeldItemRenderer.class)
public class HeldItemRendererMixin{
	
	@Inject(method = "renderFirstPersonItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/AbstractClientPlayerEntity;isUsingRiptide()Z"))
	private void renderCustom(AbstractClientPlayerEntity player, float tickDelta, float pitch, Hand hand, float swingProgress, ItemStack item, float equipProgress, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo info) {
		OnHeldItemRender.EVENT.invoker().interact(((HeldItemRenderer)(Object)this), player, tickDelta, pitch, hand, swingProgress, item, equipProgress, matrices, vertexConsumers, light);
	}
}
