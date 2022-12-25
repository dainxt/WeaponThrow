package com.dainxt.weaponthrow.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.player.PlayerEntity;

public interface OnFOVUpdate {
	 
    Event<OnFOVUpdate> EVENT = EventFactory.createArrayBacked(OnFOVUpdate.class,
        (listeners) -> (player, amount) -> {
            for (OnFOVUpdate listener : listeners) {
                float result = listener.interact(player, amount);
 
                if(result != 0) {
                    return result;
                }
                
            }
            return 0;
    });
 
    float interact(PlayerEntity entity, float fov);
}
