package com.dainxt.weaponthrow.handlers;

import com.dainxt.weaponthrow.entity.render.WeaponThrowRenderer;

import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

public class RenderRegistry {

	public static void registerRenderers() {
		EntityRendererRegistry.register(EntityRegistry.WEAPONTHROW, WeaponThrowRenderer::new);
		
	}
}
