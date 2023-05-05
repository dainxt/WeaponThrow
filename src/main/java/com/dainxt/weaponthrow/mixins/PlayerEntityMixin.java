package com.dainxt.weaponthrow.mixins;

import com.dainxt.weaponthrow.capabilities.PlayerThrowData;
import com.dainxt.weaponthrow.events.OnStartPlayerTick;
import com.dainxt.weaponthrow.interfaces.IPlayerEntityMixin;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.player.PlayerEntity;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin implements IPlayerEntityMixin {
	
	
	public PlayerThrowData throwPower = new PlayerThrowData((PlayerEntity)(Object) this);
	
	@Override
    public void setThrowPower(PlayerThrowData value) {
    	this.throwPower = value;
    }
	
	@Override
    public PlayerThrowData getThrowPower() {
    	return this.throwPower;
    }
	
	@Inject(at = @At("HEAD"), method = "tick")
	private void init(CallbackInfo info) {
		OnStartPlayerTick.EVENT.invoker().interact((PlayerEntity)(Object)this);
	}
}
