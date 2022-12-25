package com.dainxt.weaponthrow;

import com.dainxt.weaponthrow.handlers.EventsHandler;
import com.dainxt.weaponthrow.handlers.KeyBindingHandler;
import com.dainxt.weaponthrow.handlers.PacketHandler;
import com.dainxt.weaponthrow.handlers.RenderRegistry;

import net.fabricmc.api.ClientModInitializer;

public class ClientMod implements ClientModInitializer{

	@Override
	public void onInitializeClient() {

		EventsHandler.registerClientEvents();

		RenderRegistry.registerRenderers();

		PacketHandler.registerClientListeners();

		KeyBindingHandler.registerKeyBindings();
	}

}
