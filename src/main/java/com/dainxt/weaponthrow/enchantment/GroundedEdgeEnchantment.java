package com.dainxt.weaponthrow.enchantment;

import com.dainxt.weaponthrow.config.WeaponThrowConfig;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;

public class GroundedEdgeEnchantment extends Enchantment {

		   public GroundedEdgeEnchantment(Enchantment.Rarity rarityIn, EquipmentSlotType... slots) {
		      super(rarityIn, EnchantmentType.WEAPON, slots);
		   }

		   /**
		    * Returns the minimal value of enchantability needed on the enchantment level passed.
		    */
		   public int getMinEnchantability(int enchantmentLevel) {
		      return 25;
		   }

		   public int getMaxEnchantability(int enchantmentLevel) {
		      return this.getMinEnchantability(enchantmentLevel) + 40;
		   }

		   /**
		    * Returns the maximum level that the enchantment can have.
		    */
		   public int getMaxLevel() {
		      return 3;
		   }
		   
		   @Override
		public boolean canApply(ItemStack stack) {
			   boolean enchantAll = WeaponThrowConfig.COMMON.enchantAll.get();
			   boolean isAxe = stack.getItem() instanceof AxeItem;
			   boolean canApply = super.canApply(stack);
			   return isAxe || canApply || enchantAll ? WeaponThrowConfig.COMMON.groundedEdgeEnchant.get() : false;
		}
}
