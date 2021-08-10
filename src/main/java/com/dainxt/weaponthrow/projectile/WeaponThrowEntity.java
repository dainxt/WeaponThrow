package com.dainxt.weaponthrow.projectile;

import java.util.List;

import javax.annotation.Nullable;

import com.dainxt.weaponthrow.config.WeaponThrowConfig;
import com.dainxt.weaponthrow.events.WeaponThrowEvent;
import com.dainxt.weaponthrow.handlers.EnchantmentHandler;
import com.dainxt.weaponthrow.handlers.EntityHandler;

import harmonised.pmmo.skills.Skill;
import net.minecraft.block.AnvilBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SandBlock;
import net.minecraft.block.TorchBlock;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.IRendersAsItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.network.NetworkHooks;

@OnlyIn(
		   value = Dist.CLIENT,
		   _interface = IRendersAsItem.class
		)
public class WeaponThrowEntity extends AbstractArrowEntity implements IRendersAsItem{

	
	   private float clientSideRotation = 0;
	   private boolean counterClockwiseBounce = true;
	   
	   private static final DataParameter<Byte> LOYALTY_LEVEL = EntityDataManager.createKey(WeaponThrowEntity.class, DataSerializers.BYTE);
	   private static final DataParameter<CompoundNBT> COMPOUND_STACK = EntityDataManager.createKey(WeaponThrowEntity.class, DataSerializers.COMPOUND_NBT);
	   private static final DataParameter<BlockPos> DESTROYED_BLOCK = EntityDataManager.createKey(WeaponThrowEntity.class, DataSerializers.BLOCK_POS);
	   private static final DataParameter<Boolean> SHOULD_DESTROY = EntityDataManager.createKey(WeaponThrowEntity.class, DataSerializers.BOOLEAN);
	   
	   private boolean dealtDamage;
	   private float attackDamage;
	   public int returningTicks;
	   
	   private int ticksInGround;

	   public WeaponThrowEntity(EntityType<? extends WeaponThrowEntity> type, World worldIn) {
	      super(type, worldIn);
	   }

	   public WeaponThrowEntity(World worldIn, LivingEntity thrower, boolean canDestroy ,float attackDamage, ItemStack thrownStackIn) {
	      super(EntityHandler.WEAPONTHROW, thrower, worldIn);      
	      this.attackDamage = attackDamage;
	      this.dataManager.set(COMPOUND_STACK, thrownStackIn.copy().write(new CompoundNBT()));	      
	      this.dataManager.set(LOYALTY_LEVEL, (byte)WeaponThrowEntity.getReturnOrLoyaltyEnchantment(thrownStackIn));	      
	      this.dataManager.set(DESTROYED_BLOCK, BlockPos.ZERO);	      
	      this.dataManager.set(SHOULD_DESTROY, canDestroy);
	   }

	   @OnlyIn(Dist.CLIENT)
	   public WeaponThrowEntity(World worldIn, double x, double y, double z) {
	      super(EntityHandler.WEAPONTHROW, x, y, z, worldIn);
	   }
	   
	   protected void registerData() {
	      super.registerData();
	      this.dataManager.register(COMPOUND_STACK, new CompoundNBT());
	      this.dataManager.register(LOYALTY_LEVEL, (byte)0);
	      this.dataManager.register(DESTROYED_BLOCK, BlockPos.ZERO);
	      this.dataManager.register(SHOULD_DESTROY, false);
	   }
	   
		public void setItemStack(ItemStack stack) {
			this.getDataManager().set(COMPOUND_STACK, stack.write(new CompoundNBT()));
		}
		public ItemStack getItemStack() {
			return ItemStack.read(((CompoundNBT)this.getDataManager().get(COMPOUND_STACK)));
		}
		
		public void shouldDestroy(boolean stack) {
			this.getDataManager().set(SHOULD_DESTROY, stack);
		}
		public boolean shouldDestroy() {
			return ((Boolean)this.getDataManager().get(SHOULD_DESTROY));
		}
		
		public void setDestroyedBlock(BlockPos pos) {
			this.getDataManager().set(DESTROYED_BLOCK, pos);
		}
		public BlockPos getDestroyedBlock() {
			return ((BlockPos)this.getDataManager().get(DESTROYED_BLOCK));
		}


