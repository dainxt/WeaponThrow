package com.dainxt.weaponthrow.packets;

import java.util.UUID;

import com.dainxt.weaponthrow.handlers.EventsHandler;
import com.dainxt.weaponthrow.handlers.PacketHandler;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public class SPacketThrow extends BasePacket {
	
	public SPacketThrow(UUID uuid, int maxChargeTime, boolean isCharging) {
		super(PacketHandler.SPACKET_THROW);
		buf.writeUuid(uuid);
		buf.writeVarInt(maxChargeTime);
		buf.writeBoolean(isCharging);
	}

	public static void register() {
		
		ClientPlayNetworking.registerGlobalReceiver(PacketHandler.SPACKET_THROW, (client, handler, buf, responseSender) -> {
			UUID uuid = buf.readUuid();
			int maxChargeTime = buf.readVarInt();
			boolean isCharging = buf.readBoolean();
			
			client.execute(() -> {
				EventsHandler.onSeverUpdate(uuid, maxChargeTime, isCharging);
			});
		});
		
	}

}
