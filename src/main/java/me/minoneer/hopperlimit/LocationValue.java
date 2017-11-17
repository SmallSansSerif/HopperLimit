package me.minoneer.hopperlimit;

import org.bukkit.Location;

class LocationValue {
    private Location loc;
    private int value;

    LocationValue(Location loc, int value) {
        this.setLoc(loc);
        this.setValue(value);
    }

    Location getLoc() {
        return loc;
    }

    private void setLoc(Location loc) {
        this.loc = loc;
    }

    int getValue() {
        return value;
    }

    private void setValue(int value) {
        this.value = value;
    }
}
