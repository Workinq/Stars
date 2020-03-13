package kr.kieran.stars.objects;

import kr.kieran.stars.utils.LocationUtils;
import org.bukkit.Location;
import org.bukkit.Material;

import java.util.HashMap;
import java.util.Map;

public class Star {

    private String location;
    private Map<String, Integer> factions;

    public Star(Location location)
    {
        this.location = LocationUtils.serialize(location);
        this.factions = new HashMap<>();
    }

    public void destroy()
    {
        // Set the block to air.
        getLocation().getBlock().setType(Material.AIR);
        // Clear the factions map to prevent memory leaks.
        factions.clear();
    }

    public Location getLocation()
    {
        return LocationUtils.deserialize(location);
    }

    public String getSerializedLocation()
    {
        return location;
    }

    public Map<String, Integer> getFactions()
    {
        return factions;
    }

}
