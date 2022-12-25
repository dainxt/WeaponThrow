package com.dainxt.weaponthrow.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.entity.player.PlayerEntity;

public interface OnStartPlayerRender {
	 
    Event<OnStartPlayerRender> EVENT = EventFactory.createArrayBacked(OnStartPlayerRender.class,
        (listeners) -> (renderer, player) -> {
            for (OnStartPlayerRender listener : listeners) {
                listener.interact(renderer, player);
            }

    });
 
    void interact(PlayerEntityRenderer renderer, PlayerEntity entity);
}
