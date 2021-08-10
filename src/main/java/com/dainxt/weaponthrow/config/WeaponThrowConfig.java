package com.dainxt.weaponthrow.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber
public class WeaponThrowConfig {

    public static class Server {

    	public final ForgeConfigSpec.BooleanValue notUseWhenCooldown;
    	public final ForgeConfigSpec.BooleanValue creativeSpamming;

    	public final ForgeConfigSpec.BooleanValue classicRender;
    	public final ForgeConfigSpec.IntValue offsetY;
    	public final ForgeConfigSpec.IntValue offsetX;
    	
    	public final ForgeConfigSpec.DoubleValue baseDamageDefault;
    	public final ForgeConfigSpec.DoubleValue baseVelocityDefault;
    	
    	public final ForgeConfigSpec.DoubleValue pickaxeMultiplier;
    	public final ForgeConfigSpec.DoubleValue axeMultiplier;
    	public final ForgeConfigSpec.DoubleValue swordMultiplier;
    	public final ForgeConfigSpec.DoubleValue hoeMultiplier;
    	public final ForgeConfigSpec.DoubleValue shovelMultiplier;
    	
    	public final ForgeConfigSpec.DoubleValue baseDamageMultiplier;
    	public final ForgeConfigSpec.DoubleValue stackDamageMultiplier;
    	public final ForgeConfigSpec.DoubleValue modifiedDamageMultiplier;
    	
    	public final ForgeConfigSpec.DoubleValue baseVelocityMultiplier;
    	public final ForgeConfigSpec.DoubleValue stackVelocityMultiplier;
    	public final ForgeConfigSpec.DoubleValue modifiedVelocityMultiplier;
    	
    	public final ForgeConfigSpec.DoubleValue baseExhaustionMultiplier;
    	public final ForgeConfigSpec.DoubleValue stackExhaustionMultiplier;
    	public final ForgeConfigSpec.DoubleValue modifiedExhaustionMultiplier;
    	
    	public final ForgeConfigSpec.BooleanValue enablePMMOIntegration;
    	public final ForgeConfigSpec.IntValue requiredCombatLoyalty;
    	
    	public final ForgeConfigSpec.BooleanValue enableItemPhysicsFix;
    	public final ForgeConfigSpec.BooleanValue canBreakBlocks;
    	public final ForgeConfigSpec.BooleanValue shouldThrowItemsToo;
    	
    	public final ForgeConfigSpec.BooleanValue enchantAll;
    	public final ForgeConfigSpec.BooleanValue throwEnchant;
    	public final ForgeConfigSpec.BooleanValue conccusionEnchant;
    	public final ForgeConfigSpec.BooleanValue groundedEdgeEnchant;
    	public final ForgeConfigSpec.BooleanValue gravityEnchant;
    	public final ForgeConfigSpec.BooleanValue gravityDrop;
    	public final ForgeConfigSpec.BooleanValue returnEnchant;
    	
    	public final ForgeConfigSpec.IntValue despawnTime;
    	public final ForgeConfigSpec.IntValue castTimeInTicks;
    	public final ForgeConfigSpec.IntValue ticksUntilWeaponLoseOwner;
    	
