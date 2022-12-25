package com.dainxt.weaponthrow.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;

public interface OnHeldItemRender {
	 
    Event<OnHeldItemRender> EVENT = EventFactory.createArrayBacked(OnHeldItemRender.class,
        (listeners) -> (renderer, player, tickDelta, pitch, hand, swingProgress, item, equipProgress, matrices, vertexConsumers, light) -> {
            for (OnHeldItemRender listener : listeners) {
                listener.interact(renderer, player, tickDelta, pitch, hand, swingProgress, item, equipProgress, matrices, vertexConsumers, light);
            }

    });
 
    void interact(HeldItemRenderer renderer , AbstractClientPlayerEntity player, float tickDelta, float pitch, Hand hand, float swingProgress, ItemStack item, float equipProgress, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light);
}
