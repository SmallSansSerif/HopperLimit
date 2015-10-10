package me.minoneer.hopperlimit;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import com.worldcretornica.plotme_core.PlotId;
import com.worldcretornica.plotme_core.PlotMeCoreManager;
import com.worldcretornica.plotme_core.api.ILocation;
import com.worldcretornica.plotme_core.bukkit.api.BukkitLocation;
import com.worldcretornica.plotme_core.bukkit.api.BukkitWorld;

public class CountHopperPlotMe extends CountHopper
{	
	private static List<EntityType> types = new ArrayList<EntityType>(1);

	static
	{
		types.add(EntityType.MINECART_HOPPER);
	}

	private CountHopper defaultCount = new CountHopperDefault();

	@Override
	/**
	 * Searches a radius for hopper and hopper minecarts.
	 * 
	 * @param entity the Entity around which the hoppers should be counted
	 * @param radius the horizontal radius in which hoppers are counted
	 * @param limit the limit after which the search is cancelled. -1 for unlimited
	 * @return the found hopper, or the limit (whichever is smaller)
	 */

	public int countHopper(Entity ent, int radius, int limit)
	{
		if (Util.isPlotWorld(ent.getLocation().getWorld()))
		{
			return countHopperPlot(ent, radius, limit);
		}

		else
		{
			return defaultCount.countHopper(ent, radius, limit);
		}
	}


	private int countHopperPlot(Entity ent, int radius, int limit)
	{
		PlotId plotId = PlotMeCoreManager.getInstance().getPlotId(new BukkitLocation(ent.getLocation()));

		if (plotId == null)
		{
			return 0;
		} else
		{
			int hopperCount = 0;

			BukkitWorld world = new BukkitWorld(ent.getWorld());

			ILocation loc1 = PlotMeCoreManager.getInstance().getPlotBottomLoc(world, plotId);
			ILocation loc2 = PlotMeCoreManager.getInstance().getPlotTopLoc(world, plotId);

			for (int i = loc1.getBlockX(); i <= loc2.getBlockX(); i++)
			{
				for (int k = loc1.getBlockZ(); k <= loc2.getBlockZ(); k++)
				{
					for (int j = world.getWorld().getMaxHeight(); j >= 0; j--)
					{
						if (world.getWorld().getBlockAt(i, j, k).getType() == Material.HOPPER)
						{
							++ hopperCount;

							if (limit > -1 && hopperCount > limit)
							{
								return limit;
							}
						}
					}
				}
			}

			List<Entity> carts = Util.getEntities(world.getWorld(), loc1, loc2, types);

			hopperCount += carts.size();

			if (limit > -1 && hopperCount > limit)
			{
				return limit;
			}

			else
			{
				return hopperCount;
			}
		}
	}
}