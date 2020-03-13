package kr.kieran.stars;

import kr.kieran.stars.commands.StarCommand;
import kr.kieran.stars.listeners.StarListeners;
import kr.kieran.stars.managers.RewardManager;
import kr.kieran.stars.managers.StarManager;
import kr.kieran.stars.utils.LocationUtils;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public class StarsPlugin extends JavaPlugin {

    private LocationUtils locationUtils;
    private StarManager starManager;
    private RewardManager rewardManager;
    private int starTaskId = -1, particleTaskId = -1;
    private File rewardsFile;
    private FileConfiguration rewards;

    @Override
    public void onLoad()
    {
        saveDefaultConfig();
    }

    @Override
    public void onEnable()
    {
        registerUtils();
        registerManagers();
        registerRewards();
        registerTasks();
        registerCommands();
        registerListeners();
    }

    @Override
    public void onDisable()
    {
        if (starTaskId != -1)
        {
            getServer().getScheduler().cancelTask(starTaskId);
        }
        if (particleTaskId != -1)
        {
            getServer().getScheduler().cancelTask(particleTaskId);
        }
        starManager.destroyIfActive();
    }

    private void registerManagers()
    {
        starManager = new StarManager(this);
        rewardManager = new RewardManager(this);
    }

    private void registerUtils()
    {
        locationUtils = new LocationUtils(this);
    }

    private void registerCommands()
    {
        getCommand("star").setExecutor(new StarCommand(this));
    }

    private void registerListeners()
    {
        getServer().getPluginManager().registerEvents(new StarListeners(this), this);
    }

    private void registerTasks()
    {
        starTaskId = getServer().getScheduler().scheduleSyncRepeatingTask(this, () ->
        {
            starManager.createStar(getServer().getWorld(getConfig().getString("world")), false);
        }, getConfig().getInt("tasks.star-delay") * 20L, getConfig().getInt("tasks.star-delay") * 20L);
        if (getConfig().getBoolean("tasks.use-particles"))
        {
            particleTaskId = getServer().getScheduler().scheduleSyncRepeatingTask(this, () ->
            {
                if (starManager.getStar() == null) return;
                Location location = starManager.getStar().getLocation();
                for (double y = 0.0D; y <= getConfig().getDouble("tasks.particle-height"); y += getConfig().getDouble("tasks.particle-step")) {
                    double x = Math.cos(y);
                    double z = Math.sin(y);
                    location.getWorld().playEffect(new Location(location.getWorld(), location.getX() + x, location.getY() + y, location.getZ() + z), Effect.getByName("tasks.particle-effect"), getConfig().getInt("tasks.particle-effect-data"));
                }
            }, getConfig().getInt("tasks.particle-delay") * 20L, getConfig().getInt("tasks.particle-delay") * 20L);
        }
    }

    private void registerRewards()
    {
        // Save the file
        saveResource("rewards.yml", false);
        rewardsFile = new File(getDataFolder(), "rewards.yml");
        rewards = new YamlConfiguration();
        try
        {
            rewards.load(rewardsFile);
        } catch (IOException | InvalidConfigurationException e)
        {
            getLogger().log(Level.SEVERE, "Failed to load rewards.yml: " + e.getMessage());
        }
    }

    public void saveRewards()
    {
        try
        {
            rewards.save(rewardsFile);
        }
        catch (IOException e)
        {
            getLogger().log(Level.SEVERE, "Failed to save rewards.yml: " + e.getMessage());
        }
    }

    public FileConfiguration getRewards()
    {
        return rewards;
    }

    public StarManager getStarManager()
    {
        return starManager;
    }

    public RewardManager getRewardManager()
    {
        return rewardManager;
    }

    public LocationUtils getLocationUtils()
    {
        return locationUtils;
    }

}
