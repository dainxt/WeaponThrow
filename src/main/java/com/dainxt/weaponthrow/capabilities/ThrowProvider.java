package com.dainxt.weaponthrow.capabilities;

import com.dainxt.weaponthrow.interfaces.IThrowPower;

import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

public class ThrowProvider implements ICapabilitySerializable<INBT>{
	 @CapabilityInject(IThrowPower.class)
	 public static final Capability<IThrowPower> THROW_POWER = null;

	 private LazyOptional<IThrowPower> instance = LazyOptional.of(THROW_POWER::getDefaultInstance);


	 @Override
	 public <T> LazyOptional<T> getCapability(Capability<T> capability, Direction facing)
	 {
		 return THROW_POWER.orEmpty(capability, instance);
	 }

	 @Override
	 public INBT serializeNBT()
	 {
		 return THROW_POWER.getStorage().writeNBT(THROW_POWER, instance.orElseThrow(() -> new IllegalArgumentException("LazyOptional cannot be empty!")), null);
	 }

	 @Override
	 public void deserializeNBT(INBT nbt)
	 {
		 THROW_POWER.getStorage().readNBT(THROW_POWER, instance.orElseThrow(() -> new IllegalArgumentException("LazyOptional cannot be empty!")), null, nbt);
	 }

}
