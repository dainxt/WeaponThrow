package com.dainxt.weaponthrow.mixins;

import com.dainxt.weaponthrow.capabilities.PlayerThrowData;

public interface IPlayerEntityMixin {
	public void setThrowPower(PlayerThrowData value);
	
	public PlayerThrowData getThrowPower();
}
