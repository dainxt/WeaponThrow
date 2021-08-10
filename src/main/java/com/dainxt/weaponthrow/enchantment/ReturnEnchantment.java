package com.dainxt.weaponthrow.enchantment;

import com.dainxt.weaponthrow.config.WeaponThrowConfig;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;

public class ReturnEnchantment extends Enchantment {


		   public ReturnEnchantment(Rarity rarityIn, EquipmentSlotType... mainhand) {
			   super(rarityIn, EnchantmentType.WEAPON, mainhand);
		   }
		   
		   /**
		    * Returns the minimal value of enchantability needed on the enchantment level passed.
		    */
		   public int getMinEnchantability(int enchantmentLevel) {
		      return 5 + enchantmentLevel * 7;
		   }

		   public int getMaxEnchantability(int enchantmentLevel) {
		      return 50;
		   }

		   /**
		    * Returns the maximum level that the enchantment can have.
		    */
		   public int getMaxLevel() {
		      return 1;
		   }
		   
		   @Override
		   public boolean canApply(ItemStack stack) {
			   boolean enchantAll = WeaponThrowConfig.COMMON.enchantAll.get();
			   boolean isAxe = stack.getItem() instanceof AxeItem;
			   boolean canApply = super.canApply(stack);
			   return isAxe || canApply || enchantAll ? WeaponThrowConfig.COMMON.returnEnchant.get() : false;
		   }

}
