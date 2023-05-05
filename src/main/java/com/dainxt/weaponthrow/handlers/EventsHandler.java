package com.dainxt.weaponthrow.handlers;

import java.util.UUID;

import com.dainxt.weaponthrow.capabilities.PlayerThrowData;
import com.dainxt.weaponthrow.interfaces.IPlayerEntityMixin;
import com.dainxt.weaponthrow.projectile.WeaponThrowEntity;
import com.dainxt.weaponthrow.events.OnApplySlow;
import com.dainxt.weaponthrow.events.OnFOVUpdate;
import com.dainxt.weaponthrow.events.OnHeldItemRender;
import com.dainxt.weaponthrow.events.OnStartPlayerRender;
import com.dainxt.weaponthrow.events.OnStartPlayerTick;
import com.dainxt.weaponthrow.packets.CPacketThrow;
import com.dainxt.weaponthrow.packets.CPacketThrow.State;
import com.dainxt.weaponthrow.packets.SPacketThrow;
import com.google.common.collect.Multimap;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.HoeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PickaxeItem;
import net.minecraft.item.ShovelItem;
import net.minecraft.item.SwordItem;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Arm;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3f;

public class EventsHandler{
	
	public static boolean wasPressed = false;

	public static void onThrowItem(ServerPlayerEntity serverplayer, CPacketThrow.State action){


		ServerWorld world = serverplayer.getWorld();
		ItemStack stack = ((PlayerEntity) serverplayer).getMainHandStack();
		
		boolean isThrowable = ConfigRegistry.COMMON.get().experimental.shouldThrowItemsToo;
		
		Multimap<EntityAttribute, EntityAttributeModifier> multimap = stack.getAttributeModifiers(EquipmentSlot.MAINHAND);
		boolean haveAttributes = multimap.containsKey(EntityAttributes.GENERIC_ATTACK_DAMAGE) || multimap.containsKey(EntityAttributes.GENERIC_ATTACK_SPEED);
		
		PlayerThrowData data = ((IPlayerEntityMixin) (PlayerEntity) serverplayer).getThrowPower();
		
		if ((isThrowable || haveAttributes) && !stack.isEmpty()) {
			
			boolean cdConfig = ConfigRegistry.COMMON.get().general.notUseWhenCooldown;
			
			if(!(((PlayerEntity) serverplayer).getItemCooldownManager().getCooldownProgress(stack.getItem(), 1.0F) > 0 && cdConfig)) {

				data.setAction(action);
				
				if(action.equals(CPacketThrow.State.START) && data.getChargeTime() <= 0) {
					data.startCharging(stack);
				}
				
				if(action.equals(CPacketThrow.State.FINISH) && data.getChargeTime() >= 0 ) {

					float baseThrow = 0;
					float baseExhaustion = 0.05F;

					float modThrow = 1.0F;
					
					if(Math.signum(PlayerThrowData.getMaximumCharge((PlayerEntity) serverplayer)) != 0.0F) {
						modThrow = 1.F - (data.getChargeTime()/(float)PlayerThrowData.getMaximumCharge((PlayerEntity) serverplayer));
					}
					
					data.resetCharging();
					
					double defaultVelocity = ConfigRegistry.COMMON.get().defaults.velocityDefault;
					
					if (ConfigRegistry.COMMON.get().experimental.shouldThrowItemsToo){
						baseThrow = (float) defaultVelocity;
					}
					
					if(haveAttributes) {
						baseThrow = 20/ ((PlayerEntity) serverplayer).getAttackCooldownProgressPerTick();
						baseExhaustion = ((PlayerEntity) serverplayer).getAttackCooldownProgressPerTick()/20;
					}
					
					if(baseThrow>0) {
		                
						boolean shouldDestroy = modThrow > 0.99;
						double baseDamage = ConfigRegistry.COMMON.get().defaults.baseDamageDefault;
						
						double toolMultiplier = 0.0D;
						

						
						if(haveAttributes) {
							baseDamage = (float) ((PlayerEntity) serverplayer).getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE);
							
							int types = 0;
							if(stack.getItem() instanceof AxeItem) {
								toolMultiplier += ConfigRegistry.COMMON.get().multipliers.tools.axeMultiplier;
								types++;
							}
							if(stack.getItem() instanceof HoeItem) {
								toolMultiplier += ConfigRegistry.COMMON.get().multipliers.tools.hoeMultiplier;
								types++;
							}
							if(stack.getItem() instanceof PickaxeItem) {
								toolMultiplier += ConfigRegistry.COMMON.get().multipliers.tools.pickaxeMultiplier;
								types++;
							}
							if(stack.getItem() instanceof ShovelItem) {
								toolMultiplier += ConfigRegistry.COMMON.get().multipliers.tools.shovelMultiplier;
								types++;
							}
							if(stack.getItem() instanceof SwordItem) {
								toolMultiplier = ConfigRegistry.COMMON.get().multipliers.tools.swordMultiplier;
								types++;
							}

							toolMultiplier/=(types>0? types : 1);
							
						}
						
						if(toolMultiplier == 0.0F) {
							toolMultiplier = 1.0F;
						}
						
						int size = ((PlayerEntity) serverplayer).isSneaking() ? stack.getCount() : 1;
						
						double bDamageMul = ConfigRegistry.COMMON.get().multipliers.damages.baseDamageMultiplier;
						double sDamageMul = ConfigRegistry.COMMON.get().multipliers.damages.stackDamageMultiplier;	
						double mDamageMul = ConfigRegistry.COMMON.get().multipliers.damages.modifiedDamageMultiplier;	
						double totalDamage = (baseDamage)*(1*bDamageMul + modThrow*mDamageMul) + (size*sDamageMul);
						totalDamage*=toolMultiplier;
						
						double bVelocityMul = ConfigRegistry.COMMON.get().multipliers.velocities.baseVelocityMultiplier;
						double sVelocityMul = ConfigRegistry.COMMON.get().multipliers.velocities.stackVelocityMultiplier;	
						double mVelocityMul = ConfigRegistry.COMMON.get().multipliers.velocities.modifiedVelocityMultiplier;	
						double totalVelocity = (baseThrow)*(1*bVelocityMul + modThrow*mVelocityMul) - (size*sVelocityMul);
						totalVelocity*=toolMultiplier;
						
			            double bExhaustionMul = ConfigRegistry.COMMON.get().multipliers.exhaustions.baseExhaustionMultiplier;
			            double sExhaustionMul = ConfigRegistry.COMMON.get().multipliers.exhaustions.stackExhaustionMultiplier;
			            double mExhaustionMul = ConfigRegistry.COMMON.get().multipliers.exhaustions.modifiedExhaustionMultiplier;
			            double totalExhaustion = (baseExhaustion)*(1*bExhaustionMul + modThrow*mExhaustionMul) + (size*sExhaustionMul);
			            totalExhaustion*=toolMultiplier;
			            
						WeaponThrowEntity throwedEntity = new WeaponThrowEntity(world, (PlayerEntity) serverplayer, shouldDestroy, (float) totalDamage, stack.split(size));
						throwedEntity.setVelocity((PlayerEntity) serverplayer, ((PlayerEntity) serverplayer).getPitch(), ((PlayerEntity) serverplayer).getYaw(), 0.0F, (float) totalVelocity, 1.0F);
						((PlayerEntity) serverplayer).addExhaustion((float) totalExhaustion);

			            
			            world.spawnEntity(throwedEntity);
			            
			            SoundEvent soundevent = SoundEvents.ENTITY_EGG_THROW;
			            throwedEntity.playSound(soundevent, 1.0F, 0.5F);

					}
				}
				
				((IPlayerEntityMixin) (PlayerEntity) serverplayer).setThrowPower(data);
			
			}

		}
		
