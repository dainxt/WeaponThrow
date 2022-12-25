package com.dainxt.weaponthrow.mixin;

import com.dainxt.weaponthrow.capabilities.PlayerThrowData;

public interface IPlayerEntityMixin {
	public void setThrowPower(PlayerThrowData value);
	
	public PlayerThrowData getThrowPower();
}
