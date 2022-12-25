package com.dainxt.weaponthrow.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.player.PlayerEntity;

public interface OnStartPlayerTick {
	 
    Event<OnStartPlayerTick> EVENT = EventFactory.createArrayBacked(OnStartPlayerTick.class,
        (listeners) -> (player) -> {
            for (OnStartPlayerTick listener : listeners) {
                listener.interact(player);
            }

    });
 
    void interact(PlayerEntity entity);
}