	   public void tick() {
	         
	      if (this.timeInGround > 4 && !this.dealtDamage) {
	         this.dealtDamage = true;
	      }
	      
	      if(!this.getDestroyedBlock().equals(BlockPos.ZERO) && !world.isRemote) {
	    	  if (((PlayerEntity)this.func_234616_v_()).abilities.isCreativeMode && WeaponThrowConfig.COMMON.creativeSpamming.get()) {
		 			 ((PlayerEntity)this.func_234616_v_()).setHeldItem(Hand.MAIN_HAND, this.getItemStack());
					 
					 ((ServerPlayerEntity)this.func_234616_v_()).interactionManager.tryHarvestBlock(this.getDestroyedBlock());
	    	  }else {
	 			 ((PlayerEntity)this.func_234616_v_()).setHeldItem(Hand.MAIN_HAND, this.getItemStack());
				 
				 ((ServerPlayerEntity)this.func_234616_v_()).interactionManager.tryHarvestBlock(this.getDestroyedBlock());
				 
				 if(((PlayerEntity)this.func_234616_v_()).getHeldItem(Hand.MAIN_HAND).isEmpty()) {
					 this.setItemStack(ItemStack.EMPTY);
				 }else {
					 ((PlayerEntity)this.func_234616_v_()).setHeldItem(Hand.MAIN_HAND, ItemStack.EMPTY);
				 }

	    	  }
	    	  
	    	  this.setDestroyedBlock(BlockPos.ZERO);
	      }

	      
	      int gravityLevel = WeaponThrowConfig.COMMON.gravityEnchant.get() ? EnchantmentHelper.getEnchantmentLevel(EnchantmentHandler.GRAVITY, this.getItemStack()) : 0;
	      if(gravityLevel > 0) {
	    	  this.setNoGravity(true);
			   if(World.isOutsideBuildHeight(this.getPositionUnderneath())) {
				   this.setMotion(this.getMotion().mul(1, 0, 1));
			   }
			   if((Math.abs(this.getMotion().getX()) < 0.1 && Math.abs(this.getMotion().getZ()) < 0.1)) {
				   
				   if(WeaponThrowConfig.COMMON.gravityDrop.get()) {
					   this.setNoGravity(false);
				   }
				   
			   }
	      }
	      

	      if(this.inGround && MathHelper.abs(this.rotationPitch) < 45 && this.getItemStack().getItem() instanceof BlockItem) {
	    	  
	    	  this.setMotion(this.getMotion().scale(-0.5F));
	    	  this.counterClockwiseBounce = !this.counterClockwiseBounce;
	    	  this.inGround = false;
	      }

	      Entity entity = this.func_234616_v_();
	      if ((this.dealtDamage || this.getNoClip()) && entity != null) {
	         int i = WeaponThrowConfig.COMMON.returnEnchant.get() ? this.dataManager.get(LOYALTY_LEVEL) : 0;
	         
	         if (i > 0 && ModList.get().isLoaded("pmmo") && WeaponThrowConfig.COMMON.enablePMMOIntegration.get()) {
		        	int combatLevel = Skill.getLevel(Skill.COMBAT.name.toLowerCase(), entity.getUniqueID());
				 	int requiredLevel = WeaponThrowConfig.COMMON.requiredCombatLoyalty.get();
						
				 	if((requiredLevel > combatLevel) && requiredLevel > 0) {
				 		if(entity instanceof PlayerEntity) {
				 			((PlayerEntity)entity).sendStatusMessage(new TranslationTextComponent("weaponthrow.pmmo.requirementLoyalty", requiredLevel), true);
				 		}
					 	i = 0;
				 	} 
		     }
	         
	         if (i > 0 && !this.shouldReturnToThrower()) {
	        	 
	            if (!this.world.isRemote && this.pickupStatus == AbstractArrowEntity.PickupStatus.ALLOWED) {
	               this.entityDropItem(this.getArrowStack(), 0.1F);
	            }

	            this.remove();
	         } else if (i > 0) {
	            this.setNoClip(true);
	            Vector3d vector3d = new Vector3d(entity.getPosX() - this.getPosX(), entity.getPosYEye() - this.getPosY(), entity.getPosZ() - this.getPosZ());
	            this.setRawPosition(this.getPosX(), this.getPosY() + vector3d.y * 0.015D * (double)i, this.getPosZ());
	            if (this.world.isRemote) {
	               this.lastTickPosY = this.getPosY();
	            }

	            double d0 = 0.05D * (double)i;
	            this.setMotion(this.getMotion().scale(0.95D).add(vector3d.normalize().scale(d0)));
	            if (this.returningTicks == 0) {
	               this.playSound(SoundEvents.ITEM_TRIDENT_RETURN, 10.0F, 1.0F);
	            }

	            ++this.returningTicks;
	         }
	      }

	      super.tick();
	   }

