package com.dainxt.weaponthrow;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.dainxt.weaponthrow.capabilities.ThrowCapability;
import com.dainxt.weaponthrow.capabilities.ThrowPower;
import com.dainxt.weaponthrow.config.WeaponThrowConfig;
import com.dainxt.weaponthrow.entity.render.WeaponThrowRenderer;
import com.dainxt.weaponthrow.handlers.EntityHandler;
import com.dainxt.weaponthrow.handlers.EventsHandler;
import com.dainxt.weaponthrow.handlers.KeyBindingHandler;
import com.dainxt.weaponthrow.handlers.PacketHandler;
import com.dainxt.weaponthrow.interfaces.IThrowPower;
import com.dainxt.weaponthrow.util.Reference;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Reference.MODID)
public class WeaponThrow
{
	
    public static final Logger LOGGER = LogManager.getLogger();

    public WeaponThrow() {
    	
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onGatherData);
        
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, WeaponThrowConfig.commonSpec);
        FMLJavaModLoadingContext.get().getModEventBus().register(WeaponThrowConfig.class);
        
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new EventsHandler());
        
    }

    private void setup(final FMLCommonSetupEvent event)
    {
    	CapabilityManager.INSTANCE.register(IThrowPower.class, new ThrowCapability<ThrowPower>(), ()->{
    		return new ThrowPower();
    	});
    	PacketHandler.init();
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
    	ClientRegistry.registerKeyBinding(KeyBindingHandler.KEYBINDING);
    	RenderingRegistry.registerEntityRenderingHandler(EntityHandler.WEAPONTHROW, new WeaponThrowRenderer.RenderFactory());
    }

    private void enqueueIMC(final InterModEnqueueEvent event)
    {
        
    }

    private void processIMC(final InterModProcessEvent event)
    {
        
    }

    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
        
    }
    
    public void onGatherData(final GatherDataEvent event) {

    	
    }

}
