package com.dainxt.weaponthrow.config;

import com.dainxt.weaponthrow.WeaponThrow;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = WeaponThrow.MODID)

public class WeaponThrowConfig implements ConfigData {

	@ConfigEntry.Category("general")
	@ConfigEntry.Gui.TransitiveObject
    public General general = new General();
	
	@ConfigEntry.Category("defaults")
	@ConfigEntry.Gui.TransitiveObject
    public Default defaults = new Default();
	
	@ConfigEntry.Category("enchantments")
	@ConfigEntry.Gui.TransitiveObject
    public Enchantments enchantments = new Enchantments();
	
	@ConfigEntry.Category("experimental")
	@ConfigEntry.Gui.TransitiveObject
    public Experimental experimental = new Experimental();
	
	@ConfigEntry.Category("interactions")
	@ConfigEntry.Gui.TransitiveObject
    public Interactions interactions = new Interactions();
	
	@ConfigEntry.Category("multipliers")
	@ConfigEntry.Gui.TransitiveObject
    public Multipliers multipliers = new Multipliers();
    
	@ConfigEntry.Category("times")
	@ConfigEntry.Gui.TransitiveObject
    public Times times = new Times();
	
	
	public static class General implements ConfigData {
		public boolean creativeSpamming = false;
		public boolean notUseWhenCooldown = false;
	}
	
	public static class Default implements ConfigData {
		public double baseDamageDefault = 1.0D;
		public double velocityDefault = 2.0D;
	}
	
	public static class Enchantments implements ConfigData {
		public boolean enchantAllWeapons = false;
		public boolean enableThrow = true;
		public boolean enableConccusion = true;
		public boolean enableGroundedEdge = true;
		public boolean enableGravity = true;
		public boolean enableReturn = true;
	}
	
	public static class Experimental implements ConfigData {
		public boolean shouldThrowItemsToo = false;
	}
	
	public static class Interactions implements ConfigData {
		public boolean canBreakBlocks = true;
	}
	
	public static class Multipliers implements ConfigData {
		
		@ConfigEntry.Gui.CollapsibleObject
	    public ToolMultipliers tools = new ToolMultipliers();
		
		public static class ToolMultipliers implements ConfigData {
			public double pickaxeMultiplier = 0.8D;
			public double axeMultiplier = 1.2D;
			public double swordMultiplier = 1.D;
			public double hoeMultiplier = 1.3D;
			public double shovelMultiplier = 0.9D;
		}
		
		@ConfigEntry.Gui.CollapsibleObject
	    public DamageMultipliers damages = new DamageMultipliers();
		
		public static class DamageMultipliers implements ConfigData {
			public double baseDamageMultiplier = 0.25D;
			public double stackDamageMultiplier = 0.0D;
			public double modifiedDamageMultiplier = 0.50D;
		}
		
		@ConfigEntry.Gui.CollapsibleObject
	    public VelocityMultipliers velocities = new VelocityMultipliers();
		
		public static class VelocityMultipliers implements ConfigData {
			public double baseVelocityMultiplier = 0.25D;
			public double stackVelocityMultiplier = 0.005D;
			public double modifiedVelocityMultiplier = 0.4D;
		}
		
		@ConfigEntry.Gui.CollapsibleObject
	    public ExhaustionMultipliers exhaustions = new ExhaustionMultipliers();
		
		public static class ExhaustionMultipliers implements ConfigData {
			public double baseExhaustionMultiplier = 0.075D;
			public double stackExhaustionMultiplier = 0.01D;
			public double modifiedExhaustionMultiplier = 2.0D;
		}

	}
    
	public static class Times implements ConfigData {
		@ConfigEntry.BoundedDiscrete(min=0,max=Integer.MAX_VALUE)
		public int despawnTime = 60*20;
		
		public double castTimeMuliplier = 3.0D;
		
		@ConfigEntry.BoundedDiscrete(min=0,max=Integer.MAX_VALUE)
		public int ticksUntilWeaponLoseOwner = 20*7;
	}

}

