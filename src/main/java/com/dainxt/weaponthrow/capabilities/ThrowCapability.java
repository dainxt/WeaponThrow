package com.dainxt.weaponthrow.capabilities;

import com.dainxt.weaponthrow.interfaces.IThrowPower;

import net.minecraft.nbt.INBT;
import net.minecraft.nbt.IntNBT;
import net.minecraft.nbt.NumberNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

public class ThrowCapability<T> implements Capability.IStorage<IThrowPower>{

	@Override
	public INBT writeNBT(Capability<IThrowPower> capability, IThrowPower instance, Direction side) {
		return IntNBT.valueOf(instance.getChargeTime());
	}

	@Override
	public void readNBT(Capability<IThrowPower> capability, IThrowPower instance, Direction side, INBT nbt) {
		instance.setChargeTime(((NumberNBT)nbt).getInt());
	}

}
