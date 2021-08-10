package com.dainxt.weaponthrow.handlers;

import com.dainxt.weaponthrow.enchantment.ConccusionEnchantment;
import com.dainxt.weaponthrow.enchantment.GravityEnchantment;
import com.dainxt.weaponthrow.enchantment.GroundedEdgeEnchantment;
import com.dainxt.weaponthrow.enchantment.ReturnEnchantment;
import com.dainxt.weaponthrow.enchantment.ThrowEnchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
public class EnchantmentHandler {

	public static final Enchantment THROW = new ThrowEnchantment(Enchantment.Rarity.RARE, EquipmentSlotType.MAINHAND);
	public static final Enchantment GROUNDEDEDGE = new GroundedEdgeEnchantment(Enchantment.Rarity.UNCOMMON, EquipmentSlotType.MAINHAND);
	public static final Enchantment CONCCUSION = new ConccusionEnchantment(Enchantment.Rarity.VERY_RARE, EquipmentSlotType.MAINHAND);
	public static final Enchantment GRAVITY = new GravityEnchantment(Enchantment.Rarity.VERY_RARE, EquipmentSlotType.MAINHAND);
	public static final Enchantment RETURN = new ReturnEnchantment(Enchantment.Rarity.RARE, EquipmentSlotType.MAINHAND);
	
	@SubscribeEvent
    public static void onEnchantmentRegistry(final RegistryEvent.Register<Enchantment> e) {
		e.getRegistry().register(THROW.setRegistryName("throw"));
		e.getRegistry().register(GROUNDEDEDGE.setRegistryName("groundededge"));
		e.getRegistry().register(CONCCUSION.setRegistryName("conccusion"));
		e.getRegistry().register(GRAVITY.setRegistryName("gravity"));
		e.getRegistry().register(RETURN.setRegistryName("return"));
	}
	
}
