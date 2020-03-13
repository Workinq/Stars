package kr.kieran.stars.commands;

import kr.kieran.stars.StarsPlugin;
import kr.kieran.stars.utils.Color;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.concurrent.TimeUnit;

public class StarCommand implements CommandExecutor {

    private StarsPlugin plugin;

    public StarCommand(StarsPlugin plugin)
    {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (args.length == 0)
        {
            if (!sender.hasPermission("stars.commands.status"))
            {
                sender.sendMessage(Color.color(plugin.getConfig().getString("no-permission")));
                return true;
            }
            if (plugin.getStarManager().getStar() == null)
            {
                sender.sendMessage(Color.color(plugin.getConfig().getString("no-star-active")));
                return true;
            }
            Location location = plugin.getStarManager().getStar().getLocation();
            sender.sendMessage(Color.color(plugin.getConfig().getString("star-active").replace("%x%", String.format("%,d", location.getBlockX())).replace("%y%", String.format("%,d", location.getBlockY())).replace("%z%", String.format("%,d", location.getBlockZ())).replace("%world%", location.getWorld().getName())));
            return true;
        }
        switch (args[0].toLowerCase())
        {
            case "spawn":
            case "start":
                if (!sender.hasPermission("stars.commands.spawn"))
                {
                    sender.sendMessage(Color.color(plugin.getConfig().getString("no-permission")));
                    return true;
                }
                plugin.getStarManager().destroyIfActive();
                plugin.getStarManager().createStar(plugin.getServer().getWorld(plugin.getConfig().getString("world")), true);
                sender.sendMessage(Color.color(plugin.getConfig().getString("star-created.force")));
                return true;
            case "despawn":
            case "stop":
                plugin.getStarManager().destroyIfActive();
                sender.sendMessage(Color.color(plugin.getConfig().getString("star-stopped")));
                return true;
            case "timer":
            case "when":
                long timer = plugin.getStarManager().getTimer();
                long next = System.currentTimeMillis() + plugin.getConfig().getInt("star-task.delay");
                String duration = String.format("%02d min, %02d sec", TimeUnit.MILLISECONDS.toMinutes(next - timer), TimeUnit.MILLISECONDS.toSeconds(next - timer) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(next - timer)));
                sender.sendMessage(Color.color(plugin.getConfig().getString("star-timer").replace("%time%", duration)));
                return true;
            case "reload":
                plugin.reloadConfig();
                sender.sendMessage(Color.color(plugin.getConfig().getString("config-reloaded")));
                return true;
            default:
                sender.sendMessage(Color.color(plugin.getConfig().getString("invalid-usage")));
                return true;
        }
    }

}
