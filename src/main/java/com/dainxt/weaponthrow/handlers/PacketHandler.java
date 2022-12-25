package com.dainxt.weaponthrow.handlers;

import com.dainxt.weaponthrow.WeaponThrow;
import com.dainxt.weaponthrow.packets.BasePacket;
import com.dainxt.weaponthrow.packets.CPacketThrow;
import com.dainxt.weaponthrow.packets.EntitySpawnPacket;
import com.dainxt.weaponthrow.packets.SPacketThrow;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;

public class PacketHandler {
	
	public static final Identifier SPAWN_PACKET = new Identifier(WeaponThrow.MODID, "spawn_packet");
	
	public static final Identifier CPACKET_THROW = new Identifier(WeaponThrow.MODID, "cpacket_throw");
	
	public static final Identifier SPACKET_THROW = new Identifier(WeaponThrow.MODID, "spacket_throw");
	
	public static void registerClientListeners() {
		EntitySpawnPacket.register();
		SPacketThrow.register();
	}
	
	public static void registerServerListeners() {
		CPacketThrow.register();
	}
	
	public static void sendToServer(BasePacket packet) {
		ClientPlayNetworking.send(packet.getIdentifier(), packet.getBuf());
	}
	
	public static void sendToAll(Entity entity, BasePacket packet) {
		for (ServerPlayerEntity player : PlayerLookup.tracking((ServerWorld) entity.world, entity.getBlockPos())) {
            ServerPlayNetworking.send(player, packet.getIdentifier(), packet.getBuf());
        }
	}
	
}
