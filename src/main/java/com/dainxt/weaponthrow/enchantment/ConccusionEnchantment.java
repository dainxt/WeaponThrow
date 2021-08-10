package com.dainxt.weaponthrow.enchantment;

import com.dainxt.weaponthrow.config.WeaponThrowConfig;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;

public class ConccusionEnchantment extends Enchantment {

		   public ConccusionEnchantment(Enchantment.Rarity rarityIn, EquipmentSlotType... slots) {
		      super(rarityIn, EnchantmentType.WEAPON, slots);
		   }

		   public int getMinEnchantability(int enchantmentLevel) {
		      return 30;
		   }

		   public int getMaxEnchantability(int enchantmentLevel) {
		      return this.getMinEnchantability(enchantmentLevel) + 30;
		   }

		   /**
		    * Returns the maximum level that the enchantment can have.
		    */
		   public int getMaxLevel() {
		      return 2;
		   }
		   
		   @Override
		   public boolean canApply(ItemStack stack) {
			   boolean enchantAll = WeaponThrowConfig.COMMON.enchantAll.get();
			   boolean isAxe = stack.getItem() instanceof AxeItem;
			   boolean canApply = super.canApply(stack);
			   return isAxe || canApply || enchantAll ? WeaponThrowConfig.COMMON.conccusionEnchant.get() : false;
		   }

}
