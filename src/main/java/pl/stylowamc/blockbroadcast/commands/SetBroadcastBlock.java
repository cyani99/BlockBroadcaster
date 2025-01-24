package pl.stylowamc.blockbroadcast.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;

public class SetBroadcastBlock implements CommandExecutor {

    private final FileConfiguration config;

    public SetBroadcastBlock(FileConfiguration config) {
        this.config = config;
    }
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {




        if (commandSender instanceof Player){
            Player p = (Player) commandSender;
            if(p.hasPermission("setbroadcastblock.allow")){

                Block targetBlock = p.getTargetBlock(null, 5);

                //WYJĄTKI
                if (targetBlock == null || targetBlock.getType() == Material.AIR) {
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&',"&cYou are not looking at any block!"));
                    return true;
                }
                if (strings.length == 0) {
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&',"&cSet the message you want to assign to block!"));
                    return true;
                }

                //Bukkit.broadcastMessage(targetBlock.toString());
                Location location = targetBlock.getLocation();
                String message = String.join(" ", strings);

                Map<String, Object> blockData = Map.of(
                        "world", location.getWorld().getName(),
                        "x", location.getBlockX(),
                        "y", location.getBlockY(),
                        "z", location.getBlockZ(),
                        "message", message
                );

                List<Map<?, ?>> savedBlocks = config.getMapList("blocks");
                savedBlocks.add(blockData);

                // Zapisujemy konfigurację na dysk
                config.set("blocks", savedBlocks);
                p.getServer().getPluginManager().getPlugin("BlockBroadcast").saveConfig();

                p.sendMessage(ChatColor.translateAlternateColorCodes('&',"&aBlock was saved successfull with the message: " + message));
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6You can change the message in config file."));
                return true;
            }
        }


        return false;
    }
}
