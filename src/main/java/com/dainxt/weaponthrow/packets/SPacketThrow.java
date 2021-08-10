package com.dainxt.weaponthrow.packets;

import java.util.UUID;
import java.util.function.Supplier;

import com.dainxt.weaponthrow.handlers.EventsHandler;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class SPacketThrow {
	private final int progress;
	private UUID playerAnimated;
	
	public SPacketThrow(UUID player, int maxTime) {
		this.progress = maxTime;
		this.playerAnimated = player;
	}
	
	public static void encode(SPacketThrow msg, PacketBuffer buf) {
		buf.writeString(msg.playerAnimated.toString());
		buf.writeInt(msg.progress);
	}

	public static SPacketThrow decode(PacketBuffer buf) {
		return new SPacketThrow(UUID.fromString(buf.readString()), buf.readInt());
	}

	public static void handle(SPacketThrow msg, Supplier<NetworkEvent.Context> ctx) {
		if (ctx.get().getDirection().getReceptionSide().isClient()) {
			ctx.get().enqueueWork(() -> EventsHandler.onSeverUpdate(msg.playerAnimated, msg.progress));
		}
		ctx.get().setPacketHandled(true);
	}
}
