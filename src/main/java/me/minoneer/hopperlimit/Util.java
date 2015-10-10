package me.minoneer.hopperlimit;

import com.worldcretornica.plotme_core.PlotMeCoreManager;
import com.worldcretornica.plotme_core.api.ILocation;
import com.worldcretornica.plotme_core.bukkit.PlotMe_CorePlugin;
import com.worldcretornica.plotme_core.bukkit.api.BukkitWorld;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class Util
{
    public static void printList(List<String> list)
    {
        for (String s : list)
        {
            System.out.print(s);
            System.out.print(";\t");
        }
        System.out.println("");
    }

    /**
     * Get all Entities between 2 Locations. You can specify the Entity Types.
     *
     * @param loc1  The min Location
     * @param loc2  The max Location
     * @param types A List of EntityTypes to look for. Put null for all Entities.
     * @return A List with all Entities between loc1 and loc2
     */
    public static List<Entity> getEntities(World world, ILocation loc1, ILocation loc2, List<EntityType> types)
    {
        Location bukkitLoc1 = new Location(world, loc1.getX(), loc1.getY(), loc1.getZ());
        Location bukkitLoc2 = new Location(world, loc2.getX(), loc2.getY(), loc2.getZ());
        return getEntities(bukkitLoc1, bukkitLoc2, types);
    }

    /**
     * Get all Entities between 2 Locations. You can specify the Entity Types.
     *
     * @param loc1  The min Location
     * @param loc2  The max Location
     * @param types A List of EntityTypes to look for. Put null for all Entities.
     * @return A List with all Entities between loc1 and loc2
     */
    public static List<Entity> getEntities(Location loc1, Location loc2, List<EntityType> types)
    {
        if (loc1.getWorld() != loc2.getWorld())
        {
            return null;
        } else
        {
            correctLocations(loc1, loc2);

            int xChunkMin = (int) Math.floor(loc1.getX() / 16.0);
            int xChunkMax = (int) Math.floor(loc2.getX() / 16.0);
            int zChunkMin = (int) Math.floor(loc1.getZ() / 16.0);
            int zChunkMax = (int) Math.floor(loc2.getZ() / 16.0);

            int chunkCount = Math.abs((xChunkMax - xChunkMin + 1) * (zChunkMax - zChunkMin * 1));

            List<Entity[]> entLists = new ArrayList<Entity[]>(chunkCount);

            int index = 0;
            int entCount = 0;

            for (int x = xChunkMin; x <= xChunkMax; x++)
            {
                for (int z = zChunkMin; z <= zChunkMax; z++)
                {
                    entLists.add(loc1.getWorld().getChunkAt(x, z).getEntities());
                    entCount += entLists.get(index++).length;
                }
            }

            List<Entity> entities = new ArrayList<Entity>(entCount / 2);

            for (Entity[] ents : entLists)
            {
                for (Entity ent : ents)
                {
                    Location loc = ent.getLocation();

                    if ((types == null || types.contains(ent.getType())) &&
                        loc.getX() >= loc1.getX() && loc.getX() < loc2.getX() + 1 &&
                        loc.getY() >= loc1.getY() && loc.getY() < loc2.getY() + 1 &&
                        loc.getZ() >= loc1.getZ() && loc.getZ() < loc2.getZ() + 1)
                    {
                        entities.add(ent);
                    }
                }
            }

            return entities;
        }
    }

    /**
     * corrects the locations, so loc1 is the min and loc2 ist the max
     *
     * @param loc1
     * @param loc2
     */
    public static void correctLocations(Location loc1, Location loc2)
    {
        if (loc1.getX() > loc2.getX())
        {
            double temp = loc1.getX();
            loc1.setX(loc2.getX());
            loc2.setX(temp);
        }

        if (loc1.getY() > loc2.getY())
        {
            double temp = loc1.getY();
            loc1.setY(loc2.getY());
            loc2.setY(temp);
        }

        if (loc1.getZ() > loc2.getZ())
        {
            double temp = loc1.getZ();
            loc1.setZ(loc2.getZ());
            loc2.setZ(temp);
        }
    }

    public static boolean isPlotWorld(World world)
    {
        Plugin plugin = Bukkit.getPluginManager().getPlugin("PlotMe");

        if (plugin instanceof PlotMe_CorePlugin)
        {
            return PlotMeCoreManager.getInstance().isPlotWorld(new BukkitWorld(world));
        } else
        {
            return false;
        }
    }
}
