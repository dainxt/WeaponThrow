package com.dainxt.weaponthrow.handlers;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

import com.dainxt.weaponthrow.WeaponThrow;
import com.dainxt.weaponthrow.capabilities.ThrowProvider;
import com.dainxt.weaponthrow.config.WeaponThrowConfig;
import com.dainxt.weaponthrow.events.WeaponThrowEvent;
import com.dainxt.weaponthrow.events.WeaponThrowEvent.TestThrow;
import com.dainxt.weaponthrow.interfaces.IThrowPower;
import com.dainxt.weaponthrow.packets.CPacketThrow;
import com.dainxt.weaponthrow.packets.SPacketThrow;
import com.dainxt.weaponthrow.projectile.WeaponThrowEntity;
import com.dainxt.weaponthrow.util.Reference;
import com.google.common.collect.Multimap;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;

import harmonised.pmmo.config.JType;
import harmonised.pmmo.skills.Skill;
import harmonised.pmmo.util.XP;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.gui.IngameGui;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.HandSide;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.GameType;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.FOVUpdateEvent;
import net.minecraftforge.client.event.InputEvent.KeyInputEvent;
import net.minecraftforge.client.event.InputUpdateEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;

public class EventsHandler {
	
	public static final ResourceLocation THROWPOWER = new ResourceLocation(Reference.MODID, "throw_power");
	
	public static final int ITEMPHYSICSBLOCKING = -32;
	
	//ClientSide
	public boolean wasPressed = false;
	
	public float clientSideCharge;
	
