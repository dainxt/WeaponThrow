package com.dainxt.weaponthrow.handlers;

import com.dainxt.weaponthrow.projectile.WeaponThrowEntity;
import com.dainxt.weaponthrow.util.Reference;

import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
public class EntityHandler {

	public static final EntityType<WeaponThrowEntity> WEAPONTHROW = EntityType.Builder.<WeaponThrowEntity>create(WeaponThrowEntity::new, EntityClassification.MISC).size(0.5F, 0.5F).func_233606_a_(4).func_233608_b_(20).build(Reference.MODID + ":weaponthrow");
	
	@SubscribeEvent
    public static void onEntityRegistry(final RegistryEvent.Register<EntityType<?>> e) {
		e.getRegistry().register(WEAPONTHROW.setRegistryName("weaponthrow"));
	}
	
}
