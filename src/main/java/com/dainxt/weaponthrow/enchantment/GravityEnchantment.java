package com.dainxt.weaponthrow.enchantment;

import com.dainxt.weaponthrow.handlers.ConfigRegistry;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;

public class GravityEnchantment extends Enchantment{
	public GravityEnchantment(Enchantment.Rarity rarityIn, EquipmentSlot... slots) {
		  super(rarityIn, EnchantmentTarget.WEAPON, slots);
	}

	public int getMinPower(int enchantmentLevel) {
	      return 25;
	}

	public int getMaxPower(int enchantmentLevel) {
	      return 50;
	}

	public int getMaxLevel() {
	      return 1;
	}
	
	public boolean isTreasure() {
		 return true;
	}

	public boolean isCursed() {
		 return true;
	}
	
	@Override
	public boolean isAcceptableItem(ItemStack stack) {
		   boolean enchantAll = ConfigRegistry.COMMON.getConfig().enchantments.enchantAllWeapons;
		   boolean isAxe = stack.getItem() instanceof AxeItem;
		   boolean canApply = super.isAcceptableItem(stack);	   
		   return isAxe || canApply || enchantAll ? ConfigRegistry.COMMON.getConfig().enchantments.enableGravity : false;
	}
}
