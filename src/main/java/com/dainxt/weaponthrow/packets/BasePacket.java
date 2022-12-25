package com.dainxt.weaponthrow.packets;

import io.netty.buffer.Unpooled;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public abstract class BasePacket {
	
	private Identifier identifier;
	
	protected PacketByteBuf buf;
	
	public BasePacket(Identifier id) {
		this.identifier = id;
		this.buf = new PacketByteBuf(Unpooled.buffer());
	}
	
	public PacketByteBuf getBuf() {
		return this.buf;
	}
	
	public Identifier getIdentifier() {
		return this.identifier;
	}
}
