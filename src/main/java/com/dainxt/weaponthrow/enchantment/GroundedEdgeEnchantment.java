package com.dainxt.weaponthrow.enchantment;

import com.dainxt.weaponthrow.handlers.ConfigRegistry;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;

public class GroundedEdgeEnchantment extends Enchantment {

		   public GroundedEdgeEnchantment(Enchantment.Rarity rarityIn, EquipmentSlot... slots) {
		      super(rarityIn, EnchantmentTarget.WEAPON, slots);
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
		public boolean isAcceptableItem(ItemStack stack) {
			   boolean enchantAll = ConfigRegistry.COMMON.getConfig().enchantments.enchantAllWeapons;
			   boolean isAxe = stack.getItem() instanceof AxeItem;
			   boolean canApply = super.isAcceptableItem(stack);
			   return (isAxe || canApply || enchantAll) && ConfigRegistry.COMMON.getConfig().enchantments.enableGroundedEdge;
		}
}
