package com.dainxt.weaponthrow.mixin;

import java.util.Arrays;
import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.dainxt.weaponthrow.events.OnApplySlow;
import com.dainxt.weaponthrow.handlers.KeyBindingHandler;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.entity.player.PlayerEntity;

@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin {
	@Inject(at = @At("RETURN"), method = "shouldSlowDown", cancellable = true)
	private void init(CallbackInfoReturnable<Boolean> info) {
		info.setReturnValue(OnApplySlow.EVENT.invoker().interact((PlayerEntity)(Object)this) || info.getReturnValue());
	}
}
