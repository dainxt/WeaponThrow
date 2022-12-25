package com.dainxt.weaponthrow.handlers;

import com.dainxt.weaponthrow.config.WeaponThrowConfig;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;

public class ConfigRegistry {
	
	public static ConfigHolder<WeaponThrowConfig> COMMON;
	
	public static void registerConfig() {
		
		COMMON = AutoConfig.register(WeaponThrowConfig.class, GsonConfigSerializer::new);
	}
	
}
