package me.minoneer.hopperlimit;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.vehicle.VehicleCreateEvent;

public class HopperPlaceListener implements Listener {
    private HopperLimit plugin;

    HopperPlaceListener(HopperLimit plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onHopperPlace(BlockPlaceEvent e) {
        Player player = e.getPlayer();

        if (e.getBlock().getType() == Material.HOPPER && !player.hasPermission(HopperLimit.place)) {

            if (plugin.getAndSetCooldown(player.getName(), plugin.config.getCooldownPlace())) {
                player.sendMessage(plugin.config.getCooldownMessage()
                        .replaceAll("%cooldown%", String.valueOf(plugin.config.getCooldownPlace())));
                e.setCancelled(true);
                return;
            }

            int limit = plugin.config.getPlaceLimit();
            int radius = plugin.config.getPlaceRadius();
            int hopperCount = plugin.getCount().countHopper(player, radius, limit + 1);


            if (hopperCount > limit) {

                e.setCancelled(true);
                player.sendMessage(plugin.config.getDenyMessage()
                        .replaceAll("%limit%", String.valueOf(limit).replaceAll("%radius%", String.valueOf(radius))));
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onMinecartPlace(VehicleCreateEvent e) {
        Vehicle vehicle = e.getVehicle();

        if (vehicle.getType() == EntityType.MINECART_HOPPER) {
            int limit = plugin.config.getPlaceLimit();
            int radius = plugin.config.getPlaceRadius();
            int hopperCount = plugin.getCount().countHopper(vehicle, radius, limit);

            if (hopperCount >= limit) {
                vehicle.remove();
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (event.getClickedBlock() != null) {
                Material type = event.getClickedBlock().getType();
                if (type == Material.RAILS || type == Material.ACTIVATOR_RAIL || type == Material.POWERED_RAIL || type == Material.DETECTOR_RAIL) {
                    Player p = event.getPlayer();

                    if (event.getItem() != null && event.getItem().getType() == Material.HOPPER_MINECART &&
                            !p.hasPermission(HopperLimit.place)) {
                        int limit = plugin.config.getPlaceLimit();
                        int radius = plugin.config.getPlaceRadius();
                        int hopperCount = plugin.getCount().countHopper(p, radius, limit);

                        if (hopperCount >= limit) {
                            event.setCancelled(true);
                            p.sendMessage(plugin.config.getDenyMessage().replaceAll("%limit%",
                                    String.valueOf(limit).replaceAll("%radius%", String.valueOf(radius))));
                        }
                    }
                }
            }
        }
    }
}
