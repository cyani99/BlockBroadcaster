package pl.stylowamc.blockbroadcast.events;

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

    // Helper method to send debug messages if debug mode is enabled in the config
    private void debug(Player player, String message) {
        if (config.getBoolean("debug", false)) {
            player.sendMessage(ChatColor.GRAY + "[DEBUG] " + message);
        }
    }

    @EventHandler
    public void onPlayerRightClick(PlayerInteractEvent event) {
        if (event.getClickedBlock() == null) return;

        Player player = event.getPlayer();
        // Get the location of the clicked block
        Location clickedLocation = event.getClickedBlock().getLocation();
        int clickedX = clickedLocation.getBlockX();
        int clickedY = clickedLocation.getBlockY();
        int clickedZ = clickedLocation.getBlockZ();
        String clickedWorld = clickedLocation.getWorld().getName();

        // Debug: output clicked block coordinates
        debug(player, "Clicked block: " + clickedWorld + " (" + clickedX + ", " + clickedY + ", " + clickedZ + ")");
        debug(player, "Block type: " + event.getClickedBlock().getType().name());

        // Retrieve the list of blocks saved in the config
        List<Map<?, ?>> savedBlocks = config.getMapList("blocks");
        if (savedBlocks == null || savedBlocks.isEmpty()) {
            debug(player, "No saved blocks in the config.");
            return;
        }

        boolean matchFound = false;
        // Iterate through the saved blocks
        for (Map<?, ?> blockData : savedBlocks) {
            String configWorld = String.valueOf(blockData.get("world"));
            int configX = Integer.parseInt(String.valueOf(blockData.get("x")));
            int configY = Integer.parseInt(String.valueOf(blockData.get("y")));
            int configZ = Integer.parseInt(String.valueOf(blockData.get("z")));
            String message = String.valueOf(blockData.get("message"));

            // Debug: output coordinates from the config
            debug(player, "From config: " + configWorld + " (" + configX + ", " + configY + ", " + configZ + ")");

            boolean worldMatch = clickedWorld.equals(configWorld);
            boolean xMatch = (clickedX == configX);
            boolean yMatch = (clickedY == configY);
            boolean zMatch = (clickedZ == configZ);

            if (!worldMatch) {
                debug(player, "World mismatch: clicked=" + clickedWorld + ", config=" + configWorld);
            }
            if (!xMatch) {
                debug(player, "X mismatch: clicked=" + clickedX + ", config=" + configX);
            }
            if (!yMatch) {
                debug(player, "Y mismatch: clicked=" + clickedY + ", config=" + configY);
            }
            if (!zMatch) {
                debug(player, "Z mismatch: clicked=" + clickedZ + ", config=" + configZ);
            }

            if (worldMatch && xMatch && yMatch && zMatch) {
                matchFound = true;
                debug(player, "Matching block found!");

                // Translate the message (replace '&' with the proper color code)
                String coloredMessage = ChatColor.translateAlternateColorCodes('&', message);
                coloredMessage = coloredMessage.replaceAll("%n", "\n");
                player.sendMessage(coloredMessage);

                // If the block is special, cancel further interaction
                if (isSpecialBlock(event.getClickedBlock().getType())) {
                    debug(player, "Special block detected - action cancelled.");
                    event.setCancelled(true);
                }
                break;
            }
        }
        if (!matchFound) {
            debug(player, "No saved block matches the clicked block.");
        }
    }

    // Extended method to check if a block type is considered "special"
    private boolean isSpecialBlock(Material blockType) {
        // Check if the material name contains "HANGING_SIGN" (to cover hanging signs)
        if (blockType.name().contains("HANGING_SIGN")) {
            return true;
        }
        // Other special blocks
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
                blockType == Material.FLETCHING_TABLE ||
                blockType == Material.LOOM ||
                // Standing signs
                blockType == Material.OAK_SIGN ||
                blockType == Material.SPRUCE_SIGN ||
                blockType == Material.BIRCH_SIGN ||
                blockType == Material.JUNGLE_SIGN ||
                blockType == Material.ACACIA_SIGN ||
                blockType == Material.DARK_OAK_SIGN ||
                blockType == Material.CRIMSON_SIGN ||
                blockType == Material.WARPED_SIGN ||
                // Wall signs
                blockType == Material.OAK_WALL_SIGN ||
                blockType == Material.SPRUCE_WALL_SIGN ||
                blockType == Material.BIRCH_WALL_SIGN ||
                blockType == Material.JUNGLE_WALL_SIGN ||
                blockType == Material.ACACIA_WALL_SIGN ||
                blockType == Material.DARK_OAK_WALL_SIGN ||
                blockType == Material.CRIMSON_WALL_SIGN ||
                blockType == Material.WARPED_WALL_SIGN;
    }
}
