package com.dainxt.weaponthrow.packets;

import com.dainxt.weaponthrow.handlers.EventsHandler;
import com.dainxt.weaponthrow.handlers.PacketHandler;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

public class CPacketThrow extends BasePacket {
	
	public enum State {
		   NONE((byte)0),
		   START((byte)1),
		   DURING((byte)2),
		   FINISH((byte)3);
		
		   private byte index;
		   private State(byte i) {
			   this.index = i;

		  }
		  public byte toByte() {
				return index;
		  }

		  public static State fromByte(int index) {
			  for(State equipmentslottype : State.values()) {
				  if(equipmentslottype.toByte() == index) {
					  return equipmentslottype;
				  }
			  }
			  return NONE;
			  
		  }
	}
	
	public CPacketThrow(State state) {
		super(PacketHandler.CPACKET_THROW);
		buf.writeByte(state.toByte());
	}

	public static void register() {
		
		ServerPlayNetworking.registerGlobalReceiver(PacketHandler.CPACKET_THROW, (server, player, handler, buf, responseSender) -> {
			CPacketThrow.State action = CPacketThrow.State.fromByte(buf.readByte());
			
			server.execute(() -> {
				
				EventsHandler.onThrowItem(player, action);
			});
		});
		
	}

}
