package me.minoneer.hopperlimit;

import com.worldcretornica.plotme_core.PlotMeCoreManager;
import com.worldcretornica.plotme_core.api.ILocation;
import com.worldcretornica.plotme_core.bukkit.api.BukkitLocation;
import com.worldcretornica.plotme_core.bukkit.api.BukkitWorld;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import java.util.ArrayList;
import java.util.List;

public class CountHopperPlotMe extends CountHopper {
    private static List<EntityType> types = new ArrayList<>(1);

    static {
        types.add(EntityType.MINECART_HOPPER);
    }

    private CountHopper defaultCount = new CountHopperDefault();

    /**
     * Searches a radius for hopper and hopper minecarts.
     *
     * @param entity the Entity around which the hoppers should be counted
     * @param radius the horizontal radius in which hoppers are counted
     * @param limit  the limit after which the search is cancelled. -1 for unlimited
     * @return the found hopper, or the limit (whichever is smaller)
     */
    @Override
    public int countHopper(Entity entity, int radius, int limit) {
        if (Util.isPlotWorld(entity.getLocation().getWorld())) {
            return countHopperPlot(entity, limit);
        } else {
            return defaultCount.countHopper(entity, radius, limit);
        }
    }


    private int countHopperPlot(Entity ent, int limit) {
        String plotId = PlotMeCoreManager.getInstance().getPlotId(new BukkitLocation(ent.getLocation()));

        if (plotId == null || plotId.isEmpty()) {
            return 0;
        } else {
            int hopperCount = 0;

            BukkitWorld world = new BukkitWorld(ent.getWorld());

            ILocation loc1 = PlotMeCoreManager.getInstance().getPlotBottomLoc(world, plotId);
            ILocation loc2 = PlotMeCoreManager.getInstance().getPlotTopLoc(world, plotId);

            for (int i = loc1.getBlockX(); i <= loc2.getBlockX(); i++) {
                for (int k = loc1.getBlockZ(); k <= loc2.getBlockZ(); k++) {
                    for (int j = world.getWorld().getMaxHeight(); j >= 0; j--) {
                        if (world.getWorld().getBlockAt(i, j, k).getType() == Material.HOPPER) {
                            ++hopperCount;

                            if (limit > -1 && hopperCount > limit) {
                                return limit;
                            }
                        }
                    }
                }
            }

            List<Entity> carts = Util.getEntities(world.getWorld(), loc1, loc2, types);

            hopperCount += carts.size();

            if (limit > -1 && hopperCount > limit) {
                return limit;
            } else {
                return hopperCount;
            }
        }
    }
}
