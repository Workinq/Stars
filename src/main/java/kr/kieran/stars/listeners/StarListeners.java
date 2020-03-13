package kr.kieran.stars.listeners;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import kr.kieran.stars.StarsPlugin;
import kr.kieran.stars.utils.LocationUtils;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;

public class StarListeners implements Listener {

    private StarsPlugin plugin;

    public StarListeners(StarsPlugin plugin)
    {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockBreak(BlockBreakEvent event)
    {
        if (plugin.getStarManager().getStar() == null) return;
        if (event.getBlock().getType() != Material.getMaterial(plugin.getConfig().getString("material"))) return;
        if (!LocationUtils.serialize(event.getBlock().getLocation()).equals(plugin.getStarManager().getStar().getSerializedLocation())) return;
        event.setCancelled(true);
        FPlayer fPlayer = FPlayers.getInstance().getByPlayer(event.getPlayer());
        if (fPlayer.getFaction().isWilderness())
        {
            fPlayer.msg(plugin.getConfig().getString("not-in-faction"));
            return;
        }
        plugin.getStarManager().damageStar(fPlayer);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDamageBlock(BlockDamageEvent event)
    {
        if (plugin.getStarManager().getStar() == null) return;
        if (event.getBlock().getType() != Material.getMaterial(plugin.getConfig().getString("material"))) return;
        if (!LocationUtils.serialize(event.getBlock().getLocation()).equals(plugin.getStarManager().getStar().getSerializedLocation())) return;
        if (event.isCancelled()) event.setCancelled(false);
    }

}