	   private boolean shouldReturnToThrower() {
	      Entity entity = this.func_234616_v_();
	      
	      if (entity != null && entity.isAlive()) {
	         return !(entity instanceof ServerPlayerEntity) || !entity.isSpectator();
	      } else {
	         return false;
	      }
	   }

	   protected ItemStack getArrowStack() {
	      return this.getItemStack().copy();
	   }


	   @Nullable
	   protected EntityRayTraceResult rayTraceEntities(Vector3d startVec, Vector3d endVec) {
	      return this.dealtDamage ? null : super.rayTraceEntities(startVec, endVec);
	   }


	   protected void onEntityHit(EntityRayTraceResult p_213868_1_) {
	      Entity entity = p_213868_1_.getEntity();
	      float f = this.attackDamage;
	      if (entity instanceof LivingEntity) {
	         LivingEntity livingentity = (LivingEntity)entity;

	         f += WeaponThrowConfig.COMMON.throwEnchant.get() ? EnchantmentHelper.getEnchantmentLevel(EnchantmentHandler.THROW, this.getItemStack())*1F : 0;
	         f += EnchantmentHelper.getModifierForCreature(this.getItemStack(), livingentity.getCreatureAttribute());
	      }

	      Entity entity1 = this.func_234616_v_();
	      DamageSource damagesource = DamageSource.causeThrownDamage(this, (Entity)(entity1 == null ? this : entity1));
	      this.dealtDamage = true;
	      SoundEvent soundevent = SoundEvents.ITEM_TRIDENT_HIT;
	      if (entity.attackEntityFrom(damagesource, f)) {
	         if (entity.getType() == EntityType.ENDERMAN) {
	            return;
	         }
	         
	         if (entity instanceof LivingEntity) {
	            LivingEntity livingentity1 = (LivingEntity)entity;
	            
		         
			     int contusionLevel = WeaponThrowConfig.COMMON.conccusionEnchant.get() ? EnchantmentHelper.getEnchantmentLevel(EnchantmentHandler.CONCCUSION, this.getItemStack()) : 0;
		         
			     if (contusionLevel > 0) {
				      livingentity1.addPotionEffect(new EffectInstance(Effects.SLOWNESS, 20*2*contusionLevel, 5));
				      livingentity1.addPotionEffect(new EffectInstance(Effects.NAUSEA, 20*5*contusionLevel, 3));
		         }
		         
			      int fireTime = EnchantmentHelper.getEnchantmentLevel(Enchantments.FIRE_ASPECT, this.getItemStack());
			      int groundedLevel = WeaponThrowConfig.COMMON.groundedEdgeEnchant.get() ? EnchantmentHelper.getEnchantmentLevel(EnchantmentHandler.GROUNDEDEDGE, this.getItemStack()) : 0;
			      
			      if(fireTime > 0 || groundedLevel > 0) {
				      List<LivingEntity> nearEntities = world.getEntitiesWithinAABB(LivingEntity.class, this.getBoundingBox().grow(1.0D));
			
				      if(!nearEntities.isEmpty()) {
				    	  for(LivingEntity nearEntity: nearEntities) {
				    		  if(nearEntity.getRNG().nextInt(3) == 0) {
				    			  nearEntity.setFire(fireTime);
				    		  }
				    		  if(true) {
				    			  nearEntity.addPotionEffect(new EffectInstance(Effects.WEAKNESS, 80, groundedLevel-1));
				    		  }
				    	  }
				      }
			    }
			      
	            if (entity1 instanceof LivingEntity) {
	               EnchantmentHelper.applyThornEnchantments(livingentity1, entity1);
	               EnchantmentHelper.applyArthropodEnchantments((LivingEntity)entity1, livingentity1);
	            }

	            this.arrowHit(livingentity1);
	            
	            if(this.getItemStack().getItem() instanceof BlockItem) {
	            	Block blockItem = Block.getBlockFromItem(this.getItemStack().getItem());
	            	
		            if(blockItem instanceof SandBlock) {
		            	if(livingentity1.getRNG().nextInt(10) == 0) livingentity1.addPotionEffect(new EffectInstance(Effects.BLINDNESS, 60, 3));
		            }
		            else if (blockItem instanceof TorchBlock){
		            	if(livingentity1.getRNG().nextInt(5) == 0) livingentity1.setFire(1);
		            }
		            else if (blockItem instanceof AnvilBlock){
		            	livingentity1.addPotionEffect(new EffectInstance(Effects.NAUSEA, 60, 3));
		            	livingentity1.addPotionEffect(new EffectInstance(Effects.SLOWNESS, 60, 5));
		            }
	            }else {
	            	Item itemThrowed = this.getItemStack().getItem();
	            	if(itemThrowed.equals(Items.BLAZE_ROD) || itemThrowed.equals(Items.BLAZE_POWDER)) {
	            		livingentity1.setFire(1);
	            	}
	            }
	         }
	         
	         
	      }
	      
	      


	      this.setMotion(this.getMotion().mul(-0.01D, -0.1D, -0.01D));
	      float f1 = 1.0F;
	      if (this.world instanceof ServerWorld && this.world.isThundering() && EnchantmentHelper.hasChanneling(this.getItemStack())) {
	         BlockPos blockpos = entity.func_233580_cy_();
	         if (this.world.canSeeSky(blockpos)) {
	            LightningBoltEntity lightningboltentity = EntityType.LIGHTNING_BOLT.create(this.world);
	            lightningboltentity.func_233576_c_(Vector3d.func_237492_c_(blockpos));
	            lightningboltentity.setCaster(entity1 instanceof ServerPlayerEntity ? (ServerPlayerEntity)entity1 : null);
	            this.world.addEntity(lightningboltentity);
	            soundevent = SoundEvents.ITEM_TRIDENT_THUNDER;
	            f1 = 5.0F;
	         }
	      }

	      this.playSound(soundevent, f1, 1.0F);
	   }

