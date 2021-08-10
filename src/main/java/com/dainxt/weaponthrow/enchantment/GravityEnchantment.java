package com.dainxt.weaponthrow.enchantment;

import com.dainxt.weaponthrow.config.WeaponThrowConfig;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;

public class GravityEnchantment extends Enchantment{
	public GravityEnchantment(Enchantment.Rarity rarityIn, EquipmentSlotType... slots) {
		  super(rarityIn, EnchantmentType.WEAPON, slots);
	}

	public int getMinEnchantability(int enchantmentLevel) {
	      return 25;
	}

	public int getMaxEnchantability(int enchantmentLevel) {
	      return 50;
	}

	public int getMaxLevel() {
	      return 1;
	}
	
	public boolean isTreasureEnchantment() {
		 return true;
	}

	public boolean isCurse() {
		 return true;
	}
	
	@Override
	public boolean canApply(ItemStack stack) {
		   boolean enchantAll = WeaponThrowConfig.COMMON.enchantAll.get();
		   boolean isAxe = stack.getItem() instanceof AxeItem;
		   boolean canApply = super.canApply(stack);	   
		   return isAxe || canApply || enchantAll ? WeaponThrowConfig.COMMON.gravityEnchant.get() : false;
	}
}
