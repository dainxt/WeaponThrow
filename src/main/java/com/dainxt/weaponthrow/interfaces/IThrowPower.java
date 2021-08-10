package com.dainxt.weaponthrow.interfaces;

public interface IThrowPower {
	public int getChargeTime();
	public void setChargeTime(int ticks);
	public boolean doesMaxChargeChanged();
	public void tick();
}