	   protected SoundEvent getHitEntitySound() {
		   
	      return SoundEvents.BLOCK_METAL_HIT;
	   }

	   public void onCollideWithPlayer(PlayerEntity entityIn) {
		     
		  Entity entity = this.func_234616_v_();
		  
	      if (entity == null || entity.getUniqueID() == entityIn.getUniqueID() || this.ticksInGround > (WeaponThrowConfig.COMMON.ticksUntilWeaponLoseOwner.get())) {
	    	  super.onCollideWithPlayer(entityIn);
	      }
	   }

	   public void readAdditional(CompoundNBT compound) {
	      super.readAdditional(compound);
	      
	      this.dealtDamage = compound.getBoolean("DealtDamage");
	      
	      if (compound.contains("Stack", 10)) {
	    	  this.setItemStack(ItemStack.read(compound.getCompound("Stack")));
	      }
	      
	      this.dataManager.set(LOYALTY_LEVEL, (byte)WeaponThrowEntity.getReturnOrLoyaltyEnchantment(this.getItemStack()));
		   
	   }

	   public void writeAdditional(CompoundNBT compound) {
	      super.writeAdditional(compound);
	      compound.put("Stack", this.getDataManager().get(COMPOUND_STACK));
	      compound.putBoolean("DealtDamage", this.dealtDamage);
	   }

	   public void func_225516_i_() {
	      int i = this.dataManager.get(LOYALTY_LEVEL);
	      if (this.pickupStatus != AbstractArrowEntity.PickupStatus.ALLOWED || i <= 0) {
	    	  ++this.ticksInGround;
	    	  if (this.ticksInGround >= WeaponThrowConfig.COMMON.despawnTime.get()) {
	    	      this.remove();
	    	  }
	      }

	   }
	   
