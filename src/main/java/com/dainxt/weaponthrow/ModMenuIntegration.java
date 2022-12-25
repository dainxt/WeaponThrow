package com.dainxt.weaponthrow;

import com.dainxt.weaponthrow.config.WeaponThrowConfig;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

import me.shedaniel.autoconfig.AutoConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)	
public class ModMenuIntegration implements ModMenuApi{
	
	@Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> AutoConfig.getConfigScreen(WeaponThrowConfig.class, parent).get();
    }
	
}
