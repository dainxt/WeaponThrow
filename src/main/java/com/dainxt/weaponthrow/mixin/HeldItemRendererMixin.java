package com.dainxt.weaponthrow.mixin;

import java.util.Arrays;
import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import com.dainxt.weaponthrow.capabilities.PlayerThrowData;
import com.dainxt.weaponthrow.events.OnFOVUpdate;
import com.dainxt.weaponthrow.events.OnHeldItemRender;
import com.dainxt.weaponthrow.events.OnStartPlayerRender;
import com.dainxt.weaponthrow.events.OnStartPlayerTick;
import com.dainxt.weaponthrow.handlers.KeyBindingHandler;
import com.dainxt.weaponthrow.interfaces.IPlayerEntityMixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;

@Mixin(HeldItemRenderer.class)
public class HeldItemRendererMixin{
	
	@Inject(method = "renderFirstPersonItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/AbstractClientPlayerEntity;isUsingRiptide()Z"))
	private void renderCustom(AbstractClientPlayerEntity player, float tickDelta, float pitch, Hand hand, float swingProgress, ItemStack item, float equipProgress, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo info) {
		OnHeldItemRender.EVENT.invoker().interact(((HeldItemRenderer)(Object)this), player, tickDelta, pitch, hand, swingProgress, item, equipProgress, matrices, vertexConsumers, light);
	}
}
