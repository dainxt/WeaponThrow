package com.dainxt.weaponthrow.packets;

import java.util.function.Supplier;

import com.dainxt.weaponthrow.handlers.EventsHandler;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class CPacketThrow {
	private final int action;
	
	public CPacketThrow(int action) {
		this.action = action;
	}
	
	public static void encode(CPacketThrow msg, PacketBuffer buf) {
		buf.writeInt(msg.action);
	}

	public static CPacketThrow decode(PacketBuffer buf) {
		return new CPacketThrow(buf.readInt());
	}

	public static void handle(CPacketThrow msg, Supplier<NetworkEvent.Context> ctx) {
		if (ctx.get().getDirection().getReceptionSide().isServer()) {
			ctx.get().enqueueWork(() -> EventsHandler.onThrowItem(ctx.get().getSender(), msg.action));
		}
		ctx.get().setPacketHandled(true);
	}
}
