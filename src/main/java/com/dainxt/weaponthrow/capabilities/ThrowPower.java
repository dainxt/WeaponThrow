package com.dainxt.weaponthrow.capabilities;

import com.dainxt.weaponthrow.interfaces.IThrowPower;

public class ThrowPower implements IThrowPower
{
	int prevChargeTime = -1;
	int chargeTime = -1;
	
	@Override
	public int getChargeTime() {
		return chargeTime;
	}

	@Override
	public void setChargeTime(int ticks) {
		chargeTime = ticks;
	}


	@Override
	public boolean doesMaxChargeChanged() {
		return prevChargeTime != chargeTime;
	}

	@Override
	public void tick() {
		prevChargeTime = chargeTime;
		
	}
	
	

}
