package me.minoneer.hopperlimit;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

public class ConfigHandler
{
    public ConfigHandler(HopperLimit plugin)
    {
        this.plugin = plugin;
        this.config = plugin.getConfig();
        loadConfig();
    }

    private HopperLimit plugin;
    private FileConfiguration config;

    private int placeRadius;
    private int placeLimit;
    private int searchRadius;
    private int searchLimit;
    private int cooldownPlace;
    private int cooldownCount;

    private String denyMessage;
    private String countMessage;
    private String cooldownMessage;

    private static final String PLACE_RADIUS = "place.radius";
    private static final String PLACE_LIMIT = "place.limit";
    private static final String SEARCH_RADIUS = "search.radius";
    private static final String SEARCH_LIMIT = "search.limit";
    private static final String COOLDOWN_PLACE = "cooldown.place";
    private static final String COOLDOWN_COUNT = "cooldown.count";

    private static final String DENY_MESSAGE = "message.deny";
    private static final String COUNT_MESSAGE = "message.count";
    private static final String COOLDOWN_MESSAGE = "message.cooldown";


    public void reloadConfig()
    {
        this.plugin.reloadConfig();
        this.config = plugin.getConfig();
        loadConfig();
    }

    private void loadConfig()
    {

        this.config.addDefault(PLACE_RADIUS, 40);
        this.config.addDefault(PLACE_LIMIT, 50);
        this.config.addDefault(SEARCH_RADIUS, 100);
        this.config.addDefault(SEARCH_LIMIT, 60);
        this.config.addDefault(COOLDOWN_PLACE, 5);
        this.config.addDefault(COOLDOWN_COUNT, 10);
        this.config.addDefault(DENY_MESSAGE, "&8There are already more than %limit% hopper in this area. You cannot place new ones.");
        this.config.addDefault(COUNT_MESSAGE, "&8There are %count% hopper within %radius% blocks of your location.");
        this.config.addDefault(COOLDOWN_MESSAGE, "&8%cooldown% sec cooldown active. Please wait");

        this.config.options().copyDefaults(true);
        plugin.saveConfig();

        this.placeRadius = this.config.getInt(PLACE_RADIUS);
        this.placeLimit = this.config.getInt(PLACE_LIMIT);
        this.searchRadius = this.config.getInt(SEARCH_RADIUS);
        this.searchLimit = this.config.getInt(SEARCH_LIMIT);
        this.cooldownPlace = this.config.getInt(COOLDOWN_PLACE);
        this.cooldownCount = this.config.getInt(COOLDOWN_COUNT);

        this.denyMessage = ChatColor.translateAlternateColorCodes('&', this.config.getString(DENY_MESSAGE));
        this.countMessage = ChatColor.translateAlternateColorCodes('&', this.config.getString(COUNT_MESSAGE));
        this.cooldownMessage = ChatColor.translateAlternateColorCodes('&', this.config.getString(COOLDOWN_MESSAGE));
    }


    /**
     * @return the placeRadius
     */
    public int getPlaceRadius()
    {
        return placeRadius;
    }

    /**
     * @return the placeLimit
     */
    public int getPlaceLimit()
    {
        return placeLimit;
    }

    /**
     * @return the searchRadius
     */
    public int getSearchRadius()
    {
        return searchRadius;
    }

    /**
     * @return the searchLimit
     */
    public int getSearchLimit()
    {
        return searchLimit;
    }

    /**
     * @return the denyMessage
     */
    public String getDenyMessage()
    {
        return denyMessage;
    }

    /**
     * @return the countMessage
     */
    public String getCountMessage()
    {
        return countMessage;
    }

    /**
     * @return the cooldownPlace
     */
    public int getCooldownPlace()
    {
        return cooldownPlace;
    }

    /**
     * @return the cooldownCount
     */
    public int getCooldownCount()
    {
        return cooldownCount;
    }

    /**
     * @return the cooldown message
     */
    public String getCooldownMessage()
    {
        return cooldownMessage;
    }
}
