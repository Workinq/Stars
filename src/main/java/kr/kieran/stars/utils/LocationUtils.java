package kr.kieran.stars.utils;

import kr.kieran.stars.StarsPlugin;
import org.bukkit.*;
import org.bukkit.block.BlockFace;

import java.util.concurrent.ThreadLocalRandom;

public class LocationUtils {

    private StarsPlugin plugin;

    public LocationUtils(StarsPlugin plugin)
    {
        this.plugin = plugin;
    }

    public static String serialize(Location location)
    {
        return location.getWorld().getName() + ":" + location.getBlockX() + ":" + location.getBlockY() + ":" + location.getBlockZ();
    }

    public static Location deserialize(String string)
    {
        String[] split = string.split(":");
        return new Location(Bukkit.getWorld(split[0]), Double.parseDouble(split[1]), Double.parseDouble(split[2]), Double.parseDouble(split[3]));
    }

    public Location locate(World world)
    {
        int x = getRandomNumber(plugin.getConfig().getInt("coords.min-x"), plugin.getConfig().getInt("coords.max-x"));
        int z = getRandomNumber(plugin.getConfig().getInt("coords.min-z"), plugin.getConfig().getInt("coords.max-z"));
        Location location = null;
        for (int y = world.getMaxHeight(); y > 0; y--)
        {
            Location copy = world.getBlockAt(x, y, z).getLocation();
            if (copy.getBlock().getRelative(BlockFace.DOWN).getType().isSolid() && copy.getBlock().getType() != Material.WATER && copy.getBlock().getType() != Material.LAVA)
            {
                location = copy;
            }
        }
        // Very unsafe if there are no locations possible.
        if (location == null)
        {
            return locate(world);
        }
        return location;
    }

    private int getRandomNumber(int origin, int bound)
    {
        return ThreadLocalRandom.current().nextInt(origin, bound);
    }

}
