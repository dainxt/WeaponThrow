package com.dainxt.weaponthrow.mixins.client;

import com.dainxt.weaponthrow.events.OnApplySlow;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;

@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin {
	@Inject(at = @At("RETURN"), method = "shouldSlowDown", cancellable = true)
	private void init(CallbackInfoReturnable<Boolean> info) {
		info.setReturnValue(OnApplySlow.EVENT.invoker().interact((PlayerEntity)(Object)this) || info.getReturnValue());
	}
}