		/*if(stack.isEmpty() || !data.getChargingStack().equals(stack)) {
			data.resetCharging();
		}*/

	}
	

	
	public static void registerEvents(){
		OnStartPlayerTick.EVENT.register((player)->{
			if(!player.world.isClient()) {
				PlayerThrowData cap = ((IPlayerEntityMixin)player).getThrowPower();

				boolean attacked = player.getAttackCooldownProgress(0.0F) < 1.0F;
				boolean cdConfig = ConfigRegistry.COMMON.get().general.notUseWhenCooldown;
				
				boolean changedItem = !ItemStack.areEqual(cap.getChargingStack(), player.getMainHandStack());
				
				if (attacked && cdConfig  || changedItem) {
					cap.resetCharging();
				}
				
				if (cap.getChargeTime() > 0) {
					cap.setChargeTime(cap.getChargeTime() - 1);
				}


				if(cap.getAction().equals(CPacketThrow.State.START) || cap.getAction().equals(CPacketThrow.State.FINISH)) {

					PacketHandler.sendToAll(player, new SPacketThrow(player.getUuid(), PlayerThrowData.getMaximumCharge(player), cap.getAction().equals(CPacketThrow.State.START)));
					
					if(cap.getAction().equals(CPacketThrow.State.FINISH)) {
						cap.setAction(State.NONE);
					}
				}
			}else {
				PlayerThrowData cap = ((IPlayerEntityMixin)player).getThrowPower();
				
				if(cap.getChargeTime() > 0) {
					 cap.setChargeTime(cap.getChargeTime()-1);
				 }
				
				//clientSideCharge = cap.getAction().equals(State.DURING)? clientSideCharge+1 : 0;
			}
		});
	
	}
	
	public static void onSeverUpdate(UUID playerUUID, int maxChargeTime, boolean isCharging) {
		assert MinecraftClient.getInstance().world != null;
		PlayerEntity playerentity = MinecraftClient.getInstance().world.getPlayerByUuid(playerUUID);
		if(playerentity != null) {
			
			PlayerThrowData cap = ((IPlayerEntityMixin)playerentity).getThrowPower();
			cap.MAX_CHARGE = maxChargeTime;
			
			if(isCharging) {
				cap.setChargeTime(maxChargeTime);
			}
			
			cap.setAction(isCharging ? State.DURING : State.NONE);

		}
	}
	
	public static void registerClientEvents() {
		OnHeldItemRender.EVENT.register((renderer, player, tickDelta, pitch, hand, swingProgress, item, equipProgress, matrices, vertexConsumers, light)->{
			
			PlayerThrowData cap = ((IPlayerEntityMixin)player).getThrowPower();
			
			if(cap.getAction().equals(State.DURING)) {

				//float progress = MathHelper.clamp((float)(EventsHandler.clientSideCharge+tickDelta)/cap.MAX_CHARGE, 0.F, 1.0F);
				
				float preProgress = 1.0F;
				
				/*if(Math.signum(cap.MAX_CHARGE) != 0.0F) {
					preProgress = (float)(EventsHandler.clientSideCharge+tickDelta)/cap.MAX_CHARGE;
				}*/
				
				if(Math.signum(cap.MAX_CHARGE) != 0.0F && cap.getChargeTime() > 0) {
					float lerp = MathHelper.lerp(tickDelta, cap.getChargeTime()+1, cap.getChargeTime());
					preProgress = 1.F-(float)(lerp)/cap.MAX_CHARGE;
				}
				
				float progress = MathHelper.clamp(preProgress, 0.F, 1.0F);
				
				matrices.translate(0.0D, 0.0F, (double)((float)progress * 0.50F));
				matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion((float)progress * 10.0F));
				matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion((float)progress * 40.0F));
				
			}
		});
		
		OnStartPlayerRender.EVENT.register((renderer, player)->{
			
			PlayerThrowData cap = ((IPlayerEntityMixin)player).getThrowPower();
			if(cap.getAction().equals(State.DURING)) {
					if(player instanceof AbstractClientPlayerEntity) {
						Arm hand = ((AbstractClientPlayerEntity)player).getMainArm();
						if(hand == Arm.RIGHT)
							renderer.getModel().rightArmPose = BipedEntityModel.ArmPose.THROW_SPEAR;
						else
							renderer.getModel().leftArmPose = BipedEntityModel.ArmPose.THROW_SPEAR;
					}
			}
		});
		
		OnApplySlow.EVENT.register((player)->{
			PlayerThrowData cap = ((IPlayerEntityMixin)player).getThrowPower();
			return cap.getAction().equals(State.DURING);
		});
		
		OnFOVUpdate.EVENT.register((player, amount)->{
			
			PlayerThrowData cap = ((IPlayerEntityMixin)player).getThrowPower();
			
			int maxChargeTime = cap.MAX_CHARGE;
			
			int chargeTime = cap.getChargeTime();

			boolean isCharging = cap.getAction().equals(State.DURING);
			float f = amount;
			
			 if(isCharging) {

		         float f1 = 1.0F;

		         if(Math.signum(maxChargeTime) != 0.0F && chargeTime > 0) {
					 float lerp = MathHelper.lerp(MinecraftClient.getInstance().getTickDelta(), chargeTime+1, chargeTime);
					 f1 = MathHelper.clamp(1.0F-(float)lerp / maxChargeTime, 0.F, 1.0F);
				 }
		         
		         if (f1 > 1.0F) {
		            f1 = 1.0F;
		         } else {
		            f1 = f1 * f1;
		         }
		         
		         f *= 1.0F + f1 * 0.15F;
	
			 }
			 
			return f;
		});
		
		ClientTickEvents.END_WORLD_TICK.register(client -> {
			
			boolean pressed = KeyBindingHandler.KEYBINDING.isPressed();

			if (pressed) {
				PacketHandler.sendToServer(new CPacketThrow(EventsHandler.wasPressed ? CPacketThrow.State.DURING: CPacketThrow.State.START));
				EventsHandler.wasPressed = true;
			}else if(EventsHandler.wasPressed){
				PacketHandler.sendToServer(new CPacketThrow(CPacketThrow.State.FINISH));
				EventsHandler.wasPressed = false;
			}
	        
		});
		
	}
	

}



