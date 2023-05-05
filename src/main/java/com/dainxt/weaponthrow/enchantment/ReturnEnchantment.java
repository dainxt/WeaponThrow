package com.dainxt.weaponthrow.enchantment;

import com.dainxt.weaponthrow.handlers.ConfigRegistry;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;

public class ReturnEnchantment extends Enchantment {


		   public ReturnEnchantment(Rarity rarityIn, EquipmentSlot... mainhand) {
			   super(rarityIn, EnchantmentTarget.WEAPON, mainhand);
		   }
		   
		   /**
		    * Returns the minimal value of enchantability needed on the enchantment level passed.
		    */
		   public int getMinPower(int enchantmentLevel) {
		      return 5 + enchantmentLevel * 7;
		   }

		   public int getMaxPower(int enchantmentLevel) {
		      return 50;
		   }

		   /**
		    * Returns the maximum level that the enchantment can have.
		    */
		   public int getMaxLevel() {
		      return 1;
		   }
		   
		   @Override
		   public boolean isAcceptableItem(ItemStack stack) {
			   boolean enchantAll = ConfigRegistry.COMMON.getConfig().enchantments.enchantAllWeapons;
			   boolean isAxe = stack.getItem() instanceof AxeItem;
			   boolean canApply = super.isAcceptableItem(stack);
			   return isAxe || canApply || enchantAll ? ConfigRegistry.COMMON.getConfig().enchantments.enableReturn : false;
		   }

}
