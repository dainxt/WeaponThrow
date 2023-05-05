package com.dainxt.weaponthrow;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.dainxt.weaponthrow.handlers.ConfigRegistry;
import com.dainxt.weaponthrow.handlers.EnchantmentHandler;
import com.dainxt.weaponthrow.handlers.EntityRegistry;
import com.dainxt.weaponthrow.handlers.EventsHandler;
import com.dainxt.weaponthrow.handlers.PacketHandler;

import net.fabricmc.api.ModInitializer;

public class WeaponThrow implements ModInitializer {
	
	public static final Logger LOGGER = LogManager.getLogger();
	
	public static final String MODID = "weaponthrow";
		
	@Override
	public void onInitialize() {
		
		ConfigRegistry.registerConfig();

		EntityRegistry.registerEntities();
		
		EventsHandler.registerEvents();
		
		PacketHandler.registerServerListeners();
		
		EnchantmentHandler.registerEnchantments();
		
	}
}
