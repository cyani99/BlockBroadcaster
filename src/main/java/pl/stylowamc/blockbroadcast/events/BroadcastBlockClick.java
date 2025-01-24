package pl.stylowamc.blockbroadcast.events;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.List;
import java.util.Map;

public class BroadcastBlockClick implements Listener {
    private final FileConfiguration config;

    public BroadcastBlockClick(FileConfiguration config) {
        this.config = config;
    }

    @EventHandler
    public void onPlayerRightClick(PlayerInteractEvent event) {
        if (event.getClickedBlock() == null) return;
        Player player = event.getPlayer();
        Location clickedBlockLocation = event.getClickedBlock().getLocation();

        // Pobieramy zapisane bloki z konfiguracji
        List<Map<?, ?>> savedBlocks = config.getMapList("blocks");

        for (Map<?, ?> blockData : savedBlocks) {
            // Pobieramy dane zapisanych bloków
            String worldName = (String) blockData.get("world");
            int x = (int) blockData.get("x");
            int y = (int) blockData.get("y");
            int z = (int) blockData.get("z");
            String message = (String) blockData.get("message");
            Material blockType = event.getClickedBlock().getType();
            // Porównujemy lokalizację klikniętego bloku z zapisanym blokiem
            if (clickedBlockLocation.getWorld().getName().equals(worldName)
                    && clickedBlockLocation.getBlockX() == x
                    && clickedBlockLocation.getBlockY() == y
                    && clickedBlockLocation.getBlockZ() == z) {
                if (isSpecialBlock(blockType)) {
                    event.setCancelled(true); // Anulujemy działanie nawet dla specjalnych bloków
                }
                String coloredMessage = ChatColor.translateAlternateColorCodes('&',message);
                coloredMessage = coloredMessage.replaceAll("%n", "\n");
                event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&',coloredMessage));
                return; // Znaleziono blok, przerywamy pętlę
            }
        }
    }

    // Metoda sprawdzająca, czy blok jest specjalnym blokiem
    private boolean isSpecialBlock(Material blockType) {
        return blockType == Material.CRAFTING_TABLE ||
                blockType == Material.FURNACE ||
                blockType == Material.BLAST_FURNACE ||
                blockType == Material.SMOKER ||
                blockType == Material.ENCHANTING_TABLE ||
                blockType == Material.ANVIL ||
                blockType == Material.CHIPPED_ANVIL ||
                blockType == Material.DAMAGED_ANVIL ||
                blockType == Material.BEACON ||
                blockType == Material.BREWING_STAND ||
                blockType == Material.CHEST ||
                blockType == Material.TRAPPED_CHEST ||
                blockType == Material.SHULKER_BOX ||
                blockType == Material.ENDER_CHEST ||
                blockType == Material.BARREL ||
                blockType == Material.HOPPER ||
                blockType == Material.DISPENSER ||
                blockType == Material.DROPPER ||
                blockType == Material.GRINDSTONE ||
                blockType == Material.CARTOGRAPHY_TABLE ||
                blockType == Material.LECTERN ||
                blockType == Material.SMITHING_TABLE ||
                blockType == Material.STONECUTTER ||
        blockType == Material.SPRUCE_SIGN ||
                blockType ==Material.OAK_SIGN ||
                blockType == Material.DARK_OAK_SIGN;
    }

}


