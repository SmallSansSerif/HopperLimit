package me.minoneer.hopperlimit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import com.worldcretornica.plotme_core.bukkit.PlotMe_CorePlugin;

public class HopperLimit extends JavaPlugin
{
	public static final Permission place = new Permission("hopperlimit.place");
	public static final Permission search = new Permission("hopperlimit.search");
	public static final Permission count = new Permission("hopperlimit.count");

	public ArrayList<LocationValue> aboveLimit;
	private HashMap<String, Long> cooldown = new HashMap<String, Long>();
	private CountHopper countHopper;

	@Override
	public void onDisable()
	{
		Bukkit.getScheduler().cancelTasks(this);

		PluginDescriptionFile desc = this.getDescription();
		getLogger().log(Level.INFO, "{0} vers. {1} by minoneer deactivated", new String[]{desc.getName(), desc.getVersion()});
	}

	@Override
	public void onEnable()
	{
		loadConfig();

		registerCommands();

		registerEvents();

		Plugin plotMe = Bukkit.getServer().getPluginManager().getPlugin("PlotMe");

		if (plotMe != null && plotMe instanceof PlotMe_CorePlugin)
		{
			countHopper = new CountHopperPlotMe();
			getLogger().log(Level.INFO, "PlotMe instance found - will use PlotMe-hook.");
		}
		else
		{
			countHopper = new CountHopperDefault();
			getLogger().log(Level.INFO, "No PlotMe instance found - will use default count for all worlds.");
		}

		PluginDescriptionFile desc = this.getDescription();
		getLogger().log(Level.INFO, "{0} vers. {1} by minoneer activated", new String[]{desc.getName(), desc.getVersion()});
	}

	public ConfigHandler config;

	private void loadConfig()
	{
		config = new ConfigHandler(this);
	}

	private void registerCommands()
	{
		this.getCommand("hopper").setExecutor(new Commands(this));
	}

	private void registerEvents()
	{
		new HopperPlaceListener(this);
	}

	/**
	 * This method returns weather the player has cooldown or not. If he does not, it starts the cooldown timer
	 * @param player the player name
	 * @param seconds the cooldown in seconds
	 * @return weather the player has cooldown or not
	 */
	public boolean getAndSetCooldown(String player, int seconds)
	{
		long now = System.currentTimeMillis();
		seconds *= 1000;

		if (cooldown.containsKey(player))
		{
			if (cooldown.get(player) + seconds > now)
			{
				return true;
			}
		}

		cooldown.put(player, now);
		return false;
	}

	public CountHopper getCount()
	{
		return countHopper;
	}
}