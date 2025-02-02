package pl.stylowamc.blockbroadcast.commands;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.util.RayTraceResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SetBroadcastBlock implements CommandExecutor {

    private final FileConfiguration config;

    public SetBroadcastBlock(FileConfiguration config) {
        this.config = config;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (p.hasPermission("setbroadcastblock.allow")) {

                // Używamy ray trace, aby pobrać blok, na który gracz patrzy
                RayTraceResult rayResult = p.rayTraceBlocks(100); // dystans 100 bloków
                Block targetBlock = (rayResult != null) ? rayResult.getHitBlock() : null;

                // Sprawdzamy, czy gracz patrzy na jakiś blok
                if (targetBlock == null || targetBlock.getType() == Material.AIR) {
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            "&cYou are not looking at any block!"));
                    return true;
                }
                // Sprawdzamy, czy podano wiadomość
                if (args.length == 0) {
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            "&cSet the message you want to assign to block!"));
                    return true;
                }

                Location location = targetBlock.getLocation();
                String message = String.join(" ", args);

                // Tworzymy mapę danych bloku przy użyciu klasycznej HashMap
                Map<String, Object> blockData = new HashMap<>();
                blockData.put("world", location.getWorld().getName());
                blockData.put("x", location.getBlockX());
                blockData.put("y", location.getBlockY());
                blockData.put("z", location.getBlockZ());
                blockData.put("message", message);

                // Pobieramy listę zapisanych bloków z konfiguracji – jeżeli nie ma, tworzymy nową listę
                List<Map<?, ?>> savedBlocks = config.getMapList("blocks");
                if (savedBlocks == null) {
                    savedBlocks = new ArrayList<>();
                }
                savedBlocks.add(blockData);

                // Zapisujemy listę bloków w konfiguracji i zapisujemy plik config
                config.set("blocks", savedBlocks);
                p.getServer().getPluginManager().getPlugin("BlockBroadcast").saveConfig();

                p.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        "&aBlock was saved successfully with the message: " + message));
                p.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        "&6You can change the message in config file."));
                return true;
            }
        }
        return false;
    }
}