	public static void onThrowItem(ServerPlayerEntity playerentity, int action) {
		
		World worldIn = playerentity.world;
		ItemStack stack = playerentity.getHeldItemMainhand();
		
		boolean isThrowable = WeaponThrowConfig.COMMON.shouldThrowItemsToo.get();
		for(Item item: WeaponThrowConfig.COMMON.whiteList.get()) {
			if(stack.getItem().equals(item)){
				isThrowable = true;
			}
		}

		if(ModList.get().isLoaded("pmmo") && WeaponThrowConfig.COMMON.enablePMMOIntegration.get()) {
			
			ResourceLocation resLoc = playerentity.getHeldItemMainhand().getItem().getRegistryName();
            Map<String, Double> weaponReq = XP.getJsonMap( resLoc, JType.REQ_WEAPON );
            double reqLevel = 0;
            
            if(weaponReq.containsKey(Skill.COMBAT.name.toLowerCase())) {
            	reqLevel = weaponReq.get(Skill.COMBAT.name.toLowerCase());
            }
            
			int combatLevel = Skill.getLevel(Skill.COMBAT.name.toLowerCase(), playerentity.getUniqueID());
			
			if((reqLevel > combatLevel) && reqLevel > 0) {
				
				playerentity.sendStatusMessage(new TranslationTextComponent("weaponthrow.pmmo.requirementThrowing", reqLevel), true);
				return;
			}
			
		}
		

		WeaponThrowEvent.TestThrow testEvent = new WeaponThrowEvent.TestThrow(stack, playerentity);
		boolean isCancelled = MinecraftForge.EVENT_BUS.post(testEvent);
		
		Multimap<Attribute, AttributeModifier> multimap = stack.getAttributeModifiers(EquipmentSlotType.MAINHAND);
		boolean haveAttributes = multimap.containsKey(Attributes.field_233823_f_) || multimap.containsKey(Attributes.field_233825_h_);
		
		if((isThrowable || haveAttributes) && !isCancelled && !stack.isEmpty()) {
			
			boolean cdConfig = WeaponThrowConfig.COMMON.notUseWhenCooldown.get();
			if(!(playerentity.getCooldownTracker().getCooldown(stack.getItem(), 1.0F) > 0 && cdConfig)) {

				IThrowPower provider = playerentity.getCapability(ThrowProvider.THROW_POWER).orElse(ThrowProvider.THROW_POWER.getDefaultInstance());
	
				if(action == 1 && provider.getChargeTime() <= 0 ) {
					provider.setChargeTime(EventsHandler.getMaximumCharge(playerentity));
	
				}
					
				if(action == 0 && provider.getChargeTime() >= 0) {
					
					float baseThrow = 0;
					float baseExhaustion = 0.05F;
					
					float modThrow = 1.F - (provider.getChargeTime()/(float)EventsHandler.getMaximumCharge(playerentity));
					
					provider.setChargeTime(ITEMPHYSICSBLOCKING);
					
					double defaultVelocity = WeaponThrowConfig.COMMON.baseVelocityDefault.get();

					if (WeaponThrowConfig.COMMON.shouldThrowItemsToo.get()){
						baseThrow = (float) defaultVelocity;
					}
					else if (!WeaponThrowConfig.COMMON.whiteList.get().isEmpty()) {
						for(Item item: WeaponThrowConfig.COMMON.whiteList.get()) {
							if(stack.getItem().equals(item)){
								baseThrow = (float) defaultVelocity;
							}
						}
						
					}
					
					if(haveAttributes) {
						baseThrow = 20/playerentity.getCooldownPeriod();
						baseExhaustion = playerentity.getCooldownPeriod()/20;
					}
	
					if(baseThrow>0) {
		                
						boolean shouldDestroy = modThrow > 0.99;
						float baseDamage = 0.01F;
						
						double toolMultiplier = 0.0D;
						
						double defaultDamage = WeaponThrowConfig.COMMON.baseDamageDefault.get();
						
						if (!WeaponThrowConfig.COMMON.whiteList.get().isEmpty()) {
							for(Item item: WeaponThrowConfig.COMMON.whiteList.get()) {
								if(stack.getItem().equals(item)){
									baseDamage = (float) defaultDamage;
									baseExhaustion = 1/baseThrow;
								}
							}
						}
						
						if(haveAttributes) {
							baseDamage = (float) playerentity.func_233637_b_(Attributes.field_233823_f_);
							
							Set<ToolType> types = stack.getToolTypes();
							
							if(types.contains(ToolType.AXE)) {
								toolMultiplier += WeaponThrowConfig.COMMON.axeMultiplier.get();
							}
							if(types.contains(ToolType.HOE)) {
								toolMultiplier += WeaponThrowConfig.COMMON.hoeMultiplier.get();
							}
							if(types.contains(ToolType.PICKAXE)) {
								toolMultiplier += WeaponThrowConfig.COMMON.pickaxeMultiplier.get();
							}
							if(types.contains(ToolType.SHOVEL)) {
								toolMultiplier += WeaponThrowConfig.COMMON.shovelMultiplier.get();
							}
							if(types.isEmpty()) {
								toolMultiplier = WeaponThrowConfig.COMMON.swordMultiplier.get();
							}

							toolMultiplier/=(types.size()>0? types.size() : 1);
							
						}
						
						if(toolMultiplier == 0.0F) {
							toolMultiplier = 1.0F;
						}
						
						double bDamageMul = WeaponThrowConfig.COMMON.baseDamageMultiplier.get();
						double sDamageMul = WeaponThrowConfig.COMMON.stackDamageMultiplier.get();	
						double mDamageMul = WeaponThrowConfig.COMMON.modifiedDamageMultiplier.get();	
						double totalDamage = (baseDamage)*(1*bDamageMul + modThrow*mDamageMul) + (stack.getCount()*sDamageMul);
						totalDamage*=toolMultiplier;
						
						double bVelocityMul = WeaponThrowConfig.COMMON.baseVelocityMultiplier.get();
						double sVelocityMul = WeaponThrowConfig.COMMON.stackVelocityMultiplier.get();	
						double mVelocityMul = WeaponThrowConfig.COMMON.modifiedVelocityMultiplier.get();	
						double totalVelocity = (baseThrow)*(1*bVelocityMul + modThrow*mVelocityMul) - (stack.getCount()*sVelocityMul);
						totalVelocity*=toolMultiplier;
						
			            double bExhaustionMul = WeaponThrowConfig.COMMON.baseExhaustionMultiplier.get();
			            double sExhaustionMul = WeaponThrowConfig.COMMON.stackExhaustionMultiplier.get();
			            double mExhaustionMul = WeaponThrowConfig.COMMON.modifiedExhaustionMultiplier.get();
			            double totalExhaustion = (baseExhaustion)*(1*bExhaustionMul + modThrow*mExhaustionMul) + (stack.getCount()*sExhaustionMul);
			            totalExhaustion*=toolMultiplier;

						WeaponThrowEvent.OnThrow onThrowEvent = new WeaponThrowEvent.OnThrow(stack, playerentity, totalDamage, totalVelocity, totalExhaustion);
						MinecraftForge.EVENT_BUS.post(onThrowEvent);
						
						
						WeaponThrowEntity throwedEntity = new WeaponThrowEntity(worldIn, playerentity, shouldDestroy, (float) onThrowEvent.totalDamage, stack);
						throwedEntity.func_234612_a_(playerentity, playerentity.rotationPitch, playerentity.rotationYaw, 0.0F, (float) onThrowEvent.totalVelocity, 1.0F);
						playerentity.addExhaustion((float) onThrowEvent.totalExhaustion);

			            
			            worldIn.addEntity(throwedEntity);
			            
			            SoundEvent soundevent = SoundEvents.ENTITY_EGG_THROW;
			            throwedEntity.playSound(soundevent, 1.0F, 0.5F);
			            
			            if (playerentity.abilities.isCreativeMode) {
			            	throwedEntity.pickupStatus = AbstractArrowEntity.PickupStatus.CREATIVE_ONLY;
			            	if(!WeaponThrowConfig.COMMON.creativeSpamming.get()) {
			            		playerentity.inventory.deleteStack(stack);
			            	}
			            }else {
			            	playerentity.inventory.deleteStack(stack);
			            }
			            
			            
					}
				}
			}
		}
	}
	 @SubscribeEvent
	 public void onItemCrash(ItemTossEvent event)
	 {
			
	 }
	
