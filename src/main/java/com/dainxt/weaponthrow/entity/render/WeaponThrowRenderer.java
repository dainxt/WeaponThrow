package com.dainxt.weaponthrow.entity.render;

import com.dainxt.weaponthrow.config.WeaponThrowConfig;
import com.dainxt.weaponthrow.projectile.WeaponThrowEntity;
import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.SpriteRenderer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.client.registry.IRenderFactory;

@OnlyIn(Dist.CLIENT)
public class WeaponThrowRenderer extends SpriteRenderer<WeaponThrowEntity> {

   public WeaponThrowRenderer(EntityRendererManager renderManagerIn, net.minecraft.client.renderer.ItemRenderer itemRenderer) {
	   super(renderManagerIn, itemRenderer);
   }

   public void render(WeaponThrowEntity entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
	  
	  float degrees = entityIn.getRotationAnimation(partialTicks);

	  if(WeaponThrowConfig.COMMON.classicRender.get()) {
		  float amplitude = 0.25F;
		  float offsetX = amplitude*(MathHelper.sin((float) Math.toRadians(degrees)));
		  float offsetY = amplitude*(1-MathHelper.cos((float) Math.toRadians(degrees)));
		  matrixStackIn.push();
	
			  matrixStackIn.rotate(Vector3f.YP.rotationDegrees(MathHelper.lerp(partialTicks, entityIn.prevRotationYaw, entityIn.rotationYaw) - 270.0F));
			  matrixStackIn.translate(offsetX, offsetY, 0.0F);
		      matrixStackIn.scale(1.5F, 1.5F, 1.5F);
		      
		      matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(degrees));
		      
		      int count = entityIn.getItemStack().getCount();
		      
		      Minecraft.getInstance().getItemRenderer().renderItem(entityIn.getItem(), ItemCameraTransforms.TransformType.GROUND, packedLightIn, OverlayTexture.NO_OVERLAY, matrixStackIn, bufferIn);
		      
		      if(count > 32) {
		    	  matrixStackIn.translate(0.05F, 0.05F, 0.05F);
		    	  Minecraft.getInstance().getItemRenderer().renderItem(entityIn.getItem(), ItemCameraTransforms.TransformType.GROUND, packedLightIn, OverlayTexture.NO_OVERLAY, matrixStackIn, bufferIn);
		      }
	
	      matrixStackIn.pop();
	  }else {
		  float scale = 0.75F;
		  matrixStackIn.push();
			  matrixStackIn.translate(0.F, 0.15F, 0.0F);
			  matrixStackIn.rotate(Vector3f.YP.rotationDegrees(MathHelper.lerp(partialTicks, entityIn.prevRotationYaw, entityIn.rotationYaw) - 90.0F));
			  matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(-degrees));
			  matrixStackIn.scale(scale, scale, scale);
		
			  int count = entityIn.getItemStack().getCount();
			  
		      Minecraft.getInstance().getItemRenderer().renderItem(entityIn.getItem(), ItemCameraTransforms.TransformType.FIXED, packedLightIn, OverlayTexture.NO_OVERLAY, matrixStackIn, bufferIn);
		      if(count > 32) {
		    	  matrixStackIn.translate(-0.05F, -0.05F, -0.05F);
		    	  Minecraft.getInstance().getItemRenderer().renderItem(entityIn.getItem(), ItemCameraTransforms.TransformType.FIXED, packedLightIn, OverlayTexture.NO_OVERLAY, matrixStackIn, bufferIn);
		      }
	      matrixStackIn.pop();
	  }
   }
   
	public static class RenderFactory implements IRenderFactory<WeaponThrowEntity> {

		@Override
		public EntityRenderer<WeaponThrowEntity> createRenderFor(EntityRendererManager manager) {
			return new WeaponThrowRenderer(manager, Minecraft.getInstance().getItemRenderer());
		}

	}
	
}