    	public final WhiteListedItems whiteList;
    	public final BlackListedBlocks blackList;
    	
    	
        Server(ForgeConfigSpec.Builder builder) {
            builder.comment("Weapon Throw Mod Configuration Settings").push("general");
            	notUseWhenCooldown = builder.comment("Weapon Cant be thrown if it is on cooldown").define("Not Throw If Cooldown", true);
            	creativeSpamming = builder.comment("Weapons does not remove on creative").define("Creative Spamming", false);
            builder.pop();
            
            builder.comment("Client Settings").push("client");
            	classicRender = builder.comment("If you got rendering problems, try enabling the old rendering").define("Use classic render", false);
            	offsetY = builder.comment("Charging Y Hud Offset").defineInRange("Ycoord",   20, -32, 32);
            	offsetX = builder.comment("Charging X Hud Offset").defineInRange("Xcoord", - 8, -32, 32);
        	builder.pop();
        	
        	builder.comment("If an item is whitelisted and doesn't have damage or attack speed, this values will be assigned").push("default");
     			baseDamageDefault = builder.defineInRange("Default Base Damage", 1.0D, 0, 256.D);
     			baseVelocityDefault = builder.defineInRange("Default Base Velocity", 0.5D, 0, 256.D);
     		builder.pop();

            builder.push("enchantments");
            	enchantAll = builder.comment("If this is enabled, all tools can be applied mod enchantments too").define("Enchant All", false);
	        	throwEnchant = builder.comment("Should Throw Enchantment be applied on survival?").define("Enable Throw Enchantment", true);
	        	conccusionEnchant = builder.comment("Should Conccusion Enchantment be applied on survival?").define("Enable Conccusion Enchantment", true);
	        	groundedEdgeEnchant = builder.comment("Should Grounded Edge Enchantment be applied on survival?").define("Enable Grounded Edge Enchantment", true);
	        	gravityEnchant = builder.comment("Should Gravity Enchantment be applied on survival?").define("Enable Gravity Enchantment", true);
	        	gravityDrop = builder.comment("Should gravity curse drop after some time?").define("Gravity Drop", false);
	        	returnEnchant = builder.comment("Should Return Enchantment be applied on survival?").define("Enable Return Enchantment", true);
        	builder.pop();
        	
            builder.push("experimental");
        		shouldThrowItemsToo = builder.comment("Enable using all items as a weapon").define("Throw all items", false);
        	builder.pop();

            builder.push("fixes");
        		enableItemPhysicsFix = builder.comment("Disable this if theres tossing bugs").define("Item Physics Fix", true);
        	builder.pop();
        	
            builder.push("interactions");
        		canBreakBlocks = builder.comment("Throwing can break blocks at landing?").define("Break Blocks", true);
        	builder.pop();
        	
            builder.push("multipliers");
            	pickaxeMultiplier = builder.defineInRange("Pickaxe Multiplier", 0.2D, 0.0D, 256.D);
            	axeMultiplier = builder.defineInRange("Axe Multiplier", 1.2D, 0.0D, 256.D);
            	swordMultiplier = builder.defineInRange("Sword Multiplier", 1.D, 0.0D, 256.D);
            	hoeMultiplier = builder.defineInRange("Hoe Multiplier", 0.5D, 0.0D, 256.D);
            	shovelMultiplier = builder.defineInRange("Shovel Multiplier", 0.7D, 0.0D, 256.D);
            
            	baseDamageMultiplier = builder.defineInRange("Base Damage Multiplier", 0.25D, 0, 256.D);
            	stackDamageMultiplier = builder.defineInRange("Stack Size Damage Multiplier", 0.0D, 0, 256.D);
            	modifiedDamageMultiplier = builder.defineInRange("Modified Damage Multiplier", 0.50D, 0, 256.D);
            	
            	baseVelocityMultiplier = builder.defineInRange("Base Velocity Multiplier", 0.25D, 0, 64.D);
            	stackVelocityMultiplier = builder.defineInRange("Stack Size Velocity Multiplier", 0.005D, 0, 64.D);
            	modifiedVelocityMultiplier = builder.defineInRange("Modified Velocity Multiplier", 0.4D, 0, 64.D);
            	
            	baseExhaustionMultiplier = builder.defineInRange("Base Exhaustion Multiplier", 0.075D, 0, 256.D);
            	stackExhaustionMultiplier = builder.defineInRange("Stack Size Exhaustion Multiplier", 0.01D, 0, 256.D);
            	modifiedExhaustionMultiplier = builder.defineInRange("Modified Exhaustion Multiplier", 2.0D, 0, 256.D);
        	builder.pop();
        	
        	builder.comment("Integration with Project MMO").push("pmmo");
        		enablePMMOIntegration = builder.comment("Disable this if running Minecraft 1.16.3 and below").define("Enable PMMO Integration", true);
        		requiredCombatLoyalty = builder.comment("Minimum Level Required to use Loyalty, 0 is none").defineInRange("Loyalty Requirement", 10, 0, Integer.MAX_VALUE); 
        	builder.pop();
        	
            builder.push("time");
            	despawnTime = builder.comment("Time in ticks when the throwed items should dissapear").defineInRange("Despawn Time", 60*20, 0, Integer.MAX_VALUE);
            	castTimeInTicks = builder.comment("Attack Speed Multiplier to charging").defineInRange("Charging Multiplier", 3, 0, Integer.MAX_VALUE);
            	ticksUntilWeaponLoseOwner = builder.comment("Time in ticks when throwed items lose their owner").defineInRange("Lose Owner Time", 20*7, 0, Integer.MAX_VALUE);
        	builder.pop();
        	
            builder.comment("All Items Whitelisted will get default values").push("whitelist-items");
        		whiteList = new WhiteListedItems(builder);
        	builder.pop();
        	
            builder.comment("All Blacklisted blocks cannot be destroyed").push("blacklist-blocks");
        		blackList = new BlackListedBlocks(builder);
        	builder.pop();

        }
        
        public class WhiteListedItems{
        	public final List<String> DEFAULT_WHITELIST = Arrays.asList(Items.BRICK.getRegistryName().toString(), Items.GLASS.getRegistryName().toString());
        	
        	public ForgeConfigSpec.ConfigValue<List<? extends String>> whiteList;
        	
        	WhiteListedItems(ForgeConfigSpec.Builder builder){
        		whiteList = builder.defineList("Whitelisted items", DEFAULT_WHITELIST, o -> o instanceof String);
        	}
        	
        	public List<Item> get() {
        		List<Item> items = new ArrayList<Item>();
        		for(String location: whiteList.get()) {
        			ResourceLocation resourceLocation = new ResourceLocation(location);
        			if(ForgeRegistries.ITEMS.containsKey(resourceLocation)) {
        				items.add(ForgeRegistries.ITEMS.getValue(resourceLocation));
        			}
        		}
				return items;
        	}
        }
        
        public class BlackListedBlocks{
        	public final List<String> DEFAULT_BLACKLIST = Arrays.asList(Items.BEDROCK.getRegistryName().toString());
        	
        	public ForgeConfigSpec.ConfigValue<List<? extends String>> blackList;
        	
        	BlackListedBlocks(ForgeConfigSpec.Builder builder){
        		blackList = builder.defineList("Blacklisted blocks", DEFAULT_BLACKLIST, o -> o instanceof String);
        	}
        	
        	public List<Block> get() {
        		List<Block> items = new ArrayList<Block>();
        		for(String location: blackList.get()) {
        			ResourceLocation resourceLocation = new ResourceLocation(location);
        			if(ForgeRegistries.BLOCKS.containsKey(resourceLocation)) {
        				items.add(ForgeRegistries.BLOCKS.getValue(resourceLocation));
        			}
        		}
				return items;
        	}
        }
    }
	
    public static final ForgeConfigSpec commonSpec;
    public static final Server COMMON;
    static {
        final Pair<Server, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Server::new);
        commonSpec = specPair.getRight();
        COMMON = specPair.getLeft();
    }
    
    @SubscribeEvent
    public static void onLoad(final ModConfig.Loading configEvent) {

    }

    @SubscribeEvent
    public static void onFileChange(final ModConfig.Reloading configEvent) {

    }

}
