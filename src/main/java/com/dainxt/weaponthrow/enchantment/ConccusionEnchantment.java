package com.dainxt.weaponthrow.enchantment;

import com.dainxt.weaponthrow.handlers.ConfigRegistry;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;

public class ConccusionEnchantment extends Enchantment {
		   public ConccusionEnchantment(Enchantment.Rarity rarityIn, EquipmentSlot... slots) {
		      super(rarityIn, EnchantmentTarget.WEAPON, slots);
		   }
		   public int getMinPower(int enchantmentLevel) {
		      return 30;
		   }
		   public int getMaxPower(int enchantmentLevel) {
		      return this.getMinPower(enchantmentLevel) + 30;
		   }
		   // Returns the maximum level that the enchantment can have.
		   public int getMaxLevel() {
		      return 2;
		   }
		   @Override
		   public boolean isAcceptableItem(ItemStack stack) {
			   boolean enchantAll = ConfigRegistry.COMMON.getConfig().enchantments.enchantAllWeapons;
			   boolean isAxe = stack.getItem() instanceof AxeItem;
			   boolean canApply = super.isAcceptableItem(stack);
			   return (isAxe || canApply || enchantAll) && ConfigRegistry.COMMON.getConfig().enchantments.enableConccusion;
		   }

}
