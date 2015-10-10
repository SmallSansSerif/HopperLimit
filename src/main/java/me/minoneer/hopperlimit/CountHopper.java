package me.minoneer.hopperlimit;

import org.bukkit.entity.Entity;

public abstract class CountHopper {
	
	/**
	 * Searches a radius for hopper and hopper minecarts.
	 * 
	 * @param entity the Entity around which the hoppers should be counted
	 * @param radius the horizontal radius in which hoppers are counted
	 * @param limit the limit after which the search is cancelled. -1 for unlimited
	 * @return the found hopper, or the limit (whichever is smaller)
	 */
	public abstract int countHopper(Entity entity, int radius, int limit);
}
