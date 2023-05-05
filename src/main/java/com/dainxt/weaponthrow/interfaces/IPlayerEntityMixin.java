package com.dainxt.weaponthrow.interfaces;

import com.dainxt.weaponthrow.capabilities.PlayerThrowData;

public interface IPlayerEntityMixin {
	void setThrowPower(PlayerThrowData value);
	PlayerThrowData getThrowPower();
}
