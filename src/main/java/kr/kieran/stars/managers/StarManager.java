package kr.kieran.stars.managers;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.Faction;
import kr.kieran.stars.StarsPlugin;
import kr.kieran.stars.objects.Reward;
import kr.kieran.stars.objects.Star;
import kr.kieran.stars.utils.Color;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.concurrent.ThreadLocalRandom;

public class StarManager {

    private StarsPlugin plugin;
    private Star star;
    private long timer;

    public StarManager(StarsPlugin plugin)
    {
        this.plugin = plugin;
        this.star = null;
    }

    public void createStar(World world, boolean forced)
    {
        if (star == null) return;
        if (!forced) timer = System.currentTimeMillis();
        star = new Star(plugin.getLocationUtils().locate(world));
        star.getLocation().getBlock().setType(Material.getMaterial(plugin.getConfig().getString("material")));
        plugin.getServer().broadcastMessage(Color.color(plugin.getConfig().getString("star-created.broadcast")));
    }

    public void destroyStar(Player player, Faction faction)
    {
        if (star != null) star.destroy();
        // Give the player rewards and announce the faction that destroyed the star.
        for (Reward reward : plugin.getRewardManager().getRewards()) {
            if (ThreadLocalRandom.current().nextDouble() > reward.getChance()) continue;
            for (String command : reward.getCommands()) {
                plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), command.replace("%player%", player.getName()));
            }
            break;
        }
        plugin.getServer().broadcastMessage(Color.color(plugin.getConfig().getString("star-captured").replace("%faction%", faction.getTag())));
        this.star = null;
    }

    public void destroyIfActive()
    {
        if (star != null) star.destroy();
    }

    public void damageStar(FPlayer fPlayer)
    {
        if (star == null) return;
        // Check if the faction has destroyed the star.
        Faction faction = fPlayer.getFaction();
        if (star.getFactions().containsKey(faction.getId()) && star.getFactions().get(faction.getId()) >= plugin.getConfig().getInt("star-health"))
        {
            destroyStar(fPlayer.getPlayer(), faction);
            return;
        }
        star.getFactions().putIfAbsent(faction.getId(), 0);
        star.getFactions().put(faction.getId(), star.getFactions().get(faction.getId()) + 1);
        // Send a message to all faction members.
        for (Player member : faction.getOnlinePlayers())
        {
            member.sendMessage(Color.color(plugin.getConfig().getString("star-damaged.faction")));
        }
        // Broadcast a message
        if (star.getFactions().get(faction.getId()) % plugin.getConfig().getInt("star-damaged.broadcast-every") != 0) return;
        plugin.getServer().broadcastMessage(Color.color(plugin.getConfig().getString("star-damaged.broadcast")));
    }

    public long getTimer()
    {
        return timer;
    }

    public Star getStar()
    {
        return star;
    }

}