	 @SubscribeEvent
	 public void onPlayerToss(ItemTossEvent event)
	 {
			event.getPlayer().getCapability(ThrowProvider.THROW_POWER).ifPresent(cap -> {
				if (cap.getChargeTime() <= ITEMPHYSICSBLOCKING) {
					cap.setChargeTime(-1);
					if (ModList.get().isLoaded("itemphysic") && WeaponThrowConfig.COMMON.enableItemPhysicsFix.get()) {
						event.setCanceled(true);
					}

				}

			});
	 }
	 
	 @SubscribeEvent
	 public void attachCapability(AttachCapabilitiesEvent<Entity> event)
	 {
		 if (!(event.getObject() instanceof PlayerEntity)) return;
	
		 event.addCapability(THROWPOWER, new ThrowProvider());
	 }
	 
	 @SubscribeEvent
	 public void onPlayerTick(PlayerTickEvent event)
	 {
		 
		 if(event.phase.equals(TickEvent.Phase.START)) {
			 
			 if(!event.player.world.isRemote) {
				 event.player.getCapability(ThrowProvider.THROW_POWER).ifPresent(cap -> {
					
					boolean attacked = event.player.getCooledAttackStrength(0.0F) < 1.0F ? true: false;
					boolean cdConfig = WeaponThrowConfig.COMMON.notUseWhenCooldown.get();
					
					if(attacked && cdConfig) {
						cap.setChargeTime(-1);
					}
						
					 if(cap.getChargeTime() > 0) {
						cap.setChargeTime(cap.getChargeTime()-1);
					 }
						
					 
					 if(cap.doesMaxChargeChanged()) {
						 PacketHandler.sendToAll(new SPacketThrow(PlayerEntity.getUUID(event.player.getGameProfile()), (int) (cap.getChargeTime() >= 0? EventsHandler.getMaximumCharge(event.player): cap.getChargeTime())));
					 }
					 
					 cap.tick();
				 });
			 }else {
				 this.clientSideCharge++;
			 }
		 }
	 }
	 
	 public static int getMaximumCharge(PlayerEntity player) {
		return MathHelper.floor(player.getCooldownPeriod()*WeaponThrowConfig.COMMON.castTimeInTicks.get());
	 }

	 @OnlyIn(Dist.CLIENT)
	 public static void onSeverUpdate(UUID playerUUID, int chargeTime) {
			PlayerEntity playerentity = Minecraft.getInstance().world.getPlayerByUuid(playerUUID);
			
			if(playerentity != null) {
				playerentity.getCapability(ThrowProvider.THROW_POWER).ifPresent(cap -> {
					cap.setChargeTime(chargeTime);
							
				});
			}
	 }
	
