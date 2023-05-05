package com.dainxt.weaponthrow.handlers;

import com.dainxt.weaponthrow.WeaponThrow;
import com.dainxt.weaponthrow.enchantment.ConccusionEnchantment;
import com.dainxt.weaponthrow.enchantment.GravityEnchantment;
import com.dainxt.weaponthrow.enchantment.GroundedEdgeEnchantment;
import com.dainxt.weaponthrow.enchantment.ReturnEnchantment;
import com.dainxt.weaponthrow.enchantment.ThrowEnchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class EnchantmentHandler {

	public static final Enchantment THROW = Registry.register(Registry.ENCHANTMENT, new Identifier(WeaponThrow.MODID, "throw"), new ThrowEnchantment(Enchantment.Rarity.RARE, EquipmentSlot.MAINHAND));
	public static final Enchantment GROUNDEDEDGE = Registry.register(Registry.ENCHANTMENT, new Identifier(WeaponThrow.MODID, "groundededge"), new GroundedEdgeEnchantment(Enchantment.Rarity.UNCOMMON, EquipmentSlot.MAINHAND));
	public static final Enchantment CONCCUSION = Registry.register(Registry.ENCHANTMENT, new Identifier(WeaponThrow.MODID, "conccusion"), new ConccusionEnchantment(Enchantment.Rarity.VERY_RARE, EquipmentSlot.MAINHAND));
	public static final Enchantment GRAVITY = Registry.register(Registry.ENCHANTMENT, new Identifier(WeaponThrow.MODID, "gravity"), new GravityEnchantment(Enchantment.Rarity.VERY_RARE, EquipmentSlot.MAINHAND));
	public static final Enchantment RETURN = Registry.register(Registry.ENCHANTMENT, new Identifier(WeaponThrow.MODID, "return"), new ReturnEnchantment(Enchantment.Rarity.RARE, EquipmentSlot.MAINHAND));
	
	public static void registerEnchantments() {}
}
