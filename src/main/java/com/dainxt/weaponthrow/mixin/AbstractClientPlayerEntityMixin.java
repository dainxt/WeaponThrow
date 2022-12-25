package com.dainxt.weaponthrow.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.dainxt.weaponthrow.events.OnFOVUpdate;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;

@Mixin(AbstractClientPlayerEntity.class)
public abstract class AbstractClientPlayerEntityMixin{
	
	@Inject(method = "getFovMultiplier", at = @At("RETURN"), cancellable = true)
	private void getSpeed(CallbackInfoReturnable<Float> info) {
		PlayerEntity player = (PlayerEntity) (Object) this;

		float amount = info.getReturnValue().floatValue();
		
		float result = OnFOVUpdate.EVENT.invoker().interact(player, amount);

		info.setReturnValue(result);
		
		
	}
}
