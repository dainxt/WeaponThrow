package com.dainxt.weaponthrow.events;

import com.dainxt.weaponthrow.projectile.WeaponThrowEntity;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.Cancelable;

/**
 * Event for manipulate WeaponThrow throwing. Every operation is server-sided.
 */
public class WeaponThrowEvent extends PlayerEvent {

	ItemStack throwedStack;
	
	public WeaponThrowEvent(ItemStack stack, PlayerEntity player) {
		super(player);
		this.throwedStack = stack;
	}
	
	/**
	 * Event is fired when the player is about to charge the throwing,
	 * if it is cancelled the player cannot throw the item.
	 */
    @Cancelable
    public static class TestThrow extends WeaponThrowEvent
    {
        public TestThrow(ItemStack stack, PlayerEntity player)
        {
            super(stack, player);
        }
    }
    
	/**
	 * Event is fired when the player threw an item,
	 * attributes like Damage, Velocity, Exhaustion can be changed.
	 */
    public static class OnThrow extends WeaponThrowEvent
    {

    	public double totalDamage;
    	public double totalVelocity;
    	public double totalExhaustion;
    	
        public OnThrow(ItemStack stack, PlayerEntity player, double damage, double velocity, double exhaustion)
        {
            super(stack, player);
            this.totalDamage = damage;
            this.totalVelocity = velocity;
            this.totalExhaustion = exhaustion;
        }

    }
    
	/**
	 * Event is fired when item impact an entity or an block, when cancelled
	 * the default action doesn't performs
	 */
    @Cancelable
    public static class OnImpact extends WeaponThrowEvent
    {
    	WeaponThrowEntity entity;
    	RayTraceResult rayTrace;
    	
        public OnImpact( WeaponThrowEntity item, PlayerEntity player, RayTraceResult result)
        {
            super(item.getItemStack(), player);
            this.entity = item;
            this.rayTrace = result;
        }
        
        public WeaponThrowEntity getItemThrowed(){
        	return entity;
        }
        
        public RayTraceResult getRayTraceResult(){
        	return this.rayTrace;
        }
    }
	
    public ItemStack getThrowedStack(){
		return throwedStack;
    }
}
