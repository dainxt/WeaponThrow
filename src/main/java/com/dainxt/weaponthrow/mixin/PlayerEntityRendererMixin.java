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
import com.dainxt.weaponthrow.events.OnStartPlayerRender;
import com.dainxt.weaponthrow.events.OnStartPlayerTick;
import com.dainxt.weaponthrow.handlers.KeyBindingHandler;
import com.dainxt.weaponthrow.interfaces.IPlayerEntityMixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.entity.player.PlayerEntity;

@Mixin(PlayerEntityRenderer.class)
public class PlayerEntityRendererMixin{
	
	@Inject(method = "setModelPose", at = @At("TAIL"))
	private void setModelPose(AbstractClientPlayerEntity player, CallbackInfo info) {
		OnStartPlayerRender.EVENT.invoker().interact(((PlayerEntityRenderer)(Object)this), player);
	}
}
