package me.minoneer.hopperlimit;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import java.util.List;

public class CountHopperDefault extends CountHopper
{
    @Override
    /**
     * Searches a radius for hopper and hopper minecarts.
     *
     * @param entity the Entity around which the hoppers should be counted
     * @param radius the horizontal radius in which hoppers are counted
     * @param limit the limit after which the search is cancelled. -1 for unlimited
     * @return the found hopper, or the limit (whichever is smaller)
     */
    public int countHopper(Entity entity, int radius, int limit)
    {
        int hopperCount = 0;

        int x = entity.getLocation().getBlockX();
        int z = entity.getLocation().getBlockZ();

        World world = entity.getWorld();

        for (int i = x - radius; i <= x + radius; i++)
        {
            for (int k = z - radius; k <= z + radius; k++)
            {
                for (int j = 255; j >= 0; j--)
                {

                    if (world.getBlockAt(i, j, k).getType() == Material.HOPPER)
                    {

                        ++hopperCount;

                        if (limit > -1 && hopperCount > limit)
                        {

                            return limit;
                        }
                    }
                }
            }
        }

        List<Entity> entities = entity.getNearbyEntities(radius, 256, radius);

        for (Entity ent : entities)
        {

            if (ent.getType() == EntityType.MINECART_HOPPER)
            {

                ++hopperCount;

                if (limit > -1 && hopperCount >= limit)
                {

                    return limit;
                }
            }
        }

        return hopperCount;
    }
}
