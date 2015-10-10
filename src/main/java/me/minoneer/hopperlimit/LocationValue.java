package me.minoneer.hopperlimit;

import org.bukkit.Location;

public class LocationValue
{
    private Location loc;
    private int value;

    public LocationValue(Location loc, int value)
    {

        this.setLoc(loc);
        this.setValue(value);
    }

    /**
     * @return the loc
     */
    public Location getLoc()
    {
        return loc;
    }

    /**
     * @param loc the loc to set
     */
    public void setLoc(Location loc)
    {
        this.loc = loc;
    }

    /**
     * @return the value
     */
    public int getValue()
    {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(int value)
    {
        this.value = value;
    }
}
