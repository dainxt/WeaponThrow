package com.dainxt.weaponthrow.handlers;

import com.dainxt.weaponthrow.packets.CPacketThrow;
import com.dainxt.weaponthrow.packets.SPacketThrow;
import com.dainxt.weaponthrow.util.Reference;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class PacketHandler {
	private static final String PROTOCOL_VERSION = "1";
	public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
	    new ResourceLocation(Reference.MODID, "main"),
	    () -> PROTOCOL_VERSION,
	    PROTOCOL_VERSION::equals,
	    PROTOCOL_VERSION::equals
	);
	
	public static void init() {
		int id = 0;
		INSTANCE.registerMessage(id++, CPacketThrow.class, CPacketThrow::encode, CPacketThrow::decode, CPacketThrow::handle);
		INSTANCE.registerMessage(id++, SPacketThrow.class, SPacketThrow::encode, SPacketThrow::decode, SPacketThrow::handle);
	}


	public static void sendTo(ServerPlayerEntity playerMP, Object toSend) {
		INSTANCE.sendTo(toSend, playerMP.connection.getNetworkManager(), NetworkDirection.PLAY_TO_CLIENT);
	}
	
	public static void sendToAll(Object toSend) {
		INSTANCE.send(PacketDistributor.ALL.noArg(), toSend);
	}

	public static void sendNonLocal(ServerPlayerEntity playerMP, Object toSend) {
		if (playerMP.server.isDedicatedServer() || !playerMP.getGameProfile().getName().equals(playerMP.server.getServerOwner())) {
			sendTo(playerMP, toSend);
		}
	}

	public static void sendToServer(Object msg) {
		INSTANCE.sendToServer(msg);
	}

	private PacketHandler() {}

}
