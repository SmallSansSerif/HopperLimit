package me.minoneer.hopperlimit;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Commands implements CommandExecutor
{
	private int id = -1;

	private HopperLimit plugin;

	public Commands (HopperLimit plugin)
	{
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lable, String[] args)
	{
		if (cmd.getName().equalsIgnoreCase("hopper"))
		{
			if (args.length == 0 && sender.hasPermission(HopperLimit.count) && sender instanceof Player)
			{
				Player p = (Player)sender;

				if (plugin.getAndSetCooldown(p.getName(), plugin.config.getCooldownCount()))
				{
					p.sendMessage(plugin.config.getCooldownMessage().replaceAll("%cooldown%", String.valueOf(plugin.config.getCooldownCount())));
					return true;
				}

				int limit = plugin.config.getPlaceLimit();
				int radius = plugin.config.getPlaceRadius();
				int hopperCount = plugin.getCount().countHopper(p, radius, -1);

				p.sendMessage(plugin.config.getCountMessage().replaceAll("%radius%", String.valueOf(radius))
						.replaceAll("%limit%", String.valueOf(limit)).replaceAll("%count%", String.valueOf(hopperCount)));
				return true;
			} else if (args.length == 1 && args[0].equalsIgnoreCase("search") && sender.hasPermission(HopperLimit.search))
			{
				if (id != -1)
				{
					sender.sendMessage(ChatColor.GREEN + "Already scanning the server. Please wait for it to finish.");
					return true;
				} else
				{
					sender.sendMessage(ChatColor.GREEN + "Scanning Player locations for Hoppers...");
					sender.sendMessage(ChatColor.GREEN + "This operation may take a few minutes.");

					plugin.aboveLimit = new ArrayList<LocationValue>();

					final List<String> player = new ArrayList<String>(plugin.getServer().getOnlinePlayers().size());

					for (Player p : plugin.getServer().getOnlinePlayers())
					{
						player.add(p.getName());
					}

					final String name = sender.getName();
					id = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable()
					{
						@Override
						public void run()
						{
							if (player == null || player.isEmpty())
							{
								CommandSender s = Bukkit.getServer().getPlayerExact(name);

								if (s == null) {
									s = Bukkit.getServer().getConsoleSender();
								}
								s.sendMessage(ChatColor.GREEN + "Scan complete. Type /hopper list to display results.");

								int oldId = id;
								id = -1;

								Bukkit.getScheduler().cancelTask(oldId);
							} else
							{
								Player p = Bukkit.getServer().getPlayerExact(player.get(0));
								player.remove(0);

								if (p != null)
								{
									int radius = plugin.config.getSearchRadius();
									int limit = plugin.config.getSearchLimit();
									int hopperCount = plugin.getCount().countHopper(p, radius, -1);

									if (hopperCount >= limit)
									{
										plugin.aboveLimit.add(new LocationValue(p.getLocation(), hopperCount));
									}
								}
							}
						}
					}, 1L, 20L);

					return true;
				}
			} else if ((args.length == 1 || args.length == 2) && args[0].equalsIgnoreCase("list") && sender.hasPermission(HopperLimit.search))
			{
				if (plugin.aboveLimit == null)
				{
					sender.sendMessage(ChatColor.GREEN + "No search results found. Please type /hopper search to start a server wide scan.");
					return true;
				}

				int page = 0;

				if (args.length == 2)
				{
					try
					{
						page = Integer.parseInt(args[1]) - 1;
					} catch(NumberFormatException e) {}
				}

				if (page < 0)
				{
					page = 0;
				}

				sender.sendMessage(ChatColor.GREEN + "Hopper Search Results page " + (page + 1) + " of " + (int)Math.ceil((double)plugin.aboveLimit.size()/10.0));

				for (int i = 0; i < 10 && (page*10)+i < plugin.aboveLimit.size(); i++)
				{
					int j = (page*10)+i;
					LocationValue lv = plugin.aboveLimit.get(j);
					sender.sendMessage(ChatColor.YELLOW.toString() + (j+1) + ". " + lv.getLoc().getWorld().getName() + "|"+ 
							lv.getLoc().getBlockX() + "|" + lv.getLoc().getBlockY() + "|" + lv.getLoc().getBlockZ() + ":  " + lv.getValue());
				}

				return true;
			} else if (args.length == 2 && args[0].equalsIgnoreCase("tp") && sender instanceof Player && sender.hasPermission(HopperLimit.search))
			{
				Player p = (Player)sender;

				int i = -1;
				try
				{
					i = Integer.parseInt(args[1]);
				} catch (NumberFormatException e)
				{
					p.sendMessage(ChatColor.GREEN + "The id you inserted is not a valid number.");
					return true;
				} if (i > 0 && i <= plugin.aboveLimit.size())
				{
					p.teleport(plugin.aboveLimit.get(i-1).getLoc());
					p.sendMessage(ChatColor.GREEN + "Teleportet to result number " + i);
					return true;
				} else
				{
					p.sendMessage(ChatColor.GREEN + "The id you selected ist out of range. Please specifiy a valid one.");
					return true;
				}
			}
		}
		return false;
	}
}