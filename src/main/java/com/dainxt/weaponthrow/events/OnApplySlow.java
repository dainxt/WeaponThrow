package com.dainxt.weaponthrow.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.player.PlayerEntity;

public interface OnApplySlow {
	 
    Event<OnApplySlow> EVENT = EventFactory.createArrayBacked(OnApplySlow.class,
        (listeners) -> (player) -> {
        	boolean result = false;
            for (OnApplySlow listener : listeners) {
                result = listener.interact(player) || result;
            }
            return result;
    });
 
    boolean interact(PlayerEntity player);
}