	 @OnlyIn(Dist.CLIENT)
	 @SubscribeEvent
	 public void renderView(RenderGameOverlayEvent.Post event)
	 {
		 
		 int maxChargeTime = Minecraft.getInstance().player.getCapability(ThrowProvider.THROW_POWER).orElse(ThrowProvider.THROW_POWER.getDefaultInstance()).getChargeTime();
		 boolean isCharging = maxChargeTime >= 0 ? true : false;
		 
		 if(isCharging) {
	         if (Minecraft.getInstance().playerController.getCurrentGameType() != GameType.SPECTATOR) {
				 if(event.getType().equals(ElementType.CROSSHAIRS)) {
					 
					 
					 RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.ONE_MINUS_DST_COLOR, GlStateManager.DestFactor.ONE_MINUS_SRC_COLOR, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
					 MatrixStack p_238456_1_ = event.getMatrixStack();
					 
					 float f = MathHelper.clamp(this.clientSideCharge / maxChargeTime, 0.0F, 1.0F);
		
					 int scaledWidth = Minecraft.getInstance().getMainWindow().getScaledWidth();
				     int scaledHeight = Minecraft.getInstance().getMainWindow().getScaledHeight();
				     
				     IngameGui ingameGui = Minecraft.getInstance().ingameGUI;
				     
		             int j = scaledHeight / 2 + WeaponThrowConfig.COMMON.offsetY.get();
		             int k = scaledWidth / 2 + WeaponThrowConfig.COMMON.offsetX.get();

		             if (f >= 1.0F) {
		            	ingameGui.func_238474_b_(p_238456_1_, k, j, 68, 94, 16, 16);
	                 } else{
		                int l = (int)(f * 17.0F);
		                
		                ingameGui.func_238474_b_(p_238456_1_, k, j, 36, 94, 16, 4);
		                ingameGui.func_238474_b_(p_238456_1_, k, j, 52, 94, l, 4);
		             }
				 }
	         }
	         
	     }else if(!isCharging){
			 this.clientSideCharge = 0;
		 }
	 }
	 @OnlyIn(Dist.CLIENT)
	 @SubscribeEvent
	 public void updateFov(FOVUpdateEvent event)
	 {
		 int maxChargeTime = Minecraft.getInstance().player.getCapability(ThrowProvider.THROW_POWER).orElse(ThrowProvider.THROW_POWER.getDefaultInstance()).getChargeTime();
		 boolean isCharging = maxChargeTime >= 0 ? true : false;
		 
		 if(isCharging) {
			
			 float f = event.getFov();
	         int i = (int) (this.clientSideCharge);
	         
	         float f1 = (float)i / maxChargeTime;
	         if (f1 > 1.0F) {
	            f1 = 1.0F;
	         } else {
	            f1 = f1 * f1;
	         }
	         
	         f *= 1.0F + f1 * 0.15F;
	         
	         event.setNewfov(f);

		 }
	 }
	 
	 @OnlyIn(Dist.CLIENT)
	 @SubscribeEvent
	 public void renderPlayer(RenderPlayerEvent event)
	 {
			 event.getEntity().getCapability(ThrowProvider.THROW_POWER).ifPresent(cap -> {
				 
				 if(cap.getChargeTime() >= 0) {
						if(event.getEntity() instanceof AbstractClientPlayerEntity) {
							HandSide hand = ((AbstractClientPlayerEntity)event.getEntity()).getPrimaryHand();
							if(hand == HandSide.RIGHT)
								event.getRenderer().getEntityModel().rightArmPose = BipedModel.ArmPose.THROW_SPEAR;
							else
								event.getRenderer().getEntityModel().leftArmPose = BipedModel.ArmPose.THROW_SPEAR;
						}
				 }
			 });
	 }
	 
	 @OnlyIn(Dist.CLIENT)
	 @SubscribeEvent
	 public void inputUpdate(InputUpdateEvent event)
	 {
		 int maxChargeTime = Minecraft.getInstance().player.getCapability(ThrowProvider.THROW_POWER).orElse(ThrowProvider.THROW_POWER.getDefaultInstance()).getChargeTime();
		 boolean isCharging = maxChargeTime >= 0 ? true : false;
		 
		 if(isCharging) {
			 event.getMovementInput().moveStrafe *= 0.2F;
			 event.getMovementInput().moveForward *= 0.2F;
		 }
		 
	 }
	
    @SubscribeEvent
    public void onKeyInput(KeyInputEvent event)
    {
    	boolean pressed = KeyBindingHandler.KEYBINDING.isKeyDown();
    	
        if (pressed && event.getAction() != 0)
        {
        	PacketHandler.sendToServer(new CPacketThrow(this.wasPressed? 2: 1));
        	this.wasPressed = true;
        	
        } else if(this.wasPressed && event.getAction() == 0 && !pressed){
        	
        	PacketHandler.sendToServer(new CPacketThrow(event.getAction()));
        	this.wasPressed = false;
        }
        
    }
	
}
