package me.gerab.allEnchantments.Listeners.Enchantment;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.ArrayList;

public class EternityEnchantListener implements Listener {

    // Store eternal items for each player
    private final Map<UUID, List<ItemStack>> eternalItems = new HashMap<>();

    // The specific lore to check for
    private static final String ETERNAL_LORE = "§5§lÖrökkévalóság 1";

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        UUID playerId = player.getUniqueId();

        // List to store eternal items
        List<ItemStack> playerEternalItems = new ArrayList<>();

        // Check each dropped item
        List<ItemStack> itemsToRemove = new ArrayList<>();

        for (ItemStack item : event.getDrops()) {
            if (item != null && hasEternalLore(item)) {
                // Add to eternal items list
                playerEternalItems.add(item.clone());
                // Mark for removal from drops
                itemsToRemove.add(item);
            }
        }

        // Remove eternal items from drops
        event.getDrops().removeAll(itemsToRemove);

        // Store eternal items for this player
        if (!playerEternalItems.isEmpty()) {
            eternalItems.put(playerId, playerEternalItems);
        }
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();

        // Check if player has eternal items stored
        if (eternalItems.containsKey(playerId)) {
            List<ItemStack> playerEternalItems = eternalItems.get(playerId);

            // Schedule the item restoration for next tick to ensure inventory is ready
            org.bukkit.Bukkit.getScheduler().runTaskLater(
                    getPlugin(),
                    () -> {
                        // Add eternal items back to player's inventory
                        for (ItemStack item : playerEternalItems) {
                            // Try to add item to inventory
                            HashMap<Integer, ItemStack> leftover = player.getInventory().addItem(item);

                            // If inventory is full, drop the items at player's location
                            if (!leftover.isEmpty()) {
                                for (ItemStack leftoverItem : leftover.values()) {
                                    player.getWorld().dropItemNaturally(player.getLocation(), leftoverItem);
                                }
                            }
                        }

                        // Remove stored items after restoration
                        eternalItems.remove(playerId);
                    },
                    1L // 1 tick delay
            );
        }
    }

    /**
     * Check if an item has the eternal lore
     */
    private boolean hasEternalLore(ItemStack item) {
        if (item == null || !item.hasItemMeta()) {
            return false;
        }

        ItemMeta meta = item.getItemMeta();
        if (meta == null || !meta.hasLore()) {
            return false;
        }

        List<String> lore = meta.getLore();
        if (lore == null) {
            return false;
        }

        // Check if any lore line matches our eternal lore
        for (String loreLine : lore) {
            if (ETERNAL_LORE.equals(loreLine)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Get the plugin instance - you'll need to modify this based on your main plugin class
     */
    private org.bukkit.plugin.Plugin getPlugin() {
        return org.bukkit.Bukkit.getPluginManager().getPlugin("AllEnchantments");
    }
}