	   protected void onImpact(RayTraceResult result) {
			WeaponThrowEvent.OnImpact testEvent = new WeaponThrowEvent.OnImpact(this, (PlayerEntity)this.func_234616_v_(), result);
			if(MinecraftForge.EVENT_BUS.post(testEvent)) {
				return;
			}
			
		      RayTraceResult.Type raytraceresult$type = result.getType();
		      if (raytraceresult$type == RayTraceResult.Type.ENTITY) {
				   Entity hittedEntity = ((EntityRayTraceResult)result).getEntity();
				   if(hittedEntity instanceof LivingEntity && this.func_234616_v_() instanceof PlayerEntity) {
			           if (((PlayerEntity)this.func_234616_v_()).abilities.isCreativeMode && WeaponThrowConfig.COMMON.creativeSpamming.get()) {
							((PlayerEntity)this.func_234616_v_()).setHeldItem(Hand.MAIN_HAND, this.getItemStack());
							((PlayerEntity)this.func_234616_v_()).attackTargetEntityWithCurrentItem(hittedEntity);

			           }else {
						   ((PlayerEntity)this.func_234616_v_()).setHeldItem(Hand.MAIN_HAND, this.getItemStack());
						   ((PlayerEntity)this.func_234616_v_()).attackTargetEntityWithCurrentItem(hittedEntity);
						   
						   if(((PlayerEntity)this.func_234616_v_()).getHeldItem(Hand.MAIN_HAND).isEmpty()) {
							  this.setItemStack(ItemStack.EMPTY);
						   }else {
							  ((PlayerEntity)this.func_234616_v_()).setHeldItem(Hand.MAIN_HAND, ItemStack.EMPTY);
						   }			        	   
			           }

				   }
		    	  
		         this.onEntityHit((EntityRayTraceResult)result);
		         
		      } else if (raytraceresult$type == RayTraceResult.Type.BLOCK) {
		    	  
			   		BlockPos stickBlockPos = ((BlockRayTraceResult)result).getPos();
			   		BlockState state = this.world.getBlockState(stickBlockPos);
			   		
			   		
			   		if(!WeaponThrowConfig.COMMON.blackList.get().contains(state.getBlock()) && this.shouldDestroy()) {
			   			
				        	 boolean canBreak = WeaponThrowConfig.COMMON.canBreakBlocks.get();
				        	 
				        	 boolean canHarvest = false;
				        	 
				        	 canHarvest = this.getItemStack().getHarvestLevel(state.getHarvestTool(), null, null) >= state.getHarvestLevel();
			        		 canHarvest = canHarvest && this.getItemStack().getDestroySpeed(state) > 1.0F && canBreak;

				        	 if(canHarvest) {
				        		 
				        		 if(!world.isRemote && this.inBlockState == null) {
				        			 this.setDestroyedBlock(stickBlockPos);
				        		 }
				        	 }
		   			}
			   		
			   		this.func_230299_a_((BlockRayTraceResult)result);
		      }
		   }
	
	   protected void func_230299_a_(BlockRayTraceResult p_230299_1_) {
		      this.inBlockState = this.world.getBlockState(p_230299_1_.getPos());
		      Vector3d vector3d = p_230299_1_.getHitVec().subtract(this.getPosX(), this.getPosY(), this.getPosZ());
		      this.setMotion(vector3d);
		      Vector3d vector3d1 = vector3d.normalize().scale((double)0.05F);
		      this.setRawPosition(this.getPosX() - vector3d1.x, this.getPosY() - vector3d1.y, this.getPosZ() - vector3d1.z);
		      
		      SoundEvent event = SoundEvents.ITEM_TRIDENT_HIT_GROUND;
			  if(this.getItemStack().getItem() instanceof BlockItem) {
				   Block block = Block.getBlockFromItem(this.getItemStack().getItem());
				   event = block.getDefaultState().getSoundType().getHitSound();
			  }
			   
		      this.playSound(event, 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));
		      this.inGround = true;
		      this.arrowShake = 7;
		      this.setIsCritical(false);
		      this.setPierceLevel((byte)0);
		      this.setHitSound(event);
		      this.setShotFromCrossbow(false);
		      this.func_213870_w();
	   }
	   
	   @OnlyIn(Dist.CLIENT)
	   public boolean isInRangeToRender3d(double x, double y, double z) {
	      return true;
	   }

		@Override
		public ItemStack getItem() {
			return this.getItemStack();
		}
		@Override
		public IPacket<?> createSpawnPacket() {
			return NetworkHooks.getEntitySpawningPacket(this);
		}
		
		public static int getReturnOrLoyaltyEnchantment(ItemStack stack) {
			int loyaltyLevel = EnchantmentHelper.getEnchantmentLevel(Enchantments.LOYALTY, stack);
			int returnLevel = EnchantmentHelper.getEnchantmentLevel(EnchantmentHandler.RETURN, stack);
			return loyaltyLevel > 0 ? loyaltyLevel : returnLevel;
		}
		
		@OnlyIn(Dist.CLIENT)
		public float getRotationAnimation(float partialTicks) {
		    if(!this.inGround) {
		    	clientSideRotation = (this.counterClockwiseBounce? 1:-1)*(this.ticksExisted+partialTicks)*50F;
		   	}
		    return this.clientSideRotation;
		}
}